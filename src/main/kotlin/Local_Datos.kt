package ar.edu.algo2
import org.uqbar.geodds.Polygon

class Local(
    var nombre: String,
    var direccion: Direccion,
    val porcentajePlataforma: Double = 0.0,
    val porcentajeAutor: Double = 0.0
) {
    val puntuaciones: MutableMap<Usuario, Int> = mutableMapOf()
    val mediosDePago: MutableList<Pago> = mutableListOf()

    fun puntuacion(): Double {
        if (puntuaciones.isNotEmpty()){
            return puntuaciones.values.average()
        }
        return 0.0
    }

    fun eliminarPuntuacion(usuario: Usuario) {
        puntuaciones.remove(usuario)
    }

    fun aceptarMedioDepago(medio: Pago) {
        if (!mediosDePago.contains(medio)) {
            mediosDePago.add(medio)
        } else {
            throw MedioDePagoExistenteException("Este medio de pago ya está agregado")
        }
    }

    fun rechazarMedioDePago(medio: Pago) {
        mediosDePago.remove(medio)
    }

    fun puntuarLocal(usuario: Usuario, puntuacion: Int) {
        if (puntuacion in 1..5) {
            puntuaciones[usuario] = puntuacion
        } else {
            throw PuntuacionFueraDeRangoException("Debe ser un valor entre 1 y 5")
        }
    }
}


class PuntuacionFueraDeRangoException(msj: String) : Throwable(msj)
class MedioDePagoExistenteException(msj: String) : Throwable(msj)

enum class Pago {
    efectivo,
    qr,
    transferenciaBancaria
}

data class Direccion(
    var nombreDeCalle: String,
    var numeroDecalle: Int,
    var ubicacion: Polygon
)