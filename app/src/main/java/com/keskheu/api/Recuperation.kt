package com.keskheu.api

import android.content.Context
import android.util.Log
import com.keskheu.database.AccesLocal
import com.keskheu.database.Question
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

object Recuperation {
     lateinit var accesLocal : AccesLocal
        var ipServer="http://ns328061.ip-37-187-112.eu:5000"

   fun requestSynchro(context: Context) {
       val registrationForm1  =  JSONObject()
       try {registrationForm1.put("subject", "lire_tous");}
       catch (e: JSONException) {e.printStackTrace();}
       val body: RequestBody = registrationForm1.toString()
           .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(ipServer)
            .post(body)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()

        client.run {


            newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    call.cancel()
                    Log.d("FAIL", e.message.toString())
                }
                override fun onResponse(call: Call, response: Response) {
                    val str_response = response.body!!.string()
                    val jsonstr = JSONObject(str_response)
                    val ToutsLesEntree: JSONArray = jsonstr.getJSONArray("resultat")
                    accesLocal = AccesLocal(context)
                    try {
                        for (i in 0 until ToutsLesEntree.length()) {
                            val Actual = ToutsLesEntree.getJSONArray(i)
                            val Parent: Int = Actual.getInt(0)
                            val Fils: Int = Actual.getInt(1)
                            val Contenu = Actual.getString(2)
                            val Rang: Int = Actual.getInt(3)
                            val Username: String? = Actual.getString(4)
                            Log.e(
                                "Ajout données",
                                "taille Donnes:" + accesLocal.number + "Ajout de :" + Fils.toString()
                            )
                            if (accesLocal.number < Fils) {
                                Log.e("DEBUG PTN", "Ajout acutle "+Fils.toString()+" Taille bdd= "+accesLocal.number)
                                accesLocal.run { ajout(Question(Parent, Fils, Contenu, Rang,Username)) }
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    fun requestNumber() :Int{
        val registrationForm1 =  JSONObject()
        try {registrationForm1.put("subject", "nombre");}
        catch (e: JSONException) {e.printStackTrace();}
        val body: RequestBody = registrationForm1.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(ipServer)
            .post(body)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()
        var numero:Int=-1
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                Log.d("FAIL", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val str_response = response.body!!.string()
                val jsonstr = JSONObject(str_response)
                val ToutesLesEntree: JSONArray = jsonstr.getJSONArray("nombre")
                Thread.sleep(3_000)
                try {
                    val Actuel = ToutesLesEntree.getJSONArray(0)
                    numero= Actuel.getInt(0)
                    Thread.sleep(3_000)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
        return numero
    }
}