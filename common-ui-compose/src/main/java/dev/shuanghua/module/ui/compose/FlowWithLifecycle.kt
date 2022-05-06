@file:Suppress("NOTHING_TO_INLINE")

package dev.shuanghua.module.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


@Composable
fun <T> rememberFlowWithLifecycle(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = remember(flow, lifecycle) {
    flow.flowWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = minActiveState
    )
}

/**
 * 把 Flow 转成 Compose State
 */
@Composable
fun <T> rememberStateFlowWithLifecycle(
    stateFlow: StateFlow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): State<T> {
    val initialValue = remember(stateFlow) { stateFlow.value }
    return produceState(
        initialValue = initialValue,
        key1 = stateFlow,
        key2 = lifecycle,
        key3 = minActiveState
    ) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            stateFlow.collect {
                this@produceState.value = it
            }
        }
    }
}

//inline fun <T> Flow<T>.launchAndCollectIn(
//    owner: LifecycleOwner,
//    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
//    crossinline action: suspend CoroutineScope.(T) -> Unit
//) = owner.lifecycleScope.launch {
//    owner.repeatOnLifecycle(minActiveState) {
//        collect {
//            action(it)
//        }
//    }
//}
