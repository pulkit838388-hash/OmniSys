package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.filled.ArrowUpward
import com.example.model.UpdateStatus
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkSurfaceGlassLighter
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassSpecular
import com.example.ui.theme.HighContrastBorder
import com.example.ui.theme.ImmersiveBlue
import com.example.ui.theme.ImmersiveBlueGlow
import com.example.ui.theme.ImmersiveBlueLight
import com.example.ui.theme.ImmersiveIndigo
import com.example.ui.theme.ImmersivePurple
import com.example.ui.theme.LiquidAmber
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidCyanGlow
import com.example.ui.theme.LiquidEmerald
import com.example.ui.theme.LiquidEmeraldGlow
import com.example.ui.theme.LiquidMagenta
import com.example.ui.theme.LiquidRose
import com.example.ui.theme.LiquidViolet
import com.example.ui.theme.LiquidVioletGlow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.TextWhite
import com.example.ui.theme.Zinc800
import com.example.ui.theme.Zinc900

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shapeRadius: Dp = 24.dp,
    isHighContrast: Boolean = false,
    accentColor: Color = ImmersiveBlue,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(shapeRadius)
    val borderBrush = if (isHighContrast) {
        Brush.linearGradient(listOf(HighContrastBorder, HighContrastBorder))
    } else {
        Brush.linearGradient(
            colors = listOf(
                GlassSpecular,
                accentColor.copy(alpha = 0.20f),
                GlassBorderSubtle
            ),
            start = Offset(0f, 0f),
            end = Offset(400f, 400f)
        )
    }

    val borderWidth = if (isHighContrast) 2.dp else 1.dp

    val cardModifier = modifier
        .clip(shape)
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    DarkCardSurface,
                    Zinc900.copy(alpha = 0.50f)
                )
            )
        )
        .border(
            border = BorderStroke(borderWidth, borderBrush),
            shape = shape
        )
        .then(
            if (onClick != null) {
                Modifier.clickable(onClick = onClick)
            } else Modifier
        )

    Box(modifier = cardModifier) {
        content()
    }
}

@Composable
fun LiquidPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPrimary: Boolean = true,
    isHighContrast: Boolean = false,
    isLargeTarget: Boolean = false,
    accentColor: Color = ImmersiveBlue,
    enabled: Boolean = true,
    testTag: String = "liquid_pill_button"
) {
    val minHeight = if (isLargeTarget) 64.dp else 48.dp
    val shape = RoundedCornerShape(16.dp)

    val containerColor = if (isPrimary) {
        if (isHighContrast) ImmersiveBlue else Color.White
    } else {
        Zinc900
    }

    val contentColor = if (isPrimary) {
        Color.Black
    } else {
        TextWhite
    }

    val borderStroke = if (isHighContrast) {
        BorderStroke(2.dp, HighContrastBorder)
    } else if (!isPrimary) {
        BorderStroke(1.dp, GlassSpecular)
    } else null

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Zinc800,
            disabledContentColor = TextSubtle
        ),
        border = borderStroke,
        modifier = modifier
            .testTag(testTag)
            .heightIn(min = minHeight)
            .then(
                if (isPrimary && !isHighContrast) {
                    Modifier.shadow(12.dp, shape = shape, spotColor = Color(0x66000000), ambientColor = Color(0x40000000))
                } else Modifier
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(if (isLargeTarget) 22.dp else 18.dp),
                    tint = contentColor
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = if (isLargeTarget) {
                    MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                } else {
                    MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                },
                color = contentColor
            )
        }
    }
}

@Composable
fun LiquidCircularGauge(
    percentage: Float, // 0f to 100f
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    accentColor: Color = LiquidCyan,
    glowColor: Color = LiquidCyanGlow,
    size: Dp = 100.dp,
    strokeWidth: Dp = 9.dp
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage.coerceIn(0f, 100f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "gauge_progress"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(size)
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(strokeWidth / 2)) {
                val diameter = this.size.minDimension
                val arcRadius = diameter / 2f
                val sweep = (animatedPercentage / 100f) * 360f

                // Track Background
                drawCircle(
                    color = Color(0x1FFFFFFF),
                    radius = arcRadius,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )

                // Active Liquid Sweep
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            accentColor,
                            LiquidViolet,
                            accentColor
                        )
                    ),
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${animatedPercentage.toInt()}%",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = (size.value * 0.22).sp
                    ),
                    color = TextWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = TextWhite
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LiquidStatusBadge(
    status: UpdateStatus,
    modifier: Modifier = Modifier
) {
    val (text, color, icon) = when (status) {
        UpdateStatus.CRITICAL_SECURITY -> Triple("Security Update", LiquidRose, Icons.Default.Security)
        UpdateStatus.UPDATE_AVAILABLE -> Triple("Update Available", LiquidCyan, Icons.Default.AutoAwesome)
        UpdateStatus.UP_TO_DATE -> Triple("Up to Date", LiquidEmerald, Icons.Default.CheckCircle)
        UpdateStatus.CHECKING -> Triple("Checking...", LiquidAmber, Icons.Default.Refresh)
    }

    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                ),
                color = color
            )
        }
    }
}

