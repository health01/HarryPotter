package com.example.harrypotter.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkMonitor @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val networkRequest: NetworkRequest
) {

    private var isConnected: Boolean = false

    init {
        connectivityManager.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    isConnected = true
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    isConnected = false
                }
            })
    }

    fun isNetworkConnected(): Boolean {
        return isConnected
    }
}