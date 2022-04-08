@file:Suppress("NOTHING_TO_INLINE")

package com.jianmoweather.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

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
