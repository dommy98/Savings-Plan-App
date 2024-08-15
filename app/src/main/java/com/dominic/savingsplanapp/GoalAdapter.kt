package com.dominic.savingsplanapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dominic.savingsplanapp.databinding.ItemGoalBinding
import com.dominic.savingsplanapp.room.Goal

class GoalAdapter(private val viewModel: GoalViewModel) :
    ListAdapter<Goal, GoalAdapter.GoalViewHolder>(GoalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding: ItemGoalBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_goal,
            parent,
            false
        )
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

    class GoalViewHolder(private val binding: ItemGoalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(goal: Goal, viewModel: GoalViewModel) {
            binding.goal = goal
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    class GoalDiffCallback : DiffUtil.ItemCallback<Goal>() {
        override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem == newItem
        }
    }
}
