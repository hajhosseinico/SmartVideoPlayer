package ir.hajhosseini.smartvideoplayer

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : MultiDexApplication() {

    companion object {
        lateinit var simpleCache: SimpleCache
        const val exoPlayerCacheSize: Long = 90 * 1024 * 1024
        lateinit var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor
        lateinit var exoDatabaseProvider: ExoDatabaseProvider

        fun addBooleanToPrefs(context: Context, key: String, property: Boolean) {
            val preference = context.getSharedPreferences(
                context.resources.getString(R.string.app_name),
                MODE_PRIVATE
            ).edit()
            preference.putBoolean(key, property).apply()
        }

        fun getBooleanPref(context: Context, key: String): Boolean {
            val preference =
                context.getSharedPreferences(
                    context.resources.getString(R.string.app_name),
                    MODE_PRIVATE
                )
            return preference.getBoolean(key, false)
        }
    }

    override fun onCreate() {
        super.onCreate()
        leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize)
        exoDatabaseProvider = ExoDatabaseProvider(this)
        simpleCache = SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor, exoDatabaseProvider)
    }


}