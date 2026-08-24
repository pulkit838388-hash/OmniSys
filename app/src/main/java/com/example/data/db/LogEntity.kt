package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance_logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val actionType: String, // "1-TAP_UPDATE", "RAM_TRIM", "CACHE_CLEAN", "ACCESSIBILITY_PROFILE", "SYSTEM_CHECK"
    val summary: String,
    val details: String,
    val itemsAffectedCount: Int = 0,
    val ramFreedMb: Long = 0L,
    val isSuccess: Boolean = true
)
