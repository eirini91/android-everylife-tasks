package com.televantou.everylifetasks.ui.main

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.televantou.everylifetasks.data.menuItems.MenuItem
import com.televantou.everylifetasks.data.tasks.Task
import com.televantou.everylifetasks.data.tasks.TaskRepository
import com.televantou.everylifetasks.data.yearItem.repo.MenuItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject internal constructor(
        private val taskRepository: TaskRepository,
        private val menuItemRepository: MenuItemRepository
) : ViewModel() {

    var tasks: MutableLiveData<List<Task>> = MutableLiveData()
    var menuItems: MutableLiveData<List<MenuItem>> = MutableLiveData()

    var dataUpdatedListener: DataUpdatedListener? = null
    val error: ObservableField<String> = ObservableField("")

    val loading: ObservableBoolean = ObservableBoolean(false)
    val emptyList: ObservableBoolean = ObservableBoolean(false)

    //required non suspend function for testing
    fun getTasks() {
        loading.set(true)

        viewModelScope.launch(Dispatchers.IO) {
            loadTasks()
        }
    }

    private suspend fun loadTasks() {
        error.set("")
        //execute coroutine
        taskRepository.execute()
                .also {
                    loading.set(false)
                    when (it) {
                        is TaskRepository.Result.Success -> {

                            dataUpdatedListener?.onDataUpdated(it.data)
                            tasks.postValue(it.data)
                            taskRepository.deleteOldAndSave(it.data)
                            val menuItemsL = menuItemRepository.getMenuItems(it.data)
                            menuItems.postValue(menuItemsL)
                            dataUpdatedListener?.onMenuUpdated(menuItemsL)

                        }
                        is TaskRepository.Result.Error -> {
                            loadLocalData(it.e.localizedMessage)

                        }
                    }
                }
    }


    fun loadLocalData(errorS: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAll()
                    .also {

                        dataUpdatedListener?.onDataUpdated(it)
                        tasks.postValue(it)
                        val menuItemsL = menuItemRepository.getMenuItems(it)
                        menuItems.postValue(menuItemsL)
                        dataUpdatedListener?.onMenuUpdated(menuItemsL)
                        if (errorS != null) {
                            dataUpdatedListener?.onErrorDialog(errorS)
                        }else{
                            if(it.isEmpty())
                                error.set("No Internet Connection")
                        }

                    }
        }
    }

    fun filterItems() {
        val items = taskRepository.filter(tasks.value!!, menuItems.value!!)
        dataUpdatedListener?.onDataUpdated(items)
        emptyList.set(items.size == 0)
    }

    interface DataUpdatedListener {
        fun onDataUpdated(tasks: List<Task>?)
        fun onMenuUpdated(menuItems: List<MenuItem>)
        fun onErrorDialog(error: String)

    }
}