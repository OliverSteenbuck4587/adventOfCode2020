package twentyTwo

import twentyTwo.PLAYER.PLAYER1
import twentyTwo.PLAYER.PLAYER2

class CrabGame2 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../22crabs.txt")
        val player1 = input.split("\n\n").map { it.split("\n").drop(1).map { it.toInt() } }[0].toMutableList()
        val player2 = input.split("\n\n").map { it.split("\n").drop(1).map { it.toInt() } }[1].toMutableList()


        val game = Game(player1, player2)
        game.play()

        val player1Score = game.player1.reversed().mapIndexed { index, i -> i * (index + 1) }.sum()
        val player2Score = game.player2.reversed().mapIndexed { index, i -> i * (index + 1) }.sum()
        println("Player 1: ${game.player1} score $player1Score")
        println("Player 2: ${game.player2} score $player2Score")


    }


    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}

class Game(val startDeckPlayer1: List<Int>, val startDeckPlayer2: List<Int>) {
    val previousRounds = mutableSetOf<Pair<List<Int>, List<Int>>>()
    val player1 = startDeckPlayer1.toMutableList()
    val player2 = startDeckPlayer2.toMutableList()

    fun play(): PLAYER {
        do {
            previousRounds.add(Pair(player1,player2))
            val player1Card = player1.removeFirst()
            val player2Card = player2.removeFirst()
            if (player1.size >= player1Card && player2.size >= player2Card) {
                val winner = Game(player1.toList().take(player1Card), player2.toList().take(player2Card)).play()
                when (winner) {
                    PLAYER1 -> {
                        player1.add(player1Card)
                        player1.add(player2Card)
                    }
                    PLAYER2 ->{
                        player2.add(player2Card)
                        player2.add(player1Card)
                    }
                }
            } else if (player1Card > player2Card) {
                player1.add(player1Card)
                player1.add(player2Card)
            } else if (player2Card > player1Card) {
                player2.add(player2Card)
                player2.add(player1Card)
            } else {
                throw RuntimeException("Equal Cards and no recursion happened :/")
            }
        } while (player1.isNotEmpty() && player2.isNotEmpty() && !previousRounds.contains(Pair(player1, player2)))

        return when {
            previousRounds.contains(Pair(player1, player2)) -> {
                PLAYER1
            }
            player1.isEmpty() -> PLAYER2
            player2.isEmpty() -> PLAYER1
            else -> throw RuntimeException("Play stopped but both players still have cards")
        }
    }
}

enum class PLAYER {
    PLAYER1, PLAYER2;
}

fun main(args: Array<String>) {
    val baggage = CrabGame2();
    baggage.run()

}