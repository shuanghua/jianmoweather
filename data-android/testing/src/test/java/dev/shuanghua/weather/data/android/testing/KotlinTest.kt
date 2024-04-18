package dev.shuanghua.weather.data.android.testing

fun main() {
	val t = JavaTest()  // 此行输出 0 , 先调用 JavaTest 无参构造函数, 然后调用 Super 无参构造函数


	t.printThree()   // 此行输出3
}

open class Super {
	init {
		printThree()
	}

	open fun printThree() {
		println("three")
	}
}

class JavaTest : Super() {
	private val three: Int = Math.PI.toInt() // That is, 3

	override fun printThree() {
		println(three)
	}
}

