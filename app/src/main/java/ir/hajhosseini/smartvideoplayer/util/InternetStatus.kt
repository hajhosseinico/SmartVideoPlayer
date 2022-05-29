package ir.hajhosseini.smartvideoplayer.util

import android.content.Context
import android.net.ConnectivityManager
import java.net.InetAddress
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Checking if device is connected into internet of not
 */
class InternetStatus
@Inject
constructor() {
    fun isInternetAvailable(): Boolean {
        try {
            val address = InetAddress.getByName("www.google.com")
            return address.toString() != ""
        } catch (e: UnknownHostException) {
            // Log error
        }
        return false
    }
}