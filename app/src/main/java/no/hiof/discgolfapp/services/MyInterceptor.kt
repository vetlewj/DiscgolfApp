package no.hiof.discgolfapp.services

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("User-Agent", "schoolProjectDiscGolfAPP/Contact:kristkas@hiof.no")
            .build()
        return chain.proceed(request)
    }


}