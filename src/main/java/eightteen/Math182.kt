package eightteen


import eightteen.Math182.Operand.PLUS
import eightteen.Math182.Parenthese.CLOSE
import eightteen.Math182.Parenthese.OPEN
import java.util.*


class Math182 {
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


        for(equation in equations){
            println(equation.joinToString ( separator = " " ))
            println(insertOperatorPreference(equation).joinToString ( separator = " " ))
        }



        var acc = 0.toLong()
        for(equation in equations){
            acc += solve(insertOperatorPreference(equation)).first
        }

        println(acc)
    }

    fun insertOperatorPreference(input: List<Any>): List<Any>{
        var i = 0
        val result = input.toMutableList()
        while(i < result.size){
            val elem = result[i]
            if(elem is Operand && elem == PLUS){
                val blockEndAfter = findBlockEndAfter(result, i)
                val blockEndBefore = findBlockEndBefore(result, i)
                if(result.indices.contains(blockEndAfter)) result.add(blockEndAfter, CLOSE) else result.add(CLOSE)
                if(result.indices.contains(blockEndBefore)) result.add(blockEndBefore, OPEN) else result.add(0,OPEN)


                i = i + 2
            }else{
                i++
            }

        }
        return result
    }

    fun findBlockEndAfter(equation: List<Any>, idxStart: Int): Int{
        val blockStart = equation[idxStart+1]
        if(blockStart is Long){
            return idxStart+2
        }
        else if(blockStart is Parenthese && blockStart == OPEN){
            return findMatchingCloseParentheses(equation, idxStart+1)
        }
        else if (blockStart is Parenthese && blockStart == CLOSE){
            return idxStart+1
        }
        else throw RuntimeException("Strange blockstart at index " + idxStart+1 + " is: " + blockStart)
    }

    fun findBlockEndBefore(equation: List<Any>, idxStart: Int): Int{
        val blockStart = equation[idxStart-1]
        if(blockStart is Long){
            return idxStart-1
        }
        else if(blockStart is Parenthese && blockStart == CLOSE){
            return findMatchingOpenParentheses(equation, idxStart-1)
        }
        else if (blockStart is Parenthese && blockStart == OPEN){
            return idxStart-1
        }
        else throw RuntimeException("Strange blockEnd at index " + (idxStart-1) + " is: " + blockStart)
    }

    fun findMatchingCloseParentheses(equation: List<Any>, idxOpen: Int):Int{
        var openParenthese = 1
        var i = idxOpen+1
        while(openParenthese > 0){
            if(equation[i] is Parenthese && equation[i] == CLOSE)
                openParenthese--
            if(equation[i] is Parenthese && equation[i] == OPEN)
                openParenthese++
            i++
        }
        return i
    }

    fun findMatchingOpenParentheses(equation: List<Any>, idxOpen: Int):Int{
        var closeParenthese = 1
        var i = idxOpen-1
        while(closeParenthese > 0){
            if(equation[i] is Parenthese && equation[i] == CLOSE)
                closeParenthese++
            if(equation[i] is Parenthese && equation[i] == OPEN)
                closeParenthese--
            i--
        }
        return i+1
    }



    fun solve(input: List<Any>): Pair<Long, List<Any>> {
        var acc = 0.toLong()
        var equation = input.toMutableList()

        val operandStack = Stack<Operand>()
        while (equation.isNotEmpty()) {
            val part = equation.removeAt(0)
//            println(part)
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



    enum class Parenthese {
        OPEN{
            override fun toString(): String {
                return "("
            }
            },
        CLOSE{
            override fun toString(): String {
                return ")"
            }
        },;



    }

    enum class Operand {
        PLUS {
            override fun workOn(acc: Long, part: Long): Long {
                return acc + part
            }

            override fun toString(): String {
                return "+"
            }

        },
        MULTIPLICATION {
            override fun workOn(acc: Long, part: Long): Long {
                return acc * part
            }
            override fun toString(): String {
                return "*"
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
}

fun main(args: Array<String>) {
    val baggage = Math182();
    baggage.run()

}
