package twentyone

class Allergies212 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../21allergies.txt")
        val meals = input.split("\n")
        val allergens = meals
            .map { it.split("(contains")[1].trim().removeSuffix(")").trim() }
            .flatMap { it.split(", ") }
            .toSet()
        val allergensToAllMeals = allergens.map { it to mutableSetOf<Set<String>>() }.toMap()
        val ingredients = meals.flatMap { extractIngredients(it) }.toSet()

        for (meal in meals) {
            extractAllergens(meal).forEach {
                allergensToAllMeals[it]!!.add(extractIngredients(meal))
            }
        }

        println(allergensToAllMeals)

        val ingredientToReducedAllergens =
            allergensToAllMeals.map { it.key to it.value.reduce { one, two -> one.intersect(two) } }.toMap()
        println(ingredientToReducedAllergens)
        val ingredientToDefinitiveAllergen = reduceAllergens(ingredientToReducedAllergens)
        println(ingredientToDefinitiveAllergen.toSortedMap().values.map { it.first() }.joinToString(separator = ","))
    }

    private fun reduceAllergens(ingredientToAllergen: Map<String, Set<String>>): Map<String, Set<String>> {
        var result: Map<String, Set<String>> = ingredientToAllergen
        while (result.values.any { it.size > 1 }) {
            val definitiveIngredients = result.values.filter { it.size == 1 }.flatMap { it }.toSet()
            result = result
                .map {it.key to if (it.value.size == 1) it.value else it.value.minus(definitiveIngredients)}
                .toMap()
        }
        return result
    }

    private fun extractIngredients(meal: String): Set<String> {
        return meal.split("(contains")[0]
            .trim()
            .split(" ")
            .toSet()
    }

    fun extractAllergens(meal: String): Set<String> {
        return meal
            .split("(contains")[1]
            .trim()
            .removeSuffix(")")
            .trim()
            .split(", ")
            .toSet()

    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = Allergies212();
    baggage.run()

}
