package com.udacity.asteroidradar.api


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.models.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//interface ApiService {
//
//    @GET("neo/rest/v1/feed")
//    fun getAsteroidsAsync(
//        @Query("start_date") startDate: String,
//        @Query("end_date") endDate: String,
//        @Query("api_key") apiKey: String = Constants.API_KEY
//    ): Deferred<ResponseBody>
//
//    @GET("planetary/apod")
//    fun getPictureOfDayAsync(
//        @Query("api_key") apiKey: String = Constants.API_KEY
//    ): Deferred<PictureOfDay>
//}
//
//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()
//
//object Network {
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(Constants.BASE_URL)
//        .addConverterFactory(ScalarsConverterFactory.create())
//        .addConverterFactory(MoshiConverterFactory.create(moshi))
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())
//        .build()
//
//    val service: ApiService = retrofit.create(ApiService::class.java)
//}

interface AstroidRadarApiService {

        @GET("planetary/apod")
    fun getPicture(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Deferred<PictureOfDay>
    @GET("neo/rest/v1/feed")
    fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Deferred<ResponseBody>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
object AsteroidClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val service: AstroidRadarApiService =
        retrofit.create(AstroidRadarApiService::class.java)

}