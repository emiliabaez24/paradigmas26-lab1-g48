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
  
}