package fifteen

class MemoryGame152 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../15memorygame.txt")
        var startNumbers = input.split(",").map { it.toInt() }.toList()
        val howMany = 30000000

        val numbers: MutableMap<Int, Int> = mutableMapOf()
        startNumbers.dropLast(1).forEachIndexed { index, i -> numbers[i] = index+1 }
        var lastNumberSpoken = startNumbers.last()

        for(turn in startNumbers.size .. howMany-1){
            var nextNumber = findNextNumber(numbers, lastNumberSpoken, turn)
            numbers[lastNumberSpoken] = turn
            lastNumberSpoken=nextNumber
        }
        println(lastNumberSpoken)
    }

    private fun findNextNumber(numbers: MutableMap<Int, Int>, lastNumberSpoken: Int, turn: Int):Int {
        val idxOfNextToLastMention = numbers.getOrDefault(lastNumberSpoken, -1)
        if (idxOfNextToLastMention == -1)
            return 0
        return turn - idxOfNextToLastMention
    }



    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()
}


fun main(args: Array<String>) {
    val baggage = MemoryGame152();
    baggage.run()

}






