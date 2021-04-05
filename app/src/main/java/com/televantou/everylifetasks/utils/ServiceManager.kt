package com.televantou.everylifetasks.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Created by Eirini Televantou on 03/04/2021 for EveryLife.
 */
class ServiceManager(base: Context?) {
    lateinit var context: Context

    fun isNetworkAvailable(): Boolean{
        return isNetworkAvailable(context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager)
    }

    fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            activeNetwork != null && activeNetwork.isConnected
        }
    }

    init {
        if (base != null) {
            context = base
        }
    }
}