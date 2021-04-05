package com.televantou.everylifetasks.data.menuItems


/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */

//Local class used for the bottom menu in main screen
data class MenuItem(
        val name: String,
        val drawableId: Int,
        var enabled: Boolean = true
)