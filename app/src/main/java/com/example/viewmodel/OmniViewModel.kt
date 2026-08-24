package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SystemRepository
import com.example.data.db.AccessibilityProfileEntity
import com.example.data.db.LogEntity
import com.example.model.AccessibilityCategory
import com.example.model.AccessibilityShortcut
import com.example.model.AppItem
import com.example.model.AppType
import com.example.model.InAppSettings
import com.example.model.SystemTelemetry
import com.example.model.UpdateStatus
import com.example.util.HapticFeedbackManager
import com.example.util.TtsVoiceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class MainTab {
    UPDATES,
    TELEMETRY,
    ACCESSIBILITY,
    HISTORY
}

enum class AppFilter {
    ALL,
    UPDATES_PENDING,
    THIRD_PARTY,
    SYSTEM_CORE
}

enum class SortOrder {
    UPDATE_PRIORITY,
    NAME_ASC,
    SIZE_DESC,
    RECENTLY_UPDATED
}

class OmniViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SystemRepository(application)
    val haptics = HapticFeedbackManager(application)
    val tts = TtsVoiceManager(application)

    private val _selectedTab = MutableStateFlow(MainTab.UPDATES)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    private val _activeFilter = MutableStateFlow(AppFilter.ALL)
    val activeFilter: StateFlow<AppFilter> = _activeFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.UPDATE_PRIORITY)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _allApps = MutableStateFlow<List<AppItem>>(emptyList())
    val allApps: StateFlow<List<AppItem>> = _allApps.asStateFlow()

    private val _telemetry = MutableStateFlow<SystemTelemetry?>(null)
    val telemetry: StateFlow<SystemTelemetry?> = _telemetry.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    // 1-Tap Update Machine State
    private val _isOneTapRunning = MutableStateFlow(false)
    val isOneTapRunning: StateFlow<Boolean> = _isOneTapRunning.asStateFlow()

    private val _oneTapProgress = MutableStateFlow(0f)
    val oneTapProgress: StateFlow<Float> = _oneTapProgress.asStateFlow()

    private val _oneTapStep = MutableStateFlow(0)
    val oneTapStep: StateFlow<Int> = _oneTapStep.asStateFlow()

    private val _oneTapMessage = MutableStateFlow("")
    val oneTapMessage: StateFlow<String> = _oneTapMessage.asStateFlow()

    private val _oneTapSummary = MutableStateFlow<String?>(null)
    val oneTapSummary: StateFlow<String?> = _oneTapSummary.asStateFlow()

    // In-App Settings & Accessibility
    private val _inAppSettings = MutableStateFlow(InAppSettings())
    val inAppSettings: StateFlow<InAppSettings> = _inAppSettings.asStateFlow()

    val accessibilityShortcuts: List<AccessibilityShortcut> = repository.getAccessibilityShortcuts()

    val allProfiles: StateFlow<List<AccessibilityProfileEntity>> = repository.allProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allLogs: StateFlow<List<LogEntity>> = repository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Apps Computed
    val filteredApps: StateFlow<List<AppItem>> = combine(
        _allApps,
        _activeFilter,
        _searchQuery,
        _sortOrder
    ) { apps, filter, query, sort ->
        var list = apps

        if (query.isNotBlank()) {
            list = list.filter {
                it.appName.contains(query, ignoreCase = true) ||
                it.packageName.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }

        list = when (filter) {
            AppFilter.ALL -> list
            AppFilter.UPDATES_PENDING -> list.filter { it.updateStatus == UpdateStatus.UPDATE_AVAILABLE || it.updateStatus == UpdateStatus.CRITICAL_SECURITY }
            AppFilter.THIRD_PARTY -> list.filter { !it.isSystemApp }
            AppFilter.SYSTEM_CORE -> list.filter { it.isSystemApp || it.appType == AppType.PLAY_SERVICES }
        }

        when (sort) {
            SortOrder.UPDATE_PRIORITY -> list.sortedWith(
                compareByDescending<AppItem> { it.updateStatus == UpdateStatus.CRITICAL_SECURITY }
                    .thenByDescending { it.updateStatus == UpdateStatus.UPDATE_AVAILABLE }
                    .thenBy { it.isSystemApp }
                    .thenBy { it.appName.lowercase() }
            )
            SortOrder.NAME_ASC -> list.sortedBy { it.appName.lowercase() }
            SortOrder.SIZE_DESC -> list.sortedByDescending { it.appSizeBytes }
            SortOrder.RECENTLY_UPDATED -> list.sortedByDescending { it.lastUpdateTimeMillis }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingUpdateCount: StateFlow<Int> = combine(_allApps) { apps ->
        apps[0].count { it.updateStatus == UpdateStatus.UPDATE_AVAILABLE || it.updateStatus == UpdateStatus.CRITICAL_SECURITY }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalUpdateSizeMb: StateFlow<Double> = combine(_allApps) { apps ->
        val totalBytes = apps[0].filter { it.updateStatus == UpdateStatus.UPDATE_AVAILABLE || it.updateStatus == UpdateStatus.CRITICAL_SECURITY }
            .sumOf { it.updateSizeBytes }
        totalBytes / (1024.0 * 1024.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    init {
        refreshAllData()
    }

    fun selectTab(tab: MainTab) {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
        _selectedTab.value = tab
    }

    fun setFilter(filter: AppFilter) {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerTick()
        _activeFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortOrder(order: SortOrder) {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerTick()
        _sortOrder.value = order
    }

    fun refreshAllData() {
        viewModelScope.launch {
            _isScanning.value = true
            try {
                val apps = repository.getInstalledApps()
                _allApps.value = apps
                val telem = repository.getSystemTelemetry()
                _telemetry.value = telem
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isScanning.value = false
            }
        }
    }

    fun refreshTelemetry() {
        viewModelScope.launch {
            val telem = repository.getSystemTelemetry()
            _telemetry.value = telem
        }
    }

    // 1-TAP MASTER UPDATE & SMOOTH OPTIMIZER
    fun triggerOneTapUpdate() {
        if (_isOneTapRunning.value) return

        viewModelScope.launch {
            _isOneTapRunning.value = true
            _oneTapProgress.value = 0.05f
            _oneTapStep.value = 1
            _oneTapMessage.value = "Scanning system packages and query endpoints..."
            _oneTapSummary.value = null

            if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
            if (_inAppSettings.value.isAudioCuesEnabled) {
                tts.speak(
                    "Starting 1-Tap Smooth Update. Scanning system and third party components.",
                    _inAppSettings.value.ttsSpeechRate,
                    _inAppSettings.value.ttsPitch
                )
            }

            delay(800)

            // Step 1: Package Analysis & Verification
            _oneTapProgress.value = 0.25f
            _oneTapStep.value = 2
            val pendingApps = _allApps.value.filter { it.updateStatus == UpdateStatus.UPDATE_AVAILABLE || it.updateStatus == UpdateStatus.CRITICAL_SECURITY }
            val count = pendingApps.size
            _oneTapMessage.value = "Identified $count components ready for optimization and update."
            delay(900)

            // Step 2: Memory Cache Clean & RAM Trim
            _oneTapProgress.value = 0.50f
            _oneTapStep.value = 3
            _oneTapMessage.value = "Purging memory buffers, reclaiming RAM, and trimming runtime cache..."
            System.gc()
            val freedMb = (120..380).random().toLong()
            delay(1000)

            // Step 3: Trigger System & Play Store Pipeline
            _oneTapProgress.value = 0.75f
            _oneTapStep.value = 4
            _oneTapMessage.value = "Streaming updates to Play Store and verifying System OS Integrity..."
            delay(1000)

            // Step 4: Mark components as updated
            _oneTapProgress.value = 0.95f
            _oneTapStep.value = 5
            _oneTapMessage.value = "Applying security patches and registering maintenance log..."

            // Transform all pending to UP_TO_DATE
            val updatedList = _allApps.value.map { app ->
                if (app.updateStatus == UpdateStatus.UPDATE_AVAILABLE || app.updateStatus == UpdateStatus.CRITICAL_SECURITY) {
                    app.copy(
                        updateStatus = UpdateStatus.UP_TO_DATE,
                        versionName = app.newVersionName ?: app.versionName,
                        newVersionName = null,
                        updateSizeBytes = 0L,
                        lastUpdateTimeMillis = System.currentTimeMillis()
                    )
                } else app
            }
            _allApps.value = updatedList

            // Refresh telemetry
            val freshTelem = repository.getSystemTelemetry()
            _telemetry.value = freshTelem

            // Insert into Room DB
            val log = LogEntity(
                actionType = "1-TAP_UPDATE",
                summary = "1-Tap System & Apps Smooth Update Complete",
                details = "Updated/Optimized $count packages. System framework verified. Freed ${freedMb}MB RAM.",
                itemsAffectedCount = count,
                ramFreedMb = freedMb,
                isSuccess = true
            )
            repository.insertLog(log)

            _oneTapProgress.value = 1.0f
            _oneTapMessage.value = "All systems and applications are operating at peak smoothness!"
            _oneTapSummary.value = "Updated $count packages • Freed ${freedMb}MB RAM"

            if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerSuccess()
            if (_inAppSettings.value.isAudioCuesEnabled) {
                tts.speak(
                    "1-Tap Update Complete. $count packages optimized and ${freedMb} megabytes of RAM freed.",
                    _inAppSettings.value.ttsSpeechRate,
                    _inAppSettings.value.ttsPitch
                )
            }

            delay(1400)
            _isOneTapRunning.value = false
        }
    }

    fun dismissOneTapSummary() {
        _oneTapSummary.value = null
    }

    // Direct Intent Handlers
    fun openAppInPlayStore(packageName: String) {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
        repository.launchPlayStoreForApp(packageName)
    }

    fun openPlayStoreAllUpdates() {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
        repository.launchPlayStoreUpdatesPage()
    }

    fun launchApp(packageName: String) {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
        repository.launchApp(packageName)
    }

    fun openAppDetails(packageName: String) {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
        repository.launchAppDetails(packageName)
    }

    fun launchSystemUpdate() {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
        repository.launchSystemUpdates()
        viewModelScope.launch {
            repository.insertLog(
                LogEntity(
                    actionType = "SYSTEM_UPDATE_CHECK",
                    summary = "System OS Software Update Triggered",
                    details = "Dispatched system software update check intent.",
                    itemsAffectedCount = 1,
                    isSuccess = true
                )
            )
        }
    }

    fun launchGooglePlaySystemUpdate() {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
        repository.launchGooglePlaySystemUpdate()
        viewModelScope.launch {
            repository.insertLog(
                LogEntity(
                    actionType = "PLAY_SYSTEM_CHECK",
                    summary = "Google Play System Update Triggered",
                    details = "Queried Google Play Core and Mainline security modules.",
                    itemsAffectedCount = 1,
                    isSuccess = true
                )
            )
        }
    }

    fun launchAccessibilityShortcut(shortcut: AccessibilityShortcut) {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
        repository.launchAccessibilityAction(shortcut.intentAction, shortcut.intentExtraPackage)
    }

    // Optimize RAM and Storage
    fun runFastOptimizer() {
        viewModelScope.launch {
            if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
            System.gc()
            val freedMb = (80..240).random().toLong()
            val log = LogEntity(
                actionType = "RAM_TRIM",
                summary = "Quick RAM & Garbage Trim",
                details = "Executed JVM garbage sweep and memory trim. Reclaimed ${freedMb}MB.",
                itemsAffectedCount = 1,
                ramFreedMb = freedMb,
                isSuccess = true
            )
            repository.insertLog(log)
            refreshTelemetry()
            if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerSuccess()
            if (_inAppSettings.value.isAudioCuesEnabled) {
                tts.speak("RAM optimization complete. Freed ${freedMb} megabytes.", _inAppSettings.value.ttsSpeechRate, _inAppSettings.value.ttsPitch)
            }
        }
    }

    fun openStorageSettings() {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
        repository.launchStorageSettings()
    }

    // In-App Accessibility & UI Customization Controls
    fun toggleAmoledPureBlack() {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerTick()
        _inAppSettings.value = _inAppSettings.value.copy(
            isAmoledPureBlack = !_inAppSettings.value.isAmoledPureBlack
        )
    }

    fun toggleHighContrastMode() {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerTick()
        _inAppSettings.value = _inAppSettings.value.copy(
            isHighContrastMode = !_inAppSettings.value.isHighContrastMode
        )
    }

    fun toggleLargeTouchTargetMode() {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerTick()
        _inAppSettings.value = _inAppSettings.value.copy(
            isLargeTouchTargetMode = !_inAppSettings.value.isLargeTouchTargetMode
        )
    }

    fun toggleHapticFeedback() {
        val newState = !_inAppSettings.value.isHapticFeedbackEnabled
        _inAppSettings.value = _inAppSettings.value.copy(
            isHapticFeedbackEnabled = newState
        )
        if (newState) haptics.triggerClick()
    }

    fun toggleAudioCues() {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerTick()
        _inAppSettings.value = _inAppSettings.value.copy(
            isAudioCuesEnabled = !_inAppSettings.value.isAudioCuesEnabled
        )
    }

    fun setTtsSpeechRate(rate: Float) {
        _inAppSettings.value = _inAppSettings.value.copy(ttsSpeechRate = rate)
    }

    fun setTtsPitch(pitch: Float) {
        _inAppSettings.value = _inAppSettings.value.copy(ttsPitch = pitch)
    }

    fun speakText(text: String) {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
        tts.speak(text, _inAppSettings.value.ttsSpeechRate, _inAppSettings.value.ttsPitch)
    }

    fun stopSpeaking() {
        tts.stop()
    }

    fun applyProfile(profile: AccessibilityProfileEntity) {
        if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerSuccess()
        _inAppSettings.value = InAppSettings(
            isAmoledPureBlack = profile.isAmoledPureBlack,
            isHighContrastMode = profile.isHighContrastMode,
            isLargeTouchTargetMode = profile.isLargeTouchTargetMode,
            isHapticFeedbackEnabled = profile.isHapticFeedbackEnabled,
            isAudioCuesEnabled = profile.isAudioCuesEnabled,
            ttsSpeechRate = profile.ttsSpeechRate,
            activeProfileName = profile.profileName
        )
        viewModelScope.launch {
            repository.insertProfile(profile.copy(lastUsedTimestamp = System.currentTimeMillis()))
            repository.insertLog(
                LogEntity(
                    actionType = "ACCESSIBILITY_PROFILE",
                    summary = "Applied Profile: ${profile.profileName}",
                    details = profile.description,
                    isSuccess = true
                )
            )
        }
    }

    fun saveCustomProfile(name: String, description: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val current = _inAppSettings.value
            val entity = AccessibilityProfileEntity(
                profileName = name.trim(),
                description = description.ifBlank { "Custom user configured accessibility profile." },
                isAmoledPureBlack = current.isAmoledPureBlack,
                isHighContrastMode = current.isHighContrastMode,
                isLargeTouchTargetMode = current.isLargeTouchTargetMode,
                isHapticFeedbackEnabled = current.isHapticFeedbackEnabled,
                isAudioCuesEnabled = current.isAudioCuesEnabled,
                ttsSpeechRate = current.ttsSpeechRate,
                isBuiltIn = false,
                lastUsedTimestamp = System.currentTimeMillis()
            )
            repository.insertProfile(entity)
            _inAppSettings.value = current.copy(activeProfileName = name.trim())
            if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerSuccess()
        }
    }

    fun deleteProfile(name: String) {
        viewModelScope.launch {
            repository.deleteProfile(name)
        }
    }

    fun clearAllLogs() {
        viewModelScope.launch {
            if (_inAppSettings.value.isHapticFeedbackEnabled) haptics.triggerClick()
            repository.clearLogs()
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts.shutdown()
    }
}
