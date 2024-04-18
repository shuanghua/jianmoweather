package dev.shuanghua.weather.data.android.network

fun main() {
	val a = 1
	val b = 3
	val c = a + b
	println(c)
}

// ICONST_0  把常量0 放入栈顶 (操作数栈)
// ISTORE 0  栈顶出栈,放入局部变量表 索引为0 的位置   这样就完成了 val a = 0 的执行

// 同理 al b = 3 就应该是:
// ICONST_3
// ISTORE 1  放入局部变量表 索引为1 的位置 (因为0已经被占用了)


// val c = a + b
//    ILOAD 0     LOAD 是针对局部变量表的操作, 这里将索引为 0 的数据放入栈顶
//    ILOAD 1     这里将索引为 1 的数据放入栈顶
//    IADD        将栈顶和栈顶下一层相加且将结果再次放入栈顶
//    ISTORE 2    将栈顶出栈放进局部变量表 索引为 2 的位置


// 总结: 除了 store 是出栈存到局部变量, 其它都是放入操作数栈的栈顶指令
// store 出栈,放到局部变量
// load  从局部变量表 取出 放入栈顶
// const 从常量池中 取出 放入栈顶
// add  栈顶和栈顶前一个相加 放入栈顶
// sub 相减
// mul 相乘
// div 相除
// rem 求余

// iconst 和 sipush 的区别是 数值取值范围的区别
// bipush: -128 ~ 127
// iconst: -1 ~ 5
// sipush: 更大的数
// ldc: 超级大的数












