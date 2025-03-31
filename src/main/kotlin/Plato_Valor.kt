package ar.edu.algo2

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Plato(

    var nombre: String="",
    var descripcion: String="",
    var esDeAutor: Boolean,
    var valorBase: Double = 0.0,
    var porcentajeDescuento: Double = 0.0,
    var fechaLanzamiento: LocalDate,
    val diasConsideradoNuevo: Int = 30,
    val diasConsideradoNoTanNuevo: Int = 21,
    val porcentarjeNoTanNuevo: Double = 10.0,
    var local: Local
){
    val ingredientes = mutableListOf<Ingrediente>()

    fun calcularCosto(): Double {
        return ingredientes.sumOf { it.costoDeMercado }
    }

    fun agregarIngrediente(ingrediente: Ingrediente){
        ingredientes.add(ingrediente)
    }

    fun eliminarIngrediente(ingrediente: Ingrediente){
        ingredientes.remove(ingrediente)
    }

    fun esPromo() = porcentajeDescuento > 0

    fun esNuevo(lanzamiento: Int) = lanzamiento <= diasConsideradoNuevo

    fun calcularDescuentoPorcentaje(valor: Double, porcentaje: Double): Double {
        return (valor * porcentaje) / 100.0
    }

    fun chequeoPlatoAutor(valor: Double, porcentaje: Double): Double {
        return if (esDeAutor) {
            calcularDescuentoPorcentaje(valor, porcentaje)
        } else {
            0.0
        }
    }

    fun calcularDiferenciaDias(fecha1: LocalDate, fecha2: LocalDate): Long {
        return ChronoUnit.DAYS.between(fecha1, fecha2)
    }

    fun aplicarDescuento(valor: Double): Double {
        val diasLanzamiento = calcularDiferenciaDias(fechaLanzamiento, LocalDate.now()).toInt()

        return when {
            esNuevo(diasLanzamiento) && diasLanzamiento <= diasConsideradoNoTanNuevo -> {
                calcularDescuentoPorcentaje(valor, (diasConsideradoNuevo - diasLanzamiento).toDouble())
            }
            esNuevo(diasLanzamiento) -> {
                calcularDescuentoPorcentaje(valor, porcentarjeNoTanNuevo)
            }
            else -> {
                calcularDescuentoPorcentaje(valor, porcentajeDescuento)
            }
        }
    }

    fun valorPlataforma() = calcularDescuentoPorcentaje(valorBase, local.porcentajePlataforma) +
            chequeoPlatoAutor(valorBase, local.porcentajeAutor)

    fun valorDeVenta() = valorBase + valorPlataforma() - aplicarDescuento(valorBase)
}

enum class GrupoAlimenticio (var grupo: String) {
    cerealesYtuberculos("CEREALES Y TUBERCULOS"),
    azucaresYdulces("AZUCARES Y DULCES"),
    lacteos("LACTEOS"),
    frutasYverduras("FRUTAS Y VERDURAS"),
    grasasYaceites("GRASAS Y ACEITES"),
    proteinas("PROTEINAS");
}

class Ingrediente(var nombre: String,
                  var costoDeMercado: Double,
                  var grupoAlimenticio: GrupoAlimenticio,
                  var esDeOrigenAnimal: Boolean) {
    init{
        if(nombre.isBlank()){
            throw IllegalArgumentException("Debe tener un nombre")
        }
        if(costoDeMercado <= 0){
            throw IllegalArgumentException("Debe tener un costo positivo")
        }
    }
}