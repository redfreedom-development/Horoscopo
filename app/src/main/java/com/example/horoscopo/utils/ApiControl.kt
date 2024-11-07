package com.example.horoscopo.utils

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ApiControl() {

    private lateinit var con: HttpsURLConnection

    fun conexion_api(url:URL,metodo:String):Int {


        con = url.openConnection() as HttpsURLConnection
        con.requestMethod = metodo
        val responseCode = con.responseCode
        Log.i("HTTP", "Response Code :: $responseCode")


        return responseCode
    }

    fun leer_api(): StringBuffer{

        val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
        var inputLine: String?
        val response = StringBuffer()
        while (bufferedReader.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }

       //bufferedReader.close()
        return response
    }


}