import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    //runTreadsExample()
    //suspendExample()
    //CoroutineScope(Dispatchers.IO).suspendExample()
    //suspendNonBlockingExample()
    //CoroutineScope(Dispatchers.IO).suspendNonBlockingExample()

}

private suspend fun CoroutineScope.suspendNonBlockingExample() {
    val suspendExample = SuspendNonBlocking()

    launch {
        suspendExample.networkRequest("https://github.com/", 1000)
    }

    launch {
        suspendExample.networkRequest("https://bitbucket.org/", 500)
    }

    delay(2000)
}

private suspend fun CoroutineScope.suspendExample() {
    val suspendExample = Suspend()

    launch {
        println(Thread.currentThread().name)
        suspendExample.networkRequest("https://github.com/", 1000)
    }

    launch {
        println(Thread.currentThread().name)
        suspendExample.networkRequest("https://bitbucket.org/", 500)
    }

    delay(2000)
}

private fun runTreadsExample() {
    Thread {
        Threads().runChain()
    }.start()
}
