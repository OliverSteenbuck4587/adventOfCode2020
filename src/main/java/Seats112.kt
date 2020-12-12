import Seats112.Type.*
import java.util.*

class Seats112 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("11seats.txt")
        var seats = input.split("\n").map { it.toCharArray().map { Type.getType(it) }.toTypedArray() }.toTypedArray()


//        println("generation 0")
//        prettyPrint(seats)
//        println("generation 1")
//        seats = runOneGeneration(seats)
//        prettyPrint(seats)

        while (!Arrays.deepEquals(seats, runOneGeneration(seats))) {
            println("run generation")
            seats = runOneGeneration(seats);
        }

        val takenSeats = seats.flatMap { it.filter { it == OCCUPIED } }.count()
        println("Taken Seats: $takenSeats")
    }

    fun runOneGeneration(seats: Array<Array<Type>>): Array<Array<Type>> {
        return seats.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, type ->
                when (type) {
                    FLOOR -> FLOOR
                    OCCUPIED -> if (getNumberofOccupiedNeighbors(seats, rowIndex, columnIndex) >= 5) FREE else OCCUPIED
                    FREE -> if (getNumberofOccupiedNeighbors(seats, rowIndex, columnIndex) == 0) OCCUPIED else FREE
                    OUT_OF_BOUNDS -> throw RuntimeException("OUT OF BOUNDS")
                }
            }.toTypedArray()
        }.toTypedArray()
    }


    fun getNumberofOccupiedNeighbors(seats: Array<Array<Type>>, rowTarget: Int, columnTarget: Int): Int {
        return getNeighbors(seats, rowTarget, columnTarget).filter { it in listOf(Type.OCCUPIED) }.count()
    }

    fun getNeighbors(seats: Array<Array<Type>>, rowTarget: Int, columnTarget: Int): Array<Type> {
        val neighbors: MutableList<Type> = mutableListOf()
        for(direction in Directions.values()){
            neighbors.add(findNextVisibleSeat(seats, rowTarget, columnTarget,direction))
        }

        return neighbors.toTypedArray()
    }

    private fun findNextVisibleSeat(seats: Array<Array<Type>>, rowTarget: Int, columnTarget: Int, direction: Directions): Type {
        var idx: Pair<Int, Int> = Pair(rowTarget, columnTarget);
        do {
            idx = direction.apply(idx.first, idx.second)
            val type = getType(seats, idx.first, idx.second)
        }while(type == FLOOR )
        return getType(seats, idx.first, idx.second);
    }


    enum class Directions(private val rowChange: Int, private val columnChange: Int){
        UP_LEFT(-1,-1),
        UP(-1,0),
        UP_RIGHT(-1,1),
        RIGHT(0,1),
        DOWN_RIGHT(1,1),
        DOWN(1,0),
        DOWN_LEFT(1,-1),
        LEFT(0,-1);

        fun apply(rowIdx: Int, columnIdx: Int): Pair<Int, Int>{
            return Pair(rowIdx+rowChange,columnIdx+columnChange)
        }

    }

    fun getType(seats: Array<Array<Type>>, rowIdx: Int, columnIdx: Int): Type {
        if (!seats.indices.contains(rowIdx)) {
            return FREE
        }
        val row = seats.get(rowIdx)
        if (!row.indices.contains(columnIdx)) {
            return FREE
        }
        return row.get(columnIdx)
    }

    fun prettyPrint(seats: Array<Array<Type>>) {
        println(seats.map { it.joinToString(separator = "") }.joinToString(separator = "\n"))
    }

    enum class Type(val symbol: Char) {
        OCCUPIED('#'),
        FREE('L'),
        FLOOR('.'),
        OUT_OF_BOUNDS('X');

        override fun toString(): String {
            return "$symbol"
        }

        companion object {
            fun getType(symbol: Char): Type {
                return when (symbol) {
                    'X' -> return OCCUPIED
                    'L' -> return FREE
                    '.' -> return FLOOR
                    else -> throw RuntimeException("Unknown type: " + symbol)
                }
            }
        }

    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = Seats112();
    baggage.run()

}






