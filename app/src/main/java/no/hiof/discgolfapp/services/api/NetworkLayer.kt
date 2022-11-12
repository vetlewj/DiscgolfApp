package no.hiof.discgolfapp.services.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkLayer {

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(MyInterceptor())
    }.build()

    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val retrofitCourses: Retrofit = Retrofit.Builder()
        .baseUrl("https://discgolfmetrix.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val retrofitWeather: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.met.no/weatherapi/locationforecast/2.0/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val coursesService: CoursesService by lazy {
        retrofitCourses.create(CoursesService::class.java)
    }

    val weatherService: WeatherService by lazy {
        retrofitWeather.create(WeatherService::class.java)
    }

    val apiClient = ApiClient(coursesService, weatherService)

}