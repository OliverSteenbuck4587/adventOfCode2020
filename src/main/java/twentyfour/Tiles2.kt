package twentyfour

import twentyfour.Tiles2.COLOR.BLACK
import twentyfour.Tiles2.COLOR.WHITE
import kotlin.io.path.createTempDirectory

class Tiles2 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../24tiles.txt")
        val oneCharInput = input
            .replace("se", "a")
            .replace("sw", "b")
            .replace("nw", "c")
            .replace("ne", "d")
        val directions = oneCharInput.split("\n").map { it.toCharArray().map { DIRECTION.fromPair(it) } }
        val tiles = mutableMapOf<Coordinate, COLOR>()

        for (direction in directions) {
            var currentCoordinate = Coordinate(0, 0, 0)
            for (step in direction) {
                val modifier = step.modifier
                currentCoordinate = Coordinate(
                    currentCoordinate.x + modifier.x,
                    currentCoordinate.y + modifier.y,
                    +currentCoordinate.z + modifier.z
                )
            }
            val newColor = when (tiles.getOrDefault(currentCoordinate, WHITE)) {
                WHITE -> BLACK
                BLACK -> WHITE
            }
            tiles[currentCoordinate] = newColor
        }


        println("Starting game of live")
        var conwayTiles = tiles.toMap()
        for (turn in 1..100) {
            println("Turn: $turn conwayTileSize: ${conwayTiles.size}")
            val tilesUnderConsideration = enumerateTilesUnderConsideration(conwayTiles)
            conwayTiles = tilesUnderConsideration.map { it to getNextState(conwayTiles, it) }.toMap()
        }

        val numberOfBlackTiles = conwayTiles.values.filter { it == BLACK }.size
        println("Number of black tiles $numberOfBlackTiles")

    }

    private fun getNextState(tiles: Map<Coordinate, COLOR>, checkFor: Coordinate): COLOR {
        val blackNeighbors = getNeighbors(checkFor).map { tiles.getOrDefault(it, WHITE) }.filter { it == BLACK }.size
        val centerColor = tiles.getOrDefault(checkFor, WHITE)
        if (centerColor == BLACK) {
            if (blackNeighbors == 0 || blackNeighbors > 2)
                return WHITE
            else
                return centerColor
        } else {
            if (blackNeighbors == 2)
                return BLACK
            else
                return centerColor
        }
    }

    private fun enumerateTilesUnderConsideration(tiles: Map<Coordinate, COLOR>): Collection<Coordinate> {
        return tiles.filter { it.value == BLACK }.flatMap { getNeighbors(it.key).plus(it.key) }
    }

    private fun getNeighbors(center: Coordinate): Collection<Coordinate> {
        return DIRECTION.values().map { it.modifier }
            .map { Coordinate(center.x + it.x, center.y + it.y, center.z + it.z) }
    }

    data class Coordinate(val x: Int, val y: Int, val z: Int)

    enum class COLOR {
        WHITE,
        BLACK
    }

    enum class DIRECTION(val modifier: Coordinate) {
        E(Coordinate(-1, 1, 0)),
        SE(Coordinate(-1, 0, 1)),
        SW(Coordinate(0, -1, 1)),
        W(Coordinate(1, -1, 0)),
        NW(Coordinate(1, 0, -1)),
        NE(Coordinate(0, 1, -1));

        companion object {
            fun fromPair(dir: Char): DIRECTION {
                return when (dir) {
                    'e' -> E
                    'a' -> SE
                    'b' -> SW
                    'w' -> W
                    'c' -> NW
                    'd' -> NE
                    else -> throw RuntimeException("Unknown Direction: $dir")
                }
            }
        }
    }


    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()
}

fun main(args: Array<String>) {
    val baggage = Tiles2();
    baggage.run()

}