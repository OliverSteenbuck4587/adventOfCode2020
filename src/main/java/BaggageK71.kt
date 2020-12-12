class BaggageK71 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("7baggage.txt")
        val baggageTypes = input.split("\n")
                .map { it -> it.replace(Regex("bags?\\.?"), "") }
                .map { it -> it.replace(Regex("\\d"), "") }
                .map { it -> it.replace(Regex(" "), "") }
                .map { it -> it.split("contain")[0] to it.split("contain")[1].split(",") }
                .toMap()

        for (baggageType in baggageTypes) {
             println(baggageType)
        }
        val count = baggageTypes.map { it -> it.key to it.value.flatMap { findTerminals(it, baggageTypes) } }
                .map { it -> it.second }
                .filter { it -> it.contains("shinygold") }
                .count()
        println(count)

    }

    fun findTerminals(color: String, types: Map<String, List<String>>): List<String> {
        val nextColors = types.getOrDefault(color, listOf())
        if(color.equals("shinygold")){
            return listOf("shinygold")
        }

        return when (nextColors) {
            listOf("noother") -> listOf()
            else -> nextColors.flatMap { it -> findTerminals(it, types) }.toList()
        }
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = BaggageK71();
    baggage.run()

}






