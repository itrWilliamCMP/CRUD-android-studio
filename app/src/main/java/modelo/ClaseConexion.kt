package modelo

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager

class ClaseConexionw {

    fun cadenaConexion(): Connection?{

        try {
            val url = "jdbc:oracle:thin:@10.10.0.129:1521:xe"
            val usuario = "system"
            val contrasena = "desarrollo"

            val connection = DriverManager.getConnection(url, usuario, contrasena)

            return  connection
        }catch (e: Exception){
            println("Es es el error: $e")
            return null
        }
    }
}