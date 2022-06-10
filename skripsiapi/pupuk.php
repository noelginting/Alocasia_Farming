<?php
require("db.php");
if($_SERVER['REQUEST_METHOD']=='POST'){
	$aksi=$_POST['aksi'];
	if($aksi==3){
	$id_pupuk=$_POST['id_pupuk'];
	$query="DELETE FROM pupuk WHERE id_pupuk='$id_pupuk'";
	} else {
	$id_pupuk=$_POST['id_pupuk'];
	$umur_tumbuhan=$_POST['umur_tumbuhan'];
	$id_tumbuhan=$_POST['id_tumbuhan'];
	$nilai_ec=$_POST['nilai_ec'];
	if($aksi==1){
		$query="INSERT INTO pupuk (umur_tumbuhan,id_tumbuhan,nilai_ec) VALUES ('$umur_tumbuhan','$id_tumbuhan','$nilai_ec')";
	}else if($aksi==2){
		$query="UPDATE pupuk SET umur_tumbuhan='$umur_tumbuhan',id_tumbuhan='$id_tumbuhan',nilai_ec='$nilai_ec' WHERE id_pupuk='$id_pupuk'";
		}
    }
    mysqli_query($conn,$query) or die(mysqli_error($conn));
}
?>