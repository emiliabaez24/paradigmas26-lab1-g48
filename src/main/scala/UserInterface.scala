import scala.annotation.tailrec // Es para usar el @tailrec
import scala.io.StdIn.readLine // Importa la funcion que permite pausar el programa y leer lo que pasa el user


object UserInterface{

  @tailrec //Esto es para chequear que usamos bien la recursividad en la cola
  def mostrarMenu(subscriptions: List[(String,String)], posts: List[String]): Unit ={
    val IndiceSuscriptions = subscriptions.zipWithIndex //le pongo indice a las suscripciones.
    subscriptions.zipWithIndex.foreach{
      case((name,url), indice) => println(s"${indice + 1}. $name")
    }
    val entrada = readLine("\nElige una opción\nSi desea salir ingrese 0\n")
    entrada.toIntOption match {
      case Some(0) =>
          println("Hasta pronto!")
      case Some(n) =>
          if(n>0 && n <= subscriptions.length){
            val subElegida = subscriptions(n-1)
            val postsDeSub = posts(n-1)
            mostrarMenuPosts(subElegida, postsDeSub)
            mostrarMenu(subscriptions, posts) //Por si salgo del menu de posts, para que vuelva al menu de subscripciones.
          }else{
            println("La opción seleccionada es invalida.\nPor favor, ingrese un número")
            mostrarMenu(subscriptions, posts)
          }
      case None =>
        println("La opción seleccionada es invalida.\nPor favor, ingrese un número")
        mostrarMenu(subscriptions, posts)
    }
  }
  @tailrec
  def mostrarMenuPosts(subElegida: (String,String), postsDeSub: String): Unit ={ 
    val (name, _) = subElegida
    println(s"\n Posts de $name")
    
    val listaDePosts = FileIO.parsePost(postsDeSub) //Uso las herramientas de mis compañeros para limpiar el texto JSON, y que sea mas leible
    val postsFiltrados = TextProcessing.filterPosts(listaDePosts)

    
    postsFiltrados.zipWithIndex.foreach { case (post, indice) =>  //Este ciclo es encargado de ponerle indices a los posts
      val (_, title, _, _, _) = post // Extraemos solo el título de la tupla
      println(s"${indice + 1}. $title") //sumamos 1 porque empieza a enumerar desde 0 sino.
    }

    val entrada = scala.io.StdIn.readLine("\nElige una opción\nSi desea volver al menú ingrese 0\n")
    entrada.toIntOption match {
      case Some(0) =>
          println("Volviendo al menú principal ...") //Aca la funcion se cierra usando recursividad de la cola y vuelve
      case Some(num) if num > 0 && num <= postsFiltrados.length => //aseguramos que el user no rompa el programa
          
          val postElegido = postsFiltrados(num - 1) //le resto 1 para elegir realmente el post deseado
          val (subreddit, title, selftext, date, _) = postElegido  //el post viene con estas 4 tuplas
          //entonces uso el formato que usaron mis compañeros para mostrar en pantalla el post
        
          val postFormateado = s"""
            |=========================================================
            | Título: $title
            | Fecha:  $date
            |---------------------------------------------------------
            | $selftext
            |=========================================================
            """.stripMargin
          
          
          println("\n" + postFormateado)
          
          
          scala.io.StdIn.readLine("\nPresione ENTER para volver a la lista de posts...") // fundamental para darle tiempo al user a leer, sino estaria se cierra enseguida
          mostrarMenuPosts(subElegida, postsDeSub)
      case _ =>
        println("La opción seleccionada es invalida.\nPor favor, ingrese un número")
        mostrarMenuPosts(subElegida, postsDeSub)
    }
  }  
}