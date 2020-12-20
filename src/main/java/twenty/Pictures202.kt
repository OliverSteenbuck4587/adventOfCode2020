package twenty

class Pictures202 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../20pictures.txt")
        val pictures = input.split("\n\n")
            .map { it.split("\n") }
            .map { Picture.createFromPictureString(it) }
            .map { it.id to it }.toMap()

        val picturesWithNeigbhors = pictures.values
            .map { PictureWithNeighbors(it, Picture.getNeighbors(it, pictures.values.toSet())) }


        //start with one

        picturesWithNeigbhors.filter { it.picture.id == 1951}.forEach { it.neighbors.forEach { println(it) } }

    }

    data class PictureWithNeighbors(val picture: Picture, val neighbors: Set<Picture>)

    data class Picture(
        val id: Int,
        val lines: List<List<Char>>) {

        fun borders(): Set<String>{
            val topBorder = lines[0].joinToString(separator = "")
            val bottomBorder = lines.last().joinToString(separator = "")
            val leftBorder = lines.map { it[0] }.joinToString(separator = "")
            val rightBorder = lines.map { it.last() }.joinToString(separator = "")
            val borders = setOf(topBorder, bottomBorder, leftBorder, rightBorder)
            val flipedBorders = borders.map { it.reversed() }.toSet()
            return borders.plus(flipedBorders).toSet()
        }

        companion object {
            fun createFromPictureString(lines: List<String>): Picture {
                val id = lines[0].removePrefix("Tile ").removeSuffix(":").trim().toInt()

                return Picture(id, lines.drop(1).map { it.toCharArray().toList() })
            }


            fun getNeighbors(center: Picture, allParts: Set<Picture>): Set<Picture> {
                return allParts.minus(center).filter { it.borders().any { border -> center.borders().contains(border) } }
                    .toSet()
            }
        }
    }


    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = Pictures202();
    baggage.run()

}
