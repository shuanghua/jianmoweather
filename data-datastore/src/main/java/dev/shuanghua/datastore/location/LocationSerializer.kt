package dev.shuanghua.datastore.location

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import dev.shuanghua.datastore.Location
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class LocationSerializer @Inject constructor() : Serializer<Location> {
    override val defaultValue: Location = Location.getDefaultInstance()
    override suspend fun writeTo(t: Location, output: OutputStream) = t.writeTo(output)
    override suspend fun readFrom(input: InputStream): Location {
        try {
            return Location.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }
}