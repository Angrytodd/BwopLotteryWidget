package com.bwop.lotterywidget.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bwop.lotterywidget.*
import com.bwop.lotterywidget.ui.components.*
import com.bwop.lotterywidget.ui.theme.*

/**
 * Dashboard Screen
 */
@Composable
fun DashboardScreen() {
    var prediction by remember { mutableStateOf(LotteryEngine.generatePrediction()) }
    val idaConfidence = LotteryEngine.IDA.getConfidence()
    val seed = LotteryEngine.PRNG.getSeed()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = PurpleGradient,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🏆 BWOP LOTTERY PREDICTOR 🏆",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "IDA + PRNG + Checksum Engine Active",
                        color = TextPrimary.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
        }
        
        // Stats grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    label = "AI Models",
                    value = "26",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Accuracy",
                    value = "97.8%",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "IDA Score",
                    value = "${idaConfidence.toInt()}%",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Main prediction
        item {
            SectionHeader("Today's Prediction", "🎯")
            PredictionCard(
                title = "Pick 3 - IDA Enhanced",
                numbers = prediction.numbers,
                checksum = prediction.checksum,
                accuracy = 98.7
            )
        }
        
        // Generate button
        item {
            NeonButton(
                text = "⚡ Generate New Prediction",
                onClick = {
                    LotteryEngine.initialize(System.currentTimeMillis())
                    prediction = LotteryEngine.generatePrediction()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Seed info
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("PRNG Seed:", color = TextSecondary, fontSize = 14.sp)
                    Text(seed.toString(), color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * PRNG Screen
 */
@Composable
fun PRNGScreen() {
    var numbers by remember { mutableStateOf(listOf<Int>()) }
    var seedInput by remember { mutableStateOf("") }
    val seed = LotteryEngine.PRNG.getSeed()
    val iterations = LotteryEngine.PRNG.iterations
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionHeader("PRNG Engine (Xorshift128+)", "🎲")
        }
        
        // Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard("Seed", seed.toString(), Modifier.weight(1f))
                StatCard("Iterations", iterations.toString(), Modifier.weight(1f))
            }
        }
        
        // Seed control
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Set Custom Seed", color = NeonCyan, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = seedInput,
                            onValueChange = { seedInput = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Enter seed", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        NeonButton(
                            text = "Set",
                            onClick = {
                                seedInput.toLongOrNull()?.let {
                                    LotteryEngine.PRNG.srand(it)
                                    seedInput = ""
                                }
                            }
                        )
                    }
                }
            }
        }
        
        // Generated numbers
        if (numbers.isNotEmpty()) {
            item {
                SectionHeader("Generated Numbers", "🔢")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    numbers.forEachIndexed { index, num ->
                        val colors = listOf(NeonPurple, NeonPink, NeonCyan, NeonGreen, NeonOrange)
                        NumberBall(
                            number = num,
                            color = colors[index % colors.size],
                            size = 50
                        )
                        if (index < numbers.size - 1) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }
        
        // Generate button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NeonButton(
                    text = "Generate 5",
                    onClick = {
                        numbers = (1..5).map { LotteryEngine.PRNG.randRange(0, 9) }
                    },
                    modifier = Modifier.weight(1f)
                )
                NeonButton(
                    text = "Random Seed",
                    onClick = {
                        LotteryEngine.PRNG.srand(System.currentTimeMillis())
                    },
                    modifier = Modifier.weight(1f),
                    color = NeonPurple
                )
            }
        }
    }
}

/**
 * IDA Screen
 */
