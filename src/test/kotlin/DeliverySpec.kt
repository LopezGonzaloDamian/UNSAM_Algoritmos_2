package ar.edu.algo2
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.uqbar.geodds.Point
import org.uqbar.geodds.Polygon
import java.time.LocalTime
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class DeliverySpec : DescribeSpec({

    val belgrano = ZonaDeTrabajo(
        Polygon(
            listOf(
                Point(-34.5553, -58.4616),
                Point(-34.5517, -58.4488),
                Point(-34.5673, -58.4402),
                Point(-34.5722, -58.4515)
            )
        )
    )

    val palermo = ZonaDeTrabajo(
        Polygon(
            listOf(
                Point(-34.5800, -58.4300),
                Point(-34.5700, -58.4100),
                Point(-34.6000, -58.4000),
                Point(-34.6100, -58.4200)
            )
        )
    )

    val recoleta = ZonaDeTrabajo(
        Polygon(
            listOf(
                Point(-34.5881, -58.3975),
                Point(-34.5860, -58.3800),
                Point(-34.6000, -58.3750),
                Point(-34.6100, -58.3900)
            )
        )
    )

    val pedido001 = Pedido(
        retiro = Point(-34.5950, -58.3850),  // Recoleta
        entrega = Point(-34.5600, -58.4500), // Belgrano
        horarioRetiro = LocalTime.of(14, 0),
        monto = 65000.0,
        esCertificado = true,
        local = "Café Recoleta",
        estado = EstadoPedido.PREPARADO
    )

    val pedido002 = Pedido(
        retiro = Point(-34.5620, -58.4550),  // Belgrano
        entrega = Point(-34.5650, -58.4480), // Belgrano
        horarioRetiro = LocalTime.of(19, 30),
        monto = 9000.0,
        esCertificado = true,
        local = "Restaurante Belgrano",
        estado = EstadoPedido.PREPARADO
    )

    val pedido003 = Pedido(
        retiro = Point(-34.5780, -58.4200),  // Palermo
        entrega = Point(-34.5820, -58.4150), // Palermo
        horarioRetiro = LocalTime.of(21, 0),
        monto = 30000.0,
        esCertificado = false,
        local = "Burger Palermo",
        estado = EstadoPedido.PREPARADO
    )

    val jony = Delivery(
        nombre = "Jony",
        username = "ElDalto",
        password = "1234",
        zonaDeTrabajo = mutableSetOf(palermo),
        condiciones = mutableSetOf(CondicionMontoMinimo(5000.0))
    )

    val nahue = Delivery(
        nombre = "Nahue",
        username = "Sigmoide",
        password = "9999",
        zonaDeTrabajo = mutableSetOf(belgrano, recoleta),
        condiciones = mutableSetOf(
            CondicionHorarioSeguro(LocalTime.of(9, 0), LocalTime.of(22, 0)),
            CondicionLocalesPermitidos(setOf("Pizzeria Palermo", "Burger House"))
        )
    )

    val casco = Delivery(
        nombre = "Casco",
        username = "Kraken",
        password = "5656",
        zonaDeTrabajo = mutableSetOf(palermo, belgrano, recoleta),
        condiciones = mutableSetOf(
            CondicionMontoMinimo(30000.0),
            CondicionPedidoCertificado(),
            CondicionHorarioSeguro(LocalTime.of(8, 0), LocalTime.of(23, 0))
        )
    )

//######################################################################################################################

    it("Cumple condiciones"){
        val (puedeEntregar, motivo) = casco.puedeEntregar(pedido001)
        println("Motivo: ${motivo ?: "Cumple condiciones"}")
    }

    it("Estado no preparado"){
        pedido001.estado = EstadoPedido.PENDIENTE
        val (puedeEntregar, motivo) = casco.puedeEntregar(pedido001)
        println("Motivo: ${motivo ?: "Cumple condiciones"}")
    }

    it("Condiciones - Delivery´s") {
        jony.mostrarCondiciones()
        nahue.mostrarCondiciones()
        casco.mostrarCondiciones()
    }

    it("Pedido dentro de zona") {
        val (puedeEntregar, motivo) =jony.puedeEntregar(pedido003)
        puedeEntregar shouldBe true
        println("Motivo: ${motivo ?: "Cumple condiciones"}")
    }

    it("Pedido fuera de zona") {
        val (puedeEntregar, motivo) = jony.puedeEntregar(pedido002)
        puedeEntregar shouldBe false
        println("Motivo: ${motivo ?: "Cumple condiciones"}")
    }

    it("Agregar zona") {
        jony.agregarZona(belgrano)
        val (puedeEntregar, motivo) = jony.puedeEntregar(pedido002)
        puedeEntregar shouldBe true
        println("Motivo: ${motivo ?: "Cumple condiciones"}")
    }
    it("Remover zona"){
        jony.sacarZona(belgrano)
        val (puedeEntregar, motivo) = jony.puedeEntregar(pedido002)
        puedeEntregar shouldBe false
        println("Motivo: ${motivo ?: "Cumple condiciones"}")
    }

    it("No trabaja con ese local"){
        val (puedeEntregar, motivo) = nahue.puedeEntregar(pedido002)
        puedeEntregar shouldBe false
        println("Motivo: ${motivo ?: "Cumple condiciones"}")
    }

    it("Agregar una condicion"){
        jony.mostrarCondiciones()
        jony.agregarCondicion(CondicionPedidoCertificado())
        jony.mostrarCondiciones()
        val (puedeEntregar, motivo) = jony.puedeEntregar(pedido003)
        puedeEntregar shouldBe false
        println("Motivo: ${motivo ?: "Cumple condiciones"}")
    }

    it("Sacar condiciones"){
        casco.mostrarCondiciones()
        casco.sacarCondicion(CondicionMontoMinimo(30000.0))
        casco.sacarCondicion(CondicionPedidoCertificado())
        casco.mostrarCondiciones()
        val (puedeEntregar, motivo) = casco.puedeEntregar(pedido003)
        puedeEntregar shouldBe true
        println("Motivo: ${motivo ?: "Cumple condiciones"}")
    }
})