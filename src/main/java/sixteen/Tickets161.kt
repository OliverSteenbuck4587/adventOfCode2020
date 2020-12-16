package sixteen

class Tickets161 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../16tickets.txt")
        val rules = input.split("\n\n")[0].split("\n").map { Rule.create(it) }
        val otherTickets = input.split("\n\n")[2].split("\n")
                .filter { !it.equals("nearby tickets:") }
                .map { Ticket.create(it) }

        for (rule in rules)
            println(rule)



        for (ticket in otherTickets){
            val errorSum = ticket.getErrorSumForTicket(rules).toString()
            println("Result: " + ticket.toString() + " is valid ? " + errorSum)
        }

        println(otherTickets.map { it.getErrorSumForTicket(rules) }.sum())

    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()

    data class Ticket(val values: List<Int>){

        fun getErrorSumForTicket(rules: List<Rule>): Int{
           return values.filter { value -> !rules.any{rule -> rule.isValid(value)} }.sum()
        }

        companion object{
            fun create(ticketString: String):Ticket{
                return Ticket(ticketString.split(",").map { it.toInt() })
            }
        }
    }

    data class Rule(val name: String, val validRanges: List<IntRange>){

        fun isValid(value: Int):Boolean{
            return validRanges.any{it.contains(value)}
        }

        companion object{
            fun create(ruleString: String): Rule{
                val ruleName = ruleString.split(":")[0].trim()
                val validRanges = ruleString
                        .split(":")[1]
                        .split("or")
                        .map { it.trim() }
                        .map { IntRange(it.split("-")[0].toInt(), it.split("-")[1].toInt()) }
                return Rule(ruleName, validRanges)
            }
        }
    }

}


fun main(args: Array<String>) {
    val baggage = Tickets161();
    baggage.run()

}
