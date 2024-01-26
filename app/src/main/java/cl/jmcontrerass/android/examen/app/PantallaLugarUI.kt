package cl.jmcontrerass.android.examen.app

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.jmcontrerass.android.examen.R
import cl.jmcontrerass.android.examen.db.ListadoDB
import cl.jmcontrerass.android.examen.db.Lugar
import cl.jmcontrerass.android.examen.vm.CameraAppViewModel
import cl.jmcontrerass.android.examen.vm.LugarViewModel
import cl.jmcontrerass.android.examen.vm.formatearCaptura
import cl.jmcontrerass.android.examen.vm.uri2imageBitmap
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLugarUI (
    viewLugarVm: LugarViewModel,
    cameraAppVm: CameraAppViewModel,
    tomarFotoOnClick: () -> Unit = {},
    visualizarFotoOnClick: (Int) -> Unit = {},
    verMapaOnClick: () -> Unit = {}
) {
    // Establece el nombre del lugar que debe visualizarse en pantalla
    var destinoView = viewLugarVm.nombreLugar.value
    val contexto = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()

    // Se crea este valor que almacenará el objeto lugar extraído de la BD
    var (destino, setDestino) = remember {
        mutableStateOf(
            Lugar(
                id = 0,
                nombre = "",
                imagen = "",
                captura = "",
                latitud = 0.0,
                longitud = 0.0,
                orden = 0,
                alojamiento = 0.0,
                traslado = 0.0,
                comentarios = ""
            )
        )
    }
    // Llama al lugar desde la base de datos
    LaunchedEffect(destinoView) {
        withContext(Dispatchers.IO) {
            val db = ListadoDB.getInstance(contexto).lugarDao()
            val lugar = db.getByName(destinoView)

            // Toma la captura almacenada la transforma en Uri con formato legible
            val capturaStringNoFormat = lugar.captura
            val capturaStringFormat = formatearCaptura(capturaStringNoFormat)

            val capturaUri: Uri? =
                if (capturaStringNoFormat.isNotEmpty())
                    Uri.parse(capturaStringFormat)
                else null

            // Almacena la Uri en la vista
            viewLugarVm.fotoCapturada.value = capturaUri

            // se establece que destino contiene la info de lugar
            lugar?.let {
                setDestino(lugar)
            }
            Log.v("PantallaLugarUI", "LaunchedEffect")
        }
    }

    // Establece la imagen del lugar de acuerdo con la URL
    val photo = if (destino.imagen.isNotBlank()) {
        rememberImagePainter(data = destino.imagen)
    // Considera la opción de que se encuentre vacía (imagen por defecto)
    } else {
        painterResource(id = R.drawable.not_available)
    }
    // Organiza los elementos de la pantalla
    Scaffold (
        // Botón flotante para volver a la pantalla principal
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = /*androidx.compose.ui.*/Alignment.BottomEnd
            )  {
                ExtendedFloatingActionButton(
                    modifier = Modifier.width(150.dp),
                    // Regresa a pantalla principal o listado de lugares
                    onClick = { cameraAppVm.cambiarPantallaListado() },
                    // Icono asociado
                    icon = {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Volver Pantalla principal"
                        )
                    },
                    text = { Text(stringResource(id = R.string.volver_lugar)) }
                )
            }
        }
    ) { paddingValues -> // Marca rojo pero se ejecuta
        // Organiza los elementos a mostrar
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título o nombre de lugar
            item {
                Spacer(modifier = Modifier.height(15.dp))
                // Título de la Pantalla
                Text(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    text = (destino.nombre)
                )
                Divider(modifier = Modifier.height(3.dp))
                Spacer(modifier = Modifier.height(5.dp))

                // Imagen obtenida de Url o imagen por defecto
                Image(
                    painter = photo,
                    contentDescription = destino.nombre,
                    modifier = Modifier.size(width = 300.dp, height = 200.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))

                // Costo de alojamiento en pesos y dólares
                Text(
                    text = stringResource(id = R.string.alojamiento),
                    fontWeight = FontWeight.Bold
                )
                Text("$ " + destino.alojamiento + " CLP / $ "
                        + viewLugarVm.costoAlojamientoDolares.value + " USD")
                Spacer(modifier = Modifier.height(15.dp))

                // Costo de traslado en pesos y dólares
                Text(
                    text = stringResource(id = R.string.traslado),
                    fontWeight = FontWeight.Bold
                )
                Text("$ " + destino.traslado + " CLP / $ "
                        + viewLugarVm.costoTrasladoDolares.value + " USD")
                Spacer(modifier = Modifier.height(15.dp))

                // Comentarios almacenados en la tabla
                Text(
                    text = stringResource(id = R.string.comentarios),
                    fontWeight = FontWeight.Bold
                )
                Text(destino.comentarios)
                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = Color.Gray, thickness = 1.dp)
            }

            // Fila con botón e iconos con distintas funciones
            item {
                Row(
                    modifier = Modifier.padding(top = 5.dp, bottom = 1.dp)
                ) {
                    // Botón que lleva a la captura de imagen con la cámara
                    Button(
                        modifier = Modifier
                            .width(135.dp)
                            .height(35.dp),
                        onClick = {
                            // Llama a la función correspondiente
                            tomarFotoOnClick()
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.tomar_foto)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))

                    // Llamada al formulario para editar información del lugar
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Editar destino",
                        modifier = Modifier
                            .size(width = 40.dp, height = 40.dp)
                            .clickable {
                                cameraAppVm.cambiarPantallaFormularioParam()
                            }
                    )
                    Spacer(modifier = Modifier.width(30.dp))

                    // Elimina el objeto de la tabla y vuelve a la pantalla de inicio
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Eliminar destino",
                        modifier = Modifier
                            .size(width = 40.dp, height = 40.dp)
                            .clickable {
                                alcanceCorrutina.launch(Dispatchers.IO) {
                                    // Elimina el destino
                                    val dao = ListadoDB
                                        .getInstance(contexto)
                                        .lugarDao()
                                    dao.delete(destino)
                                    cameraAppVm.cambiarPantallaListado()
                                }
                            }
                    )
                }
                Spacer(modifier = Modifier.width(30.dp))
                Divider(color = Color.Gray, thickness = 1.dp)
            }
            // Intento por visualizar la imagen capturada
            item {
                viewLugarVm.fotoCapturada.value?.also {
                    Box(Modifier.size(400.dp, 300.dp)) {
                        Image(
                            painter = BitmapPainter(uri2imageBitmap(it,
                                contexto)),
                            contentDescription = "Imagen capturada",
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(0.9f)
                                .padding(8.dp)
                                .align(Alignment.Center),
                        )
                    }
                }
            }
            // Sección para visualizar el mapa del lugar
            item {
                Spacer(Modifier.height(50.dp))
                Divider(modifier = Modifier.height(3.dp))
                Spacer(Modifier.height(30.dp))

                // Botón para visualizar el mapa
                Button(
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp),
                    onClick = {
                        // Se solicitan las coordenadas desde la bd a la vista
                        viewLugarVm.latLugar.value = destino.latitud
                        viewLugarVm.longLugar.value = destino.longitud
                        // Se llama a la función correspondiente
                        // No se solicitan permisos, ya que son coordenadas ingresadas manualmente
                        verMapaOnClick()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.ver_mapa)
                    )
                }
                Spacer(Modifier.height(100.dp))
            }
        }
    }
}