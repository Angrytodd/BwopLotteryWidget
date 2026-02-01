package com.bwop.lotterywidget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bwop.lotterywidget.ui.screens.*
import com.bwop.lotterywidget.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BwopLotteryTheme {
                BwopLotteryApp()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BwopLotteryApp() {
    val tabs = listOf(
        TabItem("🏠", "Dashboard"),
        TabItem("🔥", "SamLotto"),
        TabItem("➕", "Sum"),
        TabItem("🌱", "Seed"),
        TabItem("🎲", "PRNG"),
        TabItem("📊", "IDA"),
        TabItem("🤖", "AI Models"),
        TabItem("🔐", "Checksum"),
        TabItem("📜", "History")
    )

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBg,
        topBar = {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    text = "🎯 BWOP LOTTERY PREDICTOR",
                    color = NeonCyan,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "🔥 SamLotto Integration • IDA + PRNG",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        },
        bottomBar = {
            // Tab bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .navigationBarsPadding()
            ) {
                // Page indicators
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    tabs.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (pagerState.currentPage == index) 10.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (pagerState.currentPage == index) NeonCyan
                                    else TextSecondary.copy(alpha = 0.3f)
                                )
                        )
                    }
                }
                
                // Tab buttons
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = DarkSurface,
                    contentColor = NeonCyan,
                    edgePadding = 16.dp,
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (pagerState.currentPage == index) 
                                        NeonCyan.copy(alpha = 0.2f) 
                                    else 
                                        DarkCard
                                )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = tab.emoji,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = tab.title,
                                    fontSize = 10.sp,
                                    color = if (pagerState.currentPage == index) 
                                        NeonCyan 
                                    else 
                                        TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        // Swipeable pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> DashboardScreen()
                1 -> SamLottoScreen()
                2 -> SumScreen()
                3 -> SeedScreen()
                4 -> PRNGScreen()
                5 -> IDAScreen()
                6 -> AIModelsScreen()
                7 -> ChecksumScreen()
                8 -> HistoricalScreen()
            }
        }
    }
}

data class TabItem(val emoji: String, val title: String)
