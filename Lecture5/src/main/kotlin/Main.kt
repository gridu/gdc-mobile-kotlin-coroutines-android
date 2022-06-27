import kotlinx.coroutines.*

fun main() {
    //jobExample()
    //jobExampleLazy()
    //jobCancel()
    //parentJobCancellation()
    //breakJobRelationExample()
    awaitExample()
}

fun awaitExample() = runBlocking {
    val deferred = async {
        delay(100)
        "I'm result"
    }

    println("Result: ${deferred.await()}")
}

fun breakJobRelationExample() = runBlocking {
    //launch(Job()) {
    launch {
        delay(100)
        println("Job started")
    }

    val currentJob: Job = coroutineContext.job
    println("Current Job children count: ${currentJob.children.count()}")
}

fun parentJobCancellation() = runBlocking {
    val job = launch {
        println("Parent started")
        delay(100)
        launch { println("Child started") }
    }
    delay(50)
    job.cancel()
}

fun jobExample() = runBlocking {
    val job = launch {
        delay(1000)
    }
    job.printState()
    delay(1100)
    job.printState()
}

fun jobExampleLazy() = runBlocking {
    val job = launch(start = CoroutineStart.LAZY) {
        delay(1000)
    }
    job.printState()
    job.start()
    job.printState()
    delay(1100)
    job.printState()
}

fun jobCancel() = runBlocking {
    val job = launch {
        delay(1000)
    }
    job.cancel()
    job.printState()
    delay(1100)
    job.printState()
}

fun Job.printState() {
    val state = when {
        isActive -> "Active/Completing"
        isCompleted && isCancelled -> "Cancelled"
        isCancelled -> "Cancelling"
        isCompleted -> "Completed"
        else -> "New"
    }
    println("Job state: $state")
}
