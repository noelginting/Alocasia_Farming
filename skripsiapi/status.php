<?php
require("db.php");
if($_SERVER['REQUEST_METHOD']=='GET'){
	$tipe=$_GET['tipe'];
	if($tipe==1){
        $query="SELECT * FROM status WHERE id_status='1'";
    	$rs=mysqli_query($conn,$query) or die(mysqli_error($conn));
    	$data= mysqli_fetch_array($rs);
    	}
	if($rs){
 	    $response['error'] =FALSE;
	    $response['id_tanam'] =$data['id_tanam'];
	    $response['status']=$data['status'];
	    }
        else{
 	    $response['error'] =TRUE;
        }
        echo json_encode($response);
}
if($_SERVER['REQUEST_METHOD']=='POST'){
	$id_tanam=$_POST['id_tanam'];
	$query="UPDATE status SET id_tanam='idtanam',status='off' WHERE id_status='1'";
	mysqli_query($conn,$query) or die(mysqli_error($conn));
	date_default_timezone_set('Asia/Jakarta');
	$date = date("Y-m-d"); 
	$query="UPDATE penanaman SET status='Selesai',tgl_selesai='$date' WHERE id_tanam='$id_tanam'";
	mysqli_query($conn,$query) or die(mysqli_error($conn));
}
?>