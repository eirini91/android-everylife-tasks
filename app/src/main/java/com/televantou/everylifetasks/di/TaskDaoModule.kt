package com.televantou.everylifetasks.di

import android.content.Context
import com.televantou.everylifetasks.data.tasks.TaskDao
import com.televantou.everylifetasks.db.TaskRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */

//Task module to be injected using Dagger
@InstallIn(ApplicationComponent::class)
@Module
class TaskDaoModule {

    @Singleton
    @Provides
    fun provideDao(db : TaskRoomDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): TaskRoomDatabase {
        return TaskRoomDatabase.getDatabase(appContext)
    }
}
