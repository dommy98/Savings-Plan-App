package com.dominic.savingsplanapp

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dominic.savingsplanapp.room.AppDatabase
import com.dominic.savingsplanapp.room.Goal
import com.dominic.savingsplanapp.room.GoalRepository
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate

class GoalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GoalRepository = GoalRepository(
        AppDatabase.getDatabase(application).goalDao()
    )

    val allGoals: LiveData<List<Goal>> = repository.getAllGoals()

    private val _dailySavings = MutableLiveData<Double>()
    val dailySavings: LiveData<Double> get() = _dailySavings

    private val _weeklySavings = MutableLiveData<Double>()
    val weeklySavings: LiveData<Double> get() = _weeklySavings

    private val _monthlySavings = MutableLiveData<Double>()
    val monthlySavings: LiveData<Double> get() = _monthlySavings

    private val _annualSavings = MutableLiveData<Double>()
    val annualSavings: LiveData<Double> get() = _annualSavings

    fun insertGoal(goal: Goal) = viewModelScope.launch {
        repository.insert(goal)
    }

    fun updateGoal(goal: Goal) = viewModelScope.launch {
        repository.update(goal)
    }

    fun deleteGoalById(id: Long) = viewModelScope.launch {
        repository.deleteGoalById(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateSavingsPlan(goal: Goal): Map<String, Double> {
        val days = daysBetween(goal.startDate, goal.endDate)

        // Use a safe call with the Elvis operator to handle potential null values
        val amountNeeded = goal.amountNeeded ?: return emptyMap()

        val dailySavings = amountNeeded / days
        val weeklySavings = dailySavings * 7
        val monthlySavings = dailySavings * 30
        val annualSavings = dailySavings * 365

        return mapOf(
            "daily" to dailySavings,
            "weekly" to weeklySavings,
            "monthly" to monthlySavings,
            "annual" to annualSavings
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysBetween(startDate: String, endDate: String): Long {
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        return Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays()
    }
}

