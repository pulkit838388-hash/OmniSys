package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InAppSettings
import com.example.model.SystemTelemetry
import com.example.ui.components.LiquidCircularGauge
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidPillButton
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.LiquidAmber
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidEmerald
import com.example.ui.theme.LiquidMagenta
import com.example.ui.theme.LiquidRose
import com.example.ui.theme.LiquidViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.TextWhite
import com.example.viewmodel.OmniViewModel

@Composable
fun TelemetryScreen(
    viewModel: OmniViewModel,
    telemetry: SystemTelemetry?,
    settings: InAppSettings,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Header Action Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LIVE SYSTEM TELEMETRY",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = LiquidCyan
                    )
                    Text(
                        text = "Real-time hardware & memory metrics",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = TextMuted
                    )
                }

                Row {
                    IconButton(
                        onClick = {
                            if (telemetry != null) {
                                val ramUsedGb = telemetry.ram.usedBytes / (1024.0 * 1024.0 * 1024.0)
                                val ramTotalGb = telemetry.ram.totalBytes / (1024.0 * 1024.0 * 1024.0)
                                val storageUsedGb = telemetry.storage.usedBytes / (1024.0 * 1024.0 * 1024.0)
                                val storageTotalGb = telemetry.storage.totalBytes / (1024.0 * 1024.0 * 1024.0)
                                val report = "System Diagnostics Report: RAM is ${telemetry.ram.usedPercentage.toInt()} percent utilized (${String.format("%.1f", ramUsedGb)} of ${String.format("%.1f", ramTotalGb)} GB). Storage is ${telemetry.storage.usedPercentage.toInt()} percent used. Battery is at ${telemetry.battery.levelPercent} percent, temperature is ${telemetry.battery.temperatureCelsius} degrees Celsius, health status is ${telemetry.battery.health}."
                                viewModel.speakText(report)
                            }
                        },
                        modifier = Modifier.testTag("tts_telemetry_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Read Telemetry Aloud",
                            tint = LiquidCyan
                        )
                    }

                    IconButton(
                        onClick = { viewModel.refreshTelemetry() },
                        modifier = Modifier.testTag("refresh_telemetry_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Metrics",
                            tint = LiquidCyan
                        )
                    }
                }
            }
        }

        // RAM & Storage Liquid Circular Gauges in a Glass Card
        item {
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                isHighContrast = settings.isHighContrastMode,
                accentColor = LiquidCyan
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "MEMORY & STORAGE LOAD",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val ramUsedGb = (telemetry?.ram?.usedBytes ?: 0L) / (1024.0 * 1024.0 * 1024.0)
                        val ramTotalGb = (telemetry?.ram?.totalBytes ?: 1L) / (1024.0 * 1024.0 * 1024.0)
                        val ramPct = telemetry?.ram?.usedPercentage ?: 45f

                        LiquidCircularGauge(
                            percentage = ramPct,
                            title = "Active RAM",
                            subtitle = "${String.format("%.1f", ramUsedGb)} / ${String.format("%.1f", ramTotalGb)} GB",
                            accentColor = if (ramPct > 80f) LiquidRose else LiquidCyan
                        )

                        val storageUsedGb = (telemetry?.storage?.usedBytes ?: 0L) / (1024.0 * 1024.0 * 1024.0)
                        val storageTotalGb = (telemetry?.storage?.totalBytes ?: 1L) / (1024.0 * 1024.0 * 1024.0)
                        val storagePct = telemetry?.storage?.usedPercentage ?: 60f

                        LiquidCircularGauge(
                            percentage = storagePct,
                            title = "Internal Storage",
                            subtitle = "${String.format("%.1f", storageUsedGb)} / ${String.format("%.1f", storageTotalGb)} GB",
                            accentColor = if (storagePct > 85f) LiquidAmber else LiquidViolet
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        LiquidPillButton(
                            text = "Trim Memory (GC)",
                            onClick = { viewModel.runFastOptimizer() },
                            icon = Icons.Default.CleaningServices,
                            isPrimary = true,
                            isHighContrast = settings.isHighContrastMode,
                            isLargeTarget = settings.isLargeTouchTargetMode,
                            accentColor = LiquidCyan,
                            modifier = Modifier.weight(1f),
                            testTag = "trim_memory_button"
                        )

                        LiquidPillButton(
                            text = "System Storage",
                            onClick = { viewModel.openStorageSettings() },
                            icon = Icons.Default.Storage,
                            isPrimary = false,
                            isHighContrast = settings.isHighContrastMode,
                            isLargeTarget = settings.isLargeTouchTargetMode,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Battery Thermals & Health Section
        item {
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                isHighContrast = settings.isHighContrastMode,
                accentColor = LiquidEmerald
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.BatteryChargingFull,
                                contentDescription = null,
                                tint = LiquidEmerald,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BATTERY & THERMAL METRICS",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = TextWhite
                            )
                        }

                        Text(
                            text = "${telemetry?.battery?.levelPercent ?: 85}%",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = LiquidEmerald
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TelemetryDetailBox(
                            icon = Icons.Default.Thermostat,
                            label = "Temperature",
                            value = "${String.format("%.1f", telemetry?.battery?.temperatureCelsius ?: 31.0f)} °C",
                            accentColor = if ((telemetry?.battery?.temperatureCelsius ?: 30f) > 40f) LiquidRose else LiquidCyan,
                            modifier = Modifier.weight(1f)
                        )

                        TelemetryDetailBox(
                            icon = Icons.Default.Bolt,
                            label = "Voltage",
                            value = "${telemetry?.battery?.voltageMv ?: 4050} mV",
                            accentColor = LiquidAmber,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TelemetryDetailBox(
                            icon = Icons.Default.Security,
                            label = "Health Status",
                            value = telemetry?.battery?.health ?: "Good / Optimal",
                            accentColor = LiquidEmerald,
                            modifier = Modifier.weight(1f)
                        )

                        TelemetryDetailBox(
                            icon = Icons.Default.Bolt,
                            label = "Power Source",
                            value = telemetry?.battery?.powerSource ?: "Discharging",
                            accentColor = LiquidViolet,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Hardware & Display Specifications
        item {
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                isHighContrast = settings.isHighContrastMode,
                accentColor = LiquidViolet
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "DEVICE ARCHITECTURE & SPECS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = LiquidViolet
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    SpecItemRow(label = "Device Model", value = "${telemetry?.specs?.manufacturer} ${telemetry?.specs?.deviceModel}")
                    SpecItemRow(label = "OS Version", value = telemetry?.specs?.androidVersion ?: "Android 15")
                    SpecItemRow(label = "Security Patch", value = telemetry?.specs?.securityPatch ?: "2026-08-01")
                    SpecItemRow(label = "CPU Architecture", value = "${telemetry?.specs?.boardArchitecture} (${telemetry?.specs?.cpuCores} Cores)")
                    SpecItemRow(label = "Display Resolution", value = telemetry?.specs?.displayResolution ?: "1080 × 2400")
                    SpecItemRow(label = "Display Refresh Rate", value = "${telemetry?.specs?.refreshRateHz?.toInt() ?: 120} Hz (Fluid AMOLED)")
                    SpecItemRow(label = "Display Density", value = "${telemetry?.specs?.densityDpi ?: 420} DPI")
                    SpecItemRow(label = "Google Play Services", value = telemetry?.googlePlayServicesVersion ?: "v24.x")
                    SpecItemRow(label = "Play System Update", value = telemetry?.playSystemUpdateDate ?: "v2026.08")
                    SpecItemRow(label = "Build Fingerprint", value = telemetry?.specs?.buildId ?: "AP3A.240801.001", isLast = true)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TelemetryDetailBox(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0x12FFFFFF))
            .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = TextMuted
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = TextWhite
            )
        }
    }
}

@Composable
fun SpecItemRow(
    label: String,
    value: String,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = TextMuted
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                ),
                color = TextWhite
            )
        }
        if (!isLast) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.8.dp)
                    .background(Color(0x0EFFFFFF))
            )
        }
    }
}

@Composable
fun PowerIcon(): ImageVector = Icons.Default.Bolt
