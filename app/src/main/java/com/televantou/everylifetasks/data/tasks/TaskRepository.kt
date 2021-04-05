package com.televantou.everylifetasks.data.tasks

import com.televantou.everylifetasks.api.ELApi
import com.televantou.everylifetasks.data.menuItems.MenuItem
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */


//TaskRepository Class. Gives us access to Retrofit and returns the result of the call. Also allows DB query
open class TaskRepository @Inject constructor(
        private val service: ELApi,
        private val taskDao: TaskDao
) {

    sealed class Result {
        data class Success(val data: List<Task>) : Result()
        data class Error(val e: Throwable) : Result()
    }

    suspend fun execute(
    ): Result = try {
        service.getTasks().let {
            Result.Success(it)
        }
    } catch (e: IOException) {
        Result.Error(
                e
        )
    }

    fun addAll(list: List<Task>) {
        taskDao.insert(list)
    }

    fun filter(data: List<Task>, seasons: List<MenuItem>): List<Task> {
        return data.filter {
            seasons.any { menuItem -> menuItem.enabled && menuItem.name == it.type }
        }
    }

    fun getAll(): List<Task> {
        return taskDao.getAllTasks()
    }

    suspend fun deleteOldAndSave(list: List<Task>) {
        taskDao.deleteAll()
        addAll(list)
    }
}