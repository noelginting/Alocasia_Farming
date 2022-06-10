<?php
include 'db.php';
if($_SERVER['REQUEST_METHOD']=='GET'){
$tipe=$_GET['tipe'];
if($tipe==1){
$query = "SELECT * FROM penanaman WHERE status='Belum' ORDER BY tgl_penanaman DESC LIMIT 5";}
else if($tipe==2){
$query = "SELECT * FROM penanaman WHERE status='Selesai' ORDER BY tgl_penanaman DESC LIMIT 5";
}
$rs= mysqli_query($conn,$query) or die(mysql_error($conn));
$datas=array();
while($row= mysqli_fetch_array($rs)){
    $data=[
           'id_tanam'=>$row['id_tanam'],
          'id_tumbuhan'=>$row['id_tumbuhan'],
          'tgl_penanaman'=>$row['tgl_penanaman']
      ];
    array_push($datas,$data);
}
 if($rs){
 	$response['error'] = FALSE;	
        $response['data'] = $datas;
	echo json_encode($response);
 }
 else{
 	$response['error'] = TRUE;
 }
}
?>