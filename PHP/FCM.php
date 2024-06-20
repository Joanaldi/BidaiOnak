<?php

$message = "Esto es un mensaje de prueba";
$title = "FCM Notification";
$fcm_path = "https://fcm.googleapis.com/fcm/send";

$token = $_POST['token'];

$headers = array(
    'Authorization: key=BOSzJET5Yv4iTSISTXPXG6_SSefDxzP2tV_49qtTYFxLCi8E2nDCBEV7CN2Wz1u5YBwLHj9CkVqfWACn2fpw0QE',
    'Content-Type: application/json'
    );

$msg = array (
    'to' => $token,
    'data' => array (
        "mensaje" => "Esperemos que disfrutes de la aplicacion",
        "fecha" => "15/06/2024"),
    'notification' => array (
        "body" => $message,
        "title" => $title,
        "icon" => "ic_stat_ic_notification",
        )
    );
$msgJSON= json_encode ( $msg);

echo $msgJSON;


$ch = curl_init(); #inicializar el handler de curl
#indicar el destino de la petición, el servicio FCM de google
curl_setopt( $ch, CURLOPT_URL, $fcm_path);
#indicar que la conexión es de tipo POST
curl_setopt( $ch, CURLOPT_POST, true );
#agregar las cabeceras
curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers);
#Indicar que se desea recibir la respuesta a la conexión en forma de string
curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
#agregar los datos de la petición en formato JSON
curl_setopt( $ch, CURLOPT_POSTFIELDS, $msgJSON );
#ejecutar la llamada
$resultado= curl_exec( $ch );
#cerrar el handler de curl
curl_close( $ch );
?>