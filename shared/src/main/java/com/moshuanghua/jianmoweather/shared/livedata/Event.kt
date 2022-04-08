package com.moshuanghua.jianmoweather.shared.livedata

import androidx.lifecycle.Observer

open class Event<out T>(private val content: T?) {
    var hasBeenHandled = false
        private set

    /**
     * 要么给你一个 Null,要么给你真实的 值
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * 检查当前事件中的值
     */
    fun peekContent(): T? = content
}

/**
 * 为了避免在观察的时候，都要调用 getContentIfNotHandled
 * 所以封装了一个自己的 Observer,
 * 最终在观察的时候，能直接拿到具体的内容值，而不是 Event 包装的值
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}