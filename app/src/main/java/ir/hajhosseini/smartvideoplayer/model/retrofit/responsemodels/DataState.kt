package ir.hajhosseini.smartvideoplayer.model.retrofit.responsemodels

import java.lang.Exception

/**
 * This is a simple base generic api response model
 * In more complex project, i would wrap it up with error handler classes
 * to detect error codes and consider the proper action
 */
sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
     var isFromCache: Boolean = false
}