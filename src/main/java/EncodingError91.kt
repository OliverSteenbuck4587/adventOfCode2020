class EncodingError91 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("9encoding.txt")
        val numbers = input.split("\n").map { it.toLong() }
        val preampleSize = 25
        val preamble = numbers.subList(0, preampleSize)
        val code = numbers.subList(preampleSize, numbers.size)
        println(checkNextNumber(preamble, code))


    }

    fun checkNextNumber(preamble: List<Long>, code: List<Long>): Long {
        if(code.isEmpty())
            throw RuntimeException("Did not find error in code")
        val nextCodePoint = code.get(0)
        if (getAllSums(preamble).contains(nextCodePoint)) {
            return checkNextNumber(preamble.drop(1).plus(nextCodePoint), code.drop(1))
        } else {
            return nextCodePoint
        }

    }

    fun getAllSums(numbers: List<Long>): Set<Long> {
        return numbers.flatMapIndexed { index, elem ->
            numbers.filterIndexed { index2, i -> index2 != index }
                    .map { it + elem }
        }.toSet()
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = EncodingError91();
    baggage.run()

}






