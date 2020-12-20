package nineteen
//replacement for rule 11
//11: 42 31 | 42 42 31 31 | 42 42 42 31 31 31 | 42 42 42 42 31 31 31 31
class Messages192 {
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

    data class Rule(val id: Int, val elements: List<String>) {
        fun produce(rules: Map<Int, Rule>): String {

            if(id == 8)
                return handle8(rules)


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
            if(result.contains('|')){
                return "($result)"
            }else{
                return result
            }
        }

        private fun handle8(rules: Map<Int, Rule>): String {
            //42 | 42 8
            val fortyTwo = rules.get(42)!!.produce(rules)
            return "($fortyTwo)+"
        }

        fun isFinal(): Boolean {
            return (elements.size == 1 && elements.get(0).toIntOrNull() == null)
        }

    }
}



fun main(args: Array<String>) {
    val baggage = Messages192();
    baggage.run()

}
