package cl.jmcontrerass.android.examen.vm

// Constantes enumeradas que representan las distintas pantallas de la app
enum class Pantalla {
    LISTADO,
    LUGAR,
    FOTOINDIVIDUAL,
    FORMULARIONOTPARAM,
    FORMULARIOPARAM,
    CAPTURA,
    MAPA,
    DENEGADO
}

// Gestión de excepción
class SinPermisoException(mensaje:String) : Exception(mensaje)