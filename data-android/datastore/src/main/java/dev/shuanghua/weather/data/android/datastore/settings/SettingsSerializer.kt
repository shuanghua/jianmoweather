package dev.shuanghua.weather.data.android.datastore.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import dev.shuanghua.weather.data.android.datastore.Settings
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class SettingsSerializer @Inject constructor() : Serializer<Settings> {
    override val defaultValue: Settings = Settings.getDefaultInstance()
    override suspend fun writeTo(t: Settings, output: OutputStream) = t.writeTo(output)
    override suspend fun readFrom(input: InputStream): Settings {
        try {
            return Settings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }
}