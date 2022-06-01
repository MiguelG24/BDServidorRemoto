<?php
    //header('Content-Type: application/json');
    include 'conexion.php';

    if (isset($_GET['control'])) {
        
        $no_control = $_GET['control'];

        $consulta = "SELECT * FROM alumnos WHERE no_control = '".$no_control."'";
        
        $resultado = mysqli_query($conexion, $consulta) or die (mysqli_error());

        if($resultado) {

            $datosJSON = mysqli_fetch_array($resultado, true);

            if ($datosJSON != null) {
                echo json_encode($datosJSON);
            }else {
                $consultaVacia["respuesta"]='registro no encontrado';
                echo json_encode($consultaVacia);
            }
            

        } else {
            $errorConsulta["respuesta"]='error en la consulta';
            echo json_encode($errorConsulta);
        }

        mysqli_close($conexion);
    } else {
        $errorConsulta["respuesta"]='parametro no recibido';
        echo json_encode($errorConsulta);
    }
?>