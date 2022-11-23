package no.hiof.discgolfapp.helper.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkConnectionHelper {
    companion object {
        // Used method from https://www.geeksforgeeks.org/how-to-check-internet-connection-in-kotlin/
        // and https://stackoverflow.com/questions/66675693/android-check-internet-connection-in-kotlin
        // as a base and Android Studio Intellisense to create this method
        @SuppressLint("MissingPermission")
        fun isNetworkConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(network) ?: return false
                return when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                @Suppress("DEPRECATION")
                return connectivityManager.activeNetworkInfo?.isConnected ?: false
            }
        }
    }
}
