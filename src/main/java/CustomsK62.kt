class CustomsK62 {
    fun readFileAsLinesUsingGetResourceAsStream(fileName: String)
            = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()

}


fun main(args: Array<String>){
    val customs: BaggageK71 = BaggageK71();
    val input = customs.readFileAsLinesUsingGetResourceAsStream("6customs.txt")
    val groupsArray = input.split("\n\n")

    var answerSum = 0;
    for (group in groupsArray){
        var answersOfGroup = ('a'.rangeTo('z').toSet())
        for(answersOfSingle in group.split("\n")) {
            val answerSet: Set<Char> = answersOfSingle.trim().toHashSet().minus('\n').minus(' ')
            answersOfGroup = answersOfGroup.intersect(answerSet)
        }
        answerSum = answerSum + answersOfGroup.size
    }
    println(answerSum)

}



