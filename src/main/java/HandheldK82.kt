import HandheldK82.Operation.JMP
import HandheldK82.Operation.NOP
import java.util.function.BinaryOperator
import java.util.function.IntBinaryOperator

class HandheldK82 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("8handheld.txt")
        val programm = input.split("\n").map { Line.fromString(it) }
        programm.forEachIndexed { index, line ->
            println("mdifieng programm")
            val modifiedProgramm = programm.toMutableList()
            when (line.op) {
                JMP -> modifiedProgramm.set(index, Line(NOP, line.direction, line.argument))
                NOP -> modifiedProgramm.set(index, Line(JMP, line.direction, line.argument))
            }
            val (result, acc) = runProgramm(modifiedProgramm)
            if(result){
                println("Sucess acc:" + acc)
                return
            }
        }


    }

    fun runProgramm(programm: List<Line>): Pair<Boolean, Int> {
        var accumulator = 0;
        val seenInstructions = mutableSetOf<Int>()
        var instrutionPointer = 0;
        println("Next instruction pointer: " + 0)
        while (!seenInstructions.contains(instrutionPointer)) {
            val line = programm[instrutionPointer]
//            println(line)
            seenInstructions.add(instrutionPointer)
            when (line.op) {
                Operation.ACC -> {
                    accumulator = line.direction.apply(accumulator, line.argument)
                    instrutionPointer++;
                }
                NOP -> instrutionPointer = instrutionPointer + 1
                Operation.JMP -> instrutionPointer = line.direction.apply(instrutionPointer, line.argument)
            }
            if (instrutionPointer == programm.size) {
                println("Success")
                return Pair(true, accumulator)
            }

        }
        return Pair(false, 0)
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()

    class Line(val op: Operation, val direction: IntArithmetics, val argument: Int) {

        companion object{
            fun fromString(opString: String): Line{
                val op = Operation.valueOf(opString.split(" ")[0].trim().toUpperCase())
                val direction = IntArithmetics.getDirection(opString.split(" ")[1].trim().toCharArray()[0])
                val argument = opString.split(" ")[1].trim().substring(1).toInt()
                return Line(op, direction, argument)
            }
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
    val handheld = HandheldK82();
    handheld.run()

}






