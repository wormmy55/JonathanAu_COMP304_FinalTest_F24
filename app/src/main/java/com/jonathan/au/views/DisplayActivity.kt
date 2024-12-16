package com.jonathan.au.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.jonathan.au.data.allStocks
import com.jonathan.au.ui.theme.MyStocksTheme
import com.jonathan.au.viewModel.StockViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayActivity(
    onBackButtonClick: () -> Unit,
    state: StockUIState,
    listIndex: Int,
    listVar: String
){
    //Variables to be used
    val stockViewModel: StockViewModel = koinViewModel()
    //val state by stockViewModel._state.collectAsStateWithLifecycle()
    val targetStock by remember { mutableStateOf(state.stockInfo.find { it.stockSymbol == listVar }) }
    //val targetStock = stockViewModel.getStockByStockSymbol(listVar)
    var stockSymbol = targetStock?.stockSymbol
    var companyName = targetStock?.companyName
    var stockQuote = targetStock?.stockQuote
    //var stockSymbol = allStocks[listIndex].stockSymbol
    //var companyName = allStocks[listIndex].companyName
    //var stockQuote = allStocks[listIndex].stockQuote
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text ="Stock Info",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )/*
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        //This is the back button to exit without saving the contact
                        Button(
                            onClick = { onBackButtonClick() }
                        ) {
                            Icon(Icons.Filled.KeyboardArrowLeft, "Return")
                        }
                    }*/
                }
            )
        },
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text("Stock:")
            Text("Stock Symbol: $stockSymbol")
            Text("Company Name: $companyName")
            Text("Stock Price: $stockQuote")
            Button(
                onClick = { onBackButtonClick() }
            ) {
                Icon(Icons.Filled.KeyboardArrowLeft, "Return")
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun DisplayActivityPreview() {
    MyStocksTheme {
        DisplayActivity(
            onBackButtonClick = {},
            listIndex = 0,
            listVar = ""
        )
    }
}*/