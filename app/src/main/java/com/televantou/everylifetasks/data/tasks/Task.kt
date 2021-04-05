package com.televantou.everylifetasks.data.tasks

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */
@Entity(tableName = "task_table")
data class Task(
    val description: String,
    @PrimaryKey val id: Int,
    val name: String,
    val type: String
)