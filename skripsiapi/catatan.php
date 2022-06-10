<?php
	require("db.php");
	if($_SERVER['REQUEST_METHOD']=='POST'){
		date_default_timezone_set('Asia/Jakarta'); 
        $date = date("Y-m-d");
		$id_tanam=$_POST['id_tanam'];
		$catatan=$_POST['catatan'];
		$tindakan=$_POST['penanganan'];
		$query="INSERT INTO catatan (id_tanam,tanggal,catatan,tindakan) VALUES ('$id_tanam','$date','$catatan','$tindakan')";
		mysqli_query($conn,$query) or die(mysqli_error($conn));
	}
	if($_SERVER['REQUEST_METHOD']=='GET'){
	  $id_tanam=$_GET['id_tanam'];
	  $query="SELECT * FROM catatan WHERE id_tanam='$id_tanam'";
	  $rs=mysqli_query($conn,$query) or die(mysqli_error($conn));
	  $datas=array();
	  while($row=mysqli_fetch_array($rs)){
	    $data=[
		   'id_tanam'=>$row['id_tanam'],
		   'tanggal'=>$row['tanggal'],
		   'catatan'=>$row['catatan'],
		   'tindakan'=>$row['tindakan']
		];
	   array_push($datas,$data);
         }
	  if($rs){
		$response['error']=FALSE;
		$response['data']=$datas;
 		echo json_encode($response);
    	  }
	 else{
		$response['error']=TRUE;
	  }
        }
?>