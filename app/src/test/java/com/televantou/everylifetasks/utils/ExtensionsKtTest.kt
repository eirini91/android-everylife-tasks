package com.televantou.everylifetasks.utils

import com.televantou.everylifetasks.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Eirini Televantou on 04/04/2021 for ASOS.
 */

@RunWith(JUnit4::class)
class ExtensionsKtTest {

    @Test
    fun getDrawableIdForTypeGeneralTypeReturnGeneralDrawable() {
        val drawable = generalTask.getDrawableIdForType()
        assertEquals(drawable, R.drawable.ic_general_full)
    }

    @Test
    fun getDrawableIdForTypeHydrationTypeReturnHydrationDrawable() {
        val drawable = hydrationTask.getDrawableIdForType()
        assertEquals(drawable, R.drawable.ic_hydration_full)
    }

    @Test
    fun getDrawableIdForTypeMedicationTypeReturnMedicationDrawable() {
        val drawable = medicationTask.getDrawableIdForType()
        assertEquals(drawable, R.drawable.ic_medication_full)
    }

    @Test
    fun getDrawableIdForTypeNutritionTypeReturnNutritionDrawable() {
        val drawable = nutritionTask.getDrawableIdForType()
        assertEquals(drawable, R.drawable.ic_nutrition_full)
    }

    @Test
    fun getDrawableIdForTypeNoTypeTypeReturnGeneralDrawable() {
        val drawable = emptyTypeTask.getDrawableIdForType()
        assertEquals(drawable, R.drawable.ic_general_full)
    }
}