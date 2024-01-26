package cl.jmcontrerass.android.examen.app

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.jmcontrerass.android.examen.MainActivity
import cl.jmcontrerass.android.examen.R
import cl.jmcontrerass.android.examen.db.ListadoDB
import cl.jmcontrerass.android.examen.db.Lugar
import cl.jmcontrerass.android.examen.services.obtenerValorDolar
import cl.jmcontrerass.android.examen.vm.CameraAppViewModel
import cl.jmcontrerass.android.examen.vm.LugarViewModel
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ItemLugarUI(
    viewLugarVm: LugarViewModel,
    cameraAppVm: CameraAppViewModel,
    lugar: Lugar
){
    val contexto  = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()

    // Se empleará para recargar la pantalla inicial y se puedan ver los cambios
    val intent: Intent = Intent(contexto, MainActivity::class.java)

    // Almacena el valor del dolar en un atributo de la vista
    LaunchedEffect(true) {
        try {
            // Llamado a la api que entrega el valor actualizado del dólar
            viewLugarVm.valorDolar.value = obtenerValorDolar() ?: 0.0
        } catch (e: Exception) {
            // Manejo de excepción
        }
    }

    // Valor de alojamiento y traslado en dólares con un decimal y transformado a string
    val alojDolares = String.format("%.1f", lugar.alojamiento / viewLugarVm.valorDolar.value)
    val traslDolares = String.format("%.1f", lugar.traslado / viewLugarVm.valorDolar.value)

    // Se almacenan los valores en la vista en formato string
    viewLugarVm.costoAlojamientoDolares.value = alojDolares
    viewLugarVm.costoTrasladoDolares.value = traslDolares

    // Se actualiza el nombre del lugar en la vista
    viewLugarVm.nombreLugar.value = lugar.nombre

    // Valor que almacena el campo imagen del lugar (URL)
    val photo =
        if (lugar.imagen.isNotBlank()) {
            rememberImagePainter(
                data = lugar.imagen,
                builder = {
                    // Manejar el caso de error y proporcionar la imagen por defecto
                    error(R.drawable.not_available)
                }
            )
        // Si la imagen no existe se muestra una imagen por defecto
        } else {
            painterResource(id = R.drawable.not_available)
        }

    // Titulo o nombre del lugar a visitar
    Text(
        fontSize = 20.sp,
        fontWeight = FontWeight.ExtraBold,
        text = lugar.nombre,
        modifier = Modifier
            // permite acceder a pantalla lugar individual con click
            .clickable {
                // Primero se debe actualizar el nombre
                viewLugarVm.nombreLugar.value = lugar.nombre
                cameraAppVm.cambiarPantallaLugar()
            }
    )
    // Fila clickable
    Row (
        modifier = Modifier
            .fillMaxWidth()
            // Permite acceder a pantalla lugar individual con click
            .clickable {
                // Primero se debe actualizar el nombre
                viewLugarVm.nombreLugar.value = lugar.nombre
                cameraAppVm.cambiarPantallaLugar()
            }
    ) {
        // Imagen de lugar (URL string)
        Image(
            painter = photo,
            contentDescription = lugar.nombre,
            modifier = Modifier.scale(1.0f).size(210.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))

        // Columna de elementos junto al lado derecho de la imagen
        Column(
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            // Costo de alojamiento en pesos y dólares
            Text(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.alojamiento)
            )
            Text("$ " + lugar.alojamiento + " CLP")
            Text("$ $alojDolares USD")

            Spacer(modifier = Modifier.height(15.dp))

            // Costo de traslado en pesos y dólares
            Text(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.traslado)
            )
            Text("$" + lugar.traslado + " CLP")
            Text("$ $traslDolares USD")

            Spacer(modifier = Modifier.height(15.dp))

            // Fila con icono para editar formulario y para eliminar item
            Row() {
                Spacer(modifier = Modifier.width(15.dp))

                // Editar item
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Editar destino",
                    modifier = Modifier
                        .size(width = 30.dp, height = 30.dp)
                        .clickable {
                            // Actualiza nombre lugar y llama al formulario
                            viewLugarVm.nombreLugar.value = lugar.nombre
                            cameraAppVm.cambiarPantallaFormularioParam()
                        }
                )
                Spacer(modifier = Modifier.width(30.dp))
                // Borrar item
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Eliminar destino",
                    modifier = Modifier
                        .size(width = 30.dp, height = 30.dp)
                        .clickable {

                            // Elimina el lugar de la tabla de la bd
                            alcanceCorrutina.launch(Dispatchers.IO) {
                                val dao = ListadoDB
                                    .getInstance(contexto)
                                    .lugarDao()
                                dao.delete(lugar)

                                // Recarga la pantalla principal para mostrar cambios
                                contexto.startActivity(intent)
                            }
                        }
                )
            }
        }
    }
    // Cierra cada item con una línea de separación
    Spacer(modifier = Modifier.height(15.dp))
    Divider(
        color = Color.Gray,
        thickness = 1.dp
    )
}