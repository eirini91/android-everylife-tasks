package com.televantou.everylifetasks.ui.main


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.televantou.everylifetasks.R
import com.televantou.everylifetasks.utils.waitForView
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    private val hiltRule = HiltAndroidRule(this)

    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @get:Rule
    val rule: RuleChain = RuleChain
            .outerRule(hiltRule)
            .around(mActivityTestRule)


    @Before
    fun setUp() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable")
    }


    @Test
    fun mainActivityTest() {

        onView(isRoot()).perform(waitForView("Empty the bin and take the rubbish and recycling to the communal rubbish bins that are on the lower ground floor of the building", 2000))

        val textView = onView(
                allOf(withId(R.id.description), withText("Empty the bin and take the rubbish and recycling to the communal rubbish bins that are on the lower ground floor of the building"),
                        withParent(allOf(withId(R.id.parent),
                                withParent(withId(R.id.rclTasks)))),
                        isDisplayed()))
        textView.check(matches(withText("Empty the bin and take the rubbish and recycling to the communal rubbish bins that are on the lower ground floor of the building")))

        val textView2 = onView(
                allOf(withId(R.id.title), withText("Take the rubbish out"),
                        withParent(allOf(withId(R.id.parent),
                                withParent(withId(R.id.rclTasks)))),
                        isDisplayed()))
        textView2.check(matches(withText("Take the rubbish out")))


        val recyclerView = onView(
                allOf(withId(R.id.rclBottomMenu),
                        childAtPosition(
                                withId(R.id.main),
                                4)))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(withText("Take the rubbish out")).check(doesNotExist())
        onView(withText("Could not connect to internet")).check(matches(not(isDisplayed())))


    }

    @Test
    fun mainActivityTestNoInternet() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable")
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data disable")
        Thread.sleep(1500);

        val textView = onView(
                allOf(withId(R.id.description), withText("Empty the bin and take the rubbish and recycling to the communal rubbish bins that are on the lower ground floor of the building"),
                        withParent(allOf(withId(R.id.parent),
                                withParent(withId(R.id.rclTasks)))),
                        isDisplayed()))
        textView.check(matches(withText("Empty the bin and take the rubbish and recycling to the communal rubbish bins that are on the lower ground floor of the building")))

        val textView2 = onView(
                allOf(withId(R.id.title), withText("Take the rubbish out"),
                        withParent(allOf(withId(R.id.parent),
                                withParent(withId(R.id.rclTasks)))),
                        isDisplayed()))
        textView2.check(matches(withText("Take the rubbish out")))


        onView(isRoot()).perform(waitForView("Could not connect to internet", 2000))
        onView(withText("Could not connect to internet")).check(matches(isDisplayed()))
    }

    @After
    fun tearUp() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable")
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable")
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
