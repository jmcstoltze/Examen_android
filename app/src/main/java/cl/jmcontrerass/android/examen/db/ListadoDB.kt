package cl.jmcontrerass.android.examen.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Representa las entidades de la base de datos
@Database(entities = [Lugar::class], version = 1)

// Base de datos Room
abstract class ListadoDB : RoomDatabase() {

    // Método que permite acceder al objeto de acceso de datos DAO
    abstract fun lugarDao():LugarDao

    // Permite obtener la instancia única de la base de datos
    companion object {
        @Volatile
        private var BASE_DATOS : ListadoDB? = null

        // Función para obtener una intancia de la base de datos
        fun getInstance(contexto: Context):ListadoDB {
            return BASE_DATOS ?: synchronized(this) {

                // Si la instancia es nula crea una nueva
                Room.databaseBuilder (
                    contexto.applicationContext,
                    ListadoDB::class.java,
                    "Listado.db"
                )
                    // Permite la eventual eliminación o recreación de la base de datos
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {BASE_DATOS = it}
            }
        }
    }
}