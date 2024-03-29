package dev.shuanghua.weather

import android.app.Application
import dev.shuanghua.weather.data.android.location.AMapPrivacyCheck
import dev.shuanghua.weather.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class JianMoApp : Application() {

	override fun onCreate() {
		super.onCreate()
		AMapPrivacyCheck.init(this)
		Timber.plant(Timber.DebugTree())

		// init koin
		startKoin {
			// Log Koin into Android logger
			androidLogger()
			// Reference Android context
			androidContext(this@JianMoApp)
			// Load modules
			modules(appModule)
		}
	}
}