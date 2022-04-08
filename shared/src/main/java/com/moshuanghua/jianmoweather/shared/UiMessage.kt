package com.moshuanghua.jianmoweather.shared

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*

data class UiMessage(
    val message: String,
    val id: Long = UUID.randomUUID().mostSignificantBits
)

class UiMessageManager {
    private val mutex = Mutex()

    private val _messages = MutableStateFlow(emptyList<UiMessage>())
    val message: Flow<UiMessage?> = _messages
        .map {it.firstOrNull()}
        .distinctUntilChanged()//后续的值如果和第一个值一样,则都将过滤掉(去掉重复)

    suspend fun emitMessage(message: UiMessage) {
        mutex.withLock {
            _messages.value = _messages.value + message
        }
    }

    suspend fun clearMessage(id: Long) {
        mutex.withLock {
            _messages.value = _messages.value.filter {it.id == id} // 当两id相等的时候，就返回没有该id的列表
        }
    }
}