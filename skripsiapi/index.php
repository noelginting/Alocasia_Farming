<?php

require("phpMQTT.php");
$date=null;
$umur=null;
$idtanam=null;
$coba=0;
$conn = mysqli_connect("localhost","db_noel","noel4321","db_noel");
$host = "postman.cloudmqtt.com";     // server cloudmqtt
$port =14541;                     // port cloudmqtt
$username = "zjjgkvyf";                   // user cloudmqtt
$password = "e0aBC42_gVm6";                   // password cloudmqtt
$mqtt = new phpMQTT($host, $port, "web_1"); //web_ adalah nama client id , dapat diubah sesuai keinginan
if(!$mqtt->connect(true, NULL, $username, $password)) {
 	exit(1);
}
//mensubscribe topik json 5 menit dari cloudmqtt
	$topics['led/data'] = array("qos" => 0, "function" => "procmsg");
	$mqtt->subscribe($topics, 0);
	while($mqtt->proc() && $coba==0){
	}
	
	$query= "SELECT tgl_penanaman FROM penanaman WHERE id_tanam='$idtanam'"; 
	$isi=mysqli_query($conn,$query);
        $data=mysqli_fetch_array($isi); 
        $date1=new DateTime($date);
	$date2= new DateTime($data['tgl_penanaman']);
	$days= round(($date1->format("U")-$date2->format("U"))/86400);
        $days+=1;	
	if($days!=$umur){
        $query="SELECT id_tumbuhan FROM penanaman WHERE id_tanam='$idtanam'";
	$isi=mysqli_query($conn,$query) or die(mysqli_error($conn));
	$data=mysqli_fetch_array($isi);
	$idtumbuhan=$data['id_tumbuhan'];
	$query="SELECT id_pupuk,nilai_ec FROM pupuk WHERE id_tumbuhan='$idtumbuhan' AND umur_tumbuhan='$days'";
	$isi=mysqli_query($conn,$query) or die(mysqli_error($conn));
	$data=mysqli_fetch_array($isi);
	if($data!=null){
	  $response['status'] = "on";	
          $response['batasEc'] =$data['nilai_ec'];
	  $response['idTanam']=$idtanam;
	  $response['id_pupuk']=$data['id_pupuk'];
	  $response['days']=$days;
	  $coba=json_encode($response);
       	  $mqtt->publish("/led",$coba);
	}
}
	$mqtt->close();

function procmsg($topic, $msg){
	//mendecode json dari topic
        $obj = json_decode($msg);
        var_dump($obj);

       global $date;
       global $umur;
       global $idtanam;
       date_default_timezone_set('Asia/Jakarta'); 
       $date = date("Y-m-d");
       $timewaktu=date("H:i:s");
       $idtanam=$obj->idt;
       $idpupuk=$obj->idp;
       $ec=$obj->ec;
       $tds=$obj->tds;
       $vol1=$obj->vol1;
       $vol2=$obj->vol2;
       $pupuk=$obj->pupuk;
       $umur=$obj->days; 
        mysqlinsert($idtanam,$idpupuk,$date,$timewaktu,$ec,$tds,$vol1,$vol2,$pupuk);
}

function mysqlinsert($idtanam,$idpupuk,$date,$timewaktu,$ec,$tds,$vol1,$vol2,$pupuk) {
	global $conn;
	global $coba;
        $query= "SELECT tgl_penanaman FROM penanaman WHERE id_tanam='$idtanam'"; 
	$isi=mysqli_query($conn,$query);
        $data=mysqli_fetch_array($isi);  
        $query="INSERT INTO histori_pemupukan VALUES 
                ('$idtanam','$idpupuk','$date','$timewaktu','$vol1','$vol2','$ec','$tds','$pupuk')";
        mysqli_query($conn,$query) or die(mysqli_error($conn));
	$coba=1;
}


?>