
class Bitmask141 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("14bitmask.txt")
        val commands = input.split("\n").map { Command.create(it) }

        var state = State(listOf(), mapOf())
        for(command in commands){

            println("Command: $command")
            state = command.actOn(state)
            println("State: $state")
        }

        val sum = state.mem.map { it.value }.sum()

        println("Solution: $sum")

    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()
}

data class State(val mask: List<Char>, val mem: Map<Int, Long>)

interface Command {
    fun actOn(state: State): State

    companion object{
        fun create(commandString: String): Command{
            val command = commandString.split("=")[0].trim()
            return when(command){
                "mask" -> Mask.create(commandString)
                else -> Assignment.create(commandString)
            }
        }
    }
}

class Assignment(val address: Int, val value: List<Char>) : Command {
    companion object{
        fun create(commandString: String): Assignment{
            val value = commandString.split("=")[1].trim()
            val address = Regex("mem\\[(\\d+)\\]").find(commandString.split("=")[0].trim())!!.groupValues[1]
            val binaryValue = Integer.toBinaryString(value.toInt()).toString().toCharArray().toList()
            return Assignment(address.toInt(), binaryValue)
        }
    }

    override fun actOn(state: State): State {
        val maskedValue = (35 downTo 0).map{ i ->
            val valueBit = if(value.indices.contains(i)) value.reversed().get(i) else '0'
            val maskBit = state.mask.get(i)
            when (maskBit) {
                'X' -> valueBit
                '1' -> '1'
                '0' -> '0'
                else -> throw RuntimeException("Unknown mask bit $maskBit")
            }
        }
        println("Mask:" + state.mask)
        println("Value:" + value)
        println("MaskeValue: "+maskedValue.reversed())
        println("MaskedIntegerValue: "+ java.lang.Long.parseUnsignedLong(maskedValue.joinToString(separator = "" ), 2))
        val integerMaskeValue = java.lang.Long.parseUnsignedLong(maskedValue.joinToString(separator = "" ), 2);
        return State(state.mask, state.mem.plus(Pair(address, integerMaskeValue)) )
    }

    override fun toString(): String {
        val value: Int = Integer.parseInt(value.joinToString(separator = "" ), 2);
        return "Assignment(address=$address, value='${value}')"
    }


}

class Mask(val mask:String): Command {

    companion object{
        fun create(commandString: String): Mask{
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


fun main(args: Array<String>) {
    val baggage = Bitmask141();
    baggage.run()

}






