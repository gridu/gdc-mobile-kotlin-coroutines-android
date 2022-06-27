import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

fun main() {
    //cancellationExample()
    //cancellationParentChild()
    //cancellationPointExample()
    //cancellationPointExample1()
    //cancellationPointExample2()
    //longRunningExample()
    longRunningCancellableExample()
}

fun longRunningCancellableExample() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5 && isActive) { // computation loop, just wastes CPU
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}

fun longRunningExample() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // computation loop, just wastes CPU
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}

fun cancellationPointExample2() = runBlocking {
    val job = GlobalScope.launch {
        customDelaySuspendable(50)
        println("Printing 1")
        customDelaySuspendable(150)
        println("Printing 2")

    }
    delay(100)
    job.cancelAndJoin()
    println("Cancelled successfully")
}

fun cancellationPointExample1() = runBlocking {
    val job = GlobalScope.launch {
        customDelay(50)
        println("Printing 1")
        customDelay(150)
        println("Printing 2")

    }

    job.cancelAndJoin()
    println("Cancelled successfully")
}

fun cancellationPointExample() = runBlocking {
    val job = GlobalScope.launch {
        delay(50)
        println("Printing 1")
        delay(150)
        println("Printing 2")

    }

    delay(100)
    job.cancel()
    job.join()
    println("Cancelled successfully")
}

fun cancellationParentChild() = runBlocking {
    val parentJob = launch {
        val childJob = launch {
            delay(100)
            println("Child job")
        }

        launch {
            delay(200)
            println("Child1 job")
        }

        launch {
            delay(300)
            println("Child2 job")
        }
        childJob.cancel()
    }
    delay(250)
    parentJob.cancel()
    parentJob.join()
    launch(parentJob) {
        println("Run one more coroutine")
    }.join()
}

fun cancellationExample() = runBlocking {
    val job = launch {
        val child = launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                println("Child is cancelled")
            }
        }
        yield()
        println("Cancelling child")
        child.cancel()
        child.join()
        yield()
        println("Parent is not cancelled")
    }
    job.join()
}

suspend fun customDelay(delay: Int) {
    val stopTime = System.currentTimeMillis() + delay
    while (stopTime > System.currentTimeMillis()) {
    }
}

suspend fun customDelaySuspendable(delay: Int) {
    return suspendCoroutine {
        val stopTime = System.currentTimeMillis() + delay
        while (stopTime > System.currentTimeMillis()) {
        }
    }
}
