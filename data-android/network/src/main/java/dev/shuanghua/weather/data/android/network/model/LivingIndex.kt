package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LivingIndex(
	@field:Json(name = "chenlian") val chenlian: ChenLian?,
	@field:Json(name = "chuanyi") val chuanyi: ChuanYi?,
	@field:Json(name = "co") val co: Co?,
	@field:Json(name = "gaowen") val gaowen: GaoWen?,
	@field:Json(name = "liugan") val liugan: LiuGan?,
	@field:Json(name = "luyou") val luyou: LvYou?,
	@field:Json(name = "meibian") val meibian: MeiBian?,
	@field:Json(name = "shushidu") val shushidu: ShuShiDu?,
	@field:Json(name = "ziwaixian") val ziwaixian: ZiWaiXian?,
)


@JsonClass(generateAdapter = true)
data class ChenLian(
	@field:Json(name = "level") val level: String = "",
	@field:Json(name = "level_advice") val level_advice: String = "",
	@field:Json(name = "level_desc") val level_desc: String = "",
	@field:Json(name = "title") val title: String = "",
)


@JsonClass(generateAdapter = true)
data class ChuanYi(
	@field:Json(name = "level") val level: String = "",
	@field:Json(name = "level_advice") val level_advice: String = "",
	@field:Json(name = "level_desc") val level_desc: String = "",
	@field:Json(name = "title") val title: String = "",
)


@JsonClass(generateAdapter = true)
data class Co(
	@field:Json(name = "level") val level: String = "",
	@field:Json(name = "level_advice") val level_advice: String = "",
	@field:Json(name = "level_desc") val level_desc: String = "",
	@field:Json(name = "title") val title: String = "",
)


@JsonClass(generateAdapter = true)
data class GaoWen(
	@field:Json(name = "level") val level: String = "",
	@field:Json(name = "level_advice") val level_advice: String = "",
	@field:Json(name = "downicon") val level_desc: String = "",
	@field:Json(name = "title") val title: String = "",
)


@JsonClass(generateAdapter = true)
data class LiuGan(
	@field:Json(name = "level") val level: String = "",
	@field:Json(name = "level_advice") val level_advice: String = "",
	@field:Json(name = "level_desc") val level_desc: String = "",
	@field:Json(name = "title") val title: String = "",
)


@JsonClass(generateAdapter = true)
data class LvYou(
	@field:Json(name = "level") val level: String = "",
	@field:Json(name = "level_advice") val level_advice: String = "",
	@field:Json(name = "level_desc") val level_desc: String = "",
	@field:Json(name = "title") val title: String = "",
)


@JsonClass(generateAdapter = true)
data class MeiBian(
	@field:Json(name = "level") val level: String = "",
	@field:Json(name = "level_advice") val level_advice: String = "",
	@field:Json(name = "level_desc") val level_desc: String = "",
	@field:Json(name = "title") val title: String = "",
)


@JsonClass(generateAdapter = true)
data class ShuShiDu(
	@field:Json(name = "level") val level: String = "",
	@field:Json(name = "level_advice") val level_advice: String = "",
	@field:Json(name = "level_desc") val level_desc: String = "",
	@field:Json(name = "title") val title: String = "",
)


@JsonClass(generateAdapter = true)
data class ZiWaiXian(
	@field:Json(name = "level") val level: String = "",
	@field:Json(name = "level_advice") val level_advice: String = "",
	@field:Json(name = "level_desc") val level_desc: String = "",
	@field:Json(name = "title") val title: String = "",
)
