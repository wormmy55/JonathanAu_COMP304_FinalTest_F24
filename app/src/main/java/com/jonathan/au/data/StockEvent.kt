package com.jonathan.au.data

interface StockEvent {
    object SaveStock: StockEvent
    data class SetStockSymbol(val stockSymbol: String): StockEvent
    data class SetCompanyName(val companyName: String): StockEvent
    data class SetStockPrice(val stockPrice: Double): StockEvent
    data class DeleteStock(val stock: StockInfo): StockEvent
    data class UpdateStock(val stock: StockInfo): StockEvent
    data class SortStocks(val sortType: SortType): StockEvent
    object ShowDialog: StockEvent
    object HideDialog: StockEvent
}