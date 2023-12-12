import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun launch(context: CoroutineContext, callback: suspend () -> Unit)  {

    CoroutineScope(context).launch( start = CoroutineStart.UNDISPATCHED) {
        callback()
    }
}