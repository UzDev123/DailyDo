package com.sultonuzdev.dailydo.ui.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.dailydo.domain.repository.EfficiencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import javax.inject.Inject

/**
 * Timeframe for viewing efficiency reports
 */
enum class Timeframe(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")
}

/**
 * ViewModel for the Reports screen
 */
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val efficiencyRepository: EfficiencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    init {
        loadDailyEfficiency()
    }

    /**
     * Change the timeframe for the reports
     */
    fun changeTimeframe(timeframe: Timeframe) {
        when (timeframe) {
            Timeframe.DAILY -> loadDailyEfficiency()
            Timeframe.WEEKLY -> loadWeeklyEfficiency()
            Timeframe.MONTHLY -> loadMonthlyEfficiency()
            Timeframe.YEARLY -> loadYearlyEfficiency()
        }
    }

    /**
     * Load daily efficiency
     */
    private fun loadDailyEfficiency() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val today = LocalDate.now()
            efficiencyRepository.getDailyEfficiency(today).collect { efficiency ->
                if (efficiency == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasNoData = true
                        )
                    }
                } else {
                    val completionRate = if (efficiency.totalTaskCount > 0) {
                        (efficiency.completedTaskCount.toFloat() / efficiency.totalTaskCount) * 100
                    } else 0f

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasNoData = false,
                            dailyEfficiency = efficiency.efficiencyScore,
                            dailyTaskCompletionRate = completionRate
                        )
                    }
                }
            }
        }
    }

    /**
     * Load weekly efficiency
     */
    private fun loadWeeklyEfficiency() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val today = LocalDate.now()
            // Get the first day of the current week (considering Monday as first day)
            val weekStart = today.minusDays((today.dayOfWeek.value - 1).toLong())

            efficiencyRepository.getWeeklyEfficiency(weekStart).collect { weeklyEfficiency ->
                if (weeklyEfficiency == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasNoData = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasNoData = false,
                            weeklyEfficiency = weeklyEfficiency.averageEfficiency,
                            weeklyTaskCompletionRate = weeklyEfficiency.taskCompletionRate * 100
                        )
                    }
                }
            }
        }
    }

    /**
     * Load monthly efficiency
     */
    private fun loadMonthlyEfficiency() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentMonth = YearMonth.now()

            efficiencyRepository.getMonthlyEfficiency(currentMonth).collect { monthlyEfficiency ->
                if (monthlyEfficiency == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasNoData = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasNoData = false,
                            monthlyEfficiency = monthlyEfficiency.averageEfficiency,
                            monthlyTaskCompletionRate = monthlyEfficiency.taskCompletionRate * 100
                        )
                    }
                }
            }
        }
    }

    /**
     * Load yearly efficiency
     */
    private fun loadYearlyEfficiency() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentYear = LocalDate.now().year

            efficiencyRepository.getYearlyEfficiency(currentYear).collect { yearlyEfficiency ->
                if (yearlyEfficiency == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasNoData = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            hasNoData = false,
                            yearlyEfficiency = yearlyEfficiency.averageEfficiency,
                            yearlyTaskCompletionRate = yearlyEfficiency.taskCompletionRate * 100
                        )
                    }
                }
            }
        }
    }
}

/**
 * UI state for the Reports screen
 */
data class ReportsUiState(
    val isLoading: Boolean = false,
    val hasNoData: Boolean = false,

    // Daily efficiency
    val dailyEfficiency: Float = 0f,
    val dailyTaskCompletionRate: Float = 0f,

    // Weekly efficiency
    val weeklyEfficiency: Float = 0f,
    val weeklyTaskCompletionRate: Float = 0f,

    // Monthly efficiency
    val monthlyEfficiency: Float = 0f,
    val monthlyTaskCompletionRate: Float = 0f,

    // Yearly efficiency
    val yearlyEfficiency: Float = 0f,
    val yearlyTaskCompletionRate: Float = 0f
)