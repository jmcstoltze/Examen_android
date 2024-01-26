package cl.jmcontrerass.android.examen.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Interfaz de objeto de acceso de datos para la entidad Lugar de la base de datos
@Dao
interface LugarDao {

    // Consulta para obtener el conteo total de lugares de la DB
    @Query("SELECT COUNT(*) FROM lugar")
    fun count():Int

    // Consulta para obtener los lugares por orden de visita
    @Query("SELECT * FROM lugar ORDER BY orden")
    fun getAll():List<Lugar>

    // Consulta para obtener lugar por id
    @Query("SELECT * FROM lugar WHERE id = :id")
    fun findById(id:Int):Lugar

    // Consulta para obtener lugar por nombre
    @Query("SELECT * FROM lugar WHERE nombre = :nombre")
    fun getByName(nombre:String):Lugar

    // Para actualizar el campo captura en formato String
    @Query("UPDATE Lugar SET captura = :captura WHERE nombre = :nombre")
    fun updateImagen(nombre: String, captura: String)

    // Operación de inserción de producto
    @Insert
    fun insert(lugar:Lugar):Long

    // Operación de inserción de varios productos
    @Insert
    fun insertAll(vararg productos:Lugar)

    // Operación de actualización de productos
    @Update
    fun update(vararg lugar:Lugar)

    // Operación de eliminación de uno o más productos
    @Delete
    fun delete(lugar:Lugar)
}