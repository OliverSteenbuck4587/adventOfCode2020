import kotlin.math.*

class Ship122 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("12ships.txt")
        var orders = input.split("\n").map { Order.create(it) }
        var course = Course(Pair(0, 0), Pair(-1,10))

        println(course)
        for (order in orders) {
            println(order)
            course = order.actOn(course)
            println(course)
        }

        println(abs(course.location.first) + abs(course.location.second))

    }

    data class Course(val location: Pair<Int, Int>, val waypoint: Pair<Int, Int>)

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

                val newWaypoint = course.waypoint.let { Pair(it.first - amount, it.second) }
                return Course(course.location, newWaypoint)
            }
        },
        SOUTH {
            override fun modify(course: Course, amount: Int): Course {
                val newWaypoint = course.waypoint.let { Pair(it.first + amount, it.second) }
                return Course(course.location, newWaypoint)
            }
        },
        EAST {
            override fun modify(course: Course, amount: Int): Course {
                val newWaypoint = course.waypoint.let  { Pair(it.first, it.second + amount) }
                return Course(course.location, newWaypoint)
            }
        },
        WEST {
            override fun modify(course: Course, amount: Int): Course {
                val newWaypoint = course.waypoint.let  { Pair(it.first, it.second - amount) }
                return Course(course.location, newWaypoint)
            }
        },
        TURN_LEFT {
            override fun modify(course: Course, amount: Int): Course {
                val newWaypoint = rotateLeft(course.waypoint, amount)
                return Course(course.location, newWaypoint)
            }
        },
        TURN_RIGHT {
            override fun modify(course: Course, amount: Int): Course {
                val newWaypoint = rotateRight(course.waypoint, amount)
                return Course(course.location, newWaypoint)
            }
        },
        FORWARD {
            override fun modify(course: Course, amount: Int): Course {
                var location: Pair<Int, Int> = course.location
                val change = course.waypoint
                repeat(amount) {
                    location = Pair(location.first + change.first, location.second + change.second)
                }
                return Course(location, course.waypoint)
            }
        };

        abstract fun modify(course: Course, amount: Int): Course

        fun rotateRight(origin: Pair<Int, Int>, degrees: Int): Pair<Int, Int> {
            return rotateLeft(origin, degrees * -1)
        }
        fun rotateLeft(origin: Pair<Int, Int>, degrees: Int): Pair<Int, Int>{
            val newX = (origin.first * cos(Math.toRadians(degrees.toDouble()))) - (origin.second * sin(Math.toRadians(degrees.toDouble())))
            val newY = origin.first * sin(Math.toRadians(degrees.toDouble())) + origin.second * cos(Math.toRadians(degrees.toDouble()))
            return Pair(newX.roundToInt(), newY.roundToInt())
        }

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
    val baggage = Ship122();
    baggage.run()

}






