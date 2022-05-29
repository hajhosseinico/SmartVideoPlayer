package ir.hajhosseini.smartvideoplayer.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hajhosseini.smartvideoplayer.model.retrofit.VideoRetrofitInterface
import ir.hajhosseini.smartvideoplayer.util.NetworkListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Provides retrofit dependencies
 * @Singleton is used because we had only 1 scope. singleton scope is = application lifecycle scope
 * If it was a full application, i would provide dependencies into custom scopes (or activity scope)
 */

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return Retrofit.Builder()
            .baseUrl("https://dl.dropboxusercontent.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient.Builder().addInterceptor(logging).build())
    }

    @Singleton
    @Provides
    fun provideBlogService(retrofit: Retrofit.Builder): VideoRetrofitInterface {
        return retrofit
            .build()
            .create(VideoRetrofitInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkListener(): NetworkListener {
        return NetworkListener()
    }
}