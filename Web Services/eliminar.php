<?php

    include 'conexion.php';

    function isValidJSON($str) {
        json_decode($str);
        return json_last_error() == JSON_ERROR_NONE;
    }

    $json_params = file_get_contents('php://input');
    
    $json=array();

    if (strlen($json_params) > 0 && isValidJSON($json_params)){
        
        $data = json_decode($json_params, true);

        $delete = "DELETE FROM alumnos WHERE no_control= '".$data['control']."'";
                                
        mysqli_query($conexion, $delete) or die (mysqli_error());

        $rowsEliminadas = mysqli_affected_rows($conexion);

        if ($rowsEliminadas == 1) {
            
            //$respuesta["respuesta"]='1';
            //echo json_encode($respuesta);
            echo '{"respuesta": "1"}';

        } elseif ($rowsEliminadas == 0){

            //$respuesta["respuesta"]='0';
            //echo json_encode($respuesta);
            echo '{"respuesta": "0"}';

        } else {

            //$respuesta["respuesta"]='-1';
            //echo json_encode($respuesta);
            echo '{"respuesta": "-1"}';

        }
        
        mysqli_close($conexion);

    } else{
        $errorConsulta["respuesta"]='parametro no recibido';
        echo json_encode($errorConsulta);
    }
?>