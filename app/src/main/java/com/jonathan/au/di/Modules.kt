package com.jonathan.au.di

import androidx.room.Room
import com.jonathan.au.data.StockDB
import com.jonathan.au.data.StockRepository
import com.jonathan.au.data.StockRepositoryImpl
import com.jonathan.au.viewModel.StockViewModel
import com.jonathan.au.workers.StockSyncWorker
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module{
    single<StockRepository> { StockRepositoryImpl(get(), get()) }
    single { Dispatchers.IO }
    single { Dispatchers.Main }
    single{ StockViewModel(get(), get()) }

    single{
        Room.databaseBuilder(
            androidContext(),
            StockDB::class.java,
            "stock_db"
        ).build()
    }
    single{ get<StockDB>().stockDAO() }
    worker{ StockSyncWorker(get(), get(), get()) }
    //single{ get<StockDB>().dao }
}