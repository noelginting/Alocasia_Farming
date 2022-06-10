<?php
require("db.php");
if($_SERVER['REQUEST_METHOD']=='GET'){
$id=$_GET['id'];
if($id==0){
	$offset=$_GET['offset'];
	$batas=0+(5*$offset);
	$id_tanam=$_GET['id_tanam'];
	$query="SELECT ANY_VALUE(id_tanam) AS 'id_tanam',ANY_VALUE(tgl_pemupukan) 
		AS 'tgl_pemupukan', ANY_VALUE(waktu_pemupukan) AS 'waktu_pemupukan'
		,ANY_VALUE(AVG(volume_pupuka)) AS 'volume_pupuka',ANY_VALUE(AVG(volume_pupukb)) 
		AS 'volume_pupukb',ANY_VALUE(AVG(nilai_ec)) AS 'nilai_ec',ANY_VALUE(AVG(nilai_tds)) 
		AS 'nilai_tds',ANY_VALUE(AVG(volpupuk_keluar)) AS 'volpupuk_keluar' FROM histori_pemupukan 
		WHERE id_tanam='$id_tanam' GROUP BY tgl_pemupukan ORDER BY tgl_pemupukan DESC LIMIT 5 OFFSET $batas";
} else if($id==1){
	$offset=$_GET['offset'];
	$batas=0+(5*$offset);
	$id_tanam=$_GET['id_tanam'];
	$tanggal=$_GET['tanggal'];
	$query="SELECT ANY_VALUE(id_tanam) AS 'id_tanam',ANY_VALUE(tgl_pemupukan) AS 'tgl_pemupukan', 
		ANY_VALUE(waktu_pemupukan) AS 'waktu_pemupukan',ANY_VALUE(AVG(volume_pupuka)) AS 'volume_pupuka',
		ANY_VALUE(AVG(volume_pupukb)) AS 'volume_pupukb',ANY_VALUE(AVG(nilai_ec)) AS 'nilai_ec',
		ANY_VALUE(AVG(nilai_tds)) AS 'nilai_tds',ANY_VALUE(AVG(volpupuk_keluar)) AS 'volpupuk_keluar',
		ANY_VALUE(HOUR(waktu_pemupukan)) AS 'jam' FROM histori_pemupukan WHERE id_tanam='$id_tanam' 
		AND tgl_pemupukan='$tanggal' GROUP BY jam ORDER BY jam DESC LIMIT 5 OFFSET $batas";

} else if($id==2){
	$offset=$_GET['offset'];
	$batas=0+(5*$offset);
	$id_tanam=$_GET['id_tanam'];
	$tanggal=$_GET['tanggal'];	
	$jam=date('H',strtotime($_GET['jam']));
	$jam=$jam.':00:00';
	if($jam=='23:00:00'){
	  $jam1='23:55:59';
	} else {
	$jam1=strtotime('+1 hour',strtotime($jam));
	$jam1= date('H:i:s',$jam1);
	}
	$query="SELECT * from histori_pemupukan WHERE id_tanam='$id_tanam' and MINUTE(waktu_pemupukan) MOD 12=0 and 
		tgl_pemupukan='$tanggal' and waktu_pemupukan BETWEEN 
		'$jam' and '$jam1' ORDER BY waktu_pemupukan DESC";
}
$rs=mysqli_query($conn,$query);
$datas=array();
while($row= mysqli_fetch_array($rs)){
	$query="SELECT id_tumbuhan,tgl_penanaman FROM penanaman WHERE id_tanam='$id_tanam'";
	$db=mysqli_query($conn,$query); 
	$isi=mysqli_fetch_array($db);
	$date1= new DateTime($row['tgl_pemupukan']);
        $date2=new DateTime($isi['tgl_penanaman']);
	$isi['tgl_penanaman'];
	$days= round(($date1->format("U")-$date2->format("U"))/86400);
	$id= $isi['id_tumbuhan'];
	$query="SELECT nilai_ec FROM pupuk WHERE umur_tumbuhan='$days' and id_tumbuhan='$id'";
	$db=mysqli_query($conn,$query);
	$isi=mysqli_fetch_array($db);
	$isi['nilai_ec'];
    	$data=[
          'id_tanam'=>$row['id_tanam'],
          'tgl_pemupukan'=>$row['tgl_pemupukan'],
	  'waktu_pemupukan'=>$row['waktu_pemupukan'],
          'volume_pupuka'=>intval($row['volume_pupuka']),
          'volume_pupukb'=>intval($row['volume_pupukb']),
          'nilai_ec'=>intval($row['nilai_ec']),
          'nilai_tds'=>intval($row['nilai_tds']),
          'volpupuk_keluar'=>intval($row['volpupuk_keluar']),
	  'batas_ec'=>intval($isi['nilai_ec'])
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