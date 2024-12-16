package com.jonathan.au

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
//import com.jonathan.au.data.StockDB
//import com.jonathan.au.data.StockRepositoryImpl
import com.jonathan.au.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            if (!WorkManager.isInitialized()) {
                workManagerFactory()
            }
            //workManagerFactory()
            modules(appModule)
        }
    }
}