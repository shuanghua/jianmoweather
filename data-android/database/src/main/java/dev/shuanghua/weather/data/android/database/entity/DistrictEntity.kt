package dev.shuanghua.weather.data.android.database.entity

import androidx.room.Entity
import androidx.room.Index
import dev.shuanghua.weather.data.android.model.District

@Entity(
    tableName = "district",
    primaryKeys = ["name"],
    indices = [(Index("name"))]
)
data class DistrictEntity(
    val name: String
)

fun DistrictEntity.asExternalModel() = District(
    name = name
)