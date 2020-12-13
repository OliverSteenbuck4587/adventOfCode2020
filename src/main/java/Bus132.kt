class Bus132 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("13bus.txt")
        val buses = input.split("\n")[1]
                .split(",")
                .mapIndexed { index, it -> Pair(index, it.trim()) }
                .filter { it.second != "x" }
                .map { Bus(it.first, it.second.toInt()) }

// a nicer way to go about this would be to get the increment by combining busses
// shamelessy copied from https://todd.ginsberg.com/post/advent-of-code/2020/day13/
//fun solvePart2(): Long {
//    var stepSize = indexedBusses.first().bus
//    var time = 0L
//    indexedBusses.drop(1).forEach { (offset, bus) ->
//        while ((time + offset) % bus != 0L) {
//            time += stepSize
//        }
//        stepSize *= bus // New Ratio!
//    }
//    return time
//}
        var i = 0.toLong();
        val increment = buses.maxByOrNull { it.departureTime }!!

        do {
            if (i % 1000000.toLong() == 0.toLong())
                println("Candidate: " + i)
            i += increment.departureTime
        } while (!isSolution(buses, i - increment.id))



        println("Solution: " + (i - increment.id))


    }

    fun isSolution(buses: List<Bus>, start: Long): Boolean {
        return buses.all { (start + it.id) % it.departureTime == 0.toLong() }
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()
}

data class Bus(val id: Int, val departureTime: Int)

fun main(args: Array<String>) {
    val baggage = Bus132();
    baggage.run()

}






