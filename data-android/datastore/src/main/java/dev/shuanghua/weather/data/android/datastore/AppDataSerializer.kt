package dev.shuanghua.weather.data.android.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import dev.shuanghua.weather.data.android.serializer.DataStoreMoshiSerializer
import dev.shuanghua.weather.data.android.serializer.model.AppDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class AppDataSerializer @Inject constructor(
    private val serializer: DataStoreMoshiSerializer
) : Serializer<AppDataStore> {

    override val defaultValue: AppDataStore = AppDataStore()

    override suspend fun writeTo(
        t: AppDataStore,
        output: OutputStream
    ) = withContext(Dispatchers.IO) {
        output.write(serializer.toJson(t).encodeToByteArray())
    }

    /**
     * 可以向 Repo 提供自己的 内部模型
     */
    override suspend fun readFrom(input: InputStream): AppDataStore {
        try {
            return serializer.fromJson(input.readBytes().decodeToString())
        } catch (serialization: CorruptionException) {
            throw CorruptionException("Unable to read AppLocation Data !", serialization)
        }
    }

}