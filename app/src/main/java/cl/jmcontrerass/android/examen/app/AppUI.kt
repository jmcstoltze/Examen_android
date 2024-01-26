package cl.jmcontrerass.android.examen.app

import android.Manifest
import android.util.Log
import androidx.camera.view.CameraController
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.jmcontrerass.android.examen.vm.CameraAppViewModel
import cl.jmcontrerass.android.examen.vm.LugarViewModel
import cl.jmcontrerass.android.examen.vm.Pantalla

@Composable
fun AppUI(cameraController: CameraController) {
    val viewLugarViewModel: LugarViewModel = viewModel()
    val cameraAppViewModel: CameraAppViewModel = viewModel()

    when(cameraAppViewModel.pantalla.value) {
        Pantalla.LISTADO -> {
            // Llamada a pantalla del listado
            PantallaListadoUI(
                viewLugarViewModel,
                cameraAppViewModel,
                // Redirige al formulario vacío para agregar un lugar
                agregarLugarOnClick = {
                    cameraAppViewModel.cambiarPantallaFormularioNotParam()
                }
            )
        }
        // Llamada a pantalla de lugar individual
        Pantalla.LUGAR -> {
            PantallaLugarUI(
                viewLugarViewModel,
                cameraAppViewModel,
                // Función para la toma de imagen
                tomarFotoOnClick = {
                    cameraAppViewModel.cambiarPantallaCaptura()
                    // Solicita el permiso de uso de cámara
                    cameraAppViewModel.lanzadorPermisos?.launch(arrayOf(Manifest.permission.CAMERA))
                    // si se deniega redirige a pantalla de permiso denegado
                    cameraAppViewModel.onPermisoDenegado = {
                        cameraAppViewModel.permisoConcedido.value = false
                        cameraAppViewModel.cambiarPantallaDenegado()
                    }
                },
                visualizarFotoOnClick = {
                    cameraAppViewModel.cambiarPantallaFotoIndividual()
                    viewLugarViewModel.indiceImagen.value = it
                },
                verMapaOnClick = {
                    cameraAppViewModel.cambiarPantallaMapa()
                }
            )
        }
        // Representa la pantalla con el formulario en blanco
        Pantalla.FORMULARIONOTPARAM -> {
            PantallaFormularioNotParamUI(
                volverInicioOnClick = {
                    cameraAppViewModel.cambiarPantallaListado()
                }
            )
        }
        // Representa la pantalla con el formulario de lugar editable
        Pantalla.FORMULARIOPARAM -> {
            PantallaFormularioParamUI(
                viewLugarViewModel,
                volverInicioOnClick = {
                    cameraAppViewModel.cambiarPantallaListado()
                }
            )
        }
        // Llamada a pantalla de captura de imágenes con la cámara
        Pantalla.CAPTURA -> {
            PantallaCapturaUI(
                viewLugarViewModel,
                cameraAppViewModel,
                cameraController
            )
        }
        // Llamada a pantalla de visualización de mapa
        Pantalla.MAPA -> {
            PantallaMapaUI(
                viewLugarViewModel,
                volverOnClick = {
                    cameraAppViewModel.cambiarPantallaLugar()
                }
            )
        }
        // Visualización de pantalla de permiso denegado
        Pantalla.DENEGADO -> {
            PantallaDenegadoUI(
                volverOnClick = {
                    cameraAppViewModel.cambiarPantallaLugar()
                }
            )
        }
        // Esta parte del código no debiese usarse
        else -> {
            Log.v("AppUI()", "when else, no debería entrar aquí")
        }
    }
}