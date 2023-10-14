package dev.shuanghua.weather.data.android.datastore.serialization

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import dev.shuanghua.weather.data.android.datastore.model.DataStoreModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

/**
 * 实现 datastore 库的 Serializer 接口
 * 传入自定义的的序列化实现对象 DataStoreSerialization,
 * 该对象可自行使用别的 json 解析库实现
 */
class AppDataSerializer(
	private val serializer: DataStoreSerialization
) : Serializer<DataStoreModel> {

	override val defaultValue: DataStoreModel = DataStoreModel()

	override suspend fun writeTo(
		t: DataStoreModel,
		output: OutputStream
	) = withContext(Dispatchers.IO) {
		output.write(serializer.toJson(t).encodeToByteArray())
	}


	override suspend fun readFrom(input: InputStream): DataStoreModel {
		try {
			return serializer.fromJson(input.readBytes().decodeToString())
		} catch (serialization: CorruptionException) {
			throw CorruptionException("Unable to read AppLocation Data !", serialization)
		}
	}

}