package com.example.musicai.api
import  io.ktor.http.*;
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson


class userapi {
     val baseurl = Constant.baseurl;



    suspend fun getuserDetails(username: String, email: String, password: String)
    {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }
        val response = client.get("${baseurl}/user/detail"){

        }
    }




}