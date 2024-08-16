package com.dominic.savingsplanapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.O)
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

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, proceed with PDF generation if needed
            } else {
                // Permission denied, show a message or handle it
            }
        }

        // Request permission if not granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

                val savingsPlan = goalViewModel.calculateSavingsPlan(goal)
                PdfUtils.generatePdf(this, goal, savingsPlan)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
