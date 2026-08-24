package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [LogEntity::class, AccessibilityProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class OmniDatabase : RoomDatabase() {
    abstract fun logDao(): AppLogDao
    abstract fun accessibilityProfileDao(): AccessibilityProfileDao

    companion object {
        @Volatile
        private var INSTANCE: OmniDatabase? = null

        fun getDatabase(context: Context): OmniDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OmniDatabase::class.java,
                    "omnisys_database.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.let { database ->
                                populateDefaultProfiles(database.accessibilityProfileDao())
                                populateInitialLog(database.logDao())
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateDefaultProfiles(dao: AccessibilityProfileDao) {
            val defaults = listOf(
                AccessibilityProfileEntity(
                    profileName = "Balanced Default",
                    description = "Standard Super AMOLED dark glass with smooth haptic feedback and balanced typography.",
                    isAmoledPureBlack = true,
                    isHighContrastMode = false,
                    isLargeTouchTargetMode = false,
                    isHapticFeedbackEnabled = true,
                    isAudioCuesEnabled = true,
                    ttsSpeechRate = 1.0f,
                    isBuiltIn = true
                ),
                AccessibilityProfileEntity(
                    profileName = "Vision Enhanced",
                    description = "Maximized text contrast, bold borders, larger fonts, and fast TTS voice readout.",
                    isAmoledPureBlack = true,
                    isHighContrastMode = true,
                    isLargeTouchTargetMode = false,
                    isHapticFeedbackEnabled = true,
                    isAudioCuesEnabled = true,
                    ttsSpeechRate = 0.9f,
                    isBuiltIn = true
                ),
                AccessibilityProfileEntity(
                    profileName = "Motor Easy-Touch",
                    description = "Extra-large 64dp touch targets, expanded button gaps, and prominent tactile haptics.",
                    isAmoledPureBlack = true,
                    isHighContrastMode = false,
                    isLargeTouchTargetMode = true,
                    isHapticFeedbackEnabled = true,
                    isAudioCuesEnabled = true,
                    ttsSpeechRate = 1.0f,
                    isBuiltIn = true
                ),
                AccessibilityProfileEntity(
                    profileName = "Max OLED Battery Saver",
                    description = "100% pure pitch black pixels with zero ambient glow for minimal power draw.",
                    isAmoledPureBlack = true,
                    isHighContrastMode = false,
                    isLargeTouchTargetMode = false,
                    isHapticFeedbackEnabled = false,
                    isAudioCuesEnabled = false,
                    ttsSpeechRate = 1.0f,
                    isBuiltIn = true
                )
            )
            for (p in defaults) {
                dao.insertProfile(p)
            }
        }

        private suspend fun populateInitialLog(dao: AppLogDao) {
            dao.insertLog(
                LogEntity(
                    actionType = "SYSTEM_INITIALIZE",
                    summary = "OmniSys Core Engine Initialized",
                    details = "Super AMOLED Liquid Utility Hub ready. System & package scanner operational.",
                    itemsAffectedCount = 1,
                    ramFreedMb = 0L,
                    isSuccess = true
                )
            )
        }
    }
}
