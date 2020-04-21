import io.gatling.core.structure.ChainBuilder
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import java.util.concurrent.ThreadLocalRandom
import Feeders._
object Requests {
  //val feeder = csv("search.csv").random
  val browse: ChainBuilder =
    //repeat(4, "i") { // Note how we force the counter name so we can reuse it
      exec(
        http("Browse Pages")
          .get("/computers?p=3")
          .check(status.is(200))
      ).pause(1)
  //  }
  val createComp : ChainBuilder =
  tryMax(2) {
    exec(
      http("Form to create New Computer")
        .get("/computers/new")
    ).pause(1)
      .exec(
        http("Create New Computer")
          .post("/computers")
          .formParam("name",  "Wander" + ThreadLocalRandom.current.nextInt(1000) + "Computer")
          .formParam("introduced", "2020-04-18")
          .formParam("discontinued", "")
          .formParam("company", ThreadLocalRandom.current.nextInt(43))
          .check(status.is(303))
      )
  }.exitHereIfFailed

  val deleteComp : ChainBuilder =
    exec(
      http("Home Page")
        .get("/")
    ).pause(1)
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

