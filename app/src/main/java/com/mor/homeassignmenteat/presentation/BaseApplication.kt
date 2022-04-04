package com.mor.homeassignmenteat.presentation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.Request

@HiltAndroidApp
class BaseApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        cache()
     }

    fun cache(): Cache? {
        val cacheSize = 5 * 1024 * 1024.toLong()
        return Cache(applicationContext.cacheDir, cacheSize)
    }


    var offlineInterceptor = Interceptor { chain ->
        var request: Request = chain.request()
        if (!isInternetAvailable(applicationContext)) {
            val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma")
                .build()
        }
        chain.proceed(request)
    }


    fun isInternetAvailable(context:Context): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}