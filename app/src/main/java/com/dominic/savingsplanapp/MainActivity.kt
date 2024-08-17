package com.dominic.savingsplanapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dominic.savingsplanapp.databinding.ActivityMainBinding
import com.dominic.savingsplanapp.databinding.DialogAddGoalBinding
import com.dominic.savingsplanapp.room.Goal
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    private val goalViewModel: GoalViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GoalAdapter

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


        goalViewModel.fetchGoalsFromOnline()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions()
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
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun checkPermissions() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun downloadPdf() {
        val pdfContent = "Sample PDF Content" // Replace with actual PDF generation logic

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val file = File(getExternalFilesDir(null), "savings_plan.pdf")
            try {
                FileOutputStream(file).use { fos ->
                    OutputStreamWriter(fos).use { writer ->
                        writer.write(pdfContent)
                    }
                }
                // Notify user of success
            } catch (e: IOException) {
                e.printStackTrace()
                // Notify user of failure
            }
        } else {
            // Handle for older devices
        }
    }
}
