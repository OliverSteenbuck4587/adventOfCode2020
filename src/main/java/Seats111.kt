import Seats111.Type.*
import java.util.*

class Seats111 {

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
                    OCCUPIED -> if (getNumberofOccupiedNeighbors(seats, rowIndex, columnIndex) >= 4) FREE else OCCUPIED
                    FREE -> if (getNumberofOccupiedNeighbors(seats, rowIndex, columnIndex) == 0) OCCUPIED else FREE
                }
            }.toTypedArray()
        }.toTypedArray()
    }


    fun getNumberofOccupiedNeighbors(seats: Array<Array<Type>>, rowTarget: Int, columnTarget: Int): Int {
        return getNeighbors(seats, rowTarget, columnTarget).filter { it in listOf(Type.OCCUPIED) }.count()
    }

    fun getNeighbors(seats: Array<Array<Type>>, rowTarget: Int, columnTarget: Int): Array<Type> {
        val neighbors: MutableList<Type> = mutableListOf()
        for (row in rowTarget - 1..rowTarget + 1) {
            for (column in columnTarget - 1..columnTarget + 1) {
                if (!(row == rowTarget && column == columnTarget)) {
                    neighbors.add(getType(seats, row, column))
                }
            }
        }
        return neighbors.toTypedArray()
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
        FLOOR('.');

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
    val baggage = Seats111();
    baggage.run()

}






