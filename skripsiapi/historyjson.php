<?php
include 'db.php';
if($_SERVER['REQUEST_METHOD']=='GET'){
$typedata=$_GET['typedata'];
$id_tanam=$_GET['id_tanam'];
if($typedata==0){
$query = "SELECT a.*,b.id_tumbuhan,c.nama_tumbuhan,c.nama_gambar FROM 
histori_pemupukan a INNER JOIN penanaman b ON a.id_tanam = b.id_tanam  
INNER JOIN tumbuhan c ON b.id_tumbuhan = c.id_tumbuhan WHERE a.id_tanam='$id_tanam' 
ORDER BY tgl_pemupukan DESC, waktu_pemupukan DESC LIMIT 1";
}
if($typedata==1){
$query = "SELECT a.*,b.id_tumbuhan,c.nama_tumbuhan,c.nama_gambar FROM 
histori_pemupukan a INNER JOIN penanaman b ON a.id_tanam = b.id_tanam  
INNER JOIN tumbuhan c ON b.id_tumbuhan = c.id_tumbuhan WHERE a.id_tanam='$id_tanam' AND MINUTE(waktu_pemupukan) MOD 12=0
ORDER BY tgl_pemupukan DESC, waktu_pemupukan DESC LIMIT 5";
}

$rs= mysqli_query($conn,$query) or die(mysql_error($conn));
$datas=array();
while($row= mysqli_fetch_array($rs)){
    	$data=[
          'id_tanam'=>$row['id_tanam'],
          'tgl_pemupukan'=>$row['tgl_pemupukan'],
	  'waktu_pemupukan'=>$row['waktu_pemupukan'],
          'volume_pupuka'=>intval($row['volume_pupuka']),
          'volume_pupukb'=>intval($row['volume_pupukb']),
          'nilai_ec'=>floatval(round($row['nilai_ec'],1)),
          'nilai_tds'=>intval($row['nilai_tds']),
          'volpupuk_keluar'=>intval($row['volpupuk_keluar']),
	  'nama_gambar'=>$row['nama_gambar'],
	  'nama_tumbuhan'=>$row['nama_tumbuhan']
      ];
      
    array_push($datas,$data);
}
 if($rs){
 	$response['error'] = FALSE;	
        $response['data'] = $datas;
	echo json_encode($response);
 }
 else{
 	$response['error'] =TRUE;
 }
}
?>