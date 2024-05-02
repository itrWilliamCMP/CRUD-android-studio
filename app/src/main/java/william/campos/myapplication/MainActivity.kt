package william.campos.myapplication

import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.ClaseConexionw

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1- Mnadar a llamar a todos los elementos de la pantalla
        val txtnombre = findViewById<EditText>(R.id.txtNombre)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        val txtCantidad = findViewById<EditText>(R.id.txtCantidad)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        //2- Programar el boton
        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){


                //3- Guardar datos
                //Primer Paso, creo un objeto de la clase conexion
                val claseC = ClaseConexionw().cadenaConexion()

                //Segundo Paso creo una variable que contenga un PreparedStatement
                val addProducto = claseC?.prepareStatement("insert into tbProductos(nombreProducto, precio, cantidad) values(?,?,?)")!!
                addProducto.setString(1, txtnombre.text.toString())
                addProducto.setInt(2, txtPrecio.text.toString().toInt())
                addProducto.setInt(3, txtCantidad.text.toString().toInt())
                addProducto.executeUpdate()


            }
        }

    }
}