package com.jonathan.au.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StockInfo::class],
    version = 1,
)
abstract class StockDB: RoomDatabase() {
    //abstract val dao: StockDAO
    abstract fun stockDAO(): StockDAO
}