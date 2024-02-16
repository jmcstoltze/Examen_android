package cl.jmcontrerass.android.examen.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Uso de api para obtener valor del dólar actualizado
data class ObtenerIndicador(val serie: List<Serie>)
data class Serie(val fecha: String, val valor: Double)

// Interface de la solicitud
interface IndicadorApi {
    @GET("dolar")
    suspend fun obtenerValorDolar(): ObtenerIndicador
}

// Se emplea retrofit instalado en las dependencias previamente
object RetrofitClient {
    private const val BASE_URL = "https://mindicador.cl/api/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val indicadorApi: IndicadorApi = retrofit.create(IndicadorApi::class.java)
}

// Función para obtener el valor requerido
suspend fun obtenerValorDolar(): Double? {
    return try {
        val response = RetrofitClient.indicadorApi.obtenerValorDolar()
        response.serie.firstOrNull()?.valor
    } catch (e: Exception) {
        null
    }
}