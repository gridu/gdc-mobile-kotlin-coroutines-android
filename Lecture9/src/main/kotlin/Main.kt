import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun main() {
    //channelExample()
    //closeChannelExample()
    //cancelChannelExample()
    //exceptionExample()
    //exceptionProduceExample()
    //channelTypesExample()
    channelSendSuspendExample()
}

fun channelSendSuspendExample() = runBlocking {
    val channel = Channel<Int>(capacity = Channel.RENDEZVOUS)
    val sender = launch(Dispatchers.IO) {
        for (i in 0..Int.MAX_VALUE) {
            if (channel.isClosedForSend.not()) {
                println("Send - $i")
                channel.send(i)
            } else return@launch
        }
    }

    val receiver = launch(Dispatchers.IO) {
        for (i in 1..5) {
            delay(100)
            println("Receive - ${channel.receive()}")
        }
        channel.close()
    }

    joinAll(sender, receiver)
}

fun channelTypesExample() = runBlocking {
    val channel = Channel<Int>(capacity = Channel.RENDEZVOUS)
    //val channel = Channel<Int>(capacity = Channel.CONFLATED)
    //val channel = Channel<Int>(capacity = Channel.UNLIMITED)
    //val channel = Channel<Int>(capacity = Channel.BUFFERED)
    val sender = launch(Dispatchers.IO) {
        for (i in 0..Int.MAX_VALUE) {
            if (channel.isClosedForSend.not()) {
                //try {
                println("Send - $i")
                channel.send(i)
                //} catch (ex: Exception) {} //ClosedSendChannelException may occur
            } else return@launch
        }
    }

    val receiver = launch(Dispatchers.IO) {
        for (i in 1..5) {
            delay(100)
            println("Receive - ${channel.receive()}")
        }
        channel.close()
    }

    receiver.join()
    channel.tryReceive()
}

fun exceptionProduceExample() = runBlocking {
    val receiverChannel = produce<Int>(Job()) {
        for (i in 0..6) {
            channel.send(i)
            if (i == 5) {
                1 / 0
            }
        }
    }

    val receiver = launch {
        while (receiverChannel.isClosedForReceive.not()) {
            println("Receiver: value = ${receiverChannel.receive()}")
        }
    }

    joinAll(receiver)
}

fun exceptionExample() = runBlocking {
    val channel = Channel<Int>()
    val sender = launch(Job()) {
        for (i in 0..6) {
            channel.send(i)
            if (i == 5) {
                1 / 0
            }
        }
    }

    val receiver = launch {
        while (channel.isClosedForReceive.not()) {
            println("Receiver: value = ${channel.receive()}")
        }
    }

    joinAll(sender, receiver)
}

fun cancelChannelExample() = runBlocking {
    val channel = Channel<Int>()
    val sender = launch {
        for (i in 0..6) {
            delay(50)
            channel.send(i)
        }
    }

    launch {
        delay(50)
        println("First receiver value - ${channel.receive()}")
    }

    launch {
        while (true) {
            delay(200)
            println("Second receiver value - ${channel.receive()}")
            channel.cancel()
        }
    }

    delay(1000)
}

fun closeChannelExample() = runBlocking {
    val channel = Channel<Int>()
    val sender = launch {
        for (i in 0..6) {
            delay(100)
            channel.send(i)
            if (i == 5) {
                channel.close()
                return@launch
            }
        }
    }

    launch {
        while (channel.isClosedForReceive.not()) {
            delay(50)
            println("First receiver value - ${channel.receive()}")
        }
    }

    launch {
        while (channel.isClosedForReceive.not()) {
            delay(100)
            println("Second receiver value - ${channel.receive()}")
        }
    }

    delay(1000)
}

fun channelExample() = runBlocking {
    val channel = createChannel()
    delay(300)
    println("Consumer started")
    channel.consumeEach { value -> println("Consumer received - $value") }
}

fun CoroutineScope.createChannel() = produce(capacity = 2) {
    for (i in 1..3) {
        delay(100)
        println("Channel emit value - $i")
        send(i)
    }
}

