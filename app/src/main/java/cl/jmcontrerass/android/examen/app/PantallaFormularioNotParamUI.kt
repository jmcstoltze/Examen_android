package cl.jmcontrerass.android.examen.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.jmcontrerass.android.examen.R
import cl.jmcontrerass.android.examen.db.ListadoDB
import cl.jmcontrerass.android.examen.db.Lugar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioNotParamUI(
    volverInicioOnClick: () -> Unit = {}
) {
    val contexto = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()
    // para manejar el estado del snackabar
    val snackbarHostState = remember { SnackbarHostState() }

    var nom by remember { mutableStateOf("") }
    var img by remember { mutableStateOf("") }
    var coordString by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf(0.0) }
    var long by remember { mutableStateOf(0.0) }
    var ord by remember { mutableStateOf(0) }
    var ordString by remember { mutableStateOf("") }
    var aloj by remember { mutableStateOf(0.0) }
    var alojString by remember { mutableStateOf("")}
    var trasl by remember { mutableStateOf(0.0) }
    var traslString by remember { mutableStateOf("") }
    var coment by remember { mutableStateOf("") }

    Scaffold(
        // snackbar que mostrará diversos mensajes
        snackbarHost = { SnackbarHost (snackbarHostState) },

        // botón flotante para reiniciar la app y volver a la lista
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .width(130.dp),
                onClick = { volverInicioOnClick() },
                icon = {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Volver al listado"
                    )
                },
                // texto multilenguaje
                text = { Text(stringResource(id = R.string.volver)) }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // textfield para ingresar el nombre del lugar
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.destino)
            )
            TextField(
                value = nom,
                onValueChange = {
                    nom =
                        it.lowercase().replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase()
                            else it.toString()
                        }
                },
                label = { Text(stringResource(id= R.string.nomDestino)) },
                modifier = Modifier
                    .width(320.dp)
                    .padding(horizontal = 10.dp)
            )
            // textfield para ingresar la url de la imagen referencial
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.imagen)
            )
            TextField(
                value = img,
                onValueChange = {
                    img = it.lowercase()
                },
                label = { Text(stringResource(id= R.string.imgDestino)) },
                modifier = Modifier
                    .width(320.dp)
                    .padding(horizontal = 10.dp)
            )
        // Textfield para ingresar las coordenadas
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.coordenadas)
        )
        TextField(
            value = coordString,
            onValueChange = {
                coordString = it // Contiene lo ingresado como un string
                //  Lo transforma a double dividiendo en dos el string y quitándole la coma
                val coordenadas = it.split(", ").map {it.trim().toDoubleOrNull() ?: 0.0}
                lat = coordenadas.getOrElse(0) {0.0}.toDouble()
                long = coordenadas.getOrElse(1) {0.0}.toDouble()
            },
            label = { Text(stringResource(id= R.string.coorDestino)) },
            modifier = Modifier
                .width(320.dp)
                .padding(horizontal = 10.dp)
        )
        // Textfield para ingresar el orden de visita
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.orden)
        )
        TextField(
            value = ordString,
            onValueChange = {
                ordString = it
                ord = it.toIntOrNull() ?: 0 },
            label = { Text(stringResource(id= R.string.ordDestino)) },
            keyboardOptions = KeyboardOptions (
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .width(320.dp)
                .padding(horizontal = 10.dp)
        )
        // Textfield para ingresar el costo de alojamiento
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.costo_alojamiento)
        )
        TextField(
            value = alojString,
            onValueChange = {
                alojString = it
                aloj = it.toDoubleOrNull() ?: 0.0 },
            label = { Text(stringResource(id= R.string.costoDestino)) },
            keyboardOptions = KeyboardOptions (
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .width(320.dp)
                .padding(horizontal = 10.dp)
        )
        // Textfield para ingresar el costo de traslados
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.costo_traslado)
        )
        TextField(
            value = traslString,
            onValueChange = {
                traslString = it
                trasl = it.toDoubleOrNull() ?: 0.0 },
            label = { Text(stringResource(id= R.string.costoDestino)) },
            keyboardOptions = KeyboardOptions (
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .width(320.dp)
                .padding(horizontal = 10.dp)
        )
        // Textfield para ingresar comentarios
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.comentarios)
        )
        TextField(
            value = coment,
            onValueChange = {
                coment =
                    it.lowercase().replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase()
                        else it.toString()
                    }
                            },
            label = { Text(stringResource(id= R.string.comentDestino)) },
            modifier = Modifier
                .width(320.dp)
                .padding(horizontal = 10.dp)
        )
        // Botón para guardar la información
        Spacer(modifier = Modifier.height(20.dp))
        Button (
            onClick = {

                alcanceCorrutina.launch(Dispatchers.IO) {
                    val dao = ListadoDB.getInstance(contexto).lugarDao()
                    val existe = dao.getByName(nom)
                    val lugar = Lugar(
                        id = 0,
                        nombre = nom,
                        imagen = img,
                        captura = "",
                        latitud = lat,
                        longitud = long,
                        orden = ord,
                        alojamiento = aloj,
                        traslado = trasl,
                        comentarios = coment
                    )

                    if (nom.isNotBlank() &&
                        lat != 0.0 && long != 0.0 && ord > 0 &&
                        aloj >= 0.0 && trasl >= 0.0) {

                        // considera la opción de que el lugar ya fue ingresado previamente
                        if (existe == null) {
                            val mensaje: String = contexto.getString(R.string.agregado)
                            snackbarHostState.showSnackbar(mensaje)
                            dao.insert(lugar)
                            volverInicioOnClick()
                        } else {
                            val mensaje: String = contexto.getString(R.string.actualizado)
                            snackbarHostState.showSnackbar(mensaje)
                            dao.update(lugar)
                            volverInicioOnClick()
                        }

                    } else {
                        // mensaje que avisa que los campos son requeridos
                        val mensaje: String = contexto.getString(R.string.requerido)
                        snackbarHostState.showSnackbar(mensaje)
                    }
                }
            }) {
                Text(
                    text = stringResource(id = R.string.guardar),
                    fontSize = 16.sp
                )
            }
        }
    }
}