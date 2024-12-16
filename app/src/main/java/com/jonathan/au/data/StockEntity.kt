package com.jonathan.au.data

import androidx.compose.runtime.mutableStateListOf
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_info")
data class StockInfo(
    @PrimaryKey
    val stockSymbol: String,
    val companyName: String,
    val stockQuote: Double
)
/*
var allStocks = mutableStateListOf(
    StockInfo("AAPL", "Apple Inc.", 150.00),
    StockInfo("MSFT", "Microsoft Corporation", 250.00),
    StockInfo("GOOGL", "Alphabet Inc.", 2000.00),
    StockInfo("AMZN", "Amazon.com Inc.", 300.00),
    StockInfo("FB", "Facebook Inc.", 250.00),
)*/
