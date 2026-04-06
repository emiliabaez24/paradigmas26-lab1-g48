import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TextProcessing {

  // Función pura que recibe un Long (los segundos) y devuelve un String (la fecha bonita)
  def formatDateFromUTC(createdUtc: Long): String = {
    val instant = Instant.ofEpochSecond(createdUtc)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())
    formatter.format(instant)
  }

  /*Eliminar los posts que:
  No tengan texto (selftext vacío).
  Tengan solo espacios.
  No tengan título.*/

  def isValid(text: String):Boolean = {
    text.trim.nonEmpty //trim borra espacios adelante y atras, nonEmpty ve q no quede vacio
  }

  def isValidPost(post: FileIO.Post): Boolean = {
    val(_, title, selftext, _, _) = post
    isValid(title) && isValid(selftext) 
  }

  def filterPosts(posts: List[FileIO.Post]): List[FileIO.Post] = {
    posts.filter(isValidPost)
  }  
}
