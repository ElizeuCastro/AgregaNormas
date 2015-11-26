<?php

$host = "localhost";
$username = "root";
$password = "";
$dbname = "biblioteca";

// Create connection
$conn = mysqli_connect($host, $username, $password, $dbname);

// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$sql = "SELECT *
FROM `normas_juridicas`
INNER JOIN `estado`
ON normas_juridicas.estado=estado.id
and estado.nome = '".$_POST['plano2']."' ";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
   while($row = $result->fetch_assoc()) {
        echo "id: " . $row["id"]. " - titulo: " . $row["titulo"]. " " . $row["ementa"]. "<br>";
    }
} else {
    echo "Error: " . $sql . "<br>" . mysqli_error($conn);
    echo "Não há leis disponíveis";
    
}

mysqli_close($conn); 