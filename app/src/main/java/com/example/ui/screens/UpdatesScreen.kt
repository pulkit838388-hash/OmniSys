package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppItem
import com.example.model.AppType
import com.example.model.InAppSettings
import com.example.model.SystemTelemetry
import com.example.model.UpdateStatus
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidPillButton
import com.example.ui.components.LiquidStatusBadge
import com.example.ui.components.LiquidUpdateProgressModal
import com.example.ui.components.OneTapUpdateHeroCard
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkSurfaceGlassLighter
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidEmerald
import com.example.ui.theme.LiquidMagenta
import com.example.ui.theme.LiquidRose
import com.example.ui.theme.LiquidViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AppFilter
import com.example.viewmodel.OmniViewModel
import com.example.viewmodel.SortOrder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UpdatesScreen(
    viewModel: OmniViewModel,
    apps: List<AppItem>,
    pendingCount: Int,
    totalUpdateSizeMb: Double,
    activeFilter: AppFilter,
    searchQuery: String,
    sortOrder: SortOrder,
    isOneTapRunning: Boolean,
    oneTapProgress: Float,
    oneTapStep: Int,
    oneTapMessage: String,
    oneTapSummary: String?,
    telemetry: SystemTelemetry?,
    settings: InAppSettings,
    modifier: Modifier = Modifier
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    if (isOneTapRunning) {
        LiquidUpdateProgressModal(
            step = oneTapStep,
            progress = oneTapProgress,
            statusMessage = oneTapMessage,
            onDismiss = {}
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Summary notification banner if just completed
        if (oneTapSummary != null) {
            item {
                LiquidGlassCard(
                    modifier = Modifier.fillMaxWidth().testTag("onetap_summary_banner"),
                    accentColor = LiquidEmerald,
                    isHighContrast = settings.isHighContrastMode
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = LiquidEmerald,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "System Update & Boost Success",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextWhite
                                )
                                Text(
                                    text = oneTapSummary,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                    color = LiquidEmerald
                                )
                            }
                        }

                        IconButton(onClick = { viewModel.dismissOneTapSummary() }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Dismiss",
                                tint = TextMuted
                            )
                        }
                    }
                }
            }
        }

        // Central 1-Tap Master Update Hero
        item {
            OneTapUpdateHeroCard(
                pendingCount = pendingCount,
                totalSizeMb = totalUpdateSizeMb,
                isUpdating = isOneTapRunning,
                isHighContrast = settings.isHighContrastMode,
                isLargeTarget = settings.isLargeTouchTargetMode,
                onOneTapClick = { viewModel.triggerOneTapUpdate() },
                onVoiceAnnounce = {
                    val statusText = if (pendingCount > 0) {
                        "OmniSys Update Center: There are $pendingCount updates pending totaling ${String.format("%.1f", totalUpdateSizeMb)} megabytes. Tap 1-Tap Update to proceed."
                    } else {
                        "OmniSys Update Center: All installed applications and system frameworks are up to date and operating at peak smoothness."
                    }
                    viewModel.speakText(statusText)
                }
            )
        }

        // Quick System OS & Play Updates Action Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "SYSTEM & CORE MODULES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = LiquidCyan
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // System OS Update Card
                    LiquidGlassCard(
                        modifier = Modifier.weight(1f),
                        isHighContrast = settings.isHighContrastMode,
                        accentColor = LiquidViolet,
                        onClick = { viewModel.launchSystemUpdate() }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SystemUpdate,
                                    contentDescription = null,
                                    tint = LiquidViolet,
                                    modifier = Modifier.size(24.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "System Update",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                                color = TextWhite
                            )
                            Text(
                                text = telemetry?.specs?.securityPatch?.let { "Patch: $it" } ?: "Android OS Core",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                                color = TextMuted
                            )
                        }
                    }

                    // Google Play System Card
                    LiquidGlassCard(
                        modifier = Modifier.weight(1f),
                        isHighContrast = settings.isHighContrastMode,
                        accentColor = LiquidCyan,
                        onClick = { viewModel.launchGooglePlaySystemUpdate() }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = LiquidCyan,
                                    modifier = Modifier.size(24.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Play System",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                                color = TextWhite
                            )
                            Text(
                                text = "Google Mainline",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                                color = TextMuted
                            )
                        }
                    }

                    // Play Store All Updates Card
                    LiquidGlassCard(
                        modifier = Modifier.weight(1f),
                        isHighContrast = settings.isHighContrastMode,
                        accentColor = LiquidEmerald,
                        onClick = { viewModel.openPlayStoreAllUpdates() }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shop,
                                    contentDescription = null,
                                    tint = LiquidEmerald,
                                    modifier = Modifier.size(24.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Play Store",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                                color = TextWhite
                            )
                            Text(
                                text = "Store Updates",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }

        // Search and Filters Bar
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Search apps, packages, categories...", color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = LiquidCyan)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                                }
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LiquidCyan,
                            unfocusedBorderColor = Color(0x3300F0FF),
                            focusedContainerColor = DarkSurfaceGlass,
                            unfocusedContainerColor = DarkSurfaceGlass,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = if (settings.isLargeTouchTargetMode) 56.dp else 48.dp)
                            .testTag("app_search_field")
                    )

                    // Sort Menu
                    Box {
                        IconButton(
                            onClick = { sortMenuExpanded = true },
                            modifier = Modifier
                                .size(if (settings.isLargeTouchTargetMode) 56.dp else 48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(DarkSurfaceGlass)
                                .testTag("sort_menu_button")
                        ) {
                            Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort Apps", tint = LiquidCyan)
                        }

                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false },
                            modifier = Modifier.background(DarkSurfaceGlass)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Priority (Updates First)", color = TextWhite) },
                                onClick = {
                                    viewModel.setSortOrder(SortOrder.UPDATE_PRIORITY)
                                    sortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("App Name (A-Z)", color = TextWhite) },
                                onClick = {
                                    viewModel.setSortOrder(SortOrder.NAME_ASC)
                                    sortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("App Size (Largest First)", color = TextWhite) },
                                onClick = {
                                    viewModel.setSortOrder(SortOrder.SIZE_DESC)
                                    sortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Recently Updated", color = TextWhite) },
                                onClick = {
                                    viewModel.setSortOrder(SortOrder.RECENTLY_UPDATED)
                                    sortMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Filter Chips Row
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = activeFilter == AppFilter.ALL,
                        onClick = { viewModel.setFilter(AppFilter.ALL) },
                        label = { Text("All (${apps.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LiquidCyan,
                            selectedLabelColor = Color.Black,
                            containerColor = DarkSurfaceGlass,
                            labelColor = TextWhite
                        ),
                        modifier = Modifier.testTag("filter_all_chip")
                    )

                    FilterChip(
                        selected = activeFilter == AppFilter.UPDATES_PENDING,
                        onClick = { viewModel.setFilter(AppFilter.UPDATES_PENDING) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Updates ($pendingCount)")
                                if (pendingCount > 0) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(LiquidMagenta)
                                    )
                                }
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LiquidCyan,
                            selectedLabelColor = Color.Black,
                            containerColor = DarkSurfaceGlass,
                            labelColor = TextWhite
                        ),
                        modifier = Modifier.testTag("filter_updates_chip")
                    )

                    FilterChip(
                        selected = activeFilter == AppFilter.THIRD_PARTY,
                        onClick = { viewModel.setFilter(AppFilter.THIRD_PARTY) },
                        label = { Text("3rd Party") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LiquidCyan,
                            selectedLabelColor = Color.Black,
                            containerColor = DarkSurfaceGlass,
                            labelColor = TextWhite
                        )
                    )

                    FilterChip(
                        selected = activeFilter == AppFilter.SYSTEM_CORE,
                        onClick = { viewModel.setFilter(AppFilter.SYSTEM_CORE) },
                        label = { Text("System & GMS") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LiquidCyan,
                            selectedLabelColor = Color.Black,
                            containerColor = DarkSurfaceGlass,
                            labelColor = TextWhite
                        )
                    )
                }
            }
        }

        // Apps List Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "INSTALLED PACKAGES (${apps.size})",
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextMuted
                )

                IconButton(
                    onClick = { viewModel.refreshAllData() },
                    modifier = Modifier.size(32.dp).testTag("refresh_apps_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Packages",
                        tint = LiquidCyan
                    )
                }
            }
        }

        // App Items
        items(apps, key = { it.packageName }) { app ->
            AppItemCard(
                app = app,
                isHighContrast = settings.isHighContrastMode,
                isLargeTarget = settings.isLargeTouchTargetMode,
                onUpdateInPlayStore = { viewModel.openAppInPlayStore(app.packageName) },
                onLaunchApp = { viewModel.launchApp(app.packageName) },
                onOpenDetails = { viewModel.openAppDetails(app.packageName) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AppItemCard(
    app: AppItem,
    isHighContrast: Boolean,
    isLargeTarget: Boolean,
    onUpdateInPlayStore: () -> Unit,
    onLaunchApp: () -> Unit,
    onOpenDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val isOutdated = app.updateStatus == UpdateStatus.UPDATE_AVAILABLE || app.updateStatus == UpdateStatus.CRITICAL_SECURITY

    LiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_card_${app.packageName}"),
        isHighContrast = isHighContrast,
        accentColor = if (isOutdated) LiquidCyan else Color(0x3300F0FF)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Category / Icon Box
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isOutdated) DarkSurfaceGlassLighter else DarkSurfaceGlass
                        )
                        .border(
                            1.dp,
                            if (isOutdated) LiquidCyan.copy(alpha = 0.5f) else Color(0x22FFFFFF),
                            RoundedCornerShape(14.dp)
                        )
                ) {
                    Icon(
                        imageVector = if (app.isSystemApp) Icons.Default.Android else Icons.Default.Apps,
                        contentDescription = null,
                        tint = if (isOutdated) LiquidCyan else TextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = app.appName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isLargeTarget) 16.sp else 14.sp
                            ),
                            color = TextWhite,
                            maxLines = 1
                        )
                    }

                    Text(
                        text = app.packageName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                        color = TextSubtle,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                LiquidStatusBadge(status = app.updateStatus)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Version info row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x10FFFFFF))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "v${app.versionName}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        ),
                        color = if (isOutdated) TextMuted else LiquidEmerald
                    )
                    if (app.newVersionName != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = LiquidCyan,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "v${app.newVersionName}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            ),
                            color = LiquidCyan
                        )
                    }
                }

                Text(
                    text = "${String.format("%.1f", app.appSizeBytes / (1024.0 * 1024.0))} MB",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = TextMuted
                )
            }

            if (isOutdated && app.changelogSnippet.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• ${app.changelogSnippet}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = LiquidCyan.copy(alpha = 0.85f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isOutdated) {
                    LiquidPillButton(
                        text = "1-Tap Update",
                        onClick = onUpdateInPlayStore,
                        icon = Icons.Default.CloudDownload,
                        isPrimary = true,
                        isHighContrast = isHighContrast,
                        isLargeTarget = isLargeTarget,
                        accentColor = LiquidCyan,
                        modifier = Modifier.weight(1f)
                    )
                }

                LiquidPillButton(
                    text = "Launch",
                    onClick = onLaunchApp,
                    icon = Icons.Default.PlayArrow,
                    isPrimary = !isOutdated,
                    isHighContrast = isHighContrast,
                    isLargeTarget = isLargeTarget,
                    accentColor = LiquidEmerald,
                    modifier = Modifier.weight(1f)
                )

                LiquidPillButton(
                    text = "Settings",
                    onClick = onOpenDetails,
                    icon = Icons.Default.Settings,
                    isPrimary = false,
                    isHighContrast = isHighContrast,
                    isLargeTarget = isLargeTarget,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
