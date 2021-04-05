package com.televantou.everylifetasks.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.televantou.everylifetasks.ui.main.MainActivity


/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    lateinit var activity: AppCompatActivity

    private fun checkInternet(context: Context?): Boolean {
        val serviceManager = ServiceManager(context);
        return serviceManager.isNetworkAvailable()
    }

    override fun onReceive(context: Context, intent: Intent?) {
        try {
            (activity as MainActivity).showBanner(checkInternet(context))
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


}