import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import java.util.concurrent.ThreadLocalRandom

import akka.dispatch.sysmsg.Create
class mainPage extends Simulation {

  object Browse {
    val browse = repeat(4, "i") { // Note how we force the counter name so we can reuse it
      exec(
        http("Page ${i}")
          .get("/computers?p=${i}")
      ).pause(1)
    }
  }

  object createComp {
    //val randomName = ThreadLocalRandom.current.nextInt(1000)
    val createComp = tryMax(2)
    {
      exec(
        http("Form to create New Computer")
          .get("/computers/new")
      ).pause(1)
          .exec(
            http("Create New Computer")
              .post("/computers")
              .formParam("name", "Wander" + ThreadLocalRandom.current.nextInt(1000) + "Computer")
              .formParam("introduced", "2020-04-18")
              .formParam("discontinued", "")
              .formParam("company", ThreadLocalRandom.current.nextInt(43))
              .check(status.is(303))
          )
    }.exitHereIfFailed
  }
  object deleteComp {
    val feeder = csv("search.csv").random
    val deleteComp =
      exec(
        http("Home Page")
          .get("/")
      ).pause(1)
      .feed(feeder)
        .exec(
          http("Search computer")
            .get("/computers?f=Wander}")
            .check(css("a:contains('Wander229Computer')", "href").saveAs("computerURL"))
            .check(regex("""\/computers\/(\d.*)"""").findRandom.saveAs("computerURL"))
        )
        .pause(1)
        .exec(
          http("Select computer")
            .get("${computerURL}")
            .check(status.is(200))
        )
        .pause(1)
        exec(
          http("Delete Computer")
            .post("${computerURL}/delete")
            .check(status.is(303))
          )
  }
  val httpProtocol = http
    .baseUrl("http://computer-database.gatling.io")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val users = scenario("Users").exec(Browse.browse,createComp.createComp,deleteComp.deleteComp)


  setUp(
    users.inject(rampUsers(10) during (100 seconds)),
    users.inject(rampUsers(2) during (10 seconds))
  ).protocols(httpProtocol)
}
