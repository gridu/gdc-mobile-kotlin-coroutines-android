import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.*

fun main() {
    //printContext()
    //printContextElements()
    //runCalculationCancelled()
    //mergeContext()
    //mergeDispatcher()
    //customContext()
}

fun customContext() = runBlocking {
    println("Parent context ${this.coroutineContext}")
    withContext(AwesomeContext("Passed data")) {
        launch {
            println("Child coroutine 1 ${this.coroutineContext}")
            println("Data = ${this.coroutineContext[AwesomeContext]?.data}")
            launch {
                println("Child coroutine 2 ${this.coroutineContext}")
                println("Data = ${this.coroutineContext[AwesomeContext]?.data}")
            }
        }
    }
}

fun mergeDispatcher() {
    val dispatcherIO:CoroutineContext = Dispatchers.IO
    println("DispatcherIO = $dispatcherIO")

    val dispatcherDefault:CoroutineContext = Dispatchers.Default
    println("DispatcherDefault = $dispatcherDefault")
    val merged = dispatcherDefault + dispatcherIO
    println("Merged dispatchers = $merged")
    println(dispatcherIO[ContinuationInterceptor])
}

fun mergeContext() {
    val job = Job()
    println("Job = $job")
    val dispatcher = Dispatchers.IO
    println("Dispatcher = $dispatcher")
    val merged = job + dispatcher
    println("Merged context = $merged")
}

fun printContextElements() = runBlocking {
    println(this.coroutineContext[Job])
    println(this.coroutineContext[ContinuationInterceptor])
}

fun runCalculationCancelled() = runBlocking {
    launch {
        val first = getFirstCancelled()
        val second = getSecond()
        val result = calculate(first, second)
        println("Result = $result")
    }
}

suspend fun getFirstCancelled() : Int {
    return suspendCoroutine { continuation ->
        Thread {
            continuation.context[Job]?.cancel("")
            Thread.sleep(1000)
            continuation.resumeWith(Result.success(2))
        }.run()
    }
}

suspend fun getSecond() : Int {
    delay(100)
    return 3
}

suspend fun calculate(first: Int, second: Int) : Int {
    delay(100)
    return first + second
}

fun printContext() = runBlocking {
    println(this.coroutineContext)
    launch {
        println(this.coroutineContext)
    }
}