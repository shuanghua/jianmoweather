package dev.shuanghua.ui.core.sample

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp,
) = this.layout { measurable, constraints ->
    // Measure the composable
    val placeable: Placeable = measurable.measure(constraints)
    // 图片地址: https://developer.android.com/static/images/jetpack/compose/layout-padding-baseline.png
    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline: Int = placeable[FirstBaseline] // 获得基线位置

    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline // 传入的值和计算的值做计算
    val height = placeable.height + placeableY


    layout(placeable.width, height) { // 布局大小
        // Where the composable gets placed

        // 放置的位置是以 矩形的左上顶点 为参考
        placeable.placeRelative(0, placeableY) // 位置
    }
}