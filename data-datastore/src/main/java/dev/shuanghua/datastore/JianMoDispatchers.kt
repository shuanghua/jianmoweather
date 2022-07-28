package dev.shuanghua.datastore

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val jmDispatcher: JianMoDispatchers)

enum class JianMoDispatchers {
    IO
}