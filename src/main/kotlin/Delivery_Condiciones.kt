package ar.edu.algo2
import org.uqbar.geodds.Point
import org.uqbar.geodds.Polygon
import java.time.LocalTime

interface CondicionEntrega {
    fun cumple(pedido: Pedido, delivery: Delivery): Boolean
}

class CondicionMontoMinimo(private val montoMinimo: Double) : CondicionEntrega {
    override fun cumple(pedido: Pedido, delivery: Delivery) = pedido.monto >= montoMinimo
}

class CondicionPedidoCertificado : CondicionEntrega {
    override fun cumple(pedido: Pedido, delivery: Delivery) = pedido.esCertificado
}

class CondicionHorarioSeguro(private val inicio: LocalTime, private val fin: LocalTime) : CondicionEntrega {
    override fun cumple(pedido: Pedido, delivery: Delivery) = pedido.horarioRetiro in inicio..fin
}

class CondicionLocalesPermitidos(private val localesPermitidos: Set<String>) : CondicionEntrega {
    override fun cumple(pedido: Pedido, delivery: Delivery) = pedido.local in localesPermitidos
}

class ZonaDeTrabajo(
    val coordenadas: Polygon,
)

class Delivery(
    val nombre: String,
    val username: String,
    val password: String,
    val zonaDeTrabajo: MutableSet<ZonaDeTrabajo> = mutableSetOf(),
    val condiciones: MutableSet<CondicionEntrega> = mutableSetOf()
) {
    fun agregarCondicion(condicion: CondicionEntrega) {
        condiciones.add(condicion)
    }
    fun sacarCondicion(condicion: CondicionEntrega) {
        condiciones.removeIf { it::class.simpleName == condicion::class.simpleName }
    }

    fun agregarZona(zona: ZonaDeTrabajo) {
        zonaDeTrabajo.add(zona)
    }
    fun sacarZona(zona: ZonaDeTrabajo) {
        zonaDeTrabajo.remove(zona)
    }

    fun enZona(pedido: Pedido): Boolean {
        return zonaDeTrabajo.any { it.coordenadas.isInside(pedido.retiro) } &&
                zonaDeTrabajo.any { it.coordenadas.isInside(pedido.entrega) }
    }

    fun puedeEntregar(pedido: Pedido): Pair<Boolean, String?> = when {
        !enZona(pedido) -> false to "El pedido está fuera de la zona de entrega."
        pedido.estado != EstadoPedido.PREPARADO -> false to "El pedido no está preparado para la entrega."
        else -> condiciones.find { !it.cumple(pedido, this) }
            ?.let { false to MotivoNoCumpleCondicion(it) }
            ?: (true to null)
    }

    fun mostrarCondiciones() {
        println("Condiciones de $nombre: ${condiciones.map { it::class.simpleName }}")
    }

    fun MotivoNoCumpleCondicion(condicion: CondicionEntrega): String {
        return when (condicion) {
            is CondicionMontoMinimo -> "El monto del pedido es menor al mínimo requerido."
            is CondicionPedidoCertificado -> "El pedido no está certificado."
            is CondicionHorarioSeguro -> "El pedido está fuera del horario permitido."
            is CondicionLocalesPermitidos -> "El pedido proviene de un local no permitido."
            else -> "El pedido no cumple con una condición de entrega."
        }
    }
}


/* ---- ELIMINAR ---
enum class EstadoPedido {
    PENDIENTE, PREPARADO, ENTREGADO, CANCELADO
}
data class Pedido(
    val retiro: Point,
    val entrega: Point,
    val horarioRetiro: LocalTime,
    val monto: Double,
    val esCertificado: Boolean,
    val local: String,
    var estado: EstadoPedido
)
*/
