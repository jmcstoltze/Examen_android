package cl.jmcontrerass.android.examen.app

import androidx.camera.view.CameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import cl.jmcontrerass.android.examen.R
import cl.jmcontrerass.android.examen.db.ListadoDB
import cl.jmcontrerass.android.examen.vm.CameraAppViewModel
import cl.jmcontrerass.android.examen.vm.LugarViewModel
import cl.jmcontrerass.android.examen.vm.crearArchivoImagen
import cl.jmcontrerass.android.examen.vm.tomarFotografia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Representa la pantalla de captura de fotografía
@Composable
fun PantallaCapturaUI(
    viewLugarVm: LugarViewModel,
    cameraViewModel: CameraAppViewModel,
    cameraController:CameraController
) {
    val contexto = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()

    // Vista previa de la cámara vinculada al controlador de la cámara
    AndroidView(
        factory = {
            PreviewView(it).apply {
                controller = cameraController
            }
        },
        modifier = Modifier.fillMaxSize()
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        // Botón que invoca la función tomar fotografía
        Button(
            modifier = Modifier.height(120.dp).width(120.dp),
            onClick = {
                // Llamado a la función de toma de fotografía
                tomarFotografia(
                    cameraController,
                    viewLugarVm,
                    crearArchivoImagen(contexto),
                    contexto) {

                    // Almacena la imagen en la vista del lugar
                    viewLugarVm.fotoCapturada.value = it

                    // Corrutina para poder instanciar la BD
                    alcanceCorrutina.launch(Dispatchers.IO) {
                        // Actualiza la uri en la tabla pero en forma de string
                        val dao = ListadoDB.getInstance(contexto).lugarDao()
                        dao.updateImagen(viewLugarVm.nombreLugar.value,
                            viewLugarVm.fotoCapturada.toString())
                    }

                    // Regresa a la pantalla del lugar individual
                    cameraViewModel.cambiarPantallaLugar()
                }
            }
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                text = stringResource(id = R.string.capturar)
            )
        }
    }
}