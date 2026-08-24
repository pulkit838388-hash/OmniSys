package com.example.model

enum class AccessibilityCategory {
    VISION,
    HEARING,
    MOTOR_INTERACTION,
    SYSTEM_DISPLAY,
    AUDIO_CAPTION,
    IN_APP_COMFORT
}

data class AccessibilityShortcut(
    val id: String,
    val title: String,
    val description: String,
    val category: AccessibilityCategory,
    val intentAction: String,
    val intentExtraPackage: String? = null,
    val isDirectAvailable: Boolean = true,
    val iconName: String
)

data class InAppSettings(
    val isAmoledPureBlack: Boolean = true,
    val isHighContrastMode: Boolean = false,
    val isLargeTouchTargetMode: Boolean = false,
    val isHapticFeedbackEnabled: Boolean = true,
    val isAudioCuesEnabled: Boolean = true,
    val ttsSpeechRate: Float = 1.0f,
    val ttsPitch: Float = 1.0f,
    val activeProfileName: String = "Balanced Default"
)
