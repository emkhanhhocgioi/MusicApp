package com.example.musicai.api
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.contentType
import io.ktor.serialization.gson.*


data class RegisterRequset(var username: String, var paassword : String , var email : String);
data class LoginRequest(var email: String, var password: String);

data class  User(
    var id: String,
    var username: String,
    var email: String,
    var token: String,
    var avatar_url: String = "https://example.com/default-avatar.png",
    var favourite : List<String>
)
suspend fun RegisterUser (username: String, password: String , email: String) : HttpResponse {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }
    val response = client.post("http://192.168.1.5:8080/auth/register") {
        contentType(io.ktor.http.ContentType.Application.Json);
        setBody(RegisterRequset(username,password,email))

    }
    if(response.status === io.ktor.http.HttpStatusCode.OK){

        client.close()
        return response
    } else {
        client.close()
        throw Exception("Failed to register user: ${response.status}")
    }
}

suspend fun loginUser(email: String, password: String): ReturnUsers? {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }
    val response = client.post("http://192.168.1.5:8080/auth/login") {
        contentType(io.ktor.http.ContentType.Application.Json)
        setBody(LoginRequest(email, password))
    }
    return if (response.status == io.ktor.http.HttpStatusCode.OK) {
      return response.body<ReturnUsers>()
    } else { null
    }
}