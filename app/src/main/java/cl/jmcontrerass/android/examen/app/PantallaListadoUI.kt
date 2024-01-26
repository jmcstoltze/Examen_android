package cl.jmcontrerass.android.examen.app

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.jmcontrerass.android.examen.R
import cl.jmcontrerass.android.examen.db.ListadoDB
import cl.jmcontrerass.android.examen.db.Lugar
import cl.jmcontrerass.android.examen.vm.CameraAppViewModel
import cl.jmcontrerass.android.examen.vm.LugarViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListadoUI(
    viewLugarVm: LugarViewModel,
    cameraAppVm: CameraAppViewModel,
    agregarLugarOnClick: () -> Unit = {}
) {
    val contexto = LocalContext.current
    val (lugares, setLugares) = remember { mutableStateOf(emptyList<Lugar>()) }

    // Para poder instanciar la entidad lugar
    LaunchedEffect(lugares) {
        withContext(Dispatchers.IO) {
            val dao = ListadoDB.getInstance(contexto).lugarDao()
            // Solicita los datos de la tabla para listarlos en pantalla
            setLugares(dao.getAll())
            Log.v("PantallaListadoUI", "LaunchedEffect")
        }
    }

    // Contiene los elementos de la pantalla principal listados
    Scaffold(
        // Configura el botón flotante de acción para agregar lugar
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(end = 40.dp)
                    .width(250.dp),
                onClick = { agregarLugarOnClick() },
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Agregar Lugar"
                    )
                },
                // Texto multilenguaje
                text = { Text(stringResource(id = R.string.agregar)) }
            )
        }
    ) { paddingValues -> // aunque marca en rojo igualmente funciona

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Título de la Pantalla
            Text(
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(id = R.string.titulo),
            )
            Spacer(modifier = Modifier.height(30.dp))
            Divider(modifier = Modifier.height(3.dp))

            // Llamado al listado de lugares
            ListadoLugaresUI(viewLugarVm, cameraAppVm, lugares)
        }
    }
}