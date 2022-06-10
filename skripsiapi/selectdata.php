<?php
require("db.php");
if($_SERVER['REQUEST_METHOD']=='GET'){
$id=$_GET['id'];
if($id==1){
$query="SELECT * FROM tumbuhan";
$rs=mysqli_query($conn,$query) or die(mysqli_error($conn));
$datas=array();
while ($row=mysqli_fetch_array($rs)) {
	$data=[
		'id_tumbuhan'=>$row['id_tumbuhan'],
		'nama_tumbuhan'=>$row['nama_tumbuhan'],
		'nama_gambar'=>$row['nama_gambar']
		];
                array_push($datas,$data);
  }	
} else if ($id==2){
$query="SELECT a.*,b.nama_tumbuhan FROM pupuk a INNER JOIN tumbuhan b ON a.id_tumbuhan=b.id_tumbuhan";
$rs=mysqli_query($conn,$query) or die(mysqli_error($conn));
$datas=array();
while($row=mysqli_fetch_array($rs)){
	$data=[
		'id_pupuk'=>$row['id_pupuk'],
		'umur_tumbuhan'=>$row['umur_tumbuhan'],
		'id_tumbuhan'=>$row['id_tumbuhan'],
		'nilai_ec'=>$row['nilai_ec'],
		'nama_tumbuhan'=>$row['nama_tumbuhan']
		];
		array_push($datas,$data);
 }
} else if ($id==3){
$query="SELECT a.*,b.nama_tumbuhan FROM penanaman a INNER JOIN tumbuhan b ON a.id_tumbuhan=b.id_tumbuhan 
	WHERE status='Belum'";
$rs=mysqli_query($conn,$query) or die(mysqli_error($conn));
$datas=array();
while($row=mysqli_fetch_array($rs)){
	$data=[
		'id_tanam'=>$row['id_tanam'],
		'id_tumbuhan'=>$row['id_tumbuhan'],
		'tgl_penanaman'=>$row['tgl_penanaman'],
                'nama_tumbuhan'=>$row['nama_tumbuhan']
		];
		array_push($datas,$data);
 }
}


if($rs){
 $response['error']=FALSE;
 $response['data']=$datas;
 echo json_encode($response);
}else{
 $response['error']=TRUE;
}
}
?>