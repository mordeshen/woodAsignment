package com.mor.homeassignmenteat.buisness.interceptors

import android.util.Log
import com.mor.homeassignmenteat.buisness.util.Constants.Companion.HEADER_CACHE_CONTROL
import com.mor.homeassignmenteat.buisness.util.Constants.Companion.HEADER_PRAGMA
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

private const val TAG = "NetworkInterceptor"

class NetworkInterceptor (cacheTime: Int) : Interceptor  {

    private val _cacheTime = cacheTime
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.e(TAG,"network interceptor: called.")

        val response = chain.proceed(chain.request())

        val cacheControl = CacheControl.Builder()
            .maxAge(_cacheTime, TimeUnit.SECONDS)
            .build()

        return response.newBuilder()
            .removeHeader(HEADER_PRAGMA)
            .removeHeader(HEADER_CACHE_CONTROL)
            .header(HEADER_CACHE_CONTROL, cacheControl.toString())
            .build()
    }
}
