import io.kotest.core.spec.style.DescribeSpec
import org.example.Perro
import io.kotest.matchers.shouldBe

class PerroSpec : DescribeSpec( {

    describe("Testeo del perro"){
        it("ladrido"){
            val perro = Perro()
            perro.ladrar() shouldBe "Guau"
        }
    }

})