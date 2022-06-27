import kotlinx.coroutines.*

fun main() {
    //exceptionExample()
    //exceptionCatchExample()
    //handleExceptionExample()
    //handleChildExceptionExample()
    //supervisorJobWrongExample()
    scopeExample()
}

fun scopeExample() = runBlocking {
    supervisorScope {
        launch {
            delay(1000)
            launch {
                throw Error("Some error")
            }
        }

        launch {
            delay(1500)
            throw Error("Some error")
        }

        launch {
            delay(2000)
            println("Will be printed")
        }
    }
    delay(1000)
    println("Done")
}

fun supervisorJobWrongExample() = runBlocking {
    launch(SupervisorJob()) {
        launch {
            delay(100)
            throw Error("Some error")
        }

        launch {
            delay(200)
            println("Child 2 finished")
        }
    }.join()
}

fun handleChildExceptionExample() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
    val job = GlobalScope.launch {
        val child = launch(handler) {
            println("Child 1 started")
            throw ArithmeticException("BadaBum")
        }

/*        child.invokeOnCompletion {
            println("Child 1 completed")
            it?.let {
                println("Child error: ${it.message}")
            }
        }*/

        launch(handler) {
            delay(50)
            println("Child 2 started")
        }
    }
    job.join()
}

fun handleExceptionExample() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
    val job = GlobalScope.launch(handler) { // root coroutine, running in GlobalScope
        throw AssertionError()
    }
    val deferred = GlobalScope.async(handler) { // also root, but async instead of launch
        throw ArithmeticException() // Nothing will be printed, relying on user to call deferred.await()
    }
    joinAll(job, deferred)
}


fun exceptionCatchExample() = runBlocking {
    launch {
        try {
            launch {
                delay(100)
                println("Throw exception")
                throw Exception("Test")
            }
        } catch (ex: Exception) {
            println("Catch block")
        }



        launch {
            delay(200)
            println("Child 1")
        }

        launch {
            delay(50)
            println("Child 2")
        }
    }
}

fun exceptionExample() = runBlocking {
    launch {
        launch {
            delay(100)
            println("Throw exception")
            throw Exception("Test")
        }

        launch {
            delay(200)
            println("Child 1")
        }

        launch {
            delay(50)
            println("Child 2")
        }
    }
}
