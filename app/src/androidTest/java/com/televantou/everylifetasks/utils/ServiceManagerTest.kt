package com.televantou.everylifetasks.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.test.InstrumentationRegistry
import com.televantou.everylifetasks.utils.ServiceManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


/**
 * Created by Eirini Televantou on 04/04/2021 for ASOS.
 */
@RunWith(MockitoJUnitRunner::class)
class ServiceManagerTest {

    lateinit var serviceManager: ServiceManager

    @Mock
    lateinit var connectivityManager: ConnectivityManager

    @Mock
    lateinit var capabilities: NetworkCapabilities

    @Mock
    lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        serviceManager = ServiceManager(context)
    }

    @Test
    fun getActiveNetworkInfo_fullConnectivity_shouldReturnTrueCorrectly() {

        Mockito.`when`(connectivityManager.getNetworkCapabilities(any())).thenReturn(
                capabilities
        )
        Mockito.`when`(capabilities.hasTransport(anyInt())).thenReturn(
                true
        )

        Assert.assertEquals(serviceManager.isNetworkAvailable(connectivityManager), true)

    }

    @Test
    fun getActiveNetworkInfo_NoConnectivity_shouldReturnFalseCorrectly() {

        Mockito.`when`(connectivityManager.getNetworkCapabilities(any())).thenReturn(
                null
        )

        Assert.assertEquals(serviceManager.isNetworkAvailable(connectivityManager), false)

    }


}