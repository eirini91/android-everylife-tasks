package com.televantou.everylifetasks.data.yearItem.repo

import com.televantou.everylifetasks.data.menuItems.MenuItem
import com.televantou.everylifetasks.data.tasks.Task
import com.televantou.everylifetasks.utils.getDrawableIdForType
import javax.inject.Inject

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */

class MenuItemRepository @Inject constructor() {

    //Returns menu items based on tasks
    fun getMenuItems(tasks :List<Task>): ArrayList<MenuItem> {
        val list = arrayListOf<MenuItem>()

        for(task in tasks){
            var menuItem = MenuItem(task.type, task.getDrawableIdForType())
            if(!list.contains(menuItem))
                list.add(menuItem)
        }
        return list
    }


}