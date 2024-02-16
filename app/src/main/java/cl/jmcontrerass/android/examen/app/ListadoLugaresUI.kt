package cl.jmcontrerass.android.examen.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cl.jmcontrerass.android.examen.R
import cl.jmcontrerass.android.examen.db.ListadoDB
import cl.jmcontrerass.android.examen.db.Lugar
import cl.jmcontrerass.android.examen.vm.CameraAppViewModel
import cl.jmcontrerass.android.examen.vm.LugarViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ListadoLugaresUI(
    viewLugarVm: LugarViewModel,
    cameraAppVm: CameraAppViewModel,
    lugares:List<Lugar>
) {
    val contexto = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()

    // Organiza el contenido de la lista
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        // Si la entidad lugares no está vacía
        if (lugares.isNotEmpty()) {
            alcanceCorrutina.launch(Dispatchers.IO) {
                val dao = ListadoDB.getInstance(contexto).lugarDao()

                // Iteración sobre la lista de lugares para mostrar en pantalla
                items(lugares) { lugar ->

                    // Llama a cada ítem de la lista lugares
                    ItemLugarUI(viewLugarVm, cameraAppVm, lugar)

                }

                // Espaciado adicionar para evitar la superposición con botón
                item {
                    Spacer(modifier = Modifier.height(200.dp))
                }
            }
        } else {

            // Si no hay lugares almacenados debe mostrar un mensaje lista vacía
            item {
                Spacer(modifier = Modifier.height(280.dp))
                Box(
                    modifier = Modifier.fillMaxSize(),
                    /*.padding(contentPadding),*/
                    contentAlignment = Alignment.Center,
                    ) {
                    Text(stringResource(id = R.string.vacio))
                }
            }
        }
    }
}