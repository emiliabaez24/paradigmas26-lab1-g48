object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[FileIO.Subscription] = FileIO.readSubscriptions()

    val allPosts: List[(String, String)] = subscriptions.map { case (name, url) =>
      println(s"Fetching posts from: $name ($url)")
      val posts = FileIO.downloadFeed(url)
      (url, posts)
    }

    val output = allPosts
      .map { case (url, posts) => Formatters.formatSubscription(url, posts) }
      .mkString("\n")

    println(header)
    println(output)

    val outputParseado = allPosts.map { case (url, postsCrudos) =>    //Recorro el allPosts que ya tiene los JSON descargados
      val listaDePosts: List[FileIO.Post] = FileIO.parsePost(postsCrudos)      //Paso el texto a una nueva máquina de parseo
      val postsFormateados = listaDePosts.map { post =>       //Formateo cada post individualmente
        val (subreddit, title, selftext, date) = post 
        Formatters.formatPost(title, selftext, date) 
      }.mkString("\n")
      s"--- Posts extraídos de: $url ---\n" + postsFormateados       //Retornamos el bloque de texto para esta URL
    }.mkString("\n\n") //Separamos los bloques de diferentes URLs con un salto de línea
    println(outputParseado)    //Imprimimos el resultado final
  }
}