package dev.shuanghua.ui.core.sample

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val DividerLengthInDegrees = 4f //每段弧的间距所对应的角度

private enum class AnimatedCircleProgress { START, END }

@Composable
fun AnimatedCircle(
	modifier: Modifier = Modifier,
	proportions: List<Float> = listOf(0.3f, 0.2f, 0.1f, 0.2f, 0.2f),
	colors: List<Color> = listOf(
		Color.Blue,
		Color.Red,
		Color.Yellow,
		Color.Black,
		Color.Cyan
	),
) {

	// dp to px 需要 LocalDensity 环境
	val stroke = with(LocalDensity.current) { Stroke(5.dp.toPx()) }

	val currentState: MutableTransitionState<AnimatedCircleProgress> = remember {
		MutableTransitionState(
			AnimatedCircleProgress.START
		).apply {
			targetState = AnimatedCircleProgress.END
		}
	}

	//创建动画
	val transition = rememberTransition(transitionState = currentState, label = "")


	val angleOffset: Float by transition.animateFloat(
		// 偏移角度
		label = "",
		transitionSpec = { // 过渡规范
			tween( // 补间
				delayMillis = 500,// 多少毫秒后开始 start动画
				durationMillis = 900,// 动画时长
				easing = LinearOutSlowInEasing// 开始到结束的缓动曲线 线性
			)
		},
		targetValueByState = { progress ->
			if (progress == AnimatedCircleProgress.START) {
				0f
			} else {
				360f
			}
		})

	val shift: Float by transition.animateFloat( //  移位
		label = "",
		transitionSpec = {
			tween(
				delayMillis = 500,
				durationMillis = 900,
//                easing = FastOutSlowInEasing
				easing = CubicBezierEasing( // 三阶贝塞尔【两个控制点(x,y)】。这等同于 Android 的 PathInterpolator
					0f,
					0.75f,
					0.35f,
					0.85f
				)
			)
		}
	) { progress ->
		if (progress == AnimatedCircleProgress.START) {
			0f // 当绘制第一段弧的时候不需要设置空格间距的偏移角度
		} else {
			30f
		}
	}

	Canvas(modifier.fillMaxSize()) {
		//size.minDimension: 取宽高的最小值
		val innerRadius = (size.minDimension - stroke.width) / 2
		val halfSize = size / 2.0f
		val topLeft = Offset( // 默认圆在屏幕正中心
			halfSize.width - innerRadius,
			halfSize.height - innerRadius
		)
		val size = Size(innerRadius * 2, innerRadius * 2)
		var startAngle = shift - 90f  //动画的起始角度线在 0 - 90 = -90°位置 ； 而实际绘制的线是在 30 - 90 = -60°位置

		proportions.forEachIndexed { index, proportion ->
			val sweep = proportion * angleOffset //每段弧所占的角度，也就是根据用户数据来均分 360°
			drawArc(    // 绘制弧线
				color = colors[index],
				startAngle = startAngle + DividerLengthInDegrees / 2,//起始半径线的位置，
//                startAngle = startAngle,// 实际上 /2 和不/2 只是绘制的位置不一样，不影响每段弧的面积比例
				sweepAngle = sweep - DividerLengthInDegrees,// 结束半径线的位置
				topLeft = topLeft,
				size = size,
				useCenter = false,
				style = stroke
			)
			startAngle += sweep
		}
	}

}


@Preview(device = "spec:width=411dp,height=891dp", showBackground = true)
@Composable
fun PreTest() {
	AnimatedCircle()
}