@Composable
fun OneTapUpdateHeroCard(
    pendingCount: Int,
    totalSizeMb: Double,
    isUpdating: Boolean,
    isHighContrast: Boolean,
    isLargeTarget: Boolean,
    onOneTapClick: () -> Unit,
    onVoiceAnnounce: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val shape = RoundedCornerShape(32.dp)
    val heroGradient = Brush.linearGradient(
        colors = listOf(
            Color(0x332563EB), // blue-600/20
            Color(0x1A7C3AED), // purple-600/10
            DarkCardSurface
        ),
        start = Offset(0f, 0f),
        end = Offset(600f, 600f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(heroGradient)
            .border(
                border = BorderStroke(
                    if (isHighContrast) 2.dp else 1.dp,
                    if (isHighContrast) HighContrastBorder else GlassSpecular
                ),
                shape = shape
            )
            .drawBehind {
                // Top-right Immersive Ambient Glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x333B82F6), Color.Transparent),
                        center = Offset(size.width * 0.9f, 0f),
                        radius = size.width * 0.55f
                    )
                )
            }
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (pendingCount > 0) ImmersiveBlueLight else LiquidEmerald)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SYSTEM CORE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = ImmersiveBlueLight
                    )
                }

                IconButton(
                    onClick = onVoiceAnnounce,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x1AFFFFFF))
                        .testTag("tts_announce_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Read Status Aloud",
                        tint = TextSlate100,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Central Glowing Blue Orb (Immersive UI signature)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(72.dp)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(ImmersiveBlueGlow, Color.Transparent)
                            ),
                            radius = size.minDimension * 0.9f * (if (isUpdating) pulseScale else 1f)
                        )
                    }
                    .clip(CircleShape)
                    .background(
                        if (pendingCount > 0) ImmersiveBlue else LiquidEmerald
                    )
                    .clickable(enabled = !isUpdating) { onOneTapClick() }
            ) {
                Icon(
                    imageVector = if (pendingCount > 0) Icons.Default.ArrowUpward else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (pendingCount > 0) "$pendingCount Pending Updates" else "All Systems Fresh & Secure",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = if (isLargeTarget) 22.sp else 20.sp
                ),
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (pendingCount > 0) {
                    "Everything stays fresh and secure (${String.format("%.1f", totalSizeMb)} MB)"
                } else {
                    "System operating at peak smoothness & up to date"
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    color = TextMuted
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Master Pristine Solid Action Button
            LiquidPillButton(
                text = if (pendingCount > 0) "Update All in 1 Tap" else "Re-Check & Optimize System",
                onClick = onOneTapClick,
                icon = if (pendingCount > 0) Icons.Default.Speed else Icons.Default.Refresh,
                isPrimary = true,
                isHighContrast = isHighContrast,
                isLargeTarget = isLargeTarget,
                accentColor = ImmersiveBlue,
                enabled = !isUpdating,
                modifier = Modifier.fillMaxWidth(),
                testTag = "one_tap_update_master_button"
            )
        }
    }
}

@Composable
fun LiquidUpdateProgressModal(
    step: Int,
    progress: Float,
    statusMessage: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            accentColor = LiquidCyan,
            shapeRadius = 28.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "modal_spin")
                val spinProgress by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "spin_angle"
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(80.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                colors = listOf(LiquidCyan, LiquidViolet, LiquidMagenta, LiquidCyan)
                            ),
                            startAngle = spinProgress,
                            sweepAngle = 280f,
                            useCenter = false,
                            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = LiquidCyan,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "1-Tap Optimization Pipeline",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Step $step of 5: In Progress",
                    style = MaterialTheme.typography.labelMedium.copy(color = LiquidCyan, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = LiquidCyan,
                    trackColor = Color(0x33FFFFFF)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
