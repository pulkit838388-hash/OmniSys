package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.InAppSettings
import com.example.ui.screens.AccessibilityHubScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.TelemetryScreen
import com.example.ui.screens.UpdatesScreen
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkSurfaceGlassLighter
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassSpecular
import com.example.ui.theme.HighContrastBorder
import com.example.ui.theme.ImmersiveBlue
import com.example.ui.theme.ImmersiveBlueLight
import com.example.ui.theme.ImmersivePurple
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidEmerald
import com.example.ui.theme.LiquidMagenta
import com.example.ui.theme.LiquidViolet
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate500
import com.example.ui.theme.TextWhite
import com.example.ui.theme.Zinc800
import com.example.ui.theme.Zinc900
import com.example.viewmodel.MainTab
import com.example.viewmodel.OmniViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: OmniViewModel = viewModel()
            val settings by viewModel.inAppSettings.collectAsStateWithLifecycle()

            MyApplicationTheme(isAmoledBlack = settings.isAmoledPureBlack) {
                OmniApp(viewModel = viewModel, settings = settings)
            }
        }
    }
}

@Composable
fun OmniApp(viewModel: OmniViewModel, settings: InAppSettings) {
    val currentTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val apps by viewModel.filteredApps.collectAsStateWithLifecycle()
    val pendingCount by viewModel.pendingUpdateCount.collectAsStateWithLifecycle()
    val totalUpdateSizeMb by viewModel.totalUpdateSizeMb.collectAsStateWithLifecycle()
    val activeFilter by viewModel.activeFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
    val isOneTapRunning by viewModel.isOneTapRunning.collectAsStateWithLifecycle()
    val oneTapProgress by viewModel.oneTapProgress.collectAsStateWithLifecycle()
    val oneTapStep by viewModel.oneTapStep.collectAsStateWithLifecycle()
    val oneTapMessage by viewModel.oneTapMessage.collectAsStateWithLifecycle()
    val oneTapSummary by viewModel.oneTapSummary.collectAsStateWithLifecycle()
    val telemetry by viewModel.telemetry.collectAsStateWithLifecycle()
    val profiles by viewModel.allProfiles.collectAsStateWithLifecycle()
    val logs by viewModel.allLogs.collectAsStateWithLifecycle()

    val bgModifier = if (settings.isAmoledPureBlack) {
        Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    } else {
        Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .drawBehind {
                // Ambient Liquid Cosmic Glows
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x1800F0FF), Color.Transparent),
                        center = Offset(0f, 0f),
                        radius = size.width * 0.9f
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x188B5CF6), Color.Transparent),
                        center = Offset(size.width, size.height * 0.8f),
                        radius = size.width * 0.9f
                    )
                )
            }
    }

    Scaffold(
        modifier = bgModifier,
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            OmniTopBar(
                currentTab = currentTab,
                pendingCount = pendingCount,
                settings = settings,
                onHeaderClick = {
                    val overview = "OmniSys Utility Hub. Current profile: ${settings.activeProfileName}. $pendingCount updates pending. System status: Operating normally."
                    viewModel.speakText(overview)
                }
            )
        },
        bottomBar = {
            OmniLiquidNavigationBar(
                currentTab = currentTab,
                pendingCount = pendingCount,
                isHighContrast = settings.isHighContrastMode,
                isLargeTarget = settings.isLargeTouchTargetMode,
                onTabSelect = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_transition"
            ) { targetTab ->
                when (targetTab) {
                    MainTab.UPDATES -> UpdatesScreen(
                        viewModel = viewModel,
                        apps = apps,
                        pendingCount = pendingCount,
                        totalUpdateSizeMb = totalUpdateSizeMb,
                        activeFilter = activeFilter,
                        searchQuery = searchQuery,
                        sortOrder = sortOrder,
                        isOneTapRunning = isOneTapRunning,
                        oneTapProgress = oneTapProgress,
                        oneTapStep = oneTapStep,
                        oneTapMessage = oneTapMessage,
                        oneTapSummary = oneTapSummary,
                        telemetry = telemetry,
                        settings = settings
                    )

                    MainTab.TELEMETRY -> TelemetryScreen(
                        viewModel = viewModel,
                        telemetry = telemetry,
                        settings = settings
                    )

                    MainTab.ACCESSIBILITY -> AccessibilityHubScreen(
                        viewModel = viewModel,
                        shortcuts = viewModel.accessibilityShortcuts,
                        profiles = profiles,
                        settings = settings
                    )

                    MainTab.HISTORY -> HistoryScreen(
                        viewModel = viewModel,
                        logs = logs,
                        settings = settings
                    )
                }
            }
        }
    }
}

