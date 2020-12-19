package nineteen

typealias Id = Int

class Messages191 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../19messages.txt")
        val messages = input.split("\n\n")[1].split("\n")
        val rules = input.split("\n\n")[0].split("\n")
            .map { Pair(it.split(":")[0].trim(), it.split(":")[1].trim()) }
            .map { it -> it.first.toInt() to Rule(it.first.toInt(),it.second.split(" ").map { it.removeSurrounding("\"") }) }
            .toMap()

        println(rules[0]!!.produce(rules))
        val validatorRegex = Regex(rules[0]!!.produce(rules))
        val validSize = messages.filter { validatorRegex.matches(it) }.size
        println(validSize)

    }


    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()

}

data class Rule(val id: Int, val elements: List<String>) {
    fun produce(rules: Map<Id, Rule>): String {
        if (isFinal())
            return elements.get(0)

        //println(elements)
        val result = elements.map { element ->
            if (element == "|") {
                "|"
            } else {
                rules.get(element.toInt())!!.produce(rules)
            }
        }.joinToString(separator = "")
        if(result.contains("42"))
            println("foo")
        if(result.contains('|')){
            return "($result)"
        }else{
            return result
        }
    }

    fun isFinal(): Boolean {
        return (elements.size == 1 && elements.get(0).toIntOrNull() == null)
    }

}

fun main(args: Array<String>) {
    val baggage = Messages191();
    baggage.run()

}
