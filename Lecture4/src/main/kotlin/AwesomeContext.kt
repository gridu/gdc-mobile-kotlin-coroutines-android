import kotlin.coroutines.CoroutineContext

class AwesomeContext(val data: String) : CoroutineContext.Element {

    override val key: CoroutineContext.Key<*> = Key

    companion object Key :
        CoroutineContext.Key<AwesomeContext>
}