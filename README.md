## 简墨天气 App 笔记
在写这个项目时，做的一些笔记，有一小部分的笔记可能和本项目无关。



### 笔记
在解决 Compose 函数重组问题的时候：
1. 分表查询, 将各自 Flow<List<OneDay>> 结果取出 List<OneDay> 单独放入各自 xxxUiState, 在将 uiState转成
   StateFlow<xxUiState>. 也就是不使用 combine
   > 好处: 多表插入的时候不会多次影响该表查询结果

2. 分表查询, 将各自 Flow<List<OneDay>> 都放入同一个 combine 中, 然后 new UiState , 返回 StateFlow<UiState>

3. 使用关系查询, 查询结果都放在一个 Pojo 类中 也就是 Flow<Pojo>, 放入 combine , 然后 new UiState , 返回 StateFlow<UiState>

尝试了上面三种方式从数据库查询数据, 然后在 ComposeUi 中收集 StateFlow 完成显示 , 除了第 1 个方案, 剩下两个方案的问题就是触发多次重组， 然后就有了一下的疑问:
"为什么多次重组?" - 因为只有 Compose 数据对象结构发生变化的时候才会重组，而一个 Compose 多次重组则是受多表分开插入的导致的.
"为什么每次插入相同的数据都会导致数据结构发生变化?" - 因为在 Entity 类中使用了自增长的_id, 每次插入数据库的这个_id 都不一样
"为什么 Pojo 每次查询完的内存地址都会不一样?" - 在获得查询结果第一道工序处理时使用了 .distinctUntilChanged() 来避免重复结构的对象, 又因为上面 _id
的不一样导致内嵌类或者关联类的结构不一样,最终 .distinctUntilChanged() 就会丢弃上一个对象, 已让新的 pojo 发送

- 总结:
    - bug1: 插入的时候因为 _id 自增长问题, 导致每次数据结构都不一样, 改为手动设置 id, 这样让查询结果的结构更加稳定, pojo 内存地址也更加稳定, 除非内部有数据变动

- 解决:
    - 删除自动增长的 id
    - 多表分开插入改成一次事务插入
    


### 笔记1
````kotlin
fun observerLocation(): Flow<AMapLocation> = callbackFlow {
	val callback = AMapLocationListener { location ->
		when(val result = location.status()) {
			is LocationSuccess -> {
				// sen() 是可挂起函数
				// trySend() 不是可挂起函数，能返回发送的状态，管道满返回 false, 意味发送失败
                
				// trySendBlocking 可以在不同条件下决定是使用 send() 还是使用 trySend() 的一个扩展函数
				// 如果管道没有满，则使用 trySend() 去发送，如果满了, 则开启一个协程使用可挂起的 sen() 去发送，当 sen() 发送失败, 则抛出异常
				// trySendBlocking 内部开启了一个 runBlocking 协程（ runBlocking 会阻塞当前线程，因此在 runBlocking 内的代码状态是安全的），
                // 因为 runBlocking 已经开启了一个协程, 所以千万不要自己再开起一个协程来使用 trySendBlocking , 避免两层协程
				trySendBlocking(location) // 内部：发送成功：直接 return ，发送失败：意味管道满了 或者 当前管道被关闭
			}
			is LocationError -> cancel(CancellationException(result.throwable.message))
		}
	}
	client.setLocationListener(callback)
	awaitClose { client.unRegisterLocationListener(callback) } // 当订阅者停止监听，利用挂起函数 "awaitClose" 来解除订阅
}
````



### 笔记2
kotlin 引用比较 "==="  等同 java "=="
kotlin 的 equals 和 kotlin 的 "==" 是一样的，属于结构比较 kotlin 的 equals 通常用于数据类重写
equals 默认先判断当前对象的类型是否一致,一致则判断该该对象的内部对象的内存地址是否一样
像数据库表对象,里面一般都是 String ,Int 这些常量或者基本数据类型, 地址的异同 等同 数据内容的异同
只要里面的数据一样,equals 就相等, 这对于 Compose 函数重组很重要 



### 笔记3
1.每次对数据库表插入，触发订阅的自动查询，所查询出来的 pojo 类的地址和结构都不一样，最终导致 UiState 的 equals 判断为不相等(因此当 Compose 函数重组时，造成无法跳过重组)
2.每次对数据库表插入，触发订阅的自动查询，所查询出来的 pojo 类中的 Entity 类地址不一样, 但结构一样（这中情况可能是你数据库实体类使用了自动生成 id 导致的，每次插入数据库 id 都会不一样）

因此 Compose 函数如果以 pojo 类为参数,则根据 equals 判断原则, 则结果为不一样(会重组)
Compose 函数如果以 pojo 类中的 Entity 类(引用类型)为参数也会导致重组,因为 Entity 类不是 Compose State 类型,

因此在这个 myTest Compose 函数中无论传入 Temperature 还是 Weather 都会触发重组, 这种情况要么把传入的参拆分为基本数据类传入, 或者把 Entity 类用
remember 转成 compose state 又或者使用 @State 或 @Immutable 注解 当只有确保传入的类的结构是一样的去使用 @Stable 或 @Immutable 或者

