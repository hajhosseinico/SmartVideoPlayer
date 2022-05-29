package ir.hajhosseini.smartvideoplayer.util

import android.os.Handler
import android.os.Looper
import java.lang.Exception
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
        return try {
            Thread().run() {
                InetAddress.getByName("www.google.com").toString().isNotEmpty()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}