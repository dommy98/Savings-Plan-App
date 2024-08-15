package com.dominic.savingsplanapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goalName: String,
    val startDate: String,
    val endDate: String,
    val amountNeeded: Double
)