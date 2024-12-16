package com.jonathan.au.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class StockRepositoryImpl(
    private val stockDao: StockDAO,
    private val dispatcher: CoroutineDispatcher,
) : StockRepository {

    override suspend fun insertStock(stock: StockInfo) {
        val stockSymbol = stock.stockSymbol
        val companyName = stock.companyName
        val stockQuote = stock.stockQuote

        if (stockSymbol.isEmpty() && companyName.isEmpty() && stockQuote.toString().isEmpty()) {
            return
        }
        stockDao.insertStock(StockInfo(
            stockSymbol = stock.stockSymbol,
            companyName = stock.companyName,
            stockQuote = stock.stockQuote
        ))
    }

    override suspend fun updateStock(stock: StockInfo) {
        val stockSymbol = stock.stockSymbol
        val companyName = stock.companyName
        val stockQuote = stock.stockQuote

        if (stockSymbol.isEmpty() && companyName.isEmpty() && stockQuote.toString().isEmpty()) {
            return
        }
        stockDao.updateStock(StockInfo(
            stockSymbol = stockSymbol,
            companyName = companyName,
            stockQuote = stockQuote
        ))
    }

    override suspend fun deleteStock(stock: StockInfo) {
        stockDao.deleteStock(stock)
    }

    override fun getStocks(): Flow<List<StockInfo>> {
        return stockDao.getStocks()
    }

    override fun getStockByStockSymbol(stockSymbol: String): Flow<List<StockInfo>> {
        return stockDao.getStockByStockSymbol(stockSymbol)
    }

    override fun getStockOrderedByStockSymbol(stockSymbol: String): Flow<List<StockInfo>> {
        return stockDao.getStockOrderedByStockSymbol()
    }

    override fun getStockOrderedByCompanyName(companyName: String): Flow<List<StockInfo>> {
        return stockDao.getStockOrderedByCompanyName()
    }

    override fun getStockOrderedByStockQuote(stockQuote: Double): Flow<List<StockInfo>> {
        return stockDao.getStockOrderedByStockQuote()
    }
}