package twentyTwo

class CrabGame1 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../22crabs.txt")
        val player1 = input.split("\n\n").map { it.split("\n").drop(1).map { it.toInt() } }[0].toMutableList()
        val player2 = input.split("\n\n").map { it.split("\n").drop(1).map { it.toInt() } }[1].toMutableList()


        do {
            val player1Card = player1.removeFirst()
            val player2Card = player2.removeFirst()
            if (player1Card > player2Card) {
                player1.add(player1Card)
                player1.add(player2Card)
            } else if (player2Card > player1Card) {
                player2.add(player2Card)
                player2.add(player1Card)
            } else {
                throw RuntimeException("Equal Cards :/")
            }
        }while (player1.isNotEmpty() && player2.isNotEmpty())

        val player1Score = player1.reversed().mapIndexed { index, i -> i * (index+1) }.sum()
        val player2Score = player2.reversed().mapIndexed { index, i -> i * (index+1) }.sum()
        println("Player 1: $player1 score $player1Score")
        println("Player 2: $player2 score $player2Score")


    }


    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}


fun main(args: Array<String>) {
    val baggage = CrabGame1();
    baggage.run()

}