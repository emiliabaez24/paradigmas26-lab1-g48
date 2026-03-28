
object Formatters {
  
  def formatSubscription(url: String, posts: String): String = {
    val header = s"\n${"=" * 80}\nPosts from: $url \n${"=" * 80}"
    val formattedPosts = posts.take(80) 
    header + "\n" + formattedPosts
  }

  def formatPost(title: String, selftext: String, date: String): String = {
    val textoCorto = if (selftext.length > 100) selftext.take(100) + "..." else selftext
    s"""
       |=========================================================
       | Título: $title
       | Fecha:  $date
       |---------------------------------------------------------
       | Texto: $textoCorto
       |=========================================================
       """.stripMargin
  }
}