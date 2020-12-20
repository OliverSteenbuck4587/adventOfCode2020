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
        val startPoint = picturesWithNeigbhors.find { it.picture.id == 1951 }!!
        startPoint.getRotatedSideNeighbhors().forEach { println(it) }


    }

    data class PictureWithNeighbors(val picture: Picture, val neighbors: Set<Picture>) {
        fun getSideNeighbors(): Set<Picture> {
            val result = mutableSetOf<Picture>()
            val sideBorders = picture.getAllTopBottomBorders()
            for (neigbhor in neighbors) {
                if (sideBorders.intersect(neigbhor.borders()).size > 0) {
                    result.add(neigbhor)
                }
            }
            return result;
        }

        fun getRotatedSideNeighbhors(): MutableSet<Picture> {
            val result = mutableSetOf<Picture>()
            val sideNeighbors = getSideNeighbors()
            val sideBorders = picture.getSideBorders()

            for (neigbhor in neighbors) {
                if (sideBorders.intersect(neigbhor.getSideBorders()).isNotEmpty()) {
                    result.add(neigbhor)
                }
            }

            for (neigbhor in neighbors.map { it.rotate() }) {
                if (sideBorders.intersect(neigbhor.getSideBorders()).isNotEmpty()) {
                    result.add(neigbhor)
                }
            }

            for (neigbhor in neighbors.map { it.flip() }) {
                if (sideBorders.intersect(neigbhor.getSideBorders()).isNotEmpty()) {
                    result.add(neigbhor)
                }
            }

            for (neigbhor in neighbors.map { it.rotate() }) {
                if (sideBorders.intersect(neigbhor.getSideBorders()).isNotEmpty()) {
                    result.add(neigbhor)
                }
            }
            return result
        }

    }

    data class Picture(
        val id: Int,
        val lines: List<List<Char>>
    ) {

        fun flip(): Picture {
            return Picture(id, lines.reversed())
        }

        fun rotate(): Picture {
            return Picture(id, lines.map { it.reversed() })
        }

        fun borders(): Set<String> {
            return getAllTopBottomBorders().plus(getAllSideBorders())
        }

        fun getAllTopBottomBorders(): Set<String> {
            val topBorder = lines[0].joinToString(separator = "")
            val bottomBorder = lines.last().joinToString(separator = "")


            val borders = setOf(topBorder, bottomBorder)
            val flipedBorders = borders.map { it.reversed() }.toSet()
            return borders.plus(flipedBorders).toSet()
        }

        fun getSideBorders(): Set<String> {
            val leftBorder = lines.map { it[0] }.joinToString(separator = "")
            val rightBorder = lines.map { it.last() }.joinToString(separator = "")
            return setOf(leftBorder, rightBorder)
        }

        fun getAllSideBorders(): Set<String> {
            val leftBorder = lines.map { it[0] }.joinToString(separator = "")
            val rightBorder = lines.map { it.last() }.joinToString(separator = "")
            val borders = setOf(leftBorder, rightBorder)
            val flipedBorders = borders.map { it.reversed() }.toSet()
            return borders.plus(flipedBorders).toSet()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Picture

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id
        }


        companion object {
            fun createFromPictureString(lines: List<String>): Picture {
                val id = lines[0].removePrefix("Tile ").removeSuffix(":").trim().toInt()

                return Picture(id, lines.drop(1).map { it.toCharArray().toList() })
            }


            fun getNeighbors(center: Picture, allParts: Set<Picture>): Set<Picture> {
                return allParts.minus(center)
                    .filter { it.borders().any { border -> center.borders().contains(border) } }
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
