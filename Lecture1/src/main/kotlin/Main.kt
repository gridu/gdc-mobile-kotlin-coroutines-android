import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    //Lecture1.threadsTest()
    //Lecture1.coroutinesTest()
    //Lecture1.threadsLimitationTest()
}

object Lecture1 {
    fun threadsTest() {
        val start = System.currentTimeMillis()
        val threads = List(100_000) {
            Thread {
                Thread.sleep(1000L)
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }

        val stop = System.currentTimeMillis()
        printExecutionTime(start, stop)
    }

    fun coroutinesTest() = runBlocking {
        val start = System.currentTimeMillis()
        val jobs = List(100_000) {
            launch {
                delay(1000L)
            }
        }
        jobs.forEach { it.join() }
        val stop = System.currentTimeMillis()
        printExecutionTime(start, stop)
    }

    fun threadsLimitationTest() = runBlocking {
        val threadHashSet: MutableSet<String> = mutableSetOf()
        val start = System.currentTimeMillis()
        val jobs = List(100_000) {
            //runBlocking run on main thread let's run coroutine with Dispatchers.Default
            launch(Dispatchers.Default) {
                delay(1000L)
                threadHashSet.add (Thread.currentThread().name)
            }
        }
        jobs.forEach { it.join() }
        val stop = System.currentTimeMillis()

        threadHashSet.sortedBy { it }.forEach { println(it)}
        println("Set size = ${threadHashSet.size}")
        printExecutionTime(start, stop)
    }

    private fun printExecutionTime(start: Long, stop: Long) {
        val diff: Long = stop - start
        val seconds = diff / 1000
        println("Executed in: $seconds seconds")
    }
}