package com.televantou.everylifetasks.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.televantou.everylifetasks.data.tasks.Task
import com.televantou.everylifetasks.databinding.TaskRowBinding

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */

class TaskAdapter(private var items: List<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    fun set(newItems: List<Task>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TaskRowBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.binding.item = currentItem
        holder.binding.executePendingBindings()
    }

    inner class ViewHolder(val binding: TaskRowBinding) : RecyclerView.ViewHolder(binding.root)
}