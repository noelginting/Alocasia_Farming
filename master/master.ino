//library nodemcu
#include <ESP8266WiFi.h>
//library cloud mqtt
#include <PubSubClient.h>
//library json
#include <ArduinoJson.h>
//library htpp
#include <ESP8266HTTPClient.h>
//library komunikasi serial
#include <Wire.h>

//ec sensor pin
#define EC_PIN  A0  // Analog input pin 

const char *ssid = "null"; //"FP_Keu";   // nama ssid dari wi-fi, jangan lebih dari 32 karakter
const char *pass = "padsa123457689"; //"fp_keu*.*ok";   // password dari wi-fi

//------------------------------ Konfigurasi server pada https://www.cloudmqtt.com/
const char *mqtt_server = "postman.cloudmqtt.com";
const int mqtt_port = 14541;
const char *mqtt_user = "zjjgkvyf";
const char *mqtt_pass = "e0aBC42_gVm6";
//------------------------------ End Konfigurasi server pada https://www.cloudmqtt.com/

// variable isi dari mqqt server
String coba;
//variable
long duration;
int distance;

//nilai buat cek batas
float hasil, tds;

//EcSensor------------------------------//
//variable


//Relay Pin------------------------------//
const int relayInput = D3;

//Volume
float volume1;
float volume2;
float volember;
int i = 0;
int data=0;
int waktu = 0;
float outCair;
//deklarasi konfigurasi server
WiFiClient wclient;
PubSubClient client(wclient);

//------------------------------ fungsi untuk menerima nilai balik (subcribe)
void callback(char* topic, byte* payload, unsigned int length) {
  coba = "";
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
    coba += (char)payload[i];
                                                          }
}
//------------------------------ End fungsi untuk menerima nilai balik (subcribe)
//function reconnect ke mqtt jika sambungan terputus
boolean reconnect() {
  if (client.connect("ESP8266Client", mqtt_user, mqtt_pass )) {
    Serial.println("connected");
  }
  return client.connected();
}
void setup() {
  // Setup console
  Serial.begin(115200);
  Wire.begin(D1, D2);

  // Setup relay
  pinMode(relayInput, OUTPUT);
  digitalWrite(relayInput, LOW);

  delay(10);
  Serial.println();
  Serial.println();
  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);

}

