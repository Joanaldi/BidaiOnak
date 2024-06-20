<?php
// Datos de conexión a la base de datos
$DB_SERVER = "db";
$DB_USER = "admin";
$DB_PASS = "test";
$DB_DATABASE = "database";

// Establecer la conexión
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);

// Comprobar la conexión
if (mysqli_connect_errno()) {
    echo 'Error de conexión: ' . mysqli_connect_error();
    exit();
}

// Verifica si se han enviado datos del formulario
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Obtener los datos del formulario
    $nombre = $_POST["usuario"];

    // Consulta para verificar si el usuario y la contraseña existen
    $query = "SELECT * FROM usuario WHERE nombre = '$nombre'";

    $resultado = mysqli_query($con, $query);

    if (mysqli_num_rows($resultado) > 0) {
        // Si el usuario y la contraseña coinciden, enviar respuesta de éxito
        $fila = mysqli_fetch_assoc($resultado);
        echo $fila['imagen'];
    } else {
        // Si no coinciden, enviar respuesta de error
        echo "Usuario o contraseña incorrectos";
    }

    // Cerrar la conexión
    mysqli_close($con);
} else {
    echo "Método de solicitud no permitido";
}
?>