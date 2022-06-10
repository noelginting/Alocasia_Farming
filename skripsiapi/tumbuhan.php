<?php
require("db.php");
if($_SERVER['REQUEST_METHOD']=='POST'){
	$aksi=$_POST['aksi'];
	if($aksi==3){
	$id_tumbuhan=$_POST['id_tumbuhan'];
	$query="DELETE FROM tumbuhan WHERE id_tumbuhan='$id_tumbuhan'";
	} else {
	$id_tumbuhan=$_POST['id_tumbuhan'];
	$nama_tumbuhan=$_POST['nama_tumbuhan'];
	$nama_gambar=$_POST['nama_gambar'];
	if($aksi==1){
		$query="INSERT INTO tumbuhan (nama_tumbuhan,nama_gambar) VALUES ('$nama_tumbuhan','$nama_gambar')";
	}else if($aksi==2){
		$query="UPDATE tumbuhan SET nama_tumbuhan='$nama_tumbuhan',nama_gambar='$nama_gambar' WHERE id_tumbuhan='$id_tumbuhan'";
		}
    }
    mysqli_query($conn,$query) or die(mysqli_error($conn));
}
?>