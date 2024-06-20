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
    echo 'ha entrado';
    // Obtener los datos del formulario
    $nombreUsuario = $_POST["usuario"];
    $contraseñna = $_POST["contrasena"];
    $imagenPath = $_POST["imagen"];

    // Preparar la consulta SQL para insertar los datos en la base de datos
    $query = "INSERT INTO usuarios (nombre, contraseña, imagen) VALUES ('$nombreUsuario', '$contrasena', '$imagenPath')";

    // Imprimir la consulta SQL para verificar si se está construyendo correctamente
    echo "Consulta SQL: " . $query;

    // Ejecutar la consulta
    if (mysqli_query($con, $query)) {
        // La inserción fue exitosa
        echo "Usuario registrado exitosamente";
    } else {
        // Ocurrió un error durante la inserción
        echo "Error al registrar el usuario: " . mysqli_error($con);
    }

    // Cerrar la conexión
    mysqli_close($con);
}