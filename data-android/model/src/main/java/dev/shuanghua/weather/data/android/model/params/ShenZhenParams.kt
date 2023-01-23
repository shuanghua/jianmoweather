package dev.shuanghua.weather.data.android.model.params

import dev.shuanghua.weather.data.android.model.Config

/**
 * Bean -> Map -> JsonString
 * 此请求参数模型类，综合了所有页面的参数
 * 并不是根据每个链接生成对应的模型
 * 本项目中链接对应的模型使用了 Map (因为 url 中的 jsonBody 有嵌套类型，还有重复类型，使用 Bean to Json 很麻烦),
 */
open class ShenZhenParams {
    // 因为自用，所以就不获取真实的设备信息
    val uid: String = Config.uid
    val uname: String = Config.uname
    val token: String = Config.token
    val os: String = Config.os

    val type: String = Config.type
    val version: String = Config.version
    val rever: String = Config.rever
    val net: String = Config.net
    val gif: String = Config.gif
    val w: String = Config.width
    val h: String = Config.height

    open var cityName: String = ""
    open var district: String = ""
    open var lon: String = ""
    open var lat: String = ""
}
