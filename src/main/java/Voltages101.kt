class Voltages101 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("10voltages.txt")
        val numbers = input.split("\n").map { it.toInt() }
        val device = numbers.maxOrNull()!!+3
        val adapters = numbers.plus(device).plus(0).sorted()

        val differences = adapters.windowed(2, 1).map { (one, two) -> two - one }
        val sumOfOnes = differences.filter { it == 1 }.size
        val sumOfThrees = differences.filter { it == 3 }.size
        println(differences)
        println(sumOfOnes.toString() + " * " + sumOfThrees.toString() + " = " + sumOfOnes * sumOfThrees)


    }




    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = Voltages101();
    baggage.run()

}






