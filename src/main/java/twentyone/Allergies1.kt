package twentyone

class Allergies211 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../21allergies.txt")
        val meals = input.split("\n")
        val allergens = meals
            .map{it.split("(contains")[1].trim().removeSuffix(")").trim()}
            .flatMap { it.split(", ") }
            .toSet()
        val allergensToIngredients = allergens.map { it to mutableSetOf<Set<String>>() }.toMap()
        val ingredients = meals.flatMap { extractIngredients(it) }.toSet()

        for(meal in meals){
            extractAllergens(meal).forEach{
                allergensToIngredients[it]!!.add(extractIngredients(meal))
            }
        }

        println(allergensToIngredients)

        val allergenToUniqueIngredients = allergensToIngredients.map { it.key to it.value.reduce{ one, two -> one.intersect(two)} }.toMap()
        val ingredientsWithPossibleAllergen = allergenToUniqueIngredients.values.flatMap { it }.toSet()
        val ingredientsWithoutAllergens = ingredients.minus(ingredientsWithPossibleAllergen)
        val count = ingredientsWithoutAllergens.map { howOftenDoesIngredientAppear(it, meals) }.sum()
        println(count)


    }

    fun howOftenDoesIngredientAppear(ingredient: String, meals: List<String>): Int{
        return meals.map { extractIngredients(it) }.filter { it.contains(ingredient) }.count()
    }

    private fun extractIngredients(meal: String): Set<String> {
       return meal.split("(contains")[0]
            .trim()
            .split(" ")
            .toSet()
    }

    fun extractAllergens(meal: String):Set<String>{
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
    val baggage = Allergies211();
    baggage.run()

}
