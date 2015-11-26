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

$sql = "select * from login where login = '".$_POST['login']."' and senha = '".$_POST['senha']."' ";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
    echo("<script>alert('Cadastro realizado com sucesso! Voce está sendo direcionado para area restrita!');</script>");
     header("Location:http://localhost/BibliotecaWeb/escolher.php");
} else {
    echo "Error: " . $sql . "<br>" . mysqli_error($conn);
    header("Location:http://localhost/BibliotecaWeb/");
    
}

mysqli_close($conn); 