<?php
require("db.php");
if($_SERVER['REQUEST_METHOD']=='POST'){
$username=$_POST['username'];
$password=$_POST['password'];
$query= "SELECT * FROM admin WHERE username='$username' AND password='$password'";
$isi=mysqli_query($conn,$query) or die(mysqli_error($conn));
$data=mysqli_fetch_array($isi);
if($data!=null){
	$response['error'] =FALSE;
	$response['login'] =TRUE;
} else {
	$response['error']=TRUE;
	$response['login']=FALSE;
}
echo json_encode($response);
}
?>