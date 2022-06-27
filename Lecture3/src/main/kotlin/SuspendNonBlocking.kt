import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SuspendNonBlocking {
    suspend fun networkRequest(url: String, delay: Int): String {
        withContext(Dispatchers.IO) {
            val stopTime = System.currentTimeMillis() + delay
            while (stopTime > System.currentTimeMillis()) {
            }
            println("Request $url finished")
        }
        return "Request response"
    }
}