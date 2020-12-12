class BaggageK72 {

    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("7baggage.txt")
        val baggageTypes = input.split("\n")
                .map { it -> it.replace(Regex("bags?\\.?"), "") }
                .map { it ->
                    it.split("contain")[0].replace(Regex(" "), "") to it.split("contain")[1]
                            .split(",")
                            .flatMap { it ->
                                var multiplicator = 1
                                var color = it.trim().replace(" ", "")
                                val match = Regex("(\\d+)(.*)").find(it.trim())
                                match?.let { it ->
                                    multiplicator = it.groupValues.get(1).trim().toInt()
                                    color = it.groupValues.get(2).replace(" ", "").trim()
                                }
                                (1 .. multiplicator).map { it -> color }.toList()
                            }
                }
                .toMap()

        for (baggageType in baggageTypes) {
            println(baggageType)
        }
        println(contains("shinygold", baggageTypes).size-1)



    }

    fun contains(color: String, baggageTypes: Map<String, List<String>>): List<String>{
        if(baggageTypes[color] == listOf("noother")){
            return listOf(color)
        }
        return  baggageTypes[color].orEmpty().flatMap{it -> contains(it, baggageTypes)}.plus(color)
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) = this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}

data class Rule(val might: Int, val color: String)


fun main(args: Array<String>) {
    val baggage = BaggageK72();
    baggage.run()

}






