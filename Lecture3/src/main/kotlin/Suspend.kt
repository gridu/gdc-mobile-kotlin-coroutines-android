import kotlinx.coroutines.delay

class Suspend {
    fun print() {
        Thread.sleep(1000)
        println("Print")
    }

    suspend fun printSuspend() {
        delay(1000)
        println("Print")
    }

    suspend fun networkRequest(url: String, delay: Int): String {
        val stopTime = System.currentTimeMillis() + delay
        while (stopTime > System.currentTimeMillis()) {}
        println("Request $url finished")
        return "Request response"
    }
}