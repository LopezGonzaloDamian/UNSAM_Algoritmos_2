package org.example
interface Canino {
    var energia: Int

    fun caminar(min: Int) {
        energia = energia - (min*2)
    }
}
class Perro(override var energia: Int=200) : Canino  {
    fun comer(gramos: Int) {
        energia += gramos * 2
    }
}