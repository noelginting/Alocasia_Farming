<?php
require("db.php");
if($_SERVER['REQUEST_METHOD']=='POST'){
	$aksi=$_POST['aksi'];
	if($aksi==3){
	  $id_tanam=$_POST['id_tanam'];
	  $query="DELETE FROM histori_pemupukan WHERE id_tanam='$id_tanam'";
          mysqli_query($conn,$query) or die(mysqli_error($conn));
	  $query="DELETE FROM catatan WHERE id_tanam='$id_tanam'";
          mysqli_query($conn,$query) or die(mysqli_error($conn));
	  $query="DELETE FROM penanaman WHERE id_tanam='$id_tanam'";
           	
	} else {
	$id_tanam=$_POST['id_tanam'];
	$id_tumbuhan=$_POST['id_tumbuhan'];
	$tgl_penanaman=$_POST['tgl_penanaman'];
	if($aksi==1){
		$query="INSERT INTO penanaman VALUES ('$id_tanam','$id_tumbuhan','$tgl_penanaman','Belum',null)";
	}else if($aksi==2){
			$query="UPDATE penanaman SET id_tumbuhan='$id_tumbuhan',tgl_penanaman='$tgl_penanaman' WHERE id_tanam='$id_tanam'";
	}
    }
    mysqli_query($conn,$query) or die(mysqli_error($conn));
}
?>