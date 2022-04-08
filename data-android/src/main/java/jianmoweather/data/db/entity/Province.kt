package jianmoweather.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 省份 Entity
 * Created by ShuangHua on 2017/11/20.
 */
@Entity(
    tableName = "province",
    primaryKeys = ["cityId"],
    indices = [(Index("cityId"))]
)
@JsonClass(generateAdapter = true)
data class Province(
    @Json(name = "provName") val name: String,
    @Json(name = "provId") val cityId: String
)
