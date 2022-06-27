class Threads {

    fun runChain() {
        longRunningMethodWithAResult {
            println(it)
            longRunningMethodWithAResult {
                println(it)
                longRunningMethodWithAResult {
                    println(it)
                }
            }
        }
    }

    private fun longRunningMethodWithAResult(callback:(Int) -> Unit) {
        Thread {
            Thread.sleep(1000) //long-running operation
            println("Success")
            callback(1)
        }.start()
    }
}