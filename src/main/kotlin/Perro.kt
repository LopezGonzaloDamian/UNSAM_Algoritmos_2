package org.example

class Perro (var grande: Boolean = true) {
    fun ladrar()= if (grande) "Guau" else "wuff"
}