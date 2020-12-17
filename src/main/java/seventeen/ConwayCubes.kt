package seventeen

import seventeen.ConwayCubes.State.ACTIVE
import seventeen.ConwayCubes.State.INACTIVE


class ConwayCubes {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../17conwayCubes.txt")
        val states = input.split("\n").map { it.toCharArray().map { State.create(it) } }
        var cubes =
            states.flatMapIndexed { x, line -> line.mapIndexed { y, state -> Coordinate(x, y, 0) to state } }.toMap()


        for(turn in 1 .. 6){
            println("Turn: " + turn)
            val enumerateCubesUnderConsideration = enumerateCubesUnderConsideration(turn)
            cubes = enumerateCubesUnderConsideration.map { it to getNextState(cubes, it) }.toMap()
        }

        println("actives: " + cubes.filter { it.value == ACTIVE }.size)
        println("inactives: " + cubes.filter { it.value == INACTIVE }.size)

    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()

    fun enumerateCubesUnderConsideration(turn: Int): List<Coordinate> {
        val result = mutableListOf<Coordinate>()
        for (x in 0 - turn..7 + turn) {
            for (y in 0 - turn..7 + turn) {
                for (z in 0 - turn..0 + turn) {
                    result.add(Coordinate(x,y,z))
                }
            }
        }
        return result
    }

    fun getNextState(cubes: Map<Coordinate, State>, coordinate: Coordinate): State {
        val activeNeighbors = getNeighbors(cubes, coordinate).filter { it.second == ACTIVE }.size
        val oldState = cubes.getOrDefault(coordinate, INACTIVE)
        if (oldState == ACTIVE && (activeNeighbors == 2 || activeNeighbors == 3)) {
            return ACTIVE
        } else if (oldState == INACTIVE && activeNeighbors == 3) {
            return ACTIVE
        } else {
            return INACTIVE
        }
    }

    fun getNeighbors(cubes: Map<Coordinate, State>, startCoordinate: Coordinate): List<Pair<Coordinate, State>> {
        val result = mutableListOf<Pair<Coordinate, State>>()
        for (x in startCoordinate.x - 1..startCoordinate.x + 1) {
            for (y in startCoordinate.y - 1..startCoordinate.y + 1) {
                for (z in startCoordinate.z - 1..startCoordinate.z + 1) {
                    val coordinateKey = Coordinate(x, y, z)
                    if (coordinateKey != startCoordinate) {
                        result.add(Pair(coordinateKey, cubes.getOrDefault(coordinateKey, INACTIVE)))
                    }
                }
            }
        }
        return result
    }

    data class Coordinate(val x: Int, val y: Int, val z: Int)

    enum class State {
        ACTIVE,
        INACTIVE;

        companion object {
            fun create(s: Char): State {

                return when (s) {
                    '.' -> INACTIVE
                    '#' -> ACTIVE
                    else -> throw RuntimeException("Unknown state: " + s)
                }
            }
        }
    }

}


fun main(args: Array<String>) {
    val baggage = ConwayCubes();
    baggage.run()

}