void loop() {
  //----------------------------- cek apakah wi-fi sudah tersambung
  if (WiFi.status() != WL_CONNECTED) {
    Serial.print("Connecting to ");
    Serial.print(ssid);
    Serial.println("...");
    WiFi.begin(ssid, pass);

    if (WiFi.waitForConnectResult() != WL_CONNECTED)
      return;
    Serial.println("WiFi connected");
  }
  //----------------------------- End cek apakah wi-fi sudah tersambung

  //----------------------------- cek apakah ESP sudah tersambung dengan server
  if (WiFi.status() == WL_CONNECTED) {
    if (!client.connected()) {
      Serial.println("Connecting to MQTT server");
      if (client.connect("ESP8266Client", mqtt_user, mqtt_pass )) {
        Serial.println("Connected to MQTT server");
        client.subscribe("/led");
        //        client.publish("/led/state","0");
        client.setCallback(callback);
      } else {
        Serial.println("Could not connect to MQTT server");
      }
    }
    //----------------------------- cek apakah ESP sudah tersambung dengan server

    //----------------------------- cek Json dari api
    if (coba != "") {
      const size_t bufferSize = JSON_OBJECT_SIZE(5);
      DynamicJsonBuffer jsonBuffer(bufferSize);
      JsonObject& root = jsonBuffer.parseObject(coba);
      String pstatus = root["status"];
      float batas = root["batasEc"];
      int idpupuk = root["id_pupuk"];
      String idtanam = root["idTanam"];
      int umur = root["days"];

      if (pstatus == "on") {
        hasil = komunikasiSerial('1');
        delay(100);
        Serial.print("EC");
        Serial.println(hasil);
        Serial.println("pupuk");
        Serial.println(idpupuk);
        if(hasil<batas){
          tds = komunikasiSerial('2');
          volume1 = nilaiUltrasonik(D5, D6);
          volume2 = nilaiUltrasonik(D7, D8);
          outCair = 0.00025 * i;
          buildJson(idtanam, umur, idpupuk);
          sendHttp();
          client.loop();
          delay(4000);
         while ( hasil < batas) {
          digitalWrite(relayInput, HIGH);
          hasil = komunikasiSerial('2');
          delay(100);
          Serial.print("EC loop");
          Serial.println(hasil);
          Serial.println(batas);
          Serial.println(i);
          i = i + 100;
          waktu = waktu + 100;
          if (hasil > batas) {
            digitalWrite(relayInput, LOW); 
          }
        }
       }
        digitalWrite(relayInput, LOW);
        delay(100);
        tds = komunikasiSerial('2');
        volume1 = nilaiUltrasonik(D5, D6);
        volume2 = nilaiUltrasonik(D7, D8);
        outCair = 0.00025 * i;
        if (waktu >= 60000) {
          buildJson(idtanam, umur, idpupuk);
          sendHttp();
          waktu = 0;
          i = 0;
        }
        waktu = waktu + 5900;
//        Serial.println(waktu);
        client.loop();
        delay(5900);
      }
      else if (pstatus = "off") {
        waktu = 0;
      }
      client.loop();
      //         }
    }
  }
  if (client.connected())
    client.loop();
}
int nilaiUltrasonik(int hTrig, int hEcho) {
  // Setup ultrasonik
  pinMode(hTrig, OUTPUT); // Sets the trigPin as an Output
  pinMode(hEcho, INPUT);

  digitalWrite(hTrig, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(hTrig, HIGH);
  delayMicroseconds(10);
  digitalWrite(hTrig, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(hEcho, HIGH);
  // Calculating the distance
  distance = duration * 0.034 / 2;
  volember = (3.14 * 8 * 8) * (16 - distance); // tinggi ember 16 cm, r=8 cm
  if(volember <=0){
    volember=0;
  }
  return volember;
}
void buildJson(String idtanam, int umur, int idpupuk) {
  //membuat objek yang akan diJSON kan
  StaticJsonBuffer<300> JSONbuffer;
  JsonObject& JSONencoder = JSONbuffer.createObject();
  JSONencoder["idt"] = idtanam;
  JSONencoder["idp"] = idpupuk;
  JSONencoder["ec"] =0;
  JSONencoder["tds"] =0;
  JSONencoder["vol1"] =0;
  JSONencoder["vol2"] =0;
  JSONencoder["pupuk"] =0;
  JSONencoder["days"] =umur;

  char JSONmessageBuffer[100];
  JSONencoder.printTo(JSONmessageBuffer, sizeof(JSONmessageBuffer));
  Serial.println("Sending message to MQTT topic..");
  Serial.println(JSONmessageBuffer);

  //ESP mempublish ke mqtt dengan topik cuaca/sensor/5detik
  if (client.publish("led/data", JSONmessageBuffer) == true) {
  } else {
    Serial.println("Error sending message");
  }
}

void sendHttp() {
  //save the data to mysql, access the php file to write
  HTTPClient http;
  String url = "http://103.195.90.35:1600/skripsiapi/index.php";
  Serial.println(url);
  http.begin(url);
  //using GET method to write to sql
  int httpCode = http.GET();
  if (httpCode > 0)
  {
    Serial.printf("[HTTP] GET...code: %d\n", httpCode);
    if (httpCode == HTTP_CODE_OK)
    {
      String payload = http.getString();
      Serial.println(payload);
    }
    else
    {
      Serial.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }
    http.end();
  }
}

float komunikasiSerial(char param) {
  Wire.beginTransmission(8); /* begin with device address 8 */
  Wire.write(param);  /* sends hello string */
  Wire.endTransmission();    /* stop transmitting */

  Wire.requestFrom(8, 4); /* request & read data of size 13 from slave */
  String dataString = "";
  while (Wire.available()) {
    char c = Wire.read();
    dataString = dataString + c;
  }
  float nilai = dataString.toFloat();
  return nilai;
}
