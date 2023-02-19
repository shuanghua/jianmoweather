package dev.shuanghua.ui.core.sample

class AA{
	companion object {
		@Volatile
		private var instance: AA? = null

		fun getInstance(): AA {
			return instance ?: synchronized(this) {
				instance ?: AA()
			}
		}
	}
}

val a: AA by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
	AA()
}

