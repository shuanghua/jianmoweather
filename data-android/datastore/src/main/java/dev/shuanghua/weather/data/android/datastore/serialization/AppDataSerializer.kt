package dev.shuanghua.weather.data.android.datastore.serialization

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import dev.shuanghua.weather.data.android.datastore.AppPreferences
import java.io.InputStream
import java.io.OutputStream

/**
 * 配合 protobuf 插件写法
 * AppPreferences 是 protobuf 插件根据 app_preferences.proto 生成的类
 *
 * 如果不使用 protobuf 插件，则需要使用序列化库手动实现序列化和反序列化
 */
object AppDataSerializer : Serializer<AppPreferences> {
	override val defaultValue: AppPreferences = AppPreferences.getDefaultInstance()
	override suspend fun writeTo(t: AppPreferences, output: OutputStream) = t.writeTo(output)
	override suspend fun readFrom(input: InputStream): AppPreferences {
		try {
			return AppPreferences.parseFrom(input)
		} catch (serialization: InvalidProtocolBufferException) {
			throw CorruptionException("Unable to read DataStore Data !", serialization)
		}
	}
}