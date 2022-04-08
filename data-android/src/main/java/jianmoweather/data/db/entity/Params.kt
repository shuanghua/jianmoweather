package jianmoweather.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Params(
    @PrimaryKey
    var cityId: String = "28060159493",
    var cityName: String = "",
    var district: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var isLocation: String = "1",

    val stationId: String = "",
    val ids: String = "28060159493,32010145005,28010159287,02010058362,01010054511",
)