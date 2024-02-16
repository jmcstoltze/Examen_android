package cl.jmcontrerass.android.examen.vm

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// Modelo de vista para valores de pantalla y permisos de la interfaz
class CameraAppViewModel : ViewModel() {
    val pantalla = mutableStateOf(Pantalla.LISTADO)

    // Callbacks
    var onPermisoCamaraOk : () -> Unit = {}
    var onPermisoUbicacionOk: () -> Unit = {}
    var onPermisoDenegado: () -> Unit = {}

    // Lanzador permisos
    var lanzadorPermisos: ActivityResultLauncher<Array<String>>? = null

    // Valor mutable para gestionar la denegación de permisos
    val permisoConcedido = mutableStateOf(true)

    // Cambia los valores de pantalla
    fun cambiarPantallaListado(){ pantalla.value = Pantalla.LISTADO }
    fun cambiarPantallaLugar(){ pantalla.value = Pantalla.LUGAR }
    fun cambiarPantallaFotoIndividual() { pantalla.value = Pantalla.FOTOINDIVIDUAL }
    fun cambiarPantallaFormularioNotParam(){ pantalla.value = Pantalla.FORMULARIONOTPARAM }
    fun cambiarPantallaFormularioParam(){ pantalla.value = Pantalla.FORMULARIOPARAM }
    fun cambiarPantallaCaptura(){ pantalla.value = Pantalla.CAPTURA }
    fun cambiarPantallaMapa(){ pantalla.value = Pantalla.MAPA }
    fun cambiarPantallaDenegado(){ pantalla.value = Pantalla.DENEGADO }
}

// Modelo de vista para los atributos de los lugares
class LugarViewModel : ViewModel() {

    // Para el campo de texto de lugar
    val nombreLugar = mutableStateOf("")

    // Foto capturada, lista de fotos y sus índices
    val fotoCapturada = mutableStateOf<Uri?>(null)
    val indiceImagen = mutableStateOf<Int?>(null)

    // Relacionado con coordenadas del mapa
    val latLugar = mutableStateOf(0.0)
    val longLugar = mutableStateOf(0.0)

    // Valor del dolar que arroja la Api
    val valorDolar = mutableStateOf(0.0)

    // Costo de alojamiento y costo traslado
    val costoAlojamientoDolares = mutableStateOf("")
    val costoTrasladoDolares = mutableStateOf("")
}