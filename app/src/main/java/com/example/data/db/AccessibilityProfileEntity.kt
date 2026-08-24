package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accessibility_profiles")
data class AccessibilityProfileEntity(
    @PrimaryKey
    val profileName: String,
    val description: String,
    val isAmoledPureBlack: Boolean,
    val isHighContrastMode: Boolean,
    val isLargeTouchTargetMode: Boolean,
    val isHapticFeedbackEnabled: Boolean,
    val isAudioCuesEnabled: Boolean,
    val ttsSpeechRate: Float,
    val isBuiltIn: Boolean = false,
    val lastUsedTimestamp: Long = System.currentTimeMillis()
)
