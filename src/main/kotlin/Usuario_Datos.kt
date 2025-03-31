package ar.edu.algo2

import org.uqbar.geodds.Point
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.Period

class Usuario (

    var nombre: String= "",
    var apellido: String= "",
    var username: String= "",
    var password: String= "",
    var fechaDeNacimiento: LocalDate,
    var direccion: Direccion,
    var cantidadMaxDeKM: Int,//Double
    var localesPreferidos: MutableSet<Local>
) {
    var preferencias: MutableSet<Preferencia> = mutableSetOf()
    var pedidosRealizados: MutableList<Pedido> = mutableListOf()

    val edad: Int get() = Period.between(fechaDeNacimiento, LocalDate.now()).years

    val ingredientesPreferidos = mutableSetOf<Ingrediente>()
    val ingredientesAEvitar = mutableSetOf<Ingrediente>()

    fun agregarIngredientePreferido(ingrediente: Ingrediente){
        if(!ingredientesAEvitar.contains(ingrediente)){
            ingredientesPreferidos.add(ingrediente)
        }else {
            throw IllegalStateException("El ingrediente está en la lista a Evitar")
        }
    }

    fun agregaringredientesAEvitar(ingrediente: Ingrediente){
        if(!ingredientesPreferidos.contains(ingrediente)){
            ingredientesAEvitar.add(ingrediente)
        }else {
            throw IllegalStateException("El ingrediente está en la lista a de Favoritos")
        }
    }

    fun confirmarPedido(pedido: Pedido){
        if(platosSonAcorde(pedido.platos, pedido.local)){ //Ojo que puede haber varios platos
            pedido.fechaDeConfirmacion // Pase la generación de la fecha al Pedido
            pedidosRealizados.add(pedido)
        } else {
            throw CondicionNoCumplidaException("El plato no es acorde a las preferencias del usuario")
        }
    }

    private fun esPosiblePuntuar(local: Local, limiteDias: Int)=
        pedidosRealizados.any{(it.local == local) &&
                (ChronoUnit.DAYS.between(LocalDate.now(), it.fechaDeConfirmacion) < limiteDias)}

    fun puntuarLocal(local: Local, puntuacion: Int){
        if(esPosiblePuntuar(local, 7)){
            local.puntuaciones.put(this, puntuacion)
        } else{
            throw CondicionNoCumplidaException("No se puede puntuar local porque no hay pedido realizado o se paso de tiempo")
        }
    }

    fun platosSonAcorde(platos: Iterable<Plato>, local: Local) =
        platos.all { platoEsAcorde(it, local) }

    fun platoEsAcorde(plato: Plato, local: Local) =
        platoNoContieneIngredienteAEvitar(plato) && cumpleConPreferencias(plato, local, preferencias) //Se agregó preferencias

    fun platoNoContieneIngredienteAEvitar(plato: Plato) =
        !(plato.ingredientes.any{it in ingredientesAEvitar})

    fun cumpleConPreferencias(plato: Plato, local: Local, preferencias: MutableSet<Preferencia>): Boolean {
        return preferencias.all { it.cumple(plato, local) }
    }

    val fechaRegistro: LocalDate = LocalDate.now()

    val tiempoRegistrado: Int
        get() = Period.between(fechaRegistro, LocalDate.now()).years

}

class CondicionNoCumplidaException(msj: String) : Throwable()

interface Preferencia {
    fun cumple(plato: Plato, local: Local): Boolean
}

class PreferenciaVegana : Preferencia {
    override fun cumple(plato: Plato, local: Local): Boolean = !plato.ingredientes.any { it.esDeOrigenAnimal }
}

class PreferenciaExquisita : Preferencia {
    override fun cumple(plato: Plato, local: Local): Boolean = plato.esDeAutor
}

class PreferenciaConservadora(private val ingredientesPreferidos: Set<Ingrediente>) : Preferencia {
    override fun cumple(plato: Plato, local: Local): Boolean = plato .ingredientes.equals(ingredientesPreferidos)
}

class PreferenciaFiel(private val localesPreferidos: Set<Local>) : Preferencia {
    override fun cumple(plato: Plato, local: Local): Boolean = localesPreferidos.contains(local)
}

class PreferenciaMarketinera(private val frasesMarketing: Set<String>) : Preferencia {
    override fun cumple(plato: Plato, local: Local): Boolean = frasesMarketing.any { it in plato.descripcion }
}
/*
class PreferenciaImpaciente(val local: Local, val ubicacionUsuario: Point, val maximoKM: Int) : Preferencia {
    override fun cumple(plato: Plato, local: Local): Boolean = ubicacionUsuario.distance(local.direccion.ubicacion) <= maximoKM
}
*/