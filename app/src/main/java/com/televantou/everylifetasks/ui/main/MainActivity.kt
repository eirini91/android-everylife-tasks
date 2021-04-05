package com.televantou.everylifetasks.ui.main

import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.televantou.everylifetasks.R
import com.televantou.everylifetasks.utils.NetworkChangeReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val fragment: MainFragment = MainFragment.newInstance()
    val networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commitNow()
        }
        networkChangeReceiver.activity = this
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(networkChangeReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkChangeReceiver)
    }

    fun showBanner(value: Boolean) {
        val textView = findViewById(R.id.txtConnection) as TextView
        if (!value) {
            textView.setVisibility(View.VISIBLE)
            Handler(Looper.getMainLooper()).postDelayed({
                textView.setVisibility(View.GONE)
            }, 3000)
        } else {
            fragment.refresh()
        }
    }


}