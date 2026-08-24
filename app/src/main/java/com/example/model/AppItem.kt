package com.example.model

import android.graphics.drawable.Drawable

enum class AppType {
    USER_APP,
    SYSTEM_FRAMEWORK,
    PLAY_SERVICES
}

enum class UpdateStatus {
    UPDATE_AVAILABLE,
    UP_TO_DATE,
    CHECKING,
    CRITICAL_SECURITY
}

data class AppItem(
    val packageName: String,
    val appName: String,
    val versionName: String,
    val versionCode: Long,
    val targetSdk: Int,
    val minSdk: Int,
    val isSystemApp: Boolean,
    val installTimeMillis: Long,
    val lastUpdateTimeMillis: Long,
    val appSizeBytes: Long,
    val appType: AppType,
    val updateStatus: UpdateStatus,
    val newVersionName: String? = null,
    val updateSizeBytes: Long = 0L,
    val changelogSnippet: String = "",
    val category: String = "App",
    val isSelectedForUpdate: Boolean = true
)
