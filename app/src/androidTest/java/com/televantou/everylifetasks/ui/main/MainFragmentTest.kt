package com.televantou.everylifetasks.ui.main

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.televantou.everylifetasks.R
import com.televantou.everylifetasks.api.ELApi
import com.televantou.everylifetasks.test.HiltTestActivity
import com.televantou.everylifetasks.utils.waitUntilVisible
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy

/**
 * Created by Eirini Televantou on 05/04/2021 for EveryLife.
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainFragmentTest {
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var service: ELApi

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)

    @Spy
    var mainFragment: MainFragment? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        hiltRule.inject()
        mainFragment = MainFragment()
    }

    @Test
    fun getData() {

        launchFragmentInHiltContainer<MainFragment>(null, R.style.Theme_MaterialComponents)
        Espresso.onView(ViewMatchers.withId(R.id.progress_circular)).perform(waitUntilVisible(500L))

        mainFragment!!.activity!!.runOnUiThread(Runnable {
            mainFragment?.getData()
        })

        Espresso.onView(ViewMatchers.withId(R.id.rclTasks)).perform(waitUntilVisible(7000L))

    }

    //showErrorDialog
    @Test
    fun onErrorDialog() {

        launchFragmentInHiltContainer<MainFragment>(null, R.style.Theme_MaterialComponents)
        mainFragment!!.activity!!.runOnUiThread(Runnable {
            mainFragment!!.onErrorDialog("Testing Error")

        })
        Espresso.onView(ViewMatchers.withText("Something Went Wrong")).perform(waitUntilVisible(5000L))
        Espresso.onView(ViewMatchers.withText("Testing Error")).perform(waitUntilVisible(5000L))

    }

    inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null,
        @StyleRes themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
        crossinline action: Fragment.() -> Unit = {}
    ) {
        val startActivityIntent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                HiltTestActivity::class.java
            )
        ).putExtra(FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY, themeResId)

        ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity {
            mainFragment = it.supportFragmentManager.fragmentFactory.instantiate(
                Preconditions.checkNotNull(T::class.java.classLoader),
                T::class.java.name
            ) as MainFragment
            mainFragment!!.arguments = fragmentArgs
            it.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, mainFragment!!, "")
                .commitNow()

            mainFragment!!.action()
        }
    }

}