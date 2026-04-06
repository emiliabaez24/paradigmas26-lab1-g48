object TextAnalytics{

    // Definimos la lista de stopwords provista como un Set.
    val stopWords: Set[String] = Set(
        "the", "about", "above", "after", "again", "against", "all", "am", "an",
        "and", "any", "are", "aren't", "as", "at", "be", "because", "been",
        "before", "being", "below", "between", "both", "but", "by", "can't",
        "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't",
        "doing", "don't", "down", "during", "each", "few", "for", "from", "further",
        "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd",
        "he'll", "he's", "her", "here", "here's", "hers", "herself", "him",
        "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if",
        "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me",
        "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off",
        "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves",
        "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's",
        "should", "shouldn't", "so", "some", "such", "than", "that", "that's",
        "the", "their", "theirs", "them", "themselves", "then", "there", "there's",
        "these", "they", "they'd", "they'll", "re", "they've", "this", "those",
        "through", "to", "too", "under", "until", "up", "very", "was", "wasn't",
        "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what",
        "what's", "when", "when's", "where", "where's", "which", "while", "who",
        "who's", "whom", "why", "why's", "with", "won't", "would",
        "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours",
        "yourself", "yourselves"
    )

    def getWordsFrequencies(posts : List[FileIO.Post]): List[(String, Int)] = {
        // Extraer todo el texto relevante (título y contenido) de todos los posts.
        val AllText = posts.map { case (_, title, selftext, _, _) =>
            s"$title $selftext"
        }.mkString(" ")

        // Separo el gran texto en palabras individuales. 
        // "\\W+" es una expresión regular que significa "separar por cualquier carácter que NO sea una letra o número"
        val words = AllText.split("\\W+").toList.filter(_.nonEmpty)

        // Aplicar los filtros requeridos por la consigna
        val validWords = words.filter{ word =>
            val startsWithUpper = word.headOption.exists(_.isUpper)
            val isNotStopword = !stopWords.contains(word.toLowerCase)

            startsWithUpper && isNotStopword
        }

        validWords
            .groupBy(identity)                      // Agrupa las palabras idénticas -> Map("Scala" -> List("Scala", "Scala"), "Reddit" -> List("Reddit"))
            .view.mapValues(_.size).toMap           // Cuenta el tamaño de cada lista
            .toList                                 // Lo pasa a una lista de tuplas
            .sortBy { case (_, count) => -count}    // Ordena de forma descendente usando el contador negativo

    }

    def calculateTotalScore(posts: List[FileIO.Post]): Int = {
    // Usamos foldLeft con 0 como valor inicial
    posts.foldLeft(0) { (acumulador, post) =>
      // Extraemos el 5to elemento de la tupla (el score)
      val (_, _, _, _, score) = post 
      
      // Sumamos el acumulador actual con el score de este post
      acumulador + score
    }

  }
}