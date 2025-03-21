import io.kotest.core.spec.style.DescribeSpec
import org.example.Perro
import io.kotest.matchers.shouldBe

class PerroSpec : DescribeSpec( {

    describe("Testeo del perro"){
        it("ladrido del perro grande "){
            val perroGrande = Perro()
            perroGrande.ladrar() shouldBe "Guau"
        }

        it("ladrido del perro chico"){
            val perroChico = Perro(grande = false)
            perroChico.ladrar() shouldBe "wuff"
        }
    }
})