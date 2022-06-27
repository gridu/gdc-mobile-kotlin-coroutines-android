import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.suspendCoroutine

fun main() {
    //channelExample()
    flowExample()
}

fun flowExample() = runBlocking {
    val flow = createFlow()
    println("Flow created")
    delay(1000)
    println("Consumer started")
    flow.collect { value -> println("Consumer received - $value") }
}

fun createFlow() = flow {
    for (i in 1..3) {
        delay(100)
        println("Flow emit value - $i")
        emit(i)
    }
}

fun channelExample() = runBlocking {
    val channel = createChannel()
    delay(100)
    println("Consumer started")
    channel.consumeEach { value -> println("Consumer received - $value") }
}

fun CoroutineScope.createChannel() = produce {
    for (i in 1..3) {
        delay(100)
        println("Channel emit value - $i")
        send(i)
    }
}

