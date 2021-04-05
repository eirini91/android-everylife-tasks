package com.televantou.everylifetasks.data.tasks

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */

//Dao Interface class for the task table
@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table")
    fun getAllTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tasks: List<Task>): List<Long>

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()
}