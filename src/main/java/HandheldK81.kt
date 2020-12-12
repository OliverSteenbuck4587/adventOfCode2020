import java.util.function.BinaryOperator
import java.util.function.IntBinaryOperator

class HandheldK81 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("8handheld.txt")
        val programm = input.split("\n").map { Line(it) }
        var accumulator = 0;
        val seenInstructions = mutableSetOf<Int>()
        var instrutionPointer = 0;
        println("Next instruction pointer: " + 0)
        while (!seenInstructions.contains(instrutionPointer)) {
            val line = programm[instrutionPointer]
            println(line)
            seenInstructions.add(instrutionPointer)
            when (line.op) {
                Operation.ACC -> {
                    accumulator = line.direction.apply(accumulator, line.argument)
                    instrutionPointer++;
                }
                Operation.NOP -> instrutionPointer = instrutionPointer + 1
                Operation.JMP -> instrutionPointer = line.direction.apply(instrutionPointer, line.argument)
            }
            println("Next instruction pointer: " + instrutionPointer)
            println("Accumulator : " + accumulator)
        }


    }


    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()

    class Line(val opString: String) {
        val op = Operation.valueOf(opString.split(" ")[0].trim().toUpperCase())
        val direction = IntArithmetics.getDirection(opString.split(" ")[1].trim().toCharArray()[0])
        val argument = opString.split(" ")[1].trim().substring(1).toInt()
        override fun toString(): String {
            return "Line(opString='$opString', op=$op, direction=$direction, argument=$argument)"
        }

    }


    enum class Operation {
        ACC,
        NOP,
        JMP;
    }

    enum class IntArithmetics : BinaryOperator<Int>, IntBinaryOperator {
        PLUS {
            override fun apply(t: Int, u: Int): Int = t + u
        },
        MINUS {
            override fun apply(t: Int, u: Int): Int = t - u
        };

        override fun applyAsInt(t: Int, u: Int) = apply(t, u)

        companion object {
            fun getDirection(directionality: Char): IntArithmetics {

                when (directionality) {
                    '-' -> return MINUS
                    '+' -> return PLUS
                    else -> throw RuntimeException("Unknown direction: " + directionality)
                }
            }
        }
    }

}




fun main(args: Array<String>) {
    val handheld = HandheldK81();
    handheld.run()

}






