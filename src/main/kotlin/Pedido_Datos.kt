package ar.edu.algo2
import org.uqbar.geodds.Point
import java.time.LocalDate
import java.time.LocalTime

class Pedido(
    var usuario: Usuario,
    var local: Local,
    var delivery: Delivery? =null,
    var retiro: Point,
    var entrega: Point,
    var horarioRetiro: LocalTime,
    var estado: EstadoPedido
) {
    companion object {
        var contadorPedidos = 0
    }
    var numero: Int = ++contadorPedidos

    val platos = mutableSetOf<Plato>()

    fun seleccionarSegunLocal(local: Local) {
        this.local = local
    }

    fun platosParaElegirAcordes(): Set<Plato> {
        val nuevosPlatos = (local.platos.filter { usuario.platoEsAcorde(it, local)}).toSet()

        if (nuevosPlatos.isEmpty())
            throw CondicionNoCumplidaException("No existen platos acordes a sus gustos. " +
                    "Lo sentimos. Intente con otro local.")
        else return nuevosPlatos
    }

    fun seleccionarPlato(plato: Plato){
        platos.add(plato)
    }

    fun deseleccionarPlato(plato: Plato){
        platos.remove(plato)
    }

    fun quitarTodosLosPlatosSeleccionados(){
        platos.clear()
    }

    var medioDePago: Pago? = null

    fun queMediosDePagoPuedoElegir(): Set<Pago> {
        return local.mediosDePago.toSet()
    }

    fun seleccionarMedioDePago(medio: Pago) {
        if (queMediosDePagoPuedoElegir().contains(medio)) {
            medioDePago = medio
        } else {
            throw IllegalStateException("El medio de pago no es aceptado. Utilice alguno de los sugeridos.")
        }
    }

    fun quienPuedeEntregarPedido(): Delivery {
        return (Plataforma.deliveriesLibres.first{ it.puedeEntregar(this) })
    }

    fun aceptaElPedidoEl(repartidor: Delivery){
        if (repartidor.puedeEntregar(this)){
            delivery = repartidor

        }
    }

    val esCertificado
        get() = (usuario.tiempoRegistrado >= 1) && esLocalConfiable()//Ojo todos "los cliente?"

    fun esLocalConfiable(): Boolean{
        return (local.puntuacion() in 4.0..5.0)
    }

    val montoAPagar: Double
        get() = totalApagarPorPedido()

    fun sumaDelValorDeVentaDePlatos(): Double{
        return platos.sumOf{it.valorDeVenta()}
    }

    fun costoDelVelivery(): Double {
        return platos.sumOf { it.calcularCosto() * 0.1 }
    }

    fun calculoDeSubtotal(): Double{
        return sumaDelValorDeVentaDePlatos() + costoDelVelivery()
    }

    fun totalApagarPorPedido(): Double =
        if (medioDePago == Pago.Efectivo) {1.0} else {1.05} * calculoDeSubtotal()

    val fechaDeConfirmacion: LocalDate
        get() = LocalDate.now()

}

enum class EstadoPedido (var grupo: String) {
    pendiente("PENDIENTE"),
    preparado("PREPARADO"),
    entregado("ENTREGADO"),
    cancelado("CANCELADO"),

}