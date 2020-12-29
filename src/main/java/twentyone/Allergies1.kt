package twentyone

class Pictures201 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../20pictures.txt")
        val pictures = input.split("\n\n")
            .map { it.split("\n") }
            .map { Picture.createFromPictureString(it) }
            .map { it.id to it }.toMap()


        val corners = mutableSetOf<Picture>()
        //val picture = pictures.get(1427)!!
        for(picture in pictures.values) {
            val picturesWithoutCornder = pictures.minus(picture.id)
            val nonMatchingBorder = picture.borders.filter { borderInCorner ->
                picturesWithoutCornder.values.none {
                    it.borders.contains(borderInCorner)
                }

            }.toSet()
            if(nonMatchingBorder.size == 4){
                corners.add(picture)
            }
        }

        for (corner in corners) {
            println(corner)
        }

        println("Result: " + corners.map { it.id.toLong() }.reduce{sum, element -> sum * element})

//        println(leftTopCorner)
//        println(rightTopCorner)
//        println(rightBottomCorner)
//        println(leftBottomCorner)
    }

    data class Picture(
        val id: Int,
        val borders: Set<String>
    ) {
        companion object {
            fun createFromPictureString(lines: List<String>): Picture {
                val id = lines[0].removePrefix("Tile ").removeSuffix(":").trim().toInt()
                val topBorder = lines[1]
                val bottomBorder = lines.last()
                val leftBorder = lines.drop(1).map { it[0] }.joinToString(separator = "")
                val rightBorder = lines.drop(1).map { it.last() }.joinToString(separator = "")
                val borders = setOf(topBorder, bottomBorder, leftBorder, rightBorder)
                val flipedBorders = borders.map { it.reversed() }.toSet()
                return Picture(id, borders.plus(flipedBorders))
            }
        }
    }


    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()



}



fun main(args: Array<String>) {
    val baggage = Pictures201();
    baggage.run()

}
