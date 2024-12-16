package com.jonathan.au

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.sharp.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.jonathan.au.data.StockDB
import com.jonathan.au.data.StockEvent
import com.jonathan.au.data.StockInfo
import com.jonathan.au.navigation.StockRoutes
import com.jonathan.au.ui.theme.MyStocksTheme
import com.jonathan.au.viewModel.StockViewModel
import com.jonathan.au.views.DisplayActivity
import com.jonathan.au.views.StockUIState
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            StockDB::class.java,
            "stock_db"
        ).build()
    }
    /*private val viewModel by viewModels<StockViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StockViewModel(StockRepositoryImpl(db.dao, dispatcher = Dispatchers.IO), db.dao) as T
                }
            }
        }
    )*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStocksTheme {
                //val state by viewModel.state.collectAsState()
                val stockViewModel: StockViewModel = koinViewModel()
                val state by stockViewModel._state.collectAsStateWithLifecycle()
                MainScaffold(
                    state = /*StockUIState()*/ state,
                    onEvent = {}/*viewModel::onEvent*/,
                )
            }
        }
    }
}

var globalIndex = 0
var globalVar = ""

@Composable
fun MainScaffold(
    state: StockUIState,
    onEvent: (StockEvent) -> Unit,
    //stockRepo: (StockDB) -> Unit,
) {
    Scaffold{innerPadding ->
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = StockRoutes.Home.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(StockRoutes.Home.route){
                JonathanActivity(
                    onDisplayClick = { navController.navigate(StockRoutes.Display.route) },
                    //onStockSave = { navController.navigate(StockRoutes.Home.route) },
                    state = state,
                    //onEvent = {},
                    //stockRepo = {},
                )
            }
            composable(StockRoutes.Display.route){
                DisplayActivity(
                    onBackButtonClick = { navController.navigate(StockRoutes.Home.route) },
                    state = state,
                    listIndex = globalIndex,
                    listVar = globalVar
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JonathanActivity(
    onDisplayClick: () -> Unit,
    //onStockSave: () -> Unit,
    //onEvent: (StockEvent) -> Unit,
    //stockRepo: (StockDB) -> Unit,
    state: StockUIState
) {
    var textStockSymbol by remember { mutableStateOf("") }
    var textStockName by remember { mutableStateOf("") }
    var textStockPrice by remember { mutableDoubleStateOf(0.00) }
    val stockViewModel: StockViewModel = koinViewModel()
    //val state by stockViewModel._state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        textAlign = TextAlign.Center,
                        text ="My Stocks"
                    )
                },
            )
        },
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Stock Symbol:")
            TextField(
                value = textStockSymbol,
                onValueChange = { textStockSymbol = it },
                label = { Text("Enter Stock Symbol") }
            )
            Text("Company Name:")
            TextField(
                value = textStockName,
                onValueChange = { textStockName = it },
                label = { Text("Enter Company Name") }
            )
            Text("Stock Price:")
            TextField(
                value = textStockPrice.toString().format("%.2f"),
                onValueChange = { textStockPrice = it.toDouble() },
                label = { Text("Enter Stock Price") }
            )
            SaveStockButton(
                onClick = {
                    stockViewModel.insertStock(
                        StockInfo(
                            stockSymbol = textStockSymbol,
                            companyName = textStockName,
                            stockQuote = textStockPrice
                        )
                    )
                }
            )
            Text(
                text = "Display Stock Info",
                modifier = Modifier
                    .clickable { stockViewModel.getStocks() }
            )
            LazyStocksCol(
                onDisplayClicked = { onDisplayClick() },
                stocks = state.stockInfo,
                //state = state,
                //stockRepo = {  },
                //onIconClick = { stockViewModel.deleteStock(it) }
            )
        }
    }
}

//This is the Lazy column
@Composable
fun LazyStocksCol(
    modifier: Modifier = Modifier,
    onDisplayClicked: () -> Unit,
    stocks: List<StockInfo>,
    //stockRepo: (StockViewModel) -> Unit,
    //onIconClick: (StockDAO) -> Unit,
    //state: StockUIState = StockUIState(),
){
    var selectedTask by remember { mutableStateOf("") }
    val stockViewModel: StockViewModel = koinViewModel()
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp),
    ){
        items(stocks){ stock ->
            @Composable
            fun selectedTaskLine():String {
                return stock.stockSymbol + "\n " + stock.companyName +
                        "\n $" + stock.stockQuote.toString().format("%.2f")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Column(
                    modifier = Modifier
                        //.fillMaxWidth(),
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    Text(
                        text = /*"${stock.stockSymbol}"*/
                        if(selectedTask == stock.stockSymbol){
                            GoDisplayButton(
                                onDisplayClicked = { onDisplayClicked()
                                    globalVar = stock.stockSymbol
                                    //globalIndex = allStocks.indexOf(stock)
                                }
                            )
                            selectedTaskLine()
                        } else selectedTaskLine(),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(1.dp, Color.Black)
                            .clickable {
                                selectedTask = stock.stockSymbol
                                //onDisplayClicked()
                            }
                            .background(
                                if (selectedTask == stock.stockSymbol) Color.Magenta
                                else MaterialTheme.colorScheme.tertiary
                            ),
                        fontSize = 20.sp,
                        maxLines = if (selectedTask == stock.stockSymbol) 3 else 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                IconButton(
                    onClick = { stockViewModel.deleteStock(stock) }
                ){
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                    )
                }
            }
        }
    }
}

//This is the Go to Display Button Function

@Composable
fun GoDisplayButton(
    onDisplayClicked: () -> Unit
){
    Button(
        onClick = { onDisplayClicked() },
        modifier = Modifier.border(1.dp,
            Color.Black,
            shape = MaterialTheme.shapes.medium)
    ){
        Icon(
            Icons.Filled.Edit,
            contentDescription = "Go to Display",
        )
    }
}

//This is the function which will save the new stock
@Composable
fun SaveStockButton(
    onClick: () -> Unit,
){
    Button(
        onClick = {
            onClick()
            /*stockViewModel.insertStock(
                StockInfo(
                    stockSymbol = textStockSymbol,
                    companyName = textStockName,
                    stockQuote = textStockPrice
                )
            )*/
        },
    ) {
        Icon(Icons.Sharp.Done, "Save")
    }
}
/*
@Preview(showBackground = true)
@Composable
fun MainScaffoldPreview() {
    MyStocksTheme {
        MainScaffold()
    }
}
@Preview(showBackground = true)
@Composable
fun JonathanActivityPreview() {
    MyStocksTheme {
        JonathanActivity(
            onDisplayClick = {},
            onStockSave = {}
        )
    }
}
@Preview(showBackground = true)
@Composable
fun LazyStocksColPreview() {
    MyStocksTheme {
        LazyStocksCol(onClick = {})
    }
}
@Preview(showBackground = true)
@Composable
fun GoDisplayButtonPreview() {
    MyStocksTheme {
        GoDisplayButton(onClick = {})
    }
}
@Preview(showBackground = true)
@Composable
fun SaveStockButtonPreview() {
    MyStocksTheme {
        SaveStockButton(
            textStockSymbol = "TEST",
            textStockName = "Test Preview",
            textStockPrice = 0.00,
            onClick = {}
        )
    }
}*/