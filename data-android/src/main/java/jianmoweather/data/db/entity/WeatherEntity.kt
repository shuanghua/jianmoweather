package jianmoweather.data.db.entity

import androidx.room.*
import com.squareup.moshi.JsonClass

/**
 * 当前温度及描述
 */
@Entity(
    indices = [Index(
        value = ["cityId"], // 将 cityId 设置为唯一索引
        unique = true
    )]
)
data class WeatherScreenEntity(
    @PrimaryKey
    val screen: String,// 用于区分以后如果定位城市和手动收藏城市都需要保存数据库时的情况(未实现)
    val cityId: String,
    val cityName: String,
    val temperature: String,
    val description: String,
    val aqi: String,
    val lunar: String,
    val stationName: String
)

/**
 * 预警图片
 * @Transient KVM序列化的时候跳过
 * @ColumnInfo Room 设置数据列名
 * Index: Room 索引
 * ForeignKey 外键，设置外键必须对应主表中唯一的索引，比如主表的外键
 */
@Entity(
    indices = [Index("_cityId")],
    foreignKeys = [ForeignKey(
        entity = WeatherScreenEntity::class,
        parentColumns = ["cityId"],
        childColumns = ["_cityId"],
        onDelete = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    @Transient @ColumnInfo(name = "_cityId") val cityId: String,
    var icon: String,
    var name: String
)

/**
 * 湿度，气压...
 */
@Entity(
    indices = [(Index("_cityId"))],
    foreignKeys = [(ForeignKey(
        entity = WeatherScreenEntity::class,
        parentColumns = ["cityId"],
        childColumns = ["_cityId"],
        deferred = true,
        onDelete = ForeignKey.CASCADE
    ))]
)
data class OtherItem(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    @Transient @ColumnInfo(name = "_cityId")
    var cityId: String = "",
    val name: String,
    val value: String
)

/**
 * 健康指数
 */
@Entity(
    indices = [(Index("_cityId"))],
    foreignKeys = [(ForeignKey(
        entity = WeatherScreenEntity::class,
        parentColumns = ["cityId"],
        childColumns = ["_cityId"],
        deferred = true,
        onDelete = ForeignKey.CASCADE
    ))]
)
data class HealthExponent(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    @Transient @ColumnInfo(name = "_cityId")
    var cityId: String = "",
    val title: String,
    val level: String,
    val levelDesc: String,
    val levelAdvice: String
)

/**
 * 每小时天气
 */
@Entity(
    indices = [(Index("_cityId"))],
    foreignKeys = [(ForeignKey(
        entity = WeatherScreenEntity::class,
        parentColumns = ["cityId"],
        childColumns = ["_cityId"],
        deferred = true,
        onDelete = ForeignKey.CASCADE
    ))]
)
@JsonClass(generateAdapter = true)
data class OneHour(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @Transient @ColumnInfo(name = "_cityId") var cityId: String = "",
    val hour: String,
    val t: String,
    val icon: String,
    val rain: String
)

/**
 * 每天天气
 */
@Entity(
    indices = [(Index("_cityId"))],
    foreignKeys = [(ForeignKey(
        entity = WeatherScreenEntity::class,
        parentColumns = ["cityId"],
        childColumns = ["_cityId"],
        deferred = true,
        onDelete = ForeignKey.CASCADE
    ))]
)
data class OneDay(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    @Transient @ColumnInfo(name = "_cityId")
    var cityId: String = "",
    val date: String,//日期
    val week: String,//今天
    val desc: String,//描述
    val minT: String,//最低温度
    val maxT: String,//最高温度
    val iconName: String//相应天气 icon 名字
)

@Entity(
    indices = [(Index("_cityId"))],
    foreignKeys = [(ForeignKey(
        entity = WeatherScreenEntity::class,//entity：指定父表所对应的类
        parentColumns = ["cityId"],//一般的，子表外键都设置为对应为父表的主键，
        childColumns = ["_cityId"],
        deferred = true,
        onDelete = ForeignKey.CASCADE
    ))]
)
data class HalfHour(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    @Transient @ColumnInfo(name = "_cityId") var cityId: String = "",
    val hour: String,//未来半小时时间
    val t: String//未来半个小时天气
)