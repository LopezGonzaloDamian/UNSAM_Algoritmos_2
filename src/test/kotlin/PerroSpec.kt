import io.kotest.core.spec.style.DescribeSpec
import org.example.Perro
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.example.perrito1

class PerroSpec : DescribeSpec( {

    describe("Testeo del perro"){
        it("perro sale a pasear "){
            val perroPratto = Perro()
            perroPratto.caminar(20)
            perroPratto.energia shouldBe 160

            perroPratto.comer(50)
            perroPratto.energia shouldBe 260

            perrito1.comer(5)
            perrito1.energia shouldBe 110

            perrito1.caminar(10)
            perrito1.energia shouldBe 90

        }


    }
})