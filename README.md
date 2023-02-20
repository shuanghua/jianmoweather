### 基于 Kotlin 编程语言 + Compose Ui 开发的一个用于学习和自用的安卓天气 App 项目
架构遵循官方新架构指南, 或者说是单向绑定版的 MVVM

- Ui:            [Compose-Android](https://developer.android.com/jetpack/compose) + [Material3](https://developer.android.com/jetpack/androidx/releases/compose-material3)
- Network:       [Retrofit](https://github.com/square/retrofit)
- Serialization: [Moshi](https://github.com/square/moshi)


-

DataStorage:   [Room](https://developer.android.com/training/data-storage/room) + [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

- Di:            [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- Log:           [Timber](https://github.com/JakeWharton/timber)
- ImageDownload: [Coil-Compose](https://github.com/coil-kt/coil#jetpack-compose)
- Location:      [阿里高德定位Api](https://lbs.amap.com/api/android-location-sdk/locationsummary/)

<img src="https://github.com/shuanghua/jianmoweather/blob/main/image/1.png" alt="Cover" width="25%"/> <img src="https://github.com/shuanghua/jianmoweather/blob/main/image/2.png" alt="Cover" width="25%"/> <img src="https://github.com/shuanghua/jianmoweather/blob/main/image/3.png" alt="Cover" width="25%"/> <img src="https://github.com/shuanghua/jianmoweather/blob/main/image/4.png" alt="Cover" width="25%"/>

### 学习参考:

- [nowinandroid](https://github.com/android/nowinandroid)
- [tivi](https://github.com/chrisbanes/tivi)
- [compose-samples](https://github.com/android/compose-samples)
- [Android官方新架构](https://developer.android.com/topic/architecture)
- [Compose避免重组建议](https://developer.android.com/jetpack/compose/performance/bestpractices?hl=zh-cn#defer-reads)

### 下一步计划

1. 使用新版数据接口
2. 添加支持前台自动刷新
3. 桌面微件
4. kotlin 多平台(替换 Room -> SQLDelight , 替换 Hilt -> Koin)

### License

```
    Copyright 2022 shuanghua

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


