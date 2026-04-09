import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.util.Using


object FileIO {
  type Subscription = (String, String)
  def readSubscriptions(): List[Subscription] = {
    val source = Source.fromFile("subscriptions.json")  //abro el archivo
    val textSubscriptions =source.mkString // paso todo el archivo a un bloque de String
    source.close() // cierro el archivo para no tener resource leaks
    implicit val format: Formats = DefaultFormats // Los formatos se pasan como un valor implicito
    val json = parse(textSubscriptions) //Convierto el texto en JSON
    val listMap = json.extract[List[Map[String, String]]] //Extraemos el JSON a una lista
    val subscriptionsList = listMap.map {
      dict => (dict("name"), dict("url"))   //hago que ahora retorne lo que yo quiero
    }
    subscriptionsList //Devuelve la Lista que me pide el ejercicio
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): Option[String] = { 
    try {
      val source = Source.fromURL(url)//Intento conectar a internet y descargar el texto
      val downloadedText = source.mkString //Guardo el contenido de la URL en un unico string continuo.
      source.close()//Cierro la conexion
      Some(downloadedText)//En caso de no tener algun error retorna el contenido
      } catch {
        case error: Exception => None//En caso de cualquier error retorna el contenido vacio
      }
  }

  type Post = (String, String, String, String, Int) // (subreddit, title, selftext, date, score)

  def parsePost(Text: String): List[Post] = {
    implicit val formats: Formats = DefaultFormats 
    val json = parse(Text) 
    val postsArray = (json \ "data" \ "children").children 
    
    val listaDePosts: List[Post] = postsArray.flatMap { postJson => 
      try{
        val subreddit = (postJson \ "data" \ "subreddit").extract[String] 
        val title = (postJson \ "data" \ "title").extract[String] 
        val selftext = (postJson \ "data" \ "selftext").extract[String] 
        val createdUtc = (postJson \ "data" \ "created_utc").extract[Double].toLong 
        val date = TextProcessing.formatDateFromUTC(createdUtc) 
        
        val score = (postJson \ "data" \ "score").extract[Int] 
        
        Some((subreddit, title, selftext, date, score)) 
      }
      catch {
        case _: Exception => None 
      }
    }

    listaDePosts 
  }
}