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

  type Post = (String, String, String, String)

  def parsePost(Text: String): List[Post] = {
    implicit val formats: Formats = DefaultFormats // formatos se pasan como implicito
    val json = parse(Text) // convierto el texto en json para poder manipularlo
    val postsArray = (json \ "data" \ "children").children //Me ubico directo a la lista de posts
    val listaDePosts: List[Post] = postsArray.map { postJson => //Empiezo a ubicar los campos con map para extraerlos
      val subreddit = (postJson \ "data" \ "subreddit").extract[String] //Ubico y guardo con extract el subreddit
      val title = (postJson \ "data" \ "title").extract[String] //Ubico y guardo el title con extract
      val selftext = (postJson \ "data" \ "selftext").extract[String] //Ubico y guardo el selftext con extract
      val createdUtc = (postJson \ "data" \ "created_utc").extract[Double].toLong //Ubico y guardo el utc
      val date = TextProcessing.formatDateFromUTC(createdUtc) //Convierto el double a un string donde figure la fecha y la hora
      (subreddit, title, selftext, date) //Devuelvo la tupla con los campos  
    }
  listaDePosts // Retorno la lista que me pide el ejercicio
  }
}