class CustomsK61 {
    fun readFileAsLinesUsingGetResourceAsStream(fileName: String)
            = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()

}


fun main(args: Array<String>){
    val customs: BaggageK71 = BaggageK71();
    val input = customs.readFileAsLinesUsingGetResourceAsStream("6customs.txt")
    val groupsArray = input.split("\n\n")

    var answerSum = 0;
    for (group in groupsArray){
        val answerSet: Set<Char> = group.trim().toHashSet().minus('\n').minus(' ')
        answerSum = answerSum + answerSet.size
    }
    println(answerSum)

}



