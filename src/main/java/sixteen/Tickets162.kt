package sixteen

class Tickets162 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../16tickets.txt")
        val rules = input.split("\n\n")[0].split("\n").map { Rule.create(it) }
        val myTicket = input.split("\n\n")[1].split("\n")
            .filter { !it.equals("your ticket:") }
            .map { Ticket.create(it) }
            .first()
        val allTickets = input.split("\n\n")[2].split("\n")
            .filter { !it.equals("nearby tickets:") }
            .map { Ticket.create(it) }
            .filter { it.isValid(rules) }.plus(myTicket)


        val ruleResult = mutableListOf<MutableSet<Rule>>()
        for (i in rules.indices) {
            ruleResult.add(i, mutableSetOf())
        }

        for (i in rules.indices) {
            val ruleMatchingAllIes =
                rules.filter { rule -> allTickets.all { ticket -> rule.isValid(ticket.getValueAtIndex(i)) } }
            ruleResult.get(i).addAll(ruleMatchingAllIes)
        }

        while (ruleResult.any { it -> it.size > 1 }) {
            println("Remove one iteration")
            //iterate over all ones and remove them elsewhere
            for (clearResult in ruleResult.filter { it -> it.size == 1 }) {
                ruleResult.filter { it -> it.size > 1 }.forEach { it.remove(clearResult.first()) }
            }
        }


        var result = 1.toLong()
        ruleResult.map { it.first() }.forEachIndexed { index, rule ->
            if (rule.name.startsWith("departure"))
                result = result * myTicket.getValueAtIndex(index)
        }
        println(result)
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()

    data class Ticket(val values: List<Int>) {

        fun getValueAtIndex(index: Int): Int {
            return values[index]
        }

        fun isValid(rules: List<Rule>): Boolean {
            return values.all { value -> rules.any { rule -> rule.isValid(value) } }
        }

        companion object {
            fun create(ticketString: String): Ticket {
                return Ticket(ticketString.split(",").map { it.toInt() })
            }
        }
    }

    data class Rule(val name: String, val validRanges: List<IntRange>) {

        fun isValid(value: Int): Boolean {
            return validRanges.any { it.contains(value) }
        }

        companion object {
            fun create(ruleString: String): Rule {
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
    val baggage = Tickets162();
    baggage.run()

}
