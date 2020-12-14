class Bitmask142 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("14bitmask.txt")
        val commands = input.split("\n").map { Command.create(it) }

        var state = State(listOf(), mapOf())
        for (command in commands) {

            println("Command: $command")
            state = command.actOn(state)
        }

        val sum = state.mem.map { it.value }.sum()

        println("Solution: $sum")

    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()

    data class State(val mask: List<Char>, val mem: Map<Long, Long>)

    interface Command {
        fun actOn(state: State): State

        companion object {
            fun create(commandString: String): Command {
                val command = commandString.split("=")[0].trim()
                return when (command) {
                    "mask" -> Mask.create(commandString)
                    else -> Assignment.create(commandString)
                }
            }
        }

        class Assignment(val address: Int, val value: Int) : Command {
            companion object {
                fun create(commandString: String): Assignment {
                    val value = commandString.split("=")[1].trim().toInt()
                    val address = Regex("mem\\[(\\d+)\\]").find(commandString.split("=")[0].trim())!!.groupValues[1]

                    return Assignment(address.toInt(), value)
                }
            }

            override fun actOn(state: State): State {
                var result = state.mem.toMutableMap()
                val maskedAdresses = generateAddresses(state, address)
                for (address in maskedAdresses) {
                    val addressLong = java.lang.Long.parseUnsignedLong(address.joinToString(separator = ""), 2)
                    result.put(addressLong, value.toLong())
                }

                return State(state.mask, result.toMap())
            }

            fun generateAddresses(state: State, address: Int): List<List<Char>> {
                var address = Integer.toBinaryString(address).toString().toCharArray().toList()
                while (address.size != 36) {
                    address = listOf('0').plus(address)
                }
                address = address.reversed()
                val mask = state.mask
                return getNextBits(mask, address)
            }


            fun getNextBits(state: List<Char>, address: List<Char>): List<List<Char>> {
                val valueBit = address.first()
                val maskBit = state.first()
                if (state.size == 1) {
                    return when (maskBit) {
                        'X' -> listOf(listOf('1'), (listOf('0')))
                        '1' -> listOf(listOf('1'))
                        '0' -> listOf(listOf(valueBit))
                        else -> throw RuntimeException("Unknown mask bit $maskBit")
                    }
                }

                val rest = getNextBits(state.drop(1), address.drop(1))
                return when (maskBit) {
                    'X' -> rest.map { listOf('1').plus(it) }.plus(rest.map { listOf('0').plus(it) })
                    '1' -> rest.map { listOf('1').plus(it) }
                    '0' -> rest.map { listOf(valueBit).plus(it) }
                    else -> throw RuntimeException("Unknown mask bit $maskBit")
                }
            }

            override fun toString(): String {
                return "Assignment(address=$address, value='${value}')"
            }
        }

        class Mask(val mask: String) : Command {

            companion object {
                fun create(commandString: String): Mask {
                    return Mask(commandString.split("=")[1].trim())
                }
            }

            override fun actOn(state: State): State {
                return State(mask.toCharArray().toList().reversed(), state.mem)
            }

            override fun toString(): String {
                return "Mask(mask='$mask')"
            }
        }
    }


}

fun main(args: Array<String>) {
    val baggage = Bitmask142();
    baggage.run()

}






