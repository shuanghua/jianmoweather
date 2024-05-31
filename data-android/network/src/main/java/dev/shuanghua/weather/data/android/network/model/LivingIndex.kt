package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LivingIndex(
	@SerialName("chenlian") val chenlian: ChenLian?,
	@SerialName("chuanyi") val chuanyi: ChuanYi?,
	@SerialName("co") val co: Co?,
	@SerialName("gaowen") val gaowen: GaoWen?,
	@SerialName("liugan") val liugan: LiuGan?,
	@SerialName("luyou") val luyou: LvYou?,
	@SerialName("meibian") val meibian: MeiBian?,
	@SerialName("shushidu") val shushidu: ShuShiDu?,
	@SerialName("ziwaixian") val ziwaixian: ZiWaiXian?,
)


@Serializable
data class ChenLian(
	@SerialName("level") val level: String = "",
	@SerialName("level_advice") val level_advice: String = "",
	@SerialName("level_desc") val level_desc: String = "",
	@SerialName("title") val title: String = "",
)


@Serializable
data class ChuanYi(
	@SerialName("level") val level: String = "",
	@SerialName("level_advice") val level_advice: String = "",
	@SerialName("level_desc") val level_desc: String = "",
	@SerialName("title") val title: String = "",
)


@Serializable
data class Co(
	@SerialName("level") val level: String = "",
	@SerialName("level_advice") val level_advice: String = "",
	@SerialName("level_desc") val level_desc: String = "",
	@SerialName("title") val title: String = "",
)


@Serializable
data class GaoWen(
	@SerialName("level") val level: String = "",
	@SerialName("level_advice") val level_advice: String = "",
	@SerialName("downicon") val level_desc: String = "",
	@SerialName("title") val title: String = "",
)


@Serializable
data class LiuGan(
	@SerialName("level") val level: String = "",
	@SerialName("level_advice") val level_advice: String = "",
	@SerialName("level_desc") val level_desc: String = "",
	@SerialName("title") val title: String = "",
)


@Serializable
data class LvYou(
	@SerialName("level") val level: String = "",
	@SerialName("level_advice") val level_advice: String = "",
	@SerialName("level_desc") val level_desc: String = "",
	@SerialName("title") val title: String = "",
)


@Serializable
data class MeiBian(
	@SerialName("level") val level: String = "",
	@SerialName("level_advice") val level_advice: String = "",
	@SerialName("level_desc") val level_desc: String = "",
	@SerialName("title") val title: String = "",
)


@Serializable
data class ShuShiDu(
	@SerialName("level") val level: String = "",
	@SerialName("level_advice") val level_advice: String = "",
	@SerialName("level_desc") val level_desc: String = "",
	@SerialName("title") val title: String = "",
)


@Serializable
data class ZiWaiXian(
	@SerialName("level") val level: String = "",
	@SerialName("level_advice") val level_advice: String = "",
	@SerialName("level_desc") val level_desc: String = "",
	@SerialName("title") val title: String = "",
)
