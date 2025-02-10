package com.example.scheduleapp.retrofit

import com.example.scheduleapp.data.FlatScheduleAnswer
import com.example.scheduleapp.data.FlatScheduleDetailed
import com.example.scheduleapp.data.FlatScheduleParameters
import retrofit2.http.GET
import retrofit2.http.Path

interface ScheduleService {
    @GET("schedule/parameters")
    suspend fun getScheduleParameters(): FlatScheduleParameters

    @GET("schedule/version")
    suspend fun getScheduleVersion(): Long

    @GET("schedule/current/{date}")
    suspend fun getScheduleCurrent(@Path("date") dateId: Int = -1): FlatScheduleAnswer<FlatScheduleDetailed>
}