package com.televantou.everylifetasks.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.televantou.everylifetasks.data.menuItems.MenuItem
import com.televantou.everylifetasks.databinding.MenuItemGridBinding

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */
class MenuArrayAdapter(var menuItems: List<MenuItem>?, val itemClickListener: OnItemClickListener) :
        RecyclerView.Adapter<MenuArrayAdapter.ViewHolder>() {

    fun setItems(menuItems: List<MenuItem>) {
        this.menuItems = menuItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MenuItemGridBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = menuItems!!.size

    inner class ViewHolder(val binding: MenuItemGridBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = menuItems!![position]
        holder.binding.item = currentItem
        holder.binding.executePendingBindings()
        holder.binding.parent.setOnClickListener {
            currentItem.enabled = !currentItem.enabled
            notifyItemChanged(position)
            itemClickListener.onItemClicked(currentItem)
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(menuItem: MenuItem)
    }

}