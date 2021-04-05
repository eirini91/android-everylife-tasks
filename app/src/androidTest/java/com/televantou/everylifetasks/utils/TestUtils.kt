/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.televantou.everylifetasks.utils

import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import com.televantou.everylifetasks.R
import com.televantou.everylifetasks.data.menuItems.MenuItem
import com.televantou.everylifetasks.data.tasks.Task
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException


val listOfTasks = arrayListOf(
    Task("General Type Task",1,"Task 1","general"),
    Task("Hydration Type Task",2,"Task 2","hydration"),
    Task("Medication Type Task",3,"Task 3","medication"),
    Task("Nutrition Type Task",4,"Task 4","nutrition"),
    Task("Empty Type Task",5,"Task 5","nutrition")
)

val listOfTasksFiltered = arrayListOf(
    Task("Hydration Type Task",2,"Task 2","hydration"),
    Task("Medication Type Task",3,"Task 3","medication"),
    Task("Nutrition Type Task",4,"Task 4","nutrition"),
    Task("Empty Type Task",5,"Task 5","nutrition")
)

val initialArrayOfMenuMock = arrayListOf(
    MenuItem( "general", R.drawable.ic_general_full,  true),
    MenuItem( "hydration",R.drawable.ic_hydration_full  ,true),
    MenuItem( "medication", R.drawable.ic_medication_full ,true),
    MenuItem( "nutrition",R.drawable.ic_nutrition_full  ,true)
)

/**
 * This ViewAction tells espresso to wait till a certain view is found in the view hierarchy.
 * @param text The text of the view to wait for.
 * @param timeout The maximum time which espresso will wait for the view to show up (in milliseconds)
 */
fun waitForView(text: String, timeout: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): org.hamcrest.Matcher<View>? {
            return ViewMatchers.isRoot()
        }

        override fun getDescription(): String {
            return "wait for a specific view with text $text; during $timeout millis."
        }

        override fun perform(uiController: UiController, rootView: View) {
            uiController.loopMainThreadUntilIdle()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + timeout
            val viewMatcher = ViewMatchers.withText(text)

            do {
                // Iterate through all views on the screen and see if the view we are looking for is there already
                for (child in TreeIterables.breadthFirstViewTraversal(rootView)) {
                    // found view with required ID
                    if (viewMatcher.matches(child)) {
                        return
                    }
                }
                // Loops the main thread for a specified period of time.
                // Control may not return immediately, instead it'll return after the provided delay has passed and the queue is in an idle state again.
                uiController.loopMainThreadForAtLeast(100)
            } while (System.currentTimeMillis() != endTime) // in case of a timeout we throw an exception -&gt; test fails
            throw PerformException.Builder()
                .withCause(TimeoutException())
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(rootView))
                .build()
        }
    }
}

/**
 * A [ViewAction] that waits up to [timeout] milliseconds for a [View]'s visibility value to change to [View.VISIBLE].
 */
class WaitUntilVisibleAction(private val timeout: Long) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return CoreMatchers.any(View::class.java)
    }

    override fun getDescription(): String {
        return "wait up to $timeout milliseconds for the view to become visible"
    }

    override fun perform(uiController: UiController, view: View) {

        val endTime = System.currentTimeMillis() + timeout

        do {
            if (view.visibility == View.VISIBLE) return
            uiController.loopMainThreadForAtLeast(50)
        } while (System.currentTimeMillis() < endTime)

        throw PerformException.Builder()
            .withActionDescription(description)
            .withCause(TimeoutException("Waited $timeout milliseconds"))
            .withViewDescription(HumanReadables.describe(view))
            .build()
    }
}

/**
 * @return a [WaitUntilVisibleAction] instance created with the given [timeout] parameter.
 */
fun waitUntilVisible(timeout: Long): ViewAction {
    return WaitUntilVisibleAction(timeout)
}