@Composable
fun OmniTopBar(
    currentTab: MainTab,
    pendingCount: Int,
    settings: InAppSettings,
    onHeaderClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Category & App Title Header
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onHeaderClick() }
                    .padding(vertical = 2.dp)
            ) {
                Text(
                    text = "SYSTEM CORE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    ),
                    color = ImmersiveBlueLight
                )
                Text(
                    text = "OmniSys Hub",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Normal,
                        letterSpacing = (-0.5).sp,
                        fontSize = 22.sp
                    ),
                    color = TextWhite
                )
            }

            // Right Quick Status Indicator / Voice Cue Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Zinc900.copy(alpha = 0.8f))
                    .border(
                        1.dp,
                        if (settings.isHighContrastMode) HighContrastBorder else GlassSpecular,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onHeaderClick() }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (pendingCount > 0) ImmersiveBlue else LiquidEmerald)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (pendingCount > 0) "$pendingCount Updates" else "Optimal",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        ),
                        color = if (pendingCount > 0) ImmersiveBlueLight else LiquidEmerald
                    )
                }
            }
        }
    }
}

@Composable
fun OmniLiquidNavigationBar(
    currentTab: MainTab,
    pendingCount: Int,
    isHighContrast: Boolean,
    isLargeTarget: Boolean,
    onTabSelect: (MainTab) -> Unit
) {
    val navHeight = if (isLargeTarget) 74.dp else 64.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Zinc900.copy(alpha = 0.85f),
            border = if (isHighContrast) {
                androidx.compose.foundation.BorderStroke(2.dp, HighContrastBorder)
            } else {
                androidx.compose.foundation.BorderStroke(
                    1.dp,
                    GlassSpecular
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(navHeight)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LiquidNavItem(
                    title = "Updates",
                    icon = Icons.Default.SystemUpdate,
                    isSelected = currentTab == MainTab.UPDATES,
                    badgeCount = pendingCount,
                    isHighContrast = isHighContrast,
                    isLargeTarget = isLargeTarget,
                    onClick = { onTabSelect(MainTab.UPDATES) },
                    testTag = "nav_tab_updates"
                )

                LiquidNavItem(
                    title = "Telemetry",
                    icon = Icons.Default.Memory,
                    isSelected = currentTab == MainTab.TELEMETRY,
                    isHighContrast = isHighContrast,
                    isLargeTarget = isLargeTarget,
                    onClick = { onTabSelect(MainTab.TELEMETRY) },
                    testTag = "nav_tab_telemetry"
                )

                LiquidNavItem(
                    title = "Access",
                    icon = Icons.Default.AccessibilityNew,
                    isSelected = currentTab == MainTab.ACCESSIBILITY,
                    isHighContrast = isHighContrast,
                    isLargeTarget = isLargeTarget,
                    onClick = { onTabSelect(MainTab.ACCESSIBILITY) },
                    testTag = "nav_tab_accessibility"
                )

                LiquidNavItem(
                    title = "History",
                    icon = Icons.Default.History,
                    isSelected = currentTab == MainTab.HISTORY,
                    isHighContrast = isHighContrast,
                    isLargeTarget = isLargeTarget,
                    onClick = { onTabSelect(MainTab.HISTORY) },
                    testTag = "nav_tab_history"
                )
            }
        }
    }
}

@Composable
fun LiquidNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    isHighContrast: Boolean,
    isLargeTarget: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit,
    testTag: String
) {
    val activeColor = ImmersiveBlue
    val inactiveColor = TextSlate500

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) {
                    if (isHighContrast) Color(0x333B82F6) else Zinc800.copy(alpha = 0.6f)
                } else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge(
                            containerColor = ImmersiveBlue,
                            contentColor = TextWhite
                        ) {
                            Text("$badgeCount", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) activeColor else inactiveColor,
                    modifier = Modifier.size(if (isLargeTarget) 24.dp else 20.dp)
                )
            }

            if (isSelected) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isLargeTarget) 14.sp else 12.sp
                    ),
                    color = TextWhite
                )
            }
        }
    }
}
