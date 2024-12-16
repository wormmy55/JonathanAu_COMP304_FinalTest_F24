package com.jonathan.au.data

import android.app.Application
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun insertStock(stock: StockInfo)
    suspend fun updateStock(stock: StockInfo)
    suspend fun deleteStock(stock: StockInfo)

    fun getStocks(): Flow<List<StockInfo>>
    fun getStockByStockSymbol(stockSymbol: String): Flow<List<StockInfo>>
    fun getStockOrderedByStockSymbol(stockSymbol: String): Flow<List<StockInfo>>
    fun getStockOrderedByCompanyName(companyName: String): Flow<List<StockInfo>>
    fun getStockOrderedByStockQuote(stockQuote: Double): Flow<List<StockInfo>>

}