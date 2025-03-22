import io.kotest.core.spec.style.DescribeSpec
import org.example.Perro
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class PerroSpec : DescribeSpec( {

    describe("Testeo del perro"){
        it("perro sale a pasear "){
            val perroPratto = Perro()
            perroPratto.caminar(20)
            perroPratto.energia shouldBe 160

            perroPratto.comer(50)
            perroPratto.energia shouldBe 260
        }


    }
})