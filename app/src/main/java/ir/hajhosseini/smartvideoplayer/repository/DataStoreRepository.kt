package ir.hajhosseini.smartvideoplayer.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.hajhosseini.smartvideoplayer.util.smartvideoplayercore.service.Constants.IS_PRE_LOAD_COMPLETED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository() {
    /**
     * DataStore
     * The main difference between DataStore and Share Preferences is that DataStore is running on background thread and not on main thread
     * In DataStore we need to define our preferences keys
     */

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

        fun getIsPreLoadCompleted(
            context: Context
        ): Flow<Boolean> {
            return context.dataStore.data
                .map { preferences ->
                    preferences[IS_PRE_LOAD_COMPLETED] ?: false
                }
        }

        suspend fun addBooleanToPrefs(
            key: Preferences.Key<Boolean>,
            value: Boolean,
            context: Context
        ) {
            context.dataStore.edit { settings ->
                settings[key] = value
            }
        }
    }


}


