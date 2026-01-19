package com.bwop.lotterywidget.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider
import com.bwop.lotterywidget.LotteryEngine
import com.bwop.lotterywidget.MainActivity

/**
 * BWOP Lottery Widget - Android 15 Glance Widget
 */
class LotteryWidget : GlanceAppWidget() {
    
    override val sizeMode = SizeMode.Responsive(
        setOf(
            DpSize(180.dp, 110.dp),
            DpSize(250.dp, 180.dp),
            DpSize(400.dp, 200.dp)
        )
    )
    
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Initialize engine
        LotteryEngine.initialize(System.currentTimeMillis())
        
        provideContent {
            LotteryWidgetContent()
        }
    }
}

@Composable
fun LotteryWidgetContent() {
    val prediction = LotteryEngine.generatePrediction()
    val size = LocalSize.current
    val isLarge = size.width >= 250.dp
    
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color(0xFF1A1A2E)))
            .cornerRadius(16.dp)
            .clickable(actionStartActivity<MainActivity>())
            .padding(12.dp)
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Header
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯 BWOP Lottery",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF00F5FF)),
                        fontSize = if (isLarge) 16.sp else 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            
            Spacer(modifier = GlanceModifier.height(8.dp))
            
            // Numbers
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val colors = listOf(
                    Color(0xFF9D4EDD), // Purple
                    Color(0xFFFF006E), // Pink
                    Color(0xFF00F5FF)  // Cyan
                )
                
                prediction.numbers.forEachIndexed { index, number ->
                    NumberBallWidget(
                        number = number,
                        color = colors[index % colors.size],
                        size = if (isLarge) 48 else 40
                    )
                    if (index < prediction.numbers.size - 1) {
                        Spacer(modifier = GlanceModifier.width(8.dp))
                    }
                }
            }
            
            Spacer(modifier = GlanceModifier.height(8.dp))
            
            // Checksum
            if (isLarge) {
                Text(
                    text = "✓ ${prediction.checksum}",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF4CAF50)),
                        fontSize = 10.sp
                    ),
                    modifier = GlanceModifier.fillMaxWidth(),
                )
            }
            
            Spacer(modifier = GlanceModifier.height(4.dp))
            
            // Refresh button
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .clickable(actionRunCallback<RefreshAction>()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚡ Tap to refresh",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFFB0B0B0)),
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
fun NumberBallWidget(number: Int, color: Color, size: Int) {
    Box(
        modifier = GlanceModifier
            .size(size.dp)
            .cornerRadius((size / 2).dp)
            .background(ColorProvider(color)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = (size / 2).sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

/**
 * Refresh action callback
 */
class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // Re-initialize with new seed
        LotteryEngine.initialize(System.currentTimeMillis())
        
        // Update widget
        LotteryWidget().update(context, glanceId)
    }
}

/**
 * Widget receiver
 */
class LotteryWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LotteryWidget()
}

/**
 * Boot receiver for widget updates
 */
class BootReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Widget will be updated automatically by the system
        }
    }
}
