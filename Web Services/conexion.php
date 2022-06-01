<?php
    //Datos de la base de datos
    $usuario = "username";
    $password = "password";
    $servidor = "host";
    $baseDeDatos = "database";

    // creacion de la conexion a la base de datos con mysql_conection() 
    $conexion = mysqli_connect($servidor, $usuario, $password) or die ("No se ha podido conectar al servidor de Base de datos");

    //Seleccion de la base de datos a utilizar
    $db = mysqli_select_db($conexion, $baseDeDatos) or die ("No hay conexion con la BD");
?>