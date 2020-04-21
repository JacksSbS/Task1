import com.typesafe.config.ConfigFactory

object Conf {

  private val conf = ConfigFactory.load()

  val baseUrl: String = conf.getString("http://computer-database.gatling.io")

}