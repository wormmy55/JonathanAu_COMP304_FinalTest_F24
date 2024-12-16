package com.jonathan.au.views

import com.jonathan.au.data.SortType
import com.jonathan.au.data.StockInfo

data class StockUIState(
    val stockInfo: List<StockInfo> = emptyList(),
    val stockSymbol: String = "",
    val companyName: String = "",
    val stockQuote: Double = 0.00,
    val isAddingStock: Boolean = false,
    val sortType: SortType = SortType.STOCK_SYMBOL
)