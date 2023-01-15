package dev.shuanghua.weather.data.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.shuanghua.weather.data.android.model.AlarmIcon

/**
 * 预警图片
 * @Transient KVM序列化的时候跳过
 * @ColumnInfo Room 设置数据列名
 * Index: Room 索引
 * ForeignKey 外键，设置外键必须对应主表中唯一的索引，比如主表的外键
 */
@Entity(
    tableName = "alarm",
    indices = [Index("_cityId")],
    foreignKeys = [ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["cityId"],
        childColumns = ["_cityId"],
        onDelete = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class AlarmIconEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "_cityId", defaultValue = "") val cityId: String,
    @ColumnInfo(defaultValue = "") val iconUrl: String,
    @ColumnInfo(defaultValue = "") val name: String,
)

fun AlarmIconEntity.asExternalModel() = AlarmIcon(
    id = id,
    cityId = cityId,
    iconUrl = iconUrl,
    name = name
)
