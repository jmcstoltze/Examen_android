package cl.jmcontrerass.android.examen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import cl.jmcontrerass.android.examen.app.AppUI
import cl.jmcontrerass.android.examen.vm.CameraAppViewModel

class MainActivity : ComponentActivity() {
    val cameraAppVm: CameraAppViewModel by viewModels()

    // Variable de inicio tardío que controla eventos de la cámara (capturar, detener y apagar)
    lateinit var cameraController: LifecycleCameraController

    // Sección relacionada con los permisos de uso de cámara y ubicación
    val lanzadorPermisos = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        when {
            (it[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false) or
                    (it[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false) -> {
                Log.v("callback RequestMultiplePermissions", "permiso ubicación granted")
                cameraAppVm.onPermisoUbicacionOk()
            }
            (it[android.Manifest.permission.CAMERA] ?: false) -> {
                Log.v("callback RequestMultiplePermissions", "permiso camara granted")
                cameraAppVm.onPermisoCamaraOk()
            }
            // Si los permisos no se conceden
            else -> {
                Log.v("callback RequestMultiplePermissions", "permiso any denied")
                cameraAppVm.onPermisoDenegado()
            }
        }
    }

    // Vincula ciclo de vida de la instancia controladora con los eventos de la cámara
    private fun setupCamara() {
        cameraController = LifecycleCameraController(this)
        cameraController.bindToLifecycle(this)

        // Se empleará la cámara trasera del dispositivo o emulador
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraAppVm.lanzadorPermisos = lanzadorPermisos
        setupCamara()
        // Llamada a la interfaz principal que maneja todas las pantallas
        setContent {
            AppUI(cameraController)
        }
    }
}