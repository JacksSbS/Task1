import Scenario._
import io.gatling.core.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

class LoadTest extends Simulation{
  val asserts = Seq(
    global.requestsPerSec.gte(80),
    global.failedRequests.count.is(0),
    global.responseTime.percentile3.lte(10),
    details("Browse Pages").responseTime.percentile3.lte(50),
    details("Form to create New Computer").responseTime.percentile3.lte(10),
    details("Create New Computer").responseTime.percentile3.lte(50),
    details("Search computer").responseTime.percentile3.lte(60),
    details("Delete Computer").responseTime.percentile3.lte(30)
  )

  val injectionSteps = Seq(
    rampUsersPerSec(1) to 10 during (30 seconds),
    constantUsersPerSec(10) during (60 seconds),
  )

  setUp(scn().inject(injectionSteps).protocols(httpConf))
    .assertions(asserts)
}