@Immutable: 通常用于类,且属性都是是 val 且没有自定义 getter 函数,无论是外部还是内部都能对实例进行赋值 ; 或者属性类型是基本类型;
主要目的为了将不可变的属性类型标记为稳定的 像 data class(val name: String) ,只能通过构造函数赋值，
@Stable: 可用于函数,属性,类或接口; 成员对象是 val ,但可以类型不是不可变的,比如 val a:Dog ,但可以从内部改变,像属性委托给 MutableState

> 使用场景：只有确保的这个类结构是稳定的，但 compose 识别不出稳定的情况，对于第三方库的类只能做映射转换或者包装，做不了的只能等官方以后怎么解决这类问题了】
> 像在 JianMoWeather App 中就遇到了 NavController 这个类不稳定，但它不影响界面相关的地方，也没造成界面函数出现重组，所以基本没啥性能影响。

````kotlin
@Composable
fun myTest(t: Temperature) {
	// 每次刷新, weatherPojo 的地址不一样, 所以当外部的 uiState 进行 equals  时, 结果都不是 true
	Text(text = "$t")
}
````



### 笔记4
load 命名: 数据 或 异常 
get 命名: 数据 或 Null 
find 命名: 大数据 或 Null



### 笔记5
launch 不阻塞当前线程      
runBlocking 中断阻塞当前线程



### 笔记6
org.gradle.jvmargs=-Xmx4096M : gradle jvm 的最大堆内存 (Xmx代表最大,Xms代表最小)
-Dkotlin.daemon.jvm.options\="-Xmx1024M" gradle 守护进程的最大堆内存



### 笔记7
- 接口 > 接口函数 -> 使用

```kotlin
interface Printer {
	fun print()
}

fun Printer(block: () -> Unit): Printer {
	return object : Printer {
		override fun print() = block()
	}
}
val p = Printer {
	println("Hello!")
}
```

- 接口函数 - 使用

```kotlin
fun interface Printer {
	fun print()
}
val p = Printer {
	println("Hello!")
}
```



### 笔记8
```kotlin
object Lifecycle {
	private const val version = "2.5.0-alpha04"
	// val model: MyViewModel by viewModels() // ktx： 通过 by 关键字来生成
	const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version" // 创建 ViewModel
	const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"    // Only Lifecycle
	const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"  // LiveData

	// 在 Activity / Fragment 中使用原始方式创建 ViewModel:
	// viewModel = ViewModelProvider(this)[ExampleViewModel::class.java] //defaultFactory() 无参数 ViewModel

	// 在 Compose 函数中创建 Activity/Fragment 的 ViewModel:
	// viewModel: ExampleViewModel = viewModel()
	// viewModel: ExampleViewModel = viewModel(object():ViewModelProvider.Factory)
    // 需要添加以下依赖: "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
}
```


### 笔记9
```kotlin
val callback = object : AMapLocationListener {
	// 笔记：对于大量的业务逻辑的数据读写安全，可以将协程创建在单个线程上 newSingleThreadContext 
	// 然后将业务逻辑放在协程内执行，这样读写安全且速度快，对基本数据如 Int Float 等的读写则建议使用原子类 AtomInt

	// 所谓的安全就是确保数据的完整性和统一性，当一个线程A在写，则应当确保另一个准备读取的线程B 等待 线程A写完 之后再读
	// 而这个等待的机制就叫做 “阻塞” 比如 java 的: synchronized 和 ReentrantLock
	// 对于 Coroutine 协程，则使用的时 Mutex 互斥的方案来确保数据安全，他不会阻塞下层的线程， 但更建议在多线程上使用
	val mutex = Mutex() //互斥锁，先到先获得锁
	var observeJob: Job? = null
	override fun onLocationChanged(location: AMapLocation) {
		observeJob?.cancel()
		observeJob = launch {
			mutex.withLock {
				//observer(location.status())
			}
		}
	}
}
```



### 笔记10
Modifier.navigationBarsPadding() :
如果父 Layout 不设置，子 View 设置了，则子 View 会让父 Layout 膨胀变大（父 Layout 高度增加），但父 Layout 依然占据 systemBar 空间
如果父 Layout 设置了，子 View 不设置，则子 view 并不会去占据 systemBar 空间, 父 Layout 会影响 子 View 的位置
总结：子 View 永远不会改变 父 Layout 的空间位置，但可以更改父 Layout 的大小



### 笔记11
compose 中获取 context
```kotlin
val context = LocalContext.current
val contentPadding = rememberInsetsPaddingValues( //获取 systemBar 高度
    insets = LocalWindowInsets.current.systemBars,
    applyBottom = false,
    applyTop = true
)
```



### 笔记12
RecyclerView 和 LazyColumn 取消吸顶的阴影效果
```
android:overScrollMode="never"
```
```kotlin
LocalOverScrollConfiguration provides null
```



### 笔记13
查找 Compose 不稳定的对象，官方以后可能会将该功能添加到 AS Layout Inspector 中
```
./gradlew assembleRelease -Pjianmoweather.enableComposeCompilerReports=true --rerun-tasks
```









