<?php
require("db.php");
require("phpMQTT.php");
//if($_SERVER['REQUEST_METHOD']=='POST'){
	
        $idtanam=$_POST['idtanam'];
	$status=$_POST['status'];
	
	$query1="UPDATE status SET id_tanam='$idtanam',status='$status' WHERE id_status='1'";
	mysqli_query($conn,$query1) or die(mysqli_error($conn));

	date_default_timezone_set('Asia/Jakarta');
	$date = date("Y-m-d"); 
        $query2="SELECT * FROM penanaman WHERE id_tanam='$idtanam'";
	$isi1=mysqli_query($conn,$query2) or die (mysqli_error($conn));
	$row=mysqli_fetch_array($isi1);
		$idtumbuhan=$row['id_tumbuhan'];
		$row['tgl_penanaman'];
	$date1= new DateTime($date);
	$date2= new DateTime($row['tgl_penanaman']);
	$days= round(($date1->format("U") - $date2->format("U"))/86400);
	$days+=1;
	$query3="SELECT * FROM pupuk WHERE umur_tumbuhan='$days' AND id_tumbuhan='$idtumbuhan'";
	$isi2=mysqli_query($conn,$query3) or die (mysqli_error($conn));
	$row1=mysqli_fetch_array($isi2);
        while($row1==null){
		$days=$days-1;
		$query3="SELECT * FROM pupuk WHERE umur_tumbuhan='$days' AND id_tumbuhan='$idtumbuhan'";
             	$isi2=mysqli_query($conn,$query3) or die (mysqli_error($conn));
		$row1=mysqli_fetch_array($isi2);
		echo $days;
	}
	if($row1){
	  $response['status'] = $status;	
          $response['batasEc'] =$row1['nilai_ec'];
	  $response['idTanam']=$idtanam;
	  $response['id_pupuk']=$row1['id_pupuk'];
	  $response['days']=$days;
	} else{
          $response['status'] = "";	
          $response['batasEc'] ="";
	  $response['idTanam']="";
	}
	$coba=json_encode($response);

	
	$host = "postman.cloudmqtt.com";     // server cloudmqtt
	$port =14541;                     // port cloudmqtt
	$username = "zjjgkvyf";                   // user cloudmqtt
	$password = "e0aBC42_gVm6";                   // password cloudmqtt

	$mqtt = new phpMQTT($host, $port, "web_2"); //web_ adalah nama client id , dapat diubah sesuai keinginan
	if(!$mqtt->connect(true, NULL, $username, $password)) {
    	exit(1);	
	}
	$mqtt->publish("/led",$coba);
	$mqtt->close();		
//}
?>