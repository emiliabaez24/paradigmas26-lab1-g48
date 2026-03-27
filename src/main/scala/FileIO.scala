import scala.io.Source
import scala.util.Using
import org.json4s._
import org.json4s.jackson.JsonMethods._

object FileIO {
  type Subscription = (String, String) // (subreddit name, url)

  implicit val foramts: Formats = DefaultFormats

  // Pure function to read subscriptions from a JSON file
  def readSubscriptions(): List[Subscription] = {

    // leemos de forma segura para no dejar resource leaks.
    val jsonString = Using(Source.fromFile("subscriptions.json")) {source =>
      source.mkString
    }.getOrElse("[]")
    
    // parseamos el texto a un objeto JSON
    val parsedJson = parse(jsonString)

    val subscriptions = parsedJson.children.map { elemento =>
      val name = (elemento \ "name").extract[String]
      val url = (elemento \ "url").extract[String]

      (name, url)
    }
    
    subscriptions
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): String = {
    val source = Source.fromURL(url)
    source.mkString
  }
}