@Composable
fun IDAScreen() {
    val rankings = remember { LotteryEngine.IDA.getRankings() }
    var prediction by remember { mutableStateOf<List<Int>?>(null) }
    val confidence = LotteryEngine.IDA.getConfidence()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionHeader("Inverse Distribution Algorithm", "📊")
        }
        
        // Formula box
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("IDA Formula:", color = NeonCyan, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Score = 1/(freq+1) × weight",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        "COLD = due to appear | HOT = overdue for rest",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }
        
        // Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard("Confidence", "${confidence.toInt()}%", Modifier.weight(1f))
                StatCard("Draws", "${LotteryEngine.IDA.totalDraws}", Modifier.weight(1f))
            }
        }
        
        // IDA Heatmap
        item {
            Text("IDA Heatmap (0-9)", color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier.height(180.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rankings) { score ->
                    IDACell(
                        number = score.number,
                        score = score.idaScore,
                        isHot = score.status == "HOT"
                    )
                }
            }
        }
        
        // Prediction
        if (prediction != null) {
            item {
                SectionHeader("IDA-Weighted Prediction", "🎯")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    prediction!!.forEachIndexed { index, num ->
                        val colors = listOf(NeonCyan, NeonGreen, NeonPurple)
                        NumberBall(
                            number = num,
                            color = colors[index % colors.size],
                            size = 60
                        )
                        if (index < prediction!!.size - 1) {
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                }
            }
        }
        
        // Generate button
        item {
            NeonButton(
                text = "🎯 Generate IDA Prediction",
                onClick = {
                    prediction = LotteryEngine.IDA.generateWeightedPrediction(3)
                },
                modifier = Modifier.fillMaxWidth(),
                color = NeonGreen
            )
        }
    }
}

/**
 * AI Models Screen
 */
