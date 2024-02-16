package cl.jmcontrerass.android.examen.db

import androidx.room.Entity
import androidx.room.PrimaryKey

// Clase que representa a la entidad Lugar en la base de datos
@Entity
data class Lugar(
    // Clave primaria autoincremental
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    var nombre: String, // Nombre del lugar
    var imagen: String, // Imagen del lugar URL como string
    var captura: String, // Uri almacenada en String
    var latitud: Double, // Latitud del lugar
    var longitud: Double, // Longitud del lugar
    var orden: Int, // Orden de visita
    var alojamiento: Double, // Precio de alojamiento
    var traslado: Double, // Precio de traslado
    var comentarios: String // Comentarios
)