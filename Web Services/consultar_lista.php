<?php
    header('Content-Type: application/json');
    include 'conexion.php';

    $consulta = "SELECT * FROM alumnos";

    $resultadoConsulta = mysqli_query($conexion, $consulta) or die (mysqli_error());

    if($resultadoConsulta) {
        $datosJSON = mysqli_fetch_all($resultadoConsulta, MYSQLI_ASSOC);
        
        echo json_encode($datosJSON);
    } else {
        echo json_encode('{"respuesta":"error en la inserción"}');
    }

    mysqli_close($conexion);
?>