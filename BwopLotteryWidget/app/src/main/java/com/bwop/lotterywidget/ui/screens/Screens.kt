package com.bwop.lotterywidget.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bwop.lotterywidget.LotteryEngine
import com.bwop.lotterywidget.Prediction
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
