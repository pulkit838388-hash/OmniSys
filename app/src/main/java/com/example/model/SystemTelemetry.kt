package com.example.model

data class RamInfo(
    val totalBytes: Long,
    val availableBytes: Long,
    val usedBytes: Long,
    val usedPercentage: Float,
    val isLowMemory: Boolean
)

data class StorageInfo(
    val totalBytes: Long,
    val freeBytes: Long,
    val usedBytes: Long,
    val usedPercentage: Float
)

data class BatteryInfo(
    val levelPercent: Int,
    val isCharging: Boolean,
    val temperatureCelsius: Float,
    val voltageMv: Int,
    val health: String,
    val technology: String,
    val powerSource: String
)

data class DeviceSpecs(
    val deviceModel: String,
    val manufacturer: String,
    val androidVersion: String,
    val sdkInt: Int,
    val securityPatch: String,
    val buildId: String,
    val boardArchitecture: String,
    val cpuCores: Int,
    val displayResolution: String,
    val refreshRateHz: Float,
    val densityDpi: Int
)

data class SystemTelemetry(
    val ram: RamInfo,
    val storage: StorageInfo,
    val battery: BatteryInfo,
    val specs: DeviceSpecs,
    val googlePlayServicesVersion: String,
    val playSystemUpdateDate: String,
    val lastOptimizedTimestamp: Long
)
