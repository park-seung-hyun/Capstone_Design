package com.example.parkseunghyun.achievementofall

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

object VolleyHttpService{

    fun login(context: Context, jsonObject: JSONObject,success: (Boolean) -> Unit){

        val loginRequest = object : JsonObjectRequest(Request.Method.POST, "http://192.168.22.31:3000/login", jsonObject, Response.Listener{ response ->
            println("수신 성공: $response")
            success(response.getString("success").toBoolean())

        }, Response.ErrorListener {error ->
            println("수신 에러: $error")
            success(false)
        }){

        }

        Volley.newRequestQueue(context).add(loginRequest)
    }


    fun signup(context: Context, jsonObject: JSONObject, success: (Boolean)->Unit){

        var signupRequest = object : JsonObjectRequest(Request.Method.POST,"http://192.168.22.31:3000/signup", jsonObject, Response.Listener{ response ->
            println("서버 수신: $response")
            success(response.getString("success").toBoolean())

        }, Response.ErrorListener { error ->
            println("수신 에러: $error")
            success(false)
        }){
        }
        Volley.newRequestQueue(context).add(signupRequest)
    }
}