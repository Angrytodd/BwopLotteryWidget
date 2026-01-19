package com.bwop.lotterywidget.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bwop.lotterywidget.ui.theme.*

/**
 * Animated lottery number ball with neon glow effect
 */
@Composable
fun NumberBall(
    number: Int,
    modifier: Modifier = Modifier,
    color: Color = NeonPurple,
    size: Int = 60
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ball")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )
    
    Box(
        modifier = modifier
            .size(size.dp)
            .offset(y = offset.dp)
            .shadow(
                elevation = 20.dp,
                shape = CircleShape,
                ambientColor = color,
                spotColor = color
            )
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(color, color.copy(alpha = 0.7f))
                )
            )
            .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            fontSize = (size / 2).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Prediction card with checksum
 */
@Composable
fun PredictionCard(
    title: String,
    numbers: List<Int>,
    checksum: String,
    accuracy: Double? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = NeonCyan,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (accuracy != null) {
                    Box(
                        modifier = Modifier
                            .background(Success.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${accuracy}%",
                            color = Success,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Numbers
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val colors = listOf(NeonPurple, NeonPink, NeonCyan, NeonGreen, NeonOrange)
                numbers.forEachIndexed { index, number ->
                    NumberBall(
                        number = number,
                        color = colors[index % colors.size],
                        size = 50
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Checksum
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBg.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "✓ $checksum",
                    color = Success,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Stat card component
 */
@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = NeonCyan,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

/**
 * Neon button
 */
@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = NeonCyan
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, color)
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * IDA Heatmap Cell
 */
@Composable
fun IDACell(
    number: Int,
    score: Double,
    isHot: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (isHot) NeonOrange else NeonCyan
    val alpha = (score / 100f).coerceIn(0.3f, 1f)
    
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = alpha.toFloat()))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = number.toString(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = String.format("%.1f", score),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }
    }
}

/**
 * Section header
 */
@Composable
fun SectionHeader(
    title: String,
    emoji: String = "",
    modifier: Modifier = Modifier
) {
    Text(
        text = "$emoji $title",
        color = TextPrimary,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(vertical = 16.dp)
    )
}
