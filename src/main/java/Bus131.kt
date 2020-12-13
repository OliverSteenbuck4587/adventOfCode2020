
class Bus131 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("13bus.txt")
        val timeNow = input.split("\n")[0].toInt()
        val buses = input.split("\n")[1].split(",").filter { it != "x"}.map { it.trim().toInt() }


        //take timenow modulo id +1 times id -timenow should be waittime
        val waittimes = buses.map { Pair(it, (((timeNow / it)+1)*it)-timeNow) }

        val earliestBus = waittimes.minByOrNull { it.second }!!

        println("time: " + timeNow)
        println("buses: " + buses)
        println("waittimes: " + waittimes)
        println("earliestBus: " + earliestBus)
        println("earliestBus: " + earliestBus)
        println("solution: " + earliestBus.first * earliestBus.second)

    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = Bus131();
    baggage.run()

}






