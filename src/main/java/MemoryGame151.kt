
class MemoryGame151 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("15memorygame.txt")
        var numbers = input.split(",").map { it.toInt() }

        while (numbers.size != 2020){
            numbers = numbers.plus(findNextNumber(numbers))
            println(numbers.last())

        }

        println("Numbers: $numbers")
        println("2020 Number: " + numbers[2019])

    }

    private fun findNextNumber(numbers: List<Int>): Int {
        val lastNumber = numbers.last()
        val idxOfNextToLastMention = numbers.dropLast(1).indexOfLast { it == lastNumber }
        if(idxOfNextToLastMention == -1)
            return 0
        return numbers.size-1 - idxOfNextToLastMention
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()
}



fun main(args: Array<String>) {
    val baggage = MemoryGame151();
    baggage.run()

}






