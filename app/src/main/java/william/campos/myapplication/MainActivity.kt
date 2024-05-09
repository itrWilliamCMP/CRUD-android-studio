package william.campos.myapplication

import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexionw
import modelo.dataClassProductos

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


        fun Limpiar(){
            txtnombre.setText("")
            txtCantidad.setText("")
            txtPrecio.setText("")
        }

        ////////////////////////////////TODO: Mostrar datos////////////////////////////////////
        val rcvProductos = findViewById<RecyclerView>(R.id.rcvProductos)

        //Asignar un layout al RecyclerView
        rcvProductos.layoutManager = LinearLayoutManager(this)

        //Funcion para obtener datos
        fun obtenerDatos(): List<dataClassProductos>{
            val objConexionw = ClaseConexionw().cadenaConexion()

            val statement = objConexionw?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbProductos")!!

            val productos = mutableListOf<dataClassProductos>()
            while (resultSet.next()){
                val nombre = resultSet.getString("nombreProducto")
                val producto = dataClassProductos(nombre)
                productos.add(producto)
            }
            return productos
        }

        //Asignar un adaptador
        CoroutineScope(Dispatchers.IO).launch {
            val productosDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val miApadapter = Adaptador(productosDB)

                rcvProductos.adapter = miApadapter
            }
        }

        ///////////////////// TODO: Guardar Datos //////////////////////



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

                val nuevosProductos = obtenerDatos()
                withContext(Dispatchers.Main){
                    (rcvProductos.adapter as? Adaptador)?.actualizarLista(nuevosProductos)
                }


            }
            //Limpiar()
        }





    }
}