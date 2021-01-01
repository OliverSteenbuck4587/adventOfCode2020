package twentyfive


class Decryption1 {
    fun run() {
        val input = readFileAsLinesUsingGetResourceAsStream("../25decryption.txt")
        val cardPublicKey = input.split("\n")[0].toLong()
        val doorPublicKey = input.split("\n")[1].toLong()

        val cardLoopSize = crackPublicKey(7,cardPublicKey)
        val doorLoopSize = crackPublicKey(7,doorPublicKey)
        println("CardLoopSize: $cardLoopSize" )
        println("DoorLoopSize: $doorLoopSize" )
        val encryptionKey = transform(cardPublicKey, doorLoopSize.toInt())
        println("EncryptionKey: $encryptionKey")
    }

    fun transform(subjectNumber: Long, loops: Int):Long{
        var value: Long = 1
        for(loop in 0.until(loops)){
            value = value * subjectNumber
            value = value % 20201227
        }
        return value
    }

    fun crackPublicKey(subjectNumber: Long, targetPublicKey: Long): Long {
        var value = 1.toLong()
        var loops: Long = 0
        while(value != targetPublicKey){
            if(loops % 1000 == 0.toLong())
                println("Still cracking on: $targetPublicKey")
            value = value * subjectNumber
            value = value % 20201227
            loops++
        }
        return loops
    }

    fun readFileAsLinesUsingGetResourceAsStream(fileName: String) =
        this::class.java.getResourceAsStream(fileName).bufferedReader().readText()
}

fun main(args: Array<String>) {
    val baggage = Decryption1();
    baggage.run()

}