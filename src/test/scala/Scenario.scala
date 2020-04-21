import Conf._
import Requests._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
object Scenario {
  val httpConf: HttpProtocolBuilder = http
    .baseUrl("http://computer-database.gatling.io")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  def scn(loopCount: Int = 1): ScenarioBuilder = scenario("computer-databases")
    .repeat(loopCount) {
      randomSwitch(
        (70, browse),
        (25, createComp),
        (5, deleteComp),
      )
    }
}
