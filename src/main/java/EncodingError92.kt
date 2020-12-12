class EncodingError92 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("9encoding.txt")
        val numbers = input.split("\n").map { it.toLong() }
        val target = 393911906.toLong()
        val code = numbers.filter { it != target }
        val summands = findSummands(target, code)
        val max:Long = summands.maxOrNull()!!
        val min:Long = summands.minOrNull()!!
        println(min + max)


    }

    fun findSummands(target: Long, code: List<Long>): List<Long> {
        if(code.isEmpty())
            throw RuntimeException("Did not find error in code")
        var sum = code.get(0)
        code.drop(1).forEachIndexed{index, elem ->
            sum = sum + elem
            if(sum == target){
                return code.subList(0,index+1)
            }
        }
        return findSummands(target, code.drop(1))
    }


    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = EncodingError92();
    baggage.run()

}






