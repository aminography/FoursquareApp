package com.aminography.foursquareapp.core.tools

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.PowerManager

/**
 * A class which helps to know if the network is available.
 *
 * @author aminography
 */
class NetworkUtils {

    enum class NetworkStatus {
        DISCONNECTED,
        METERED,
        UNMETERED,
    }

    companion object {

        /**
         * Helps to know if the network is available
         *
         * @param context
         * @return true if the the network is available
         */
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return !isDozing(context) && networkInfo.state == NetworkInfo.State.CONNECTED && networkInfo.isConnected
//            return getNetworkStatus(context) == NetworkStatus.UNMETERED
        }

        private fun getNetworkStatus(context: Context): NetworkStatus {
            if (isDozing(context)) {
                return NetworkStatus.DISCONNECTED
            }
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo ?: return NetworkStatus.DISCONNECTED
            return if (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_ETHERNET) {
                NetworkStatus.UNMETERED
            } else NetworkStatus.METERED
        }

        /**
         * Returns true if the device is in Doze/Idle mode. Should be called before checking the network connection because
         * the ConnectionManager may report the device is connected when it isn't during Idle mode.
         */
        @TargetApi(23)
        private fun isDozing(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= 23) {
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                powerManager.isDeviceIdleMode && !powerManager.isIgnoringBatteryOptimizations(context.packageName)
            } else {
                false
            }
        }
    }

}
