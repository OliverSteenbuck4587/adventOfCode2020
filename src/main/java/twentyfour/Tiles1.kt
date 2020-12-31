package twentyfour

import twentyfour.Tiles1.COLOR.BLACK
import twentyfour.Tiles1.COLOR.WHITE
import java.awt.Color

class Tiles1 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../24tiles.txt")
        val oneCharInput = input
            .replace("se", "a")
            .replace("sw", "b")
            .replace("nw", "c")
            .replace("ne", "d")
        val directions = oneCharInput.split("\n").map { it.toCharArray().map { DIRECTION.fromPair(it) }}
        val tiles = mutableMapOf<Coordinate,COLOR>()

        for (direction in directions){
            var currentCoordinate = Coordinate(0,0,0)
            for(step in direction){
                val modifier = step.modifier
                currentCoordinate = Coordinate(currentCoordinate.x + modifier.x, currentCoordinate.y + modifier.y, + currentCoordinate.z + modifier.z)
            }
            val newColor = when(tiles.getOrDefault(currentCoordinate, WHITE)){
                WHITE -> BLACK
                BLACK -> WHITE
            }
            tiles[currentCoordinate] = newColor
        }

        val numberOfBlackTiles = tiles.values.filter { it == BLACK }.size
        println("Number of black tiles $numberOfBlackTiles")

    }

    data class Coordinate(val x:Int, val y:Int, val z:Int)

    enum class COLOR{
        WHITE,
        BLACK
    }

    enum class DIRECTION(val modifier: Coordinate) {
        E(Coordinate(-1,1,0)),
        SE(Coordinate(-1,0,1)),
        SW(Coordinate(0,-1,1)),
        W(Coordinate(1,-1,0)),
        NW(Coordinate(1,0,-1)),
        NE(Coordinate(0,1,-1));

        companion object {
            fun fromPair(dir:Char):DIRECTION{
                return when(dir){
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
    val baggage = Tiles1();
    baggage.run()

}