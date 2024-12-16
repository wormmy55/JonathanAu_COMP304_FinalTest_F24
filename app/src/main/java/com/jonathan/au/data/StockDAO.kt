package com.jonathan.au.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: StockInfo)
    @Update
    suspend fun updateStock(stock: StockInfo)
    @Delete
    suspend fun deleteStock(stock: StockInfo)

    @Query("SELECT * FROM stock_info")
    fun getStocks(): Flow<List<StockInfo>>
    @Query("SELECT * FROM stock_info WHERE stockSymbol = :stockSymbol")
    fun getStockByStockSymbol(stockSymbol: String): Flow<List<StockInfo>>

    @Query("SELECT * FROM stock_info ORDER BY stockSymbol ASC")
    fun getStockOrderedByStockSymbol(): Flow<List<StockInfo>>
    @Query("SELECT * FROM stock_info ORDER BY companyName ASC")
    fun getStockOrderedByCompanyName(): Flow<List<StockInfo>>
    @Query("SELECT * FROM stock_info ORDER BY stockQuote ASC")
    fun getStockOrderedByStockQuote(): Flow<List<StockInfo>>
}