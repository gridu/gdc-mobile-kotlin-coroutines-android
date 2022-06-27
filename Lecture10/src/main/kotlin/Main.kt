import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() {
    //flowExample()
    //flowTransformExample()
    //flowContextSwitchingExample()
    //preservingContextExample()
    flowExceptionExample()
}

fun flowExceptionExample() = runBlocking {
    val flow = flow {
        for (i in 0..9) {
            emit(i)
        }
    }
        .onEach {
            if (it == 8)
                1 / 0
        }
        .catch { println("Exception occur - $it") }
        .map { "Value $it" }

    flow.collect {
        println("Value received - $it")
    }
}

fun preservingContextExample() = runBlocking {
    val flow = flow {
        for (i in 0..9) {
            withContext(Dispatchers.IO) {
                emit(i)
            }
        }
    }
        .filter { it % 2 == 0 }

    flow.collect {
        println("Value received - $it")
    }
}

fun flowContextSwitchingExample() = runBlocking {
    val flow = flow {
        for (i in 0..9) {
            emit(i)
        }
    }
        .filter { it % 2 == 0 }
        .onEach { println("1st flowOn - ${Thread.currentThread().name}") }
        .flowOn(Dispatchers.IO)
        .onEach { println("2st flowOn - ${Thread.currentThread().name}") }
        .flowOn(Dispatchers.Default)

    flow.collect {
        println("Value received - $it")
    }
}

fun flowTransformExample() = runBlocking {
    val flow = flow {
        for (i in 0..9) {
            emit(i)
        }
    }
        .filter { it % 2 == 0 }
        .map { "Value $it" }
        .onEach { delay(10) }
    println("Flow created")
    delay(100)
    println("Delay finished")
    flow.collect {
        println("Value received - $it")
    }
}

fun flowExample() = runBlocking {
    val flow = flow {
        for (i in 0..9) {
            delay(10)
            emit(i)
        }
    }
    println("Flow created")
    delay(100)
    println("Delay finished")
    flow.collect {
        println("Value received - $it")
    }
}