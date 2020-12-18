package eightteen

import eightteen.Parenthese.CLOSE
import eightteen.Parenthese.OPEN
import java.util.*


class Math181 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../18math.txt")
        val equations = input.split("\n").map {
            it.split(" ").map { it.trim() }.flatMap { elem ->
                if (Operand.accepts(elem)) {
                    listOf(Operand.create(elem))
                } else {
                    parseLong(elem)
                }
            }
        }

        var acc = 0.toLong()
        for(equation in equations){
            acc += solve(equation).first
        }

        println(acc)

    }

    fun solve(input: List<Any>): Pair<Long, List<Any>> {
        var acc = 0.toLong()
        var equation = input.toMutableList()

        val operandStack = Stack<Operand>()
        while (equation.isNotEmpty()) {
            val part = equation.removeAt(0)
            println(part)
            if (part is Operand) {
                operandStack.push(part)
            } else if (part is Long) {
                if (operandStack.empty()) {
                    acc = part
                } else {
                    acc = operandStack.pop().workOn(acc, part)
                }
            } else if (part is Parenthese && part == OPEN) {
                val (result, newEquation) = solve(equation)
                equation = listOf(result).plus(newEquation).toMutableList()
            } else if (part is Parenthese && part == CLOSE) {
                return Pair(acc, equation)
            } else {
                throw RuntimeException("Unknown part of equation: " + part)
            }
        }
        return Pair(acc, equation)
    }

    fun consumeOpeningBraces(element: String): Pair<String, List<Parenthese>> {
        var remainder = element
        val braces = mutableListOf<Parenthese>()
        while (remainder.startsWith("(")) {
            remainder = remainder.drop(1)
            braces.add(OPEN)
        }
        return Pair(remainder, braces)
    }

    fun consumeClosingBraces(element: String): Pair<String, List<Parenthese>> {
        var remainder = element
        val braces = mutableListOf<Parenthese>()
        while (remainder.endsWith(")")) {
            remainder = remainder.dropLast(1)
            braces.add(CLOSE)
        }
        return Pair(remainder, braces)
    }

    fun parseLong(s: String): List<Any> {
        if (s.toLongOrNull() != null) {
            return listOf(s.toLong())
        } else {
            if (s.startsWith('(')) {
                val consumedBraces = consumeOpeningBraces(s)
                return consumedBraces.second.plus(consumedBraces.first.toLong())
            } else if (s.endsWith(')')) {
                val consumedBraces = consumeClosingBraces(s)
                return listOf(consumedBraces.first.toLong()).plus(consumedBraces.second)
            } else {
                return listOf(s.toLong())
            }
        }

    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()
}

enum class Parenthese {
    OPEN,
    CLOSE
}

enum class Operand {
    PLUS {
        override fun workOn(acc: Long, part: Long): Long {
            return acc + part
        }
    },
    MULTIPLICATION {
        override fun workOn(acc: Long, part: Long): Long {
            return acc * part
        }
    };

    abstract fun workOn(acc: Long, part: Long): Long

    companion object {
        fun accepts(s: String): Boolean {
            return s.contains('+') || s.contains('*')
        }

        fun create(s: String): Operand {
            return when (s.trim()) {
                "+" -> PLUS
                "*" -> MULTIPLICATION
                else -> throw RuntimeException("Unsupported operand: " + s)
            }

        }
    }
}


fun main(args: Array<String>) {
    val baggage = Math181();
    baggage.run()

}
