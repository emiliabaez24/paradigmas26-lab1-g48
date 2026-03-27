import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._

object FileIO {
  // Pure function to read subscriptions from a JSON file
  
  def readSubscriptions(): List[Subscription] = {
    val source = Source.fromFile("subscriptions.json")  //abro el archivo
    val textSubscriptions =source.mkString // paso todo el archivo a un bloque de String
    source.close() // cierro el archivo para no tener resource leaks
    val json = parse(textSubscriptions) //Convierto el texto en JSON
    val listMap = json.extract[List[Map[String, String]]](DefaultFormats) //Extraemos el JSON a una lista
    val subscriptionsList = listMap.map { 
      dict => (dict("name"), dict("url"))   //hago que ahora retorne lo que yo quiero
    }
    subscriptionsList //Devuelve la Lista que me pide el ejercicio
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): String = {
    val source = Source.fromURL(url)
    source.mkString
  }
}
