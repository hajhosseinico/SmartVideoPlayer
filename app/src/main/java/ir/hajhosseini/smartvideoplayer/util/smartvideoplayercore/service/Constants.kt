package ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.service

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {
    const val VIDEO_LIST = "VIDEO_LIST"
    const val VIDEO_URL = "VIDEO_URL"
    val IS_PRE_LOAD_COMPLETED = booleanPreferencesKey("IS_PRE_LOAD_COMPLETED")
}