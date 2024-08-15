package com.dominic.savingsplanapp.room

import androidx.lifecycle.LiveData

class GoalRepository(private val goalDao: GoalDao) {
    suspend fun insert(goal: Goal) = goalDao.insert(goal)
    suspend fun update(goal: Goal) = goalDao.update(goal)
    suspend fun getGoalById(id: Long) = goalDao.getGoalById(id)
    fun getAllGoals() =goalDao.getAllGoals()
    suspend fun deleteGoalById(id: Long) = goalDao.deleteGoalById(id)
}