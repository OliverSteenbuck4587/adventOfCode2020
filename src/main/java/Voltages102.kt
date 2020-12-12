import java.lang.RuntimeException

class Voltages102 {

    val voltagesToNumberOfOptions: MutableMap<Int, Long> = mutableMapOf()
    var foo = 0.toLong()
    var iteration = 0.toLong()
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("10voltages.txt")
        val numbers = input.split("\n").map { it.toInt() }
        val device = numbers.maxOrNull()!! + 3
        val adapters = numbers.plus(device).toSet()

        println(numbers.size)
        println(numbers.distinct().size)
//        val options = getNextElements(0, adapters, device)
        val options = listOf(1);
        val resultWithInt = getNextElementsInt(0, adapters, device)
        println("Correct Result: " + options.size + " result with int: " + resultWithInt)



    }

    fun getNextElements(start: Int, adapters: Set<Int>, device: Int): Set<List<Int>> {
        //works with val adapters = numbers.plus(device).toSet()
        if (start == device)
            return setOf(listOf(device))
        val nextOptions = (1..3).filter { adapters.contains(start + it) }.map { it + start }.toSet()
        val continuations = nextOptions.flatMap { nextOption -> getNextElements(nextOption, adapters.minus(nextOption), device) }.map { listOf(start).plus(it) }.toSet()

        return continuations
    }

    fun getNextElementsInt(start: Int, adapters: Set<Int>, device: Int): Long {
        if(voltagesToNumberOfOptions.containsKey(start)){
            return voltagesToNumberOfOptions.get(start)!!
        }
        if (start == device)
            return 1
        val nextOptions = (1..3).filter { adapters.contains(start + it) }.map { it + start }.toSet()
        val continuations = nextOptions.map { nextOption -> getNextElementsInt(nextOption, adapters.minus(nextOption), device) }.sum()
        voltagesToNumberOfOptions.put(start, continuations)

        return continuations
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = Voltages102();
    baggage.run()

}






