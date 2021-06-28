package com.lessons.films.connectcheck

import android.content.Context
import android.net.ConnectivityManager


class NetworkUtil {
    companion object {
        val TYPE_WIFI = 1
        val TYPE_MOBILE = 2
        val TYPE_NOT_CONNECTED = 0
        val NETWORK_STATUS_NOT_CONNECTED = 0
        val NETWORK_STATUS_WIFI = 1
        val NETWORK_STATUS_MOBILE = 2

        private fun getConnectivityStatus(context: Context): Int {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            activeNetwork?.let {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI
                if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE
            }
            return TYPE_NOT_CONNECTED
        }

        fun getConnectivityStatusString(context: Context) =
                when (getConnectivityStatus(context)) {
                    TYPE_WIFI -> NETWORK_STATUS_WIFI
                    TYPE_MOBILE -> NETWORK_STATUS_MOBILE
                    TYPE_NOT_CONNECTED -> NETWORK_STATUS_NOT_CONNECTED
                    else -> 0
                }
    }
}