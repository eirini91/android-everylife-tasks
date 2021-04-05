package com.televantou.everylifetasks.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.televantou.everylifetasks.api.ELApi
import com.televantou.everylifetasks.data.menuItems.MenuItem
import com.televantou.everylifetasks.data.tasks.Task
import com.televantou.everylifetasks.data.tasks.TaskDao
import com.televantou.everylifetasks.data.tasks.TaskRepository
import com.televantou.everylifetasks.data.menuItems.MenuItemRepository
import com.televantou.everylifetasks.utils.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.mockito.*
import org.mockito.Mockito.mock

/**
 * Created by Eirini Televantou on 05/04/2021 for EveryLife.
 */

// I believe This is the most important test class of the project. It's where the logic is being tested.
@HiltAndroidTest
class MainViewModelTest {

    lateinit var mainViewModel: MainViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)

    @Mock
    lateinit var dataUpdatedListener: MainViewModel.DataUpdatedListener

    @InjectMocks
    @Spy
    var taskRepository: TaskRepository = TaskRepository(
        ELApi.create(),
        mock(TaskDao::class.java)
    )

    @InjectMocks
    @Spy
    var menuItemRepository: MenuItemRepository = MenuItemRepository()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        hiltRule.inject()
        mainViewModel = MainViewModel(taskRepository, menuItemRepository)
    }

    @Test
    fun getTasks_LoadingSetToTrue() {
        // Act
        mainViewModel.getTasks()
        //Assert
        Assert.assertEquals(mainViewModel.loading.get(), true)

    }

    @Test
    fun getTasks_MenuItemsCalledAndInitialised() {
        runBlocking {
            Mockito.`when`(taskRepository.execute()).thenReturn(
                TaskRepository.Result.Success(
                    listOfTasks
                )
            )
        }

        // Act
        mainViewModel.getTasks()
        Thread.sleep(1500);

        //Assert
        Mockito.verify(menuItemRepository).getMenuItems(listOfTasks) //verify method called
        Assert.assertEquals(
            mainViewModel.menuItems.value,
            initialArrayOfMenuMock
        ) // assert value got updated
    }

    @Test
    fun loadTasks_success_loadingStoppedAndDataUpdated() {
        val mutableLiveData: MutableLiveData<List<Task>> = MutableLiveData()
        runBlocking {
            Mockito.`when`(taskRepository.execute()).thenReturn(
                TaskRepository.Result.Success(
                    listOfTasks
                )
            )
        }
        mutableLiveData.value = TaskRepository.Result.Success(
            listOfTasks
        ).data

        mainViewModel.getTasks()
        Thread.sleep(1500)
        Assert.assertEquals(
            mainViewModel.tasks.value,
            mutableLiveData.value
        ) // assert value got updated
        Assert.assertEquals(mainViewModel.loading.get(), false)
    }


    //This is just an example of an errored response. In a real project we should have covered all main HTTP errors
    // along with use case scenarios for when we don't have any data to begin with(the UI doesn't show the dialog on that case. It just updates the error string)
    @Test
    fun loadCharacters_failed_loadingStoppedAndErrorUpdated() {
        mainViewModel.dataUpdatedListener = dataUpdatedListener
        runBlocking {
            Mockito.`when`(taskRepository.execute()).thenReturn(
                TaskRepository.Result.Error(
                    Throwable("Something Went Wrong")
                )
            )
        }

        mainViewModel.getTasks()
        Thread.sleep(2500)
        Assert.assertEquals(
            mainViewModel.tasks.value?.size,0)

        Assert.assertEquals(mainViewModel.loading.get(), false)

        Mockito.verify(dataUpdatedListener).onErrorDialog("Something Went Wrong") //verify method called

    }

    @Test
    fun updateMenuItemsAndCallFilter_tasksUpdated() {
        mainViewModel.tasks.value = listOfTasks
        mainViewModel.dataUpdatedListener = dataUpdatedListener

        mainViewModel.menuItems.value = initialArrayOfMenuMock
        (mainViewModel.menuItems.value as ArrayList<MenuItem>).get(0).enabled = false
        mainViewModel.filterItems()
        Mockito.verify(dataUpdatedListener).onDataUpdated(listOfTasksFiltered) //verify method called

    }

}