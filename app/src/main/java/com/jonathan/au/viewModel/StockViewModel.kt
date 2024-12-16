package com.jonathan.au.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jonathan.au.data.SortType
import com.jonathan.au.data.StockDAO
import com.jonathan.au.data.StockEvent
import com.jonathan.au.data.StockInfo
import com.jonathan.au.data.StockRepository
import com.jonathan.au.views.StockUIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class StockViewModel (
    private val stockRepository: StockRepository,
    private val dao: StockDAO,
): ViewModel() {
    private val _sortType = MutableStateFlow(SortType.STOCK_SYMBOL)
    private val _stocks = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.STOCK_SYMBOL -> dao.getStockOrderedByStockSymbol()
                SortType.COMPANY_NAME -> dao.getStockOrderedByCompanyName()
                SortType.STOCK_QUOTE -> dao.getStockOrderedByStockQuote()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val _state = MutableStateFlow(StockUIState())
    val state = combine(_state, _sortType, _stocks) { state, sortType, stocks ->
        state.copy(
            stockInfo = stocks,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), StockUIState())

    init {
        getStocks()
    }
    fun insertStock(stockInfo: StockInfo) {
        val stockSymbol = state.value.stockSymbol
        val companyName = state.value.companyName
        val stockQuote = state.value.stockQuote

        if (stockSymbol.isEmpty() && companyName.isEmpty() && stockQuote.toString().isEmpty()) {
            return
        }
        /*
        val newStock = StockInfo(
            stockSymbol = stockSymbol,
            companyName = companyName,
            stockQuote = stockQuote
        )*/
        viewModelScope.launch {
            stockRepository.insertStock(stockInfo)
        }
        _state.update { it.copy(
            isAddingStock = false,
            stockSymbol = "",
            companyName = "",
            stockQuote = 0.00
        )}
    }
    fun updateStock(stock: StockInfo) {
        /*val stockSymbol = stock.stockSymbol
        val companyName = stock.companyName
        val stockQuote = stock.stockQuote

        if (stockSymbol.isEmpty() && companyName.isEmpty() && stockQuote.toString().isEmpty()) {
            return
        }
        val updatedStock = StockInfo(
            stockSymbol = stockSymbol,
            companyName = companyName,
            stockQuote = stockQuote
        )*/
        viewModelScope.launch {
            stockRepository.updateStock(stock)
        }
    }
    fun deleteStock(stock: StockInfo) {
        viewModelScope.launch {
            stockRepository.deleteStock(stock)
        }
    }

    fun getStocks() {
        viewModelScope.launch {
            stockRepository.getStocks()
                .asLiveData()
                .observeForever {
                    _state.update{
                        it.copy(
                            stockInfo = it.stockInfo
                        )
                    }
                _state.value = state.value.copy(
                    stockInfo = it,
                )
            }
        }
    }
    fun getStockByStockSymbol(stockSymbol: String) {
        viewModelScope.launch {
            stockRepository.getStockByStockSymbol(stockSymbol)
        }
    }

    fun onEvent(event: StockEvent) {
        when(event) {
            is StockEvent.DeleteStock -> {
                viewModelScope.launch {
                    dao.deleteStock(event.stock)
                }
            }
            is StockEvent.SaveStock -> {
                val stockSymbol = state.value.stockSymbol
                val companyName = state.value.companyName
                val stockQuote = state.value.stockQuote
                if (stockSymbol.isEmpty() && companyName.isEmpty() && stockQuote.toString()
                        .isEmpty()
                ) {
                    return
                }
                val stock = StockInfo(
                    stockSymbol = stockSymbol,
                    companyName = companyName,
                    stockQuote = stockQuote
                )
                viewModelScope.launch {
                    dao.insertStock(stock)
                }
                _state.update {
                    it.copy(
                        isAddingStock = false,
                        stockSymbol = "",
                        companyName = "",
                        stockQuote = 0.00
                    )
                }
            }
            is StockEvent.UpdateStock -> {
                val stockSymbol = state.value.stockSymbol
                val companyName = state.value.companyName
                val stockQuote = state.value.stockQuote
                if (stockSymbol.isEmpty() && companyName.isEmpty() && stockQuote.toString()
                        .isEmpty()
                ) {
                    return
                }
                val updatedStock = StockInfo(
                    stockSymbol = stockSymbol,
                    companyName = companyName,
                    stockQuote = stockQuote
                )
                viewModelScope.launch {
                    dao.updateStock(updatedStock)
                }
            }
            is StockEvent.SetStockSymbol -> {
                _state.update {
                    it.copy(
                        stockSymbol = event.stockSymbol
                    )
                }
            }
            is StockEvent.SetCompanyName -> {
                _state.update {
                    it.copy(
                        companyName = event.companyName
                    )
                }
            }
            is StockEvent.SetStockPrice -> {
                _state.update {
                    it.copy(
                        stockQuote = event.stockPrice
                    )
                }
            }
            is StockEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingStock = true
                    )
                }
            }
            is StockEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingStock = false
                    )
                }
            }
            is StockEvent.SortStocks -> {
                _sortType.value = event.sortType
            }
        }
    }
}