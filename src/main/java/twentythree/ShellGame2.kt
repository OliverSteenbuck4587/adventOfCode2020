package twentythree

class ShellGame2 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../23shellgame.txt")

        val startSet = input.toCharArray().map { it.toString().toInt() }.map { Cup(it) }.toMutableList()
        while(startSet.size < 1000000){
            startSet.add(Cup(startSet.size+1))
        }
        val cupList = CupList(startSet)
        val howMany = 10000000
        val startLabel = mapOf(
            1 to 3,
            2 to 3,
            3 to 3,
            4 to 7,
            5 to 3,
            6 to 9,
            7 to 7,
            8 to 8,
            9 to 7,
            10 to 5
        )

        println("Starting turns")
        var currentCup = startSet.first()
        for (turn in 1..howMany) {
            if(turn % 1000000==0) {
                println("Turn $turn")
            }
            cupList.moveToClipboardAfter(currentCup.label)
            var destinationLabel = selectDestionationCup(currentCup.label, 1000000, cupList.clipboardList())
            cupList.insertClipBoardAfter(destinationLabel)
            currentCup = currentCup.next

        }
        println("Finished")
        val cupOne = cupList.get(1)
        println("Goal1: " + cupOne.next.label)
        println("Goal2: " + cupOne.next.next.label)
        val res =  cupOne.next.label.toLong() *  cupOne.next.next.label.toLong()
        println("Result: " + res)

    }

    fun selectDestionationCup(currentCup: Int, maxLabel: Int, movedCups: List<Int>): Int {
        var destinationCup = currentCup
        var minLabel = 1
        do {
            destinationCup -= 1
            if (destinationCup < minLabel)
                destinationCup = maxLabel
        } while (movedCups.contains(destinationCup))
        return destinationCup
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()


}

class Cup(val label: Int) {
    lateinit var next: Cup
}

class CupList(initialCups: List<Cup>) {
    private val cupMap: Map<Int, Cup> = initialCups.map { it.label to it }.toMap()
    private val clipboard = mutableListOf<Cup>()

    init {
        for (i in 1.until(initialCups.size)) {
            val currentCup = initialCups[i]
            val predecessor = initialCups[i - 1]
            predecessor.next = currentCup
        }
        initialCups.last().next = initialCups.first()
    }

    fun toString(startLabel: Int): String {
        val startCup = cupMap[startLabel]!!
        val elems = mutableListOf<Int>()
        var current = startCup
        do {
            elems.add(current.label)
            current = current.next
        } while (current != startCup)
        return elems.joinToString(separator = "  ")
    }

    override fun toString(): String {
        return toString(cupMap.values.first().label)
    }

    fun asList(): List<Int> {
        val startCup = cupMap.values.minus(clipboard).first()
        val elems = mutableListOf<Int>()
        var current = startCup
        do {
            elems.add(current.label)
            current = current.next
        } while (current != startCup)
        return elems
    }

    fun moveToClipboardAfter(cutAfter: Int) {
        val cupToCutAfter = cupMap[cutAfter]!!
        if (clipboard.isNotEmpty())
            throw RuntimeException("Copying to a non empty clipboard")
        clipboard.add(cupToCutAfter.next)
        clipboard.add(cupToCutAfter.next.next)
        clipboard.add(cupToCutAfter.next.next.next)
        cupToCutAfter.next = clipboard.last().next
    }

    fun clipboardList(): List<Int> {
        return clipboard.map { it.label }
    }

    fun insertClipBoardAfter(destinationLabel: Int) {
        val destinationCup = cupMap.get(destinationLabel)!!
        val next = destinationCup.next
        destinationCup.next = clipboard.first()
        clipboard.last().next = next
        clipboard.removeAll { true }
    }

    fun get(label: Int): Cup {
        return cupMap[label]!!
    }


}

fun main(args: Array<String>) {
    val baggage = ShellGame2();
    baggage.run()

}