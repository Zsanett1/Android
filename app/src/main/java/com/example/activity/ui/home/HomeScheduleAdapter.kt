package com.example.activity.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.activity.databinding.ItemHomeScheduleBinding
import com.example.activity.model.ScheduleResponse
import java.time.format.DateTimeFormatter

class HomeScheduleAdapter :
    ListAdapter<ScheduleResponse, HomeScheduleAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val binding: ItemHomeScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScheduleResponse) {

            // Habit title
            binding.tvTitle.text = item.habit?.name ?: "Unknown Habit"

            // Time formatting
            val start = item.startTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "?"
            val end = item.endTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "?"
            binding.tvTime.text = "$start - $end"

            // Notes (if empty -> hide)
            if (!item.notes.isNullOrEmpty()) {
                binding.tvNotes.text = item.notes
                binding.tvNotes.visibility = View.VISIBLE
            } else {
                binding.tvNotes.visibility = View.GONE
            }

            // Set placeholder icon (will replace later with category icon)
            binding.ivIcon.setImageResource(android.R.drawable.ic_menu_add)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ScheduleResponse>() {
        override fun areItemsTheSame(oldItem: ScheduleResponse, newItem: ScheduleResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScheduleResponse, newItem: ScheduleResponse): Boolean {
            return oldItem == newItem
        }
    }
}
