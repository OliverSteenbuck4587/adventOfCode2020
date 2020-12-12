import Ship121.HEADING.*
import java.lang.RuntimeException

class Ship121 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("12ships.txt")
        var orders = input.split("\n").map { Order.create(it) }
        var course = Course(Pair(0, 0), EAST)

        println(course)
        for (order in orders) {
            print(order)
            course = order.actOn(course)
            println(course)
        }

        println(course.location.first + course.location.second)
    }

    data class Course(val location: Pair<Int, Int>, val heading: HEADING)

    enum class HEADING(val leftNinety: String, val hundredEighty: String, val rightNinety: String, val forward: Pair<Int, Int>) {
        NORTH("WEST", "SOUTH", "EAST", Pair(-1, 0)),
        EAST("NORTH", "WEST", "SOUTH", Pair(0, 1)),
        SOUTH("EAST", "NORTH", "WEST", Pair(1, 0)),
        WEST("SOUTH", "EAST", "NORTH", Pair(0, -1));

        fun turnLeft(amount: Int): HEADING {
            return when (amount) {
                90 -> valueOf(this.leftNinety)
                180 -> valueOf(this.hundredEighty)
                270 -> valueOf(this.rightNinety)
                else -> throw RuntimeException("Unknown direction change left: " + amount)
            }
        }

        fun turnRight(amount: Int): HEADING {
            return when (amount) {
                90 -> valueOf(this.rightNinety)
                180 -> valueOf(this.hundredEighty)
                270 -> valueOf(this.leftNinety)
                else -> throw RuntimeException("Unknown direction change right: " + amount)
            }
        }
    }

    data class Order(val action: ACTION, val amount: Int) {
        fun actOn(course: Course): Course {
            return action.modify(course, amount)
        }

        companion object {
            fun create(orderString: String): Order {
                val action = ACTION.create(orderString.elementAt(0))
                val amount = orderString.substring(1).toInt()
                return Order(action, amount)
            }
        }
    }

    enum class ACTION() {
        NORTH {
            override fun modify(course: Course, amount: Int): Course {

                val newCoordinates = course.location.let { it: Pair<Int, Int> -> Pair(it.first - amount, it.second) }
                return Course(newCoordinates, course.heading)
            }
        },
        SOUTH {
            override fun modify(course: Course, amount: Int): Course {
                val newCoordinates = course.location.let { it: Pair<Int, Int> -> Pair(it.first + amount, it.second) }
                return Course(newCoordinates, course.heading)
            }
        },
        EAST {
            override fun modify(course: Course, amount: Int): Course {
                val newCoordinates = course.location.let { it: Pair<Int, Int> -> Pair(it.first, it.second + amount) }
                return Course(newCoordinates, course.heading)
            }
        },
        WEST {
            override fun modify(course: Course, amount: Int): Course {
                val newCoordinates = course.location.let { it: Pair<Int, Int> -> Pair(it.first, it.second - amount) }
                return Course(newCoordinates, course.heading)
            }
        },
        TURN_LEFT {
            override fun modify(course: Course, amount: Int): Course {
                val newHeading = course.heading.turnLeft(amount)
                return Course(course.location, newHeading)
            }
        },
        TURN_RIGHT {
            override fun modify(course: Course, amount: Int): Course {
                val newHeading = course.heading.turnRight(amount)
                return Course(course.location, newHeading)
            }
        },
        FORWARD {
            override fun modify(course: Course, amount: Int): Course {
                var location: Pair<Int, Int> = course.location
                val change = course.heading.forward
                repeat(amount) {
                    location = Pair(location.first + change.first, location.second + change.second)
                }
                return Course(location, course.heading)
            }
        };

        abstract fun modify(course: Course, amount: Int): Course


        companion object {
            fun create(action: Char): ACTION {
                return when (action) {
                    'N' -> NORTH
                    'S' -> SOUTH
                    'E' -> EAST
                    'W' -> WEST
                    'L' -> TURN_LEFT
                    'R' -> TURN_RIGHT
                    'F' -> FORWARD
                    else -> throw RuntimeException("Unknown action: " + action)
                }
            }

        }
    }


    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = Ship121();
    baggage.run()

}






