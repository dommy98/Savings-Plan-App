package com.dominic.savingsplanapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: Goal)

    @Query("SELECT * FROM goals")
    fun getAllGoals(): LiveData<List<Goal>>

    @Update
    suspend fun update(goal: Goal)

    @Query("SELECT * FROM goals WHERE id = :id")
    suspend fun getGoalById(id: Long): Goal?



    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteGoalById(id: Long)
}