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

        $insert = "INSERT INTO alumnos VALUES('".$data['control']."', '".$data['nombre']."', '".
                        $data['apellidos']."', '".$data['carrera']."', '".$data['telefono']."', '".
                        $data['email']."', '".$data['direccion']."')";
                                
        $resultadoInsert  = mysqli_query($conexion, $insert) or die (mysqli_error());

        if ($resultadoInsert) {
            
            echo '{"respuesta":"ok"}';

        }else{
            echo json_encode('{"respuesta":"error en la inserción"}');
        }
        
        mysqli_close($conexion);

    } else{
        $resulta["control"]=0;
        $resulta["nombre"]='WS No retorna';
        $resulta["apellidos"]='WS No retorna';
        $resulta["carrera"]='WS No retorna';
        $resulta["telefono"]='WS No retorna';
        $resulta["email"]='WS No retorna';
        $resulta["direccion"]='WS No retorna';
        $json['alumno'][]=$resulta;
        echo json_encode($json);
    }
?>