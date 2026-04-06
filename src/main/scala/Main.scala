object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[FileIO.Subscription] = FileIO.readSubscriptions()

    val allPosts: List[(String, String)] = subscriptions.flatMap { case (name, url) => 
      println(s"Fetching posts from: $name ($url)")
      val tryDownloading = FileIO.downloadFeed(url)
      
      tryDownloading match { //Intento abrir el contenido con pattern matching
        case Some(posts) => List((url, posts)) 
        case None => List.empty 
      }
    }

    // val output = allPosts
    //   .map { case (url, posts) => Formatters.formatSubscription(url, posts) }
    //   .mkString("\n")

    // println(header)
    // println(output)

    val outputParseado = allPosts.map { case (url, postsCrudos) =>    //Recorro el allPosts que ya tiene los JSON descargados
      val listaDePosts: List[FileIO.Post] = FileIO.parsePost(postsCrudos)      //Paso el texto a una nueva máquina de parseo
      val postsFiltrados: List[FileIO.Post] = TextProcessing.filterPosts(listaDePosts) //filtro los posts invalidos

      val postsFormateados = postsFiltrados.map { post =>       //Formateo cada post individualmente
        val (subreddit, title, selftext, date, _) = post 
        Formatters.formatPost(title, selftext, date) 
      }.mkString("\n")
      s"--- Posts extraídos de: $url ---\n" + postsFormateados       //Retornamos el bloque de texto para esta URL
    }.mkString("\n\n") //Separamos los bloques de diferentes URLs con un salto de línea
    println(outputParseado)    //Imprimimos el resultado final

    //Agregado para ejercicio 6
    subscriptions.zip(allPosts).foreach { case (sub, (url, postsCrudos)) =>  //recorremos las subs con sus posts descargados
      val nombreSub = sub._1 //ahora tengo el nombre de la subscripcion.
      val listaDePosts = FileIO.parsePost(postsCrudos) //transformo el texto de JSON, a algo mas trabajable
      val postsFiltrados = TextProcessing.filterPosts(listaDePosts) //Filtro los vacios o invalidos

      val scoreTotal = TextAnalytics.calculateTotalScore(postsFiltrados) //obtengo el score
      val frecuencias = TextAnalytics.getWordsFrequencies(postsFiltrados) //obtengo las palabras

      println(s"REPORTE DE $nombreSub")
      println(s"\nScore: $scoreTotal")

      println("\nPalabras mas frecuentes:")
      frecuencias.take(5).foreach { case (palabra, cantidad) =>  //imprimo las palabras
        println(s" * $palabra: $cantidad") 
      }

      println("\nPrimeros 5 posts:")
      postsFiltrados.take(5).zipWithIndex.foreach { case (post, i) =>
        val (subreddit, title, selftext, date, score) = post
        println(s"${i + 1}. $title | Fecha: $date | URL: $url")
      }
  
      println("-" * 30) //imprimo lineas para que quede mas estetico
    }
   //Agregado para punto estrella
    println("\n" + "=" * 40)
    val respuesta = scala.io.StdIn.readLine("¿Desea ingresar al Menú Interactivo de Lectura? (S/N): ")

    if (respuesta.toLowerCase == "s") { //si se desea entrar adapto los datos de entrada y llamo a mi funcion.
      val subsParaMenu = subscriptions.map(_.asInstanceOf[(String, String)]) //aca agarro toda mi lista de suibscripciones y las envio a una nueva lista con formato (Nombre, URL)
      val postsParaMenu = allPosts.map(tupla => tupla._2) //aca ignoro la primer tupla que son los url, y agarro el texto en JSON
      
      UserInterface.mostrarMenu(subsParaMenu, postsParaMenu) //llamo a mi funcion con las variables ya corregidas para que ande el programa.
    } else { //sino imprimo mensaje de despedida y me las tomo!!.
      println("Fin de la ejecución. ¡Hasta pronto!")
    }
  }
}

