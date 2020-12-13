


class Bus132 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("13bus.txt")
        val buses = input.split("\n")[1]
                .split(",")
                .mapIndexed{ index,it -> Pair(index,it.trim()) }
                .filter { it.second != "x" }
                .map{Bus(it.first,it.second.toInt() )}

//        var i = 0;
//        val increment = buses.maxByOrNull { it.id }!!
//        while(!isSolution(buses,i)){
//            if(i % 100000 == 0)
//                println("Candidate: " + i)
//            i++
//        }

        var i = 0.toLong();
        val increment = buses.maxByOrNull { it.departureTime }!!

        do {
            if(i % 1000000.toLong() == 0.toLong())
                println("Candidate: " + i)
            i+=increment.departureTime
        }while(!isSolution(buses,i-increment.id))



        println("Solution: " + (i-increment.id))


    }

    fun isSolution(buses: List<Bus>, start: Long): Boolean{
        return buses.all { (start+it.id)%it.departureTime == 0.toLong()}
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()
}

data class Bus(val id: Int, val departureTime: Int)

fun main(args: Array<String>) {
    val baggage = Bus132();
    baggage.run()

}






