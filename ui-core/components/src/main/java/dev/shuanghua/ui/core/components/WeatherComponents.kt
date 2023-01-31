package dev.shuanghua.ui.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.model.previewWeather
import timber.log.Timber

@Composable
fun AlarmIconsItem(
    iconUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(// coil 异步下载网络图片
        modifier = modifier
            .size(44.dp, 40.dp)
            .padding(start = 2.dp)
            .clip(shape = RoundedCornerShape(percent = 10)),
        model = iconUrl,
        contentDescription = null
    )
}


@Composable
fun MainTemperature(
    weather: Weather,
    modifier: Modifier = Modifier,
    openDistrictListScreen: (String, String) -> Unit = { _, _ -> },
    openAirDetailsScreen: (String) -> Unit = {}
) {
    Surface(
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(36.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            //.padding(top = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = weather.temperature,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 68.sp),
                textAlign = TextAlign.Start,
            )

            Text(
                modifier = modifier.padding(vertical = 8.dp),
                text = weather.description,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium,
            )

            if (weather.airQuality.isNotBlank()) {
                TextButton(onClick = { openAirDetailsScreen(weather.cityId) }) {
                    AsyncImage(
                        modifier = modifier.size(18.dp, 18.dp),
                        model = weather.airQualityIcon,
                        contentDescription = "空气质量"
                    )
                    Text(text = weather.airQuality)
                }
            }

            Spacer(modifier = modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    if (weather.stationName != "") {
                        openDistrictListScreen(
                            weather.cityId,
                            weather.stationId
                        )
                    }
                },
            ) {
                Text(
                    text = weather.stationName,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = modifier.height(16.dp))
    }
}

@Composable
fun HorizontalListTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun OneItem(
    modifier: Modifier = Modifier,
    topText: String,
    centerText: String,
    bottomText: String,
    dialogText: String = "",
) {
    var dialogShow by remember { mutableStateOf(false) }
    if (dialogShow) {
        OneDayDescriptionDialog(dialogText) { dialogShow = false }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(94.dp)
            .clickable(
                enabled = dialogText.isNotEmpty(),
                onClick = { dialogShow = true }
            )
    ) {
        Text(text = topText, fontWeight = FontWeight.Bold)
        Text(text = centerText, modifier.padding(vertical = 16.dp))
        Text(text = bottomText, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ConditionItem(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.padding(16.dp),
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(36.dp)
    ) {
        Column(
            modifier = modifier
                .width(148.dp)
                .height(90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = name)
            Spacer(Modifier.height(8.dp))
            Text(text = value, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ExponentItem(
    title: String,
    levelDesc: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(6.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.width(300.dp)
        ) {
            // 如果有图标在此处插入
            Text(
                text = "$title · $levelDesc",
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                textAlign = TextAlign.Start
            )
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewMainTemperature() {
    MainTemperature(
        weather = previewWeather
    )
}

@Preview
@Composable
internal fun PreviewOneItem() {
    Surface(tonalElevation = 2.dp) {
        LazyRow {
            repeat(10) {
                item {
                    OneItem(
                        topText = "明日",
                        centerText = "1/24",
                        bottomText = "20°~26°",
                    )
                }
            }
        }
    }
}


@Preview
@Composable
internal fun PreviewExponentItem() {
    ExponentItem(
        title = "舒适度指数",
        levelDesc = "舒服"
    )
}