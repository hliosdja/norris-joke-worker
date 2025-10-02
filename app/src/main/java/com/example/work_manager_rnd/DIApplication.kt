package com.example.work_manager_rnd

import android.app.Application
import com.example.work_manager_rnd.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DIApplication)
            modules(appModule)
        }
    }
}