#include <Wire.h>

#include "DFRobot_EC.h"
#include <EEPROM.h>

#define EC_PIN A0
float x,voltage,ecValue,tdsValue,temperature = 25;
DFRobot_EC ec;
char c;
char voltBuff[4];

void setup() {
 Wire.begin(8);                /* join i2c bus with address 8 */
 Wire.onReceive(receiveEvent); /* register receive event */
 Wire.onRequest(requestEvent); /* register request event */
 Serial.begin(9600);           /* start serial for debug */
 ec.begin();
}

void loop() {
 if(c=='1'){
 tdsSensor();
 x= tdsValue;
 }
 if(c=='2'){
 ecSensor();
 x= ecValue;
 }
 delay(100);
}

// function that executes whenever data is received from master
void receiveEvent(int howMany) {
 while (0 <Wire.available()) {
    c = Wire.read();      /* receive byte as a character */
    }
    Serial.print(c);
 Serial.println();             /* to newline */
}

// function that executes whenever data is requested from master
void requestEvent() {
//      float x= ecValue;
      dtostrf(x,4,2,voltBuff);
      Wire.write(voltBuff);  /*send string on request */
}

void ecSensor(){
  static unsigned long timepoint = millis();
    if(millis()-timepoint>1000U)  //time interval: 1s
    {
      timepoint = millis();
      voltage = analogRead(EC_PIN)/1024.0*5000;  // read the voltage
      //temperature = readTemperature();  // read your temperature sensor to execute temperature compensation
      ecValue =  ec.readEC(voltage,temperature);  // convert voltage to EC with temperature compensation
      ecValue=ecValue*1000;
     }
}
void tdsSensor(){
    voltage=analogRead(EC_PIN);
    tdsValue = (0.3417*voltage)+281.08;
}

