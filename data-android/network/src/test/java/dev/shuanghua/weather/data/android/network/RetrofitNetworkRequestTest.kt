package dev.shuanghua.weather.data.android.network

import dev.shuanghua.weather.data.android.network.model.CommonResult
import dev.shuanghua.weather.data.android.network.model.MainWeatherModel
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


class RetrofitNetworkRequestTest {

	private lateinit var mockWebServer: MockWebServer
	private lateinit var apiService: ApiService

	@Before
	fun setUp() {
		mockWebServer = MockWebServer()
		apiService = Retrofit.Builder()
			.baseUrl(
				mockWebServer.url(
					"/fake/url/"
				)
			)
			.addConverterFactory(MoshiConverterFactory.create())
			.build()
			.create(ApiService::class.java)
	}

	@After
	fun tearDown() {
		mockWebServer.shutdown()
	}

	@Test
	fun `test successful network request`() = runBlocking {
		// 使用真的json 模拟假的网络请求
		val response = MockResponse().setBody(
			"{\"success\":true,\"message\":\"成功\",\"code\":200,\"result\":{\"ddatetime\":\"202403191129\",\"obsTime\":\"2024/03/19 11:29:00\",\"fcstTime\":\"2024/03/19 11:00:00\",\"date\":\"2024年03月19日(二月初十)\",\"currTime\":\"At 11:31\",\"pointOutTime\":\"深圳市气象台2024年03月19日08时25分发布\",\"pointOut\":\"预计我市白天小雨转阴天间多云，白天气温徘徊在18-21℃之间，北部和山区再低1-2℃，偏北风沿海高地和海区最大阵风5-6级，体感阴凉。20-21日多云到晴天，空气干爽，最低气温16℃左右，早晚清凉。22-25日受西南风影响，温湿回升，多云间阴天，间中有阵雨和轻雾或雾。请关注阴凉天气，以及沿海、高地和海区局地短时大风的影响。深圳市气象台2024年03月19日08时25分发布\",\"t\":\"19.7℃\",\"aptmp\":\"18.3℃\",\"cityid\":\"28060159493\",\"cityName\":\"深圳\",\"obtid\":\"\",\"stationName\":\"共和\",\"backgroundImage\":\"/sztq-app/v6/oss/backimg/sunny/daytime/c6e393e2a21c461c8b231f08d8d22b31.jpg\",\"weatherStatus\":\"5\",\"issele\":1,\"weatherpic\":null,\"hotTopics\":[],\"alarmList\":[],\"alarmListAsSS\":[],\"iconObj\":{\"aqi\":{\"aqic\":\"优\",\"aqi\":\"11\",\"icon\":\"/webcache/app/client/h5/aqi/home_leaf_green.png\",\"url\":\"/sztq-app/v6/client/h5/aqi\",\"aqi_en\":\"excellent\"},\"solarTerm\":null,\"mayorVote\":null,\"specialTopic\":null,\"otherIcon\":[]},\"mapMode\":{\"show\":1,\"icon\":\"/webcache/appimagesnew/mapmode/mapmode.png\",\"url\":\"/sztq-app/v6/client/h5/mapModel\"},\"halfCircle\":{\"upicon\":\"/webcache/appimagesnew/images/index_icon_sunrise.png\",\"downicon\":\"/webcache/appimagesnew/images/index_icon_sunset.png\",\"size\":12,\"halfCircleList\":[{\"wd\":\"北风\",\"wf\":\"3级\",\"hour\":\"12:00\",\"t\":\"21°\",\"rain\":\"无降雨\",\"num\":6,\"wtype\":\"/webcache/appimagesnew/weatherIconHalf/03.gif\",\"weatherpic\":\"03.png\",\"weatherstatus\":\"阴\",\"wd_en\":\"N\",\"wf_en\":\"force 3\",\"hour_en\":\"12:00\",\"desc_en\":\"Overcast\"},{\"wd\":\"北风\",\"wf\":\"3级\",\"hour\":\"13:00\",\"t\":\"22°\",\"rain\":\"无降雨\",\"num\":7,\"wtype\":\"/webcache/appimagesnew/weatherIconHalf/09.gif\",\"weatherpic\":\"02_2.png\",\"weatherstatus\":\"多云\",\"wd_en\":\"N\",\"wf_en\":\"force 3\",\"hour_en\":\"13:00\",\"desc_en\":\"Cloudy\"}],\"sunup\":\"06:29\",\"sundown\":\"18:35\"},\"rainfallList\":[],\"todayAndTomorrow\":[{\"whichDay\":\"今天\",\"maxT\":\"23°\",\"minT\":\"19°\",\"hint\":\"较凉\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/03.png\",\"color\":\"#3333ff\",\"desc\":\"阴天间多云\",\"whichDay_en\":\"today\",\"hint_en\":\"cool\",\"desc_en\":\"Overcast, with small diurnal temperature range\"},{\"whichDay\":\"明天\",\"maxT\":\"23°\",\"minT\":\"16°\",\"hint\":\"较凉\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"color\":\"#3333ff\",\"desc\":\"阴天转多云\",\"whichDay_en\":\"tomorrow\",\"hint_en\":\"cool\",\"desc_en\":\"Cloudy, mostly sunny\"}],\"element\":{\"r24h\":\"3.3mm\",\"r01h\":\"0.1mm\",\"pa\":\"1021.5hPa\",\"rh\":\"64%\",\"ws\":\"4.7m/s\",\"wd\":\"东北风\",\"v\":\"暂无观测\",\"wd_en\":\"NE\"},\"dayList\":[{\"wd\":\"东北风\",\"wf\":\"3-4级\",\"week\":\"今日\",\"maxT\":\"23°\",\"minT\":\"19°\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/03.png\",\"fdatetime\":\"2024-03-19\",\"desc\":\"阴天间多云\",\"desc1\":\"阴天\",\"wpic\":\"03.png\",\"wd_en\":\"NE\",\"wf_en\":\"force 3-4\",\"week_en\":\"Tue\",\"desc_en\":\"Overcast, with small diurnal temperature range\"},{\"wd\":\"东北风\",\"wf\":\"2-3级\",\"week\":\"明日\",\"maxT\":\"23°\",\"minT\":\"16°\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"fdatetime\":\"2024-03-20\",\"desc\":\"阴天转多云\",\"desc1\":\"多云\",\"wpic\":\"02_2.png\",\"wd_en\":\"NE\",\"wf_en\":\"force 2-3\",\"week_en\":\"Wed\",\"desc_en\":\"Cloudy, mostly sunny\"},{\"wd\":\"东风\",\"wf\":\"2-3级\",\"week\":\"星期四\",\"maxT\":\"24°\",\"minT\":\"16°\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/02.png\",\"fdatetime\":\"2024-03-21\",\"desc\":\"多云间晴天\",\"desc1\":null,\"wpic\":\"02.png\",\"wd_en\":\"E\",\"wf_en\":\"force 2-3\",\"week_en\":\"Thu\",\"desc_en\":\"Mainly fine with plenty of sunshine\"},{\"wd\":\"东南风\",\"wf\":\"3-4级\",\"week\":\"星期五\",\"maxT\":\"25°\",\"minT\":\"18°\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"fdatetime\":\"2024-03-22\",\"desc\":\"多云\",\"desc1\":null,\"wpic\":\"02_2.png\",\"wd_en\":\"SE\",\"wf_en\":\"force 3-4\",\"week_en\":\"Fri\",\"desc_en\":\"Cloudy, mostly sunny\"},{\"wd\":\"南风\",\"wf\":\"3-4级\",\"week\":\"星期六\",\"maxT\":\"27°\",\"minT\":\"20°\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"fdatetime\":\"2024-03-23\",\"desc\":\"多云\",\"desc1\":null,\"wpic\":\"02_2.png\",\"wd_en\":\"S\",\"wf_en\":\"force 3-4\",\"week_en\":\"Sat\",\"desc_en\":\"Cloudy, mostly sunny\"},{\"wd\":\"南风\",\"wf\":\"2-3级\",\"week\":\"星期日\",\"maxT\":\"28°\",\"minT\":\"21°\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"fdatetime\":\"2024-03-24\",\"desc\":\"多云，早晚有（轻）雾\",\"desc1\":null,\"wpic\":\"02_2.png\",\"wd_en\":\"S\",\"wf_en\":\"force 2-3\",\"week_en\":\"Sun\",\"desc_en\":\"Cloudy, mostly sunny\"},{\"wd\":\"南风\",\"wf\":\"2-3级\",\"week\":\"星期一\",\"maxT\":\"29°\",\"minT\":\"22°\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"fdatetime\":\"2024-03-25\",\"desc\":\"多云，早晚有（轻）雾\",\"desc1\":null,\"wpic\":\"02_2.png\",\"wd_en\":\"S\",\"wf_en\":\"force 2-3\",\"week_en\":\"Mon\",\"desc_en\":\"Cloudy, mostly sunny\"},{\"wd\":\"东风\",\"wf\":\"1-2级\",\"week\":\"星期二\",\"maxT\":\"26°\",\"minT\":\"21°\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/04.png\",\"fdatetime\":\"2024-03-26\",\"desc\":\"多云，有分散阵雨，早晚有（轻）雾\",\"desc1\":null,\"wpic\":\"04.png\",\"wd_en\":\"E\",\"wf_en\":\"force 1-2\",\"week_en\":\"Tue\",\"desc_en\":\"Shower or thundershower, partly sunny\"},{\"wd\":\"东风\",\"wf\":\"2-3级\",\"week\":\"星期三\",\"maxT\":\"25°\",\"minT\":\"20°\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"fdatetime\":\"2024-03-27\",\"desc\":\"多云\",\"desc1\":null,\"wpic\":\"02_2.png\",\"wd_en\":\"E\",\"wf_en\":\"force 2-3\",\"week_en\":\"Wed\",\"desc_en\":\"Cloudy, mostly sunny\"},{\"wd\":\"东风\",\"wf\":\"2-3级\",\"week\":\"星期四\",\"maxT\":\"25°\",\"minT\":\"20°\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"fdatetime\":\"2024-03-28\",\"desc\":\"多云\",\"desc1\":null,\"wpic\":\"02_2.png\",\"wd_en\":\"E\",\"wf_en\":\"force 2-3\",\"week_en\":\"Thu\",\"desc_en\":\"Cloudy, mostly sunny\"}],\"hourForeList\":[{\"wd\":\"北风\",\"wf\":\"3级\",\"t\":\"21°\",\"rain\":\"无降雨\",\"hour\":\"12时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/03.png\",\"weatherstatus\":\"阴\",\"wd_en\":\"N\",\"wf_en\":\"force 3\",\"rain_en\":\"Overcast\",\"hour_en\":\"12:00\"},{\"wd\":\"北风\",\"wf\":\"3级\",\"t\":\"22°\",\"rain\":\"无降雨\",\"hour\":\"13时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"weatherstatus\":\"多云\",\"wd_en\":\"N\",\"wf_en\":\"force 3\",\"rain_en\":\"Cloudy\",\"hour_en\":\"13:00\"},{\"wd\":\"北风\",\"wf\":\"3级\",\"t\":\"22°\",\"rain\":\"无降雨\",\"hour\":\"14时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"weatherstatus\":\"多云\",\"wd_en\":\"N\",\"wf_en\":\"force 3\",\"rain_en\":\"Cloudy\",\"hour_en\":\"14:00\"},{\"wd\":\"北风\",\"wf\":\"3级\",\"t\":\"22°\",\"rain\":\"无降雨\",\"hour\":\"15时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"weatherstatus\":\"多云\",\"wd_en\":\"N\",\"wf_en\":\"force 3\",\"rain_en\":\"Cloudy\",\"hour_en\":\"15:00\"},{\"wd\":\"北风\",\"wf\":\"3级\",\"t\":\"21°\",\"rain\":\"无降雨\",\"hour\":\"16时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"weatherstatus\":\"多云\",\"wd_en\":\"N\",\"wf_en\":\"force 3\",\"rain_en\":\"Cloudy\",\"hour_en\":\"16:00\"},{\"wd\":\"北风\",\"wf\":\"3级\",\"t\":\"21°\",\"rain\":\"无降雨\",\"hour\":\"17时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"weatherstatus\":\"多云\",\"wd_en\":\"N\",\"wf_en\":\"force 3\",\"rain_en\":\"Cloudy\",\"hour_en\":\"17:00\"},{\"wd\":\"东北风\",\"wf\":\"2级\",\"t\":\"20°\",\"rain\":\"无降雨\",\"hour\":\"18时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/09.png\",\"weatherstatus\":\"多云\",\"wd_en\":\"NE\",\"wf_en\":\"force 2\",\"rain_en\":\"Cloudy\",\"hour_en\":\"18:00\"},{\"wd\":\"东北风\",\"wf\":\"2级\",\"t\":\"19°\",\"rain\":\"无降雨\",\"hour\":\"19时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/03.png\",\"weatherstatus\":\"阴\",\"wd_en\":\"NE\",\"wf_en\":\"force 2\",\"rain_en\":\"Overcast\",\"hour_en\":\"19:00\"},{\"wd\":\"东北风\",\"wf\":\"2级\",\"t\":\"19°\",\"rain\":\"无降雨\",\"hour\":\"20时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/03.png\",\"weatherstatus\":\"阴\",\"wd_en\":\"NE\",\"wf_en\":\"force 2\",\"rain_en\":\"Overcast\",\"hour_en\":\"20:00\"},{\"wd\":\"东北风\",\"wf\":\"3级\",\"t\":\"18°\",\"rain\":\"无降雨\",\"hour\":\"21时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/30.png\",\"weatherstatus\":\"少云\",\"wd_en\":\"NE\",\"wf_en\":\"force 3\",\"rain_en\":\"Partly cloudy\",\"hour_en\":\"21:00\"},{\"wd\":\"东北风\",\"wf\":\"3级\",\"t\":\"18°\",\"rain\":\"无降雨\",\"hour\":\"22时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/30.png\",\"weatherstatus\":\"少云\",\"wd_en\":\"NE\",\"wf_en\":\"force 3\",\"rain_en\":\"Partly cloudy\",\"hour_en\":\"22:00\"},{\"wd\":\"东北风\",\"wf\":\"3级\",\"t\":\"17°\",\"rain\":\"无降雨\",\"hour\":\"23时\",\"wtype\":\"/webcache/appimagesnew/weatherIcon/30.png\",\"weatherstatus\":\"少云\",\"wd_en\":\"NE\",\"wf_en\":\"force 3\",\"rain_en\":\"Partly cloudy\",\"hour_en\":\"23:00\"}],\"minList\":[{\"rain\":\"0.0\",\"min\":\"11:36\"},{\"rain\":\"0.0\",\"min\":\"11:42\"},{\"rain\":\"0.0\",\"min\":\"11:48\"},{\"rain\":\"0.0\",\"min\":\"11:54\"},{\"rain\":\"0.0\",\"min\":\"12:00\"},{\"rain\":\"0.0\",\"min\":\"12:06\"},{\"rain\":\"0.0\",\"min\":\"12:12\"},{\"rain\":\"0.0\",\"min\":\"12:18\"},{\"rain\":\"0.0\",\"min\":\"12:24\"},{\"rain\":\"0.0\",\"min\":\"12:30\"},{\"rain\":\"0.0\",\"min\":\"12:36\"},{\"rain\":\"0.0\",\"min\":\"12:42\"},{\"rain\":\"0.0\",\"min\":\"12:48\"},{\"rain\":\"0.0\",\"min\":\"12:54\"},{\"rain\":\"0.0\",\"min\":\"13:00\"},{\"rain\":\"0.0\",\"min\":\"13:06\"},{\"rain\":\"0.0\",\"min\":\"13:12\"},{\"rain\":\"0.0\",\"min\":\"13:18\"},{\"rain\":\"0.0\",\"min\":\"13:24\"},{\"rain\":\"0.0\",\"min\":\"13:30\"},{\"rain\":\"0.0\",\"min\":\"13:36\"},{\"rain\":\"0.0\",\"min\":\"13:42\"},{\"rain\":\"0.0\",\"min\":\"13:48\"},{\"rain\":\"0.0\",\"min\":\"13:54\"},{\"rain\":\"0.0\",\"min\":\"14:00\"},{\"rain\":\"0.0\",\"min\":\"14:06\"},{\"rain\":\"0.0\",\"min\":\"14:12\"},{\"rain\":\"0.0\",\"min\":\"14:18\"}],\"lunar\":{\"info1\":\"2024年03月19日\",\"info5\":\"还有1天\",\"info4\":\"春分\",\"info3\":\"距离\",\"info2\":\"农历二月初十\"},\"indexOfLiving\":{\"chuanyi\":{\"level\":\"3\",\"icon\":\"/webcache/appimagesnew/living-index/chuanyi.png\",\"title\":\"穿衣指数\",\"level_advice\":\"穿衣指数3级（舒适），早晚清凉，要穿外套。\",\"level_desc\":\"舒适\"},\"luyou\":{\"level\":\"3\",\"icon\":\"/webcache/appimagesnew/living-index/luyou.png\",\"title\":\"旅游指数\",\"level_advice\":\"旅游指数3级，基本适宜旅游，有分散阵雨，外出携带雨具，早晚清凉。\",\"level_desc\":\"不太适宜\"},\"shushidu\":{\"level\":\"2\",\"icon\":\"/webcache/appimagesnew/living-index/shushidu.png\",\"title\":\"舒适度指数\",\"level_advice\":\"舒适度指数2级，天气清凉。\",\"level_desc\":\"较舒适\"},\"gaowen\":{\"level\":\"0\",\"icon\":\"/webcache/appimagesnew/living-index/gaowen.png\",\"title\":\"高温热浪指数\",\"level_advice\":\"未到发布时间\",\"level_desc\":\"未达到风险\"},\"ziwaixian\":{\"level\":\"1\",\"icon\":\"/webcache/appimagesnew/living-index/ziwaixian.png\",\"title\":\"紫外线指数\",\"level_advice\":\"不需要采取防护措施。\",\"level_desc\":\"弱\"},\"co\":{\"level\":\"0\",\"icon\":\"/webcache/appimagesnew/living-index/co.png\",\"title\":\"一氧化碳指数\",\"level_advice\":\"未达到风险\",\"level_desc\":\"未达到风险\"},\"meibian\":{\"level\":\"3\",\"icon\":\"/webcache/appimagesnew/living-index/meibian.png\",\"title\":\"霉变指数\",\"level_advice\":\"食品、药品、皮毛类衣物等需选择低温或密封干燥环境存放，并采取适当防霉措施\",\"level_desc\":\"易发生\"},\"chenlian\":{\"level\":\"4\",\"icon\":\"/webcache/appimagesnew/living-index/chenlian.png\",\"title\":\"晨练指数\",\"level_advice\":\"晨练指数4级，阴天有雨，路面湿滑，不太适宜晨练。\",\"level_desc\":\"不适宜\"}},\"likeNum\":\"571\",\"unlikeNum\":\"117\",\"like\":\"0\",\"remarks\":{\"dayList\":{\"title\":\"逐日预报\",\"content\":\"当前展示为深圳未来十天预报\"},\"hourForeList\":{\"title\":\"逐时预报\",\"content\":\"当前展示为共和逐时预报\"},\"element\":{\"title\":\"实况要素\",\"content\":\"当前数据为共和气象站数据\"}},\"t_en\":\"67.5℉\"},\"timestamp\":1710819069575}"
		)
		mockWebServer.enqueue(response)

		// Make the network request
//		val result = apiService.getMainWeather()
		val result = apiService.makeRequest(3, "jifj")

		// Verify the result
		assert(result.data.cityName == "深圳")
	}

	@Test
	fun `test failed network request`() = runBlocking {
		// Mock the server response
		val response = MockResponse().setResponseCode(500)
		mockWebServer.enqueue(response)

		// Make the network request
		try {
			apiService.makeRequest(3, "jifj")
			assert(false) // Should not reach here
		} catch (e: Exception) {
			// Verify the exception
			assert(e is Exception)
		}
	}
}

interface ApiService {
	@GET("/users/{id}")
	suspend fun makeRequest(
		@Path("id") id: Int,
		@Query("name") name: String
	): CommonResult<MainWeatherModel>
}
