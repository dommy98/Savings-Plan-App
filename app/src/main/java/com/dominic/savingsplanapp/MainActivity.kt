package com.dominic.savingsplanapp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dominic.savingsplanapp.databinding.ActivityMainBinding
import com.dominic.savingsplanapp.databinding.DialogAddGoalBinding
import com.dominic.savingsplanapp.room.Goal
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private val goalViewModel: GoalViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GoalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = goalViewModel
        binding.lifecycleOwner = this

        adapter = GoalAdapter(goalViewModel)
        binding.goalsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.goalsRecyclerView.adapter = adapter

        goalViewModel.allGoals.observe(this) { goals ->
            adapter.submitList(goals)
        }

        binding.addGoalButton.setOnClickListener {
            showAddGoalDialog()
        }
    }

    private fun showAddGoalDialog() {
        val dialogBinding: DialogAddGoalBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_add_goal,
            null,
            false
        )

        MaterialAlertDialogBuilder(this)
            .setTitle("Add Goal")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val goalName = dialogBinding.goalNameEditText.text.toString()
                val amount = dialogBinding.amountEditText.text.toString().toDouble()
                val startDate = dialogBinding.startDateEditText.text.toString()
                val endDate = dialogBinding.endDateEditText.text.toString()

                val goal = Goal(goalName = goalName, amountNeeded = amount, startDate = startDate, endDate = endDate)
                goalViewModel.insertGoal(goal)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
