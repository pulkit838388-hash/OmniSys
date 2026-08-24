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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.AccessibilityProfileEntity
import com.example.model.AccessibilityShortcut
import com.example.model.InAppSettings
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidPillButton
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkSurfaceGlass
import com.example.ui.theme.DarkSurfaceGlassLighter
import com.example.ui.theme.GlassSpecular
import com.example.ui.theme.HighContrastBorder
import com.example.ui.theme.ImmersiveBlue
import com.example.ui.theme.ImmersiveBlueLight
import com.example.ui.theme.ImmersivePurple
import com.example.ui.theme.LiquidAmber
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidEmerald
import com.example.ui.theme.LiquidMagenta
import com.example.ui.theme.LiquidRose
import com.example.ui.theme.LiquidViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.TextWhite
import com.example.ui.theme.Zinc800
import com.example.ui.theme.Zinc900
import com.example.viewmodel.OmniViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AccessibilityHubScreen(
    viewModel: OmniViewModel,
    shortcuts: List<AccessibilityShortcut>,
    profiles: List<AccessibilityProfileEntity>,
    settings: InAppSettings,
    modifier: Modifier = Modifier
) {
    var showCustomProfileDialog by remember { mutableStateOf(false) }
    var customProfileName by remember { mutableStateOf("") }
    var customProfileDesc by remember { mutableStateOf("") }
    var ttsInputText by remember { mutableStateOf("OmniSys accessibility synthesizer operational. Everything is running smoothly.") }

    if (showCustomProfileDialog) {
        AlertDialog(
            onDismissRequest = { showCustomProfileDialog = false },
            containerColor = DarkSurfaceGlass,
            title = {
                Text(
                    text = "Save Accessibility Profile",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextWhite
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Save your current UI, font, haptic, and speech configuration to Room Database.",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = TextMuted
                    )
                    OutlinedTextField(
                        value = customProfileName,
                        onValueChange = { customProfileName = it },
                        label = { Text("Profile Name", color = LiquidCyan) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = LiquidCyan,
                            unfocusedBorderColor = Color(0x33FFFFFF)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                    )
                    OutlinedTextField(
                        value = customProfileDesc,
                        onValueChange = { customProfileDesc = it },
                        label = { Text("Description (Optional)", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = LiquidCyan,
                            unfocusedBorderColor = Color(0x33FFFFFF)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customProfileName.isNotBlank()) {
                            viewModel.saveCustomProfile(customProfileName, customProfileDesc)
                            showCustomProfileDialog = false
                            customProfileName = ""
                            customProfileDesc = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LiquidCyan, contentColor = Color.Black)
                ) {
                    Text("Save Profile", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomProfileDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
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

        // Header
        item {
            Column {
                Text(
                    text = "ALL-IN-ONE ACCESSIBILITY SUITE",
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = LiquidCyan
                )
                Text(
                    text = "System accessibility shortcuts, tactile controls & voice engine",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = TextMuted
                )
            }
        }

        // Immersive UI Quick Accessibility Deck
        item {
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shapeRadius = 24.dp,
                isHighContrast = settings.isHighContrastMode,
                accentColor = ImmersiveBlue
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "ACCESSIBILITY DECK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = ImmersiveBlueLight
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // High Contrast Quick Toggle
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.toggleHighContrastMode() }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (settings.isHighContrastMode) ImmersiveBlue else Zinc800
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Contrast,
                                    contentDescription = "Toggle High Contrast",
                                    tint = if (settings.isHighContrastMode) Color.Black else TextWhite,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Contrast",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = if (settings.isHighContrastMode) ImmersiveBlueLight else TextMuted
                            )
                        }

                        // Voice Cue Quick Toggle
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.toggleAudioCues() }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (settings.isAudioCuesEnabled) LiquidViolet else Zinc800
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Toggle Voice Cues",
                                    tint = if (settings.isAudioCuesEnabled) Color.Black else TextWhite,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Voice Cues",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = if (settings.isAudioCuesEnabled) ImmersivePurple else TextMuted
                            )
                        }

                        // OLED Black Quick Toggle
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.toggleAmoledPureBlack() }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (settings.isAmoledPureBlack) LiquidEmerald else Zinc800
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NightlightRound,
                                    contentDescription = "Toggle OLED Pure Black",
                                    tint = if (settings.isAmoledPureBlack) Color.Black else TextWhite,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "OLED Black",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = if (settings.isAmoledPureBlack) LiquidEmerald else TextMuted
                            )
                        }

                        // Large Touch Quick Toggle
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.toggleLargeTouchTargetMode() }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (settings.isLargeTouchTargetMode) LiquidMagenta else Zinc800
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TouchApp,
                                    contentDescription = "Toggle Large Touch Targets",
                                    tint = if (settings.isLargeTouchTargetMode) Color.Black else TextWhite,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Touch 64dp",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = if (settings.isLargeTouchTargetMode) LiquidMagenta else TextMuted
                            )
                        }
                    }
                }
            }
        }

        // Active Profile Pill & Switcher
        item {
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                isHighContrast = settings.isHighContrastMode,
                accentColor = LiquidViolet
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ACTIVE PROFILE",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = LiquidViolet
                            )
                            Text(
                                text = settings.activeProfileName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextWhite
                            )
                        }

                        LiquidPillButton(
                            text = "+ New Profile",
                            onClick = { showCustomProfileDialog = true },
                            isPrimary = false,
                            isHighContrast = settings.isHighContrastMode,
                            isLargeTarget = settings.isLargeTouchTargetMode,
                            accentColor = LiquidViolet,
                            modifier = Modifier.testTag("new_profile_button")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Profiles Horizontal Chips
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (profile in profiles) {
                            val isSelected = profile.profileName == settings.activeProfileName
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) LiquidViolet else DarkSurfaceGlassLighter)
                                    .border(
                                        1.dp,
                                        if (isSelected) LiquidCyan else Color(0x22FFFFFF),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { viewModel.applyProfile(profile) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .testTag("profile_chip_${profile.profileName}")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = TextWhite,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = profile.profileName,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 12.sp
                                        ),
                                        color = if (isSelected) TextWhite else TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // In-App Liquid Comfort & Tactile Controls
        item {
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                isHighContrast = settings.isHighContrastMode,
                accentColor = LiquidCyan
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "IN-APP TACTILE & VISUAL CONTROLS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = LiquidCyan
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    AccessibilityToggleRow(
                        icon = Icons.Default.NightlightRound,
                        title = "Super AMOLED Pure Black (0% Power)",
                        description = "Enforces pitch-black OLED pixels for maximum battery efficiency",
                        checked = settings.isAmoledPureBlack,
                        onCheckedChange = { viewModel.toggleAmoledPureBlack() },
                        accentColor = LiquidEmerald,
                        testTag = "toggle_amoled_switch"
                    )

                    AccessibilityToggleRow(
                        icon = Icons.Default.Contrast,
                        title = "High-Contrast Liquid Outlines",
                        description = "Bold neon outlines and maximum readability text styling",
                        checked = settings.isHighContrastMode,
                        onCheckedChange = { viewModel.toggleHighContrastMode() },
                        accentColor = LiquidCyan,
                        testTag = "toggle_contrast_switch"
                    )

                    AccessibilityToggleRow(
                        icon = Icons.Default.TouchApp,
                        title = "Large Touch Target Mode (64dp+)",
                        description = "Expands all button hitboxes and spacing for easier motor interaction",
                        checked = settings.isLargeTouchTargetMode,
                        onCheckedChange = { viewModel.toggleLargeTouchTargetMode() },
                        accentColor = LiquidMagenta,
                        testTag = "toggle_large_touch_switch"
                    )

                    AccessibilityToggleRow(
                        icon = Icons.Default.Vibration,
                        title = "Tactile Haptic Feedback",
                        description = "Vibrations and micro-ticks on every button press & state change",
                        checked = settings.isHapticFeedbackEnabled,
                        onCheckedChange = { viewModel.toggleHapticFeedback() },
                        accentColor = LiquidAmber,
                        testTag = "toggle_haptic_switch"
                    )

                    AccessibilityToggleRow(
                        icon = Icons.Default.VolumeUp,
                        title = "Voice Speech Cues (TTS)",
                        description = "Synthesizer vocalizes update completions and diagnostic metrics",
                        checked = settings.isAudioCuesEnabled,
                        onCheckedChange = { viewModel.toggleAudioCues() },
                        accentColor = LiquidViolet,
                        isLast = true,
                        testTag = "toggle_voice_switch"
                    )
                }
            }
        }

        // Text-To-Speech Synthesizer Studio
        item {
            LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                isHighContrast = settings.isHighContrastMode,
                accentColor = LiquidEmerald
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = LiquidEmerald,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TEXT-TO-SPEECH VOICE COMPANION",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = TextWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = ttsInputText,
                        onValueChange = { ttsInputText = it },
                        label = { Text("Text to Read Aloud", color = TextMuted) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = LiquidEmerald,
                            unfocusedBorderColor = Color(0x3300F0FF),
                            focusedContainerColor = DarkSurfaceGlass,
                            unfocusedContainerColor = DarkSurfaceGlass
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("tts_input_field")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Speech Rate Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Speech Rate", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp), color = TextMuted)
                            Text(text = "${String.format("%.1f", settings.ttsSpeechRate)}x", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp), color = LiquidEmerald)
                        }
                        Slider(
                            value = settings.ttsSpeechRate,
                            onValueChange = { viewModel.setTtsSpeechRate(it) },
                            valueRange = 0.5f..2.0f,
                            colors = SliderDefaults.colors(
                                thumbColor = LiquidEmerald,
                                activeTrackColor = LiquidEmerald,
                                inactiveTrackColor = Color(0x22FFFFFF)
                            )
                        )
                    }

                    // Speech Pitch Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Voice Pitch", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp), color = TextMuted)
                            Text(text = "${String.format("%.1f", settings.ttsPitch)}x", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp), color = LiquidCyan)
                        }
                        Slider(
                            value = settings.ttsPitch,
                            onValueChange = { viewModel.setTtsPitch(it) },
                            valueRange = 0.5f..1.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = LiquidCyan,
                                activeTrackColor = LiquidCyan,
                                inactiveTrackColor = Color(0x22FFFFFF)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LiquidPillButton(
                            text = "Speak Text",
                            onClick = { viewModel.speakText(ttsInputText) },
                            icon = Icons.Default.PlayArrow,
                            isPrimary = true,
                            isHighContrast = settings.isHighContrastMode,
                            isLargeTarget = settings.isLargeTouchTargetMode,
                            accentColor = LiquidEmerald,
                            modifier = Modifier.weight(1f),
                            testTag = "speak_custom_text_button"
                        )

                        LiquidPillButton(
                            text = "Stop",
                            onClick = { viewModel.stopSpeaking() },
                            icon = Icons.Default.Stop,
                            isPrimary = false,
                            isHighContrast = settings.isHighContrastMode,
                            isLargeTarget = settings.isLargeTouchTargetMode,
                            modifier = Modifier.weight(0.6f)
                        )
                    }
                }
            }
        }

        // Master Android System Accessibility Portals (Direct 1-Tap launch)
        item {
            Text(
                text = "SYSTEM ACCESSIBILITY SERVICES",
                style = MaterialTheme.typography.labelMedium.copy(
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = LiquidCyan
            )
        }

        items(shortcuts, key = { it.id }) { shortcut ->
            AccessibilityShortcutCard(
                shortcut = shortcut,
                isHighContrast = settings.isHighContrastMode,
                isLargeTarget = settings.isLargeTouchTargetMode,
                onClick = { viewModel.launchAccessibilityShortcut(shortcut) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AccessibilityToggleRow(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    accentColor: Color,
    isLast: Boolean = false,
    testTag: String = "accessibility_toggle"
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x12FFFFFF))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = TextWhite
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    checkedTrackColor = accentColor,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = Color(0x22FFFFFF)
                ),
                modifier = Modifier.testTag(testTag)
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
fun AccessibilityShortcutCard(
    shortcut: AccessibilityShortcut,
    isHighContrast: Boolean,
    isLargeTarget: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (shortcut.id) {
        "acc_main" -> Icons.Default.AccessibilityNew
        "acc_display_size" -> Icons.Default.FormatSize
        "acc_captioning" -> Icons.Default.ClosedCaption
        "acc_sound" -> Icons.Default.Hearing
        "acc_battery_saver" -> Icons.Default.NightlightRound
        "acc_app_notification" -> Icons.Default.NotificationsActive
        "acc_storage" -> Icons.Default.Storage
        "acc_dev_options" -> Icons.Default.Animation
        "acc_locale" -> Icons.Default.Translate
        else -> Icons.Default.Settings
    }

    LiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("acc_shortcut_${shortcut.id}"),
        isHighContrast = isHighContrast,
        accentColor = LiquidCyan,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurfaceGlassLighter)
                    .border(1.dp, Color(0x3300F0FF), RoundedCornerShape(14.dp))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LiquidCyan,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = shortcut.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isLargeTarget) 16.sp else 14.sp
                    ),
                    color = TextWhite
                )
                Text(
                    text = shortcut.description,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0x12FFFFFF))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Open Setting",
                    tint = LiquidCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
