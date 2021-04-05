package com.televantou.everylifetasks.utils

import com.televantou.everylifetasks.R
import com.televantou.everylifetasks.data.tasks.Task

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */

//Extension function to get the drawable based on task type
fun Task.getDrawableIdForType():Int = when(this.type) {
    "general"->
        R.drawable.ic_general_full
    "hydration"->
        R.drawable.ic_hydration_full
    "medication"->
        R.drawable.ic_medication_full
    "nutrition"->
        R.drawable.ic_nutrition_full
    else ->  R.drawable.ic_general_full
}
