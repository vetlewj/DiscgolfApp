package no.hiof.discgolfapp.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkLayer {

    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val retrofitCourses: Retrofit = Retrofit.Builder()
        .baseUrl("https://discgolfmetrix.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val coursesService: CoursesService by lazy {
        retrofitCourses.create(CoursesService::class.java)
    }
    val apiClient = ApiClient(coursesService)


    val retrofitWeather: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.met.no/weatherapi/locationforecast/2.0/").
        addConverterFactory(MoshiConverterFactory.create(moshi)).
        build()

//    val coursesService: CoursesService by lazy {
//        retrofitCourses.create(CoursesService::class.java)
//    }
//    val apiClient = ApiClient(coursesService)





}