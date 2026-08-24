package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccessibilityProfileDao {
    @Query("SELECT * FROM accessibility_profiles ORDER BY lastUsedTimestamp DESC")
    fun getAllProfiles(): Flow<List<AccessibilityProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: AccessibilityProfileEntity)

    @Query("DELETE FROM accessibility_profiles WHERE profileName = :name AND isBuiltIn = 0")
    suspend fun deleteCustomProfile(name: String)

    @Query("SELECT * FROM accessibility_profiles WHERE profileName = :name LIMIT 1")
    suspend fun getProfileByName(name: String): AccessibilityProfileEntity?
}