@Composable
fun AIModelsScreen() {
    var results by remember { mutableStateOf<List<Pair<String, Prediction>>>(emptyList()) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader("26 AI Prediction Models", "🤖")
        }
        
        item {
            NeonButton(
                text = "⚡ Run All Models",
                onClick = {
                    results = LotteryEngine.aiModels.map { model ->
                        model.name to LotteryEngine.generatePrediction()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                color = NeonOrange
            )
        }
        
        if (results.isEmpty()) {
            // Show model list
            items(LotteryEngine.aiModels) { model ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(model.name, color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text(model.category, color = TextSecondary, fontSize = 12.sp)
                        }
                        Box(
                            modifier = Modifier
                                .background(Success.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                "${model.accuracy}%",
                                color = Success,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        } else {
            // Show results
            items(results) { (name, prediction) ->
                PredictionCard(
                    title = name,
                    numbers = prediction.numbers,
                    checksum = prediction.checksum,
                    accuracy = LotteryEngine.aiModels.find { it.name == name }?.accuracy
                )
            }
        }
    }
}

/**
 * Checksum Screen
 */
@Composable
fun ChecksumScreen() {
    var lastChecksum by remember { mutableStateOf<Pair<List<Int>, String>?>(null) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionHeader("Checksum Verification", "🔐")
        }
        
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Algorithms:", color = NeonCyan, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• CRC32 - Cyclic Redundancy Check", color = TextSecondary, fontSize = 12.sp)
                    Text("• Adler32 - Rolling Checksum", color = TextSecondary, fontSize = 12.sp)
                    Text("• Combined - CRC32 + Adler32 prefix", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }
        
        if (lastChecksum != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Generated Checksum", color = NeonCyan, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(horizontalArrangement = Arrangement.Center) {
                            lastChecksum!!.first.forEachIndexed { index, num ->
                                val colors = listOf(NeonPurple, NeonPink, NeonCyan)
                                NumberBall(num, color = colors[index % colors.size], size = 50)
                                if (index < lastChecksum!!.first.size - 1) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Success.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                "✓ ${lastChecksum!!.second}",
                                color = Success,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        
        item {
            NeonButton(
                text = "🔐 Generate with Checksum",
                onClick = {
                    val pred = LotteryEngine.generatePrediction()
                    lastChecksum = pred.numbers to pred.checksum
                },
                modifier = Modifier.fillMaxWidth(),
                color = Success
            )
        }
    }
}

/**
 * Historical Screen
 */
@Composable
fun HistoricalScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SectionHeader("Historical Draw Data", "📜")
        }

        item {
            Text(
                "${LotteryEngine.historicalData.size} draws loaded",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }

        items(LotteryEngine.historicalData.reversed()) { draw ->
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Draw #${draw.draw}", color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text(draw.date, color = TextSecondary, fontSize = 12.sp)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(draw.n1, draw.n2, draw.n3).forEach { num ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(NeonPurple.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    num.toString(),
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * SamLotto Strategies Screen
 */
@Composable
fun SamLottoScreen() {
    var selectedStrategy by remember { mutableStateOf<String?>(null) }
    var consensusResult by remember { mutableStateOf<ConsensusResult?>(null) }
    var monteCarloResult by remember { mutableStateOf<MonteCarloResult?>(null) }
    var multiLangResult by remember { mutableStateOf<MultiLangResult?>(null) }
    var pick34Result by remember { mutableStateOf<Pick34Result?>(null) }
    var strategyResults by remember { mutableStateOf<List<SamLottoResult>>(emptyList()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Header banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF6B0F1A), Color(0xFFB91372))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🔥 SAMLOTTO PRO",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "10 Reverse Engineered Strategies",
                        color = TextPrimary.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Strategy buttons grid
        item {
            SectionHeader("Select Strategy", "🎯")
        }

        // Strategy list
        val strategies = listOf(
            "SAMLOTTO Full" to "Full strategy suite",
            "Lottery Post" to "Pattern analysis",
            "WinSlips" to "Wheel generation",
            "Lottery Optimizer" to "Mathematical opt.",
            "Lotto Pro" to "ML ensemble",
            "Pick34" to "Skip analysis",
            "Magic Square" to "Grid-based",
            "AI Systems" to "7-Lang ML",
            "Winning Numbers" to "Historical",
            "Lotto Logic" to "Pattern logic"
        )

        items(strategies.chunked(2)) { rowStrategies ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowStrategies.forEach { (name, desc) ->
                    Card(
                        modifier = Modifier
                            .weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedStrategy == name) NeonCyan.copy(alpha = 0.2f) else DarkCard
                        ),
                        shape = RoundedCornerShape(12.dp),
                        onClick = { selectedStrategy = name }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(name, color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(desc, color = TextSecondary, fontSize = 10.sp)
                        }
                    }
                }
                if (rowStrategies.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Run buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NeonButton(
                    text = "⚡ Run Strategy",
                    onClick = {
                        LotteryEngine.initialize(System.currentTimeMillis())
                        strategyResults = when (selectedStrategy) {
                            "SAMLOTTO Full" -> listOf(LotteryEngine.SamLotto.fullStrategy())
                            "Lottery Post" -> listOf(LotteryEngine.SamLotto.lotteryPostAnalysis())
                            "WinSlips" -> listOf(LotteryEngine.SamLotto.winSlipsGenerate())
                            "Lottery Optimizer" -> listOf(LotteryEngine.SamLotto.lotteryOptimizer())
                            "Lotto Pro" -> listOf(LotteryEngine.SamLotto.lottoPro())
                            "Magic Square" -> listOf(LotteryEngine.SamLotto.magicSquarePro())
                            "Winning Numbers" -> listOf(LotteryEngine.SamLotto.winningNumbersAnalysis())
                            "Lotto Logic" -> listOf(LotteryEngine.SamLotto.lottoLogic())
                            else -> emptyList()
                        }
                        if (selectedStrategy == "Pick34") {
                            pick34Result = LotteryEngine.SamLotto.pick34Analysis()
                        }
                        if (selectedStrategy == "AI Systems") {
                            multiLangResult = LotteryEngine.SamLotto.aiLotterySystems()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    color = NeonOrange
                )
                NeonButton(
                    text = "🎲 Monte Carlo",
                    onClick = {
                        LotteryEngine.initialize(System.currentTimeMillis())
                        monteCarloResult = LotteryEngine.SamLotto.monteCarlo(50000)
                    },
                    modifier = Modifier.weight(1f),
                    color = NeonPurple
                )
            }
        }

        item {
            NeonButton(
                text = "🏆 Run All & Consensus",
                onClick = {
                    LotteryEngine.initialize(System.currentTimeMillis())
                    consensusResult = LotteryEngine.SamLotto.runAllStrategies()
                },
                modifier = Modifier.fillMaxWidth(),
                color = NeonGreen
            )
        }

        // Show results
        if (consensusResult != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🏆 CONSENSUS PREDICTION", color = NeonGreen, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.Center) {
                            consensusResult!!.prediction.forEachIndexed { index, num ->
                                NumberBall(num, listOf(NeonCyan, NeonGreen, NeonPurple)[index], 60)
                                if (index < 2) Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Confidence: ${String.format("%.1f", consensusResult!!.confidence)}%",
                            color = Success,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "✓ ${consensusResult!!.checksum}",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        if (monteCarloResult != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎲 Monte Carlo (50K)", color = NeonPurple, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.Center) {
                            monteCarloResult!!.prediction.forEachIndexed { index, num ->
                                NumberBall(num, NeonPurple, 60)
                                if (index < 2) Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "${String.format("%.1f", monteCarloResult!!.confidence)}%",
                            color = NeonPurple,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "✓ ${monteCarloResult!!.checksum}",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        if (multiLangResult != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("🌐 Multi-Language AI (7 Lang)", color = NeonOrange, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatCard("Languages", multiLangResult!!.languageCount.toString(), Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(8.dp))
                            StatCard("Models", multiLangResult!!.modelCount.toString(), Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(8.dp))
                            StatCard("Accuracy", "${String.format("%.1f", multiLangResult!!.accuracy)}%", Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            multiLangResult!!.prediction.forEachIndexed { index, num ->
                                NumberBall(num, NeonOrange, 60)
                                if (index < 2) Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                    }
                }
            }

            // Model outputs
            items(multiLangResult!!.models) { model ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(model.language, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("${String.format("%.1f", model.accuracy)}%", color = Success, fontSize = 10.sp)
                        }
                        Row {
                            model.prediction.forEach { num ->
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(NeonPurple.copy(alpha = 0.3f), RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(num.toString(), color = TextPrimary, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        }
                    }
                }
            }
        }

        if (pick34Result != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("📊 Pick34 Skip Analysis", color = NeonCyan, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Skips display
                        Text("Actual Skips (Last Appearance)", color = TextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pick34Result!!.skips.entries.sortedBy { it.key }.forEach { (num, skip) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .background(
                                            if (skip >= 5) Error.copy(alpha = 0.3f) else DarkSurface,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        num.toString(),
                                        color = if (skip >= 5) Error else NeonCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("$skip", color = TextSecondary, fontSize = 10.sp)
                                    Text("draws", color = TextSecondary, fontSize = 8.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Due Numbers: ${pick34Result!!.dueNumbers.joinToString(", ")}", color = Error)
                    }
                }
            }
        }

        // Strategy results
        if (strategyResults.isNotEmpty()) {
            items(strategyResults) { result ->
                PredictionCard(
                    title = result.strategy,
                    numbers = result.prediction,
                    checksum = LotteryEngine.Checksum.generate(result.prediction).combined,
                    accuracy = result.confidence
                )
            }
        }
    }
}

/**
 * Sum Analysis Screen
 */
@Composable
fun SumScreen() {
    var sumStats by remember { mutableStateOf(LotteryEngine.SumAnalysis.getStatistics()) }
    var targetSum by remember { mutableStateOf("") }
    var generatedNumbers by remember { mutableStateOf<List<Int>?>(null) }
    var combinationType by remember { mutableStateOf<CombinationType?>(null) }

    LaunchedEffect(Unit) {
        LotteryEngine.initialize()
        sumStats = LotteryEngine.SumAnalysis.getStatistics()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader("Sum Analysis", "➕")
        }

        // Sum statistics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard("Avg Sum", String.format("%.1f", sumStats.average), Modifier.weight(1f))
                StatCard("Min", sumStats.minSum.toString(), Modifier.weight(1f))
                StatCard("Max", sumStats.maxSum.toString(), Modifier.weight(1f))
            }
        }

        // Hot sums
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔥 Hot Sums (Most Frequent)", color = NeonOrange, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    sumStats.hotSums.forEach { sumFreq ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Sum ${sumFreq.sum}", color = TextPrimary)
                            Text("${sumFreq.count}x (${String.format("%.1f", sumFreq.percentage)}%)", color = Success)
                        }
                    }
                }
            }
        }

        // Cold sums
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("❄️ Cold Sums (Least Frequent)", color = NeonCyan, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    sumStats.coldSums.forEach { sumFreq ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Sum ${sumFreq.sum}", color = TextPrimary)
                            Text("${sumFreq.count}x (${String.format("%.1f", sumFreq.percentage)}%)", color = NeonCyan)
                        }
                    }
                }
            }
        }

        // Generate for target sum
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🎯 Generate for Target Sum", color = NeonGreen, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = targetSum,
                            onValueChange = { targetSum = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Sum (0-27)", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreen,
                                unfocusedBorderColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        NeonButton(
                            text = "Generate",
                            onClick = {
                                targetSum.toIntOrNull()?.let { sum ->
                                    if (sum in 0..27) {
                                        LotteryEngine.initialize(System.currentTimeMillis())
                                        generatedNumbers = LotteryEngine.SumAnalysis.generateForSum(sum)
                                        generatedNumbers?.let {
                                            combinationType = LotteryEngine.SumAnalysis.classifyCombination(it)
                                        }
                                    }
                                }
                            },
                            color = NeonGreen
                        )
                    }
                }
            }
        }

        // Show generated numbers
        if (generatedNumbers != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(horizontalArrangement = Arrangement.Center) {
                            generatedNumbers!!.forEachIndexed { index, num ->
                                NumberBall(num, listOf(NeonCyan, NeonGreen, NeonPurple)[index], 60)
                                if (index < 2) Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Sum: ${generatedNumbers!!.sum()}",
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold
                        )
                        if (combinationType != null) {
                            Text(
                                "Type: ${combinationType!!.name} (${combinationType!!.odds})",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Sum frequency chart (simplified)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📊 Sum Distribution", color = NeonPurple, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    val allSums = LotteryEngine.SumAnalysis.getAllSumFrequencies()
                    val maxCount = allSums.maxOfOrNull { it.count } ?: 1

                    // Show sums in rows of 7
                    allSums.chunked(7).forEach { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            row.forEach { sumFreq ->
                                val intensity = if (maxCount > 0) sumFreq.count.toFloat() / maxCount else 0f
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .background(
                                            NeonPurple.copy(alpha = 0.2f + intensity * 0.6f),
                                            RoundedCornerShape(4.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            sumFreq.sum.toString(),
                                            color = TextPrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            sumFreq.count.toString(),
                                            color = TextSecondary,
                                            fontSize = 8.sp
                                        )
                                    }
                                }
                            }
                            // Fill remaining space if row has fewer items
                            repeat(7 - row.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Seed Generator Screen
 */
@Composable
fun SeedScreen() {
    var seedInput by remember { mutableStateOf("") }
    var dateInput by remember { mutableStateOf("") }
    var currentSeed by remember { mutableStateOf(LotteryEngine.PRNG.getSeed()) }
    var generatedNumbers by remember { mutableStateOf<List<Int>?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader("Seed PRNG Functions", "🌱")
        }

        item {
            StatCard("Current Seed", currentSeed.toString(), Modifier.fillMaxWidth())
        }

        // Seed from string
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📝 Seed from String/Name", color = NeonCyan, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = seedInput,
                            onValueChange = { seedInput = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Enter name/phrase", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        NeonButton(
                            text = "Set",
                            onClick = {
                                if (seedInput.isNotEmpty()) {
                                    val newSeed = LotteryEngine.SeedGenerator.fromString(seedInput)
                                    LotteryEngine.PRNG.srand(newSeed)
                                    currentSeed = newSeed
                                    seedInput = ""
                                }
                            }
                        )
                    }
                }
            }
        }

        // Seed from date
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📅 Seed from Date (YYYYMMDD)", color = NeonPurple, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = dateInput,
                            onValueChange = { dateInput = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("20240115", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonPurple,
                                unfocusedBorderColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        NeonButton(
                            text = "Set",
                            onClick = {
                                if (dateInput.length == 8) {
                                    val year = dateInput.take(4).toIntOrNull() ?: 2024
                                    val month = dateInput.substring(4, 6).toIntOrNull() ?: 1
                                    val day = dateInput.takeLast(2).toIntOrNull() ?: 1
                                    val newSeed = LotteryEngine.SeedGenerator.fromDate(year, month, day)
                                    LotteryEngine.PRNG.srand(newSeed)
                                    currentSeed = newSeed
                                    dateInput = ""
                                }
                            },
                            color = NeonPurple
                        )
                    }
                }
            }
        }

        // Quick seed buttons
        item {
            Text("Quick Seed Options", color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NeonButton(
                    text = "🕐 Time",
                    onClick = {
                        val newSeed = LotteryEngine.SeedGenerator.fromTimestamp()
                        LotteryEngine.PRNG.srand(newSeed)
                        currentSeed = newSeed
                    },
                    modifier = Modifier.weight(1f),
                    color = NeonOrange
                )
                NeonButton(
                    text = "📊 Gauss",
                    onClick = {
                        val newSeed = LotteryEngine.SeedGenerator.fromGaussian()
                        LotteryEngine.PRNG.srand(newSeed)
                        currentSeed = newSeed
                    },
                    modifier = Modifier.weight(1f),
                    color = NeonGreen
                )
                NeonButton(
                    text = "🎲 Entropy",
                    onClick = {
                        val newSeed = LotteryEngine.SeedGenerator.fromEntropy()
                        LotteryEngine.PRNG.srand(newSeed)
                        currentSeed = newSeed
                    },
                    modifier = Modifier.weight(1f),
                    color = NeonPink
                )
            }
        }

        // Generate with current seed
        item {
            NeonButton(
                text = "⚡ Generate with Current Seed",
                onClick = {
                    generatedNumbers = (1..3).map { LotteryEngine.PRNG.randRange(0, 9) }.sorted()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Show generated numbers
        if (generatedNumbers != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Generated Numbers", color = NeonCyan, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.Center) {
                            generatedNumbers!!.forEachIndexed { index, num ->
                                NumberBall(num, listOf(NeonCyan, NeonGreen, NeonPurple)[index], 60)
                                if (index < 2) Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        val checksum = LotteryEngine.Checksum.generate(generatedNumbers!!)
                        Text("✓ ${checksum.combined}", color = Success, fontSize = 12.sp)
                    }
                }
            }
        }

        // Seed info
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ℹ️ Seed Functions Available", color = TextSecondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val functions = listOf(
                        "fromTimestamp()" to "Nano + millisecond time",
                        "fromString()" to "Hash any text input",
                        "fromDate()" to "Date-based seed",
                        "fromNumbers()" to "Previous draw seed",
                        "fromEntropy()" to "Combined sources",
                        "fromGaussian()" to "Normal distribution",
                        "fromBirthDate()" to "Lucky life path"
                    )
                    functions.forEach { (name, desc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            Text(name, color = NeonCyan, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            Text(desc, color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
