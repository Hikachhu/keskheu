package com.keskheu.api

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

object Recuperation {
    private var ipServer="http://ns328061.ip-37-187-112.eu:5000"
    private var DernierAppel=(System.currentTimeMillis()/1000).toInt()
    fun ping(): String {
        while (DernierAppel+3<(System.currentTimeMillis()/1000).toInt()){
            Thread.sleep(250)
        }
        val recupAPI= RetourApi(0)
        val valeur=pingC(recupAPI)
        Log.e("Retour ping",valeur)
        return valeur
    }

    fun requestSynchro(): ArrayList<Question> {
        val recupAPI = RetourApi(0)
        DernierAppel= (System.currentTimeMillis()/1000).toInt()
        Log.e("Appel", DernierAppel.toString())
        return requestSynchroC(recupAPI)
    }

    fun requestNumber(): Int {
        val recupAPI=RetourApi(0)
        val valeur = requestNumberC(recupAPI)
        while(recupAPI.retour!=1){
            Thread.sleep(300)
        }
        return valeur

    }

    private fun pingC(ret:RetourApi): String {
        var reponsePing="KO"
        val registrationForm1  =  JSONObject()
        try {registrationForm1.put("subject", "ping");}
        catch (e: JSONException) {e.printStackTrace();}
        val body: RequestBody = registrationForm1.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(ipServer)
            .post(body)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .addHeader("Connection","close")
            .build()
        client.run {
            newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    call.cancel()
                    Log.d("FAIL", e.message.toString())
                    ret.retour=1
                }
                override fun onResponse(call: Call, response: Response) {
                    val strResponse = response.body!!.string()
                    reponsePing=strResponse
                    Log.e("Ping",strResponse)
                    ret.retour=1
                }
            })
        }
        while(ret.retour!=1){
            Thread.sleep(300)
        }
        Log.e("avant retour ping",reponsePing)
        return reponsePing
    }

    private fun  requestSynchroC(ret:RetourApi): ArrayList<Question> {
        ret.retour=0
        val listQuestion = arrayListOf<Question>()
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
            .addHeader("Connection","close")
            .build()
            client.run {
                newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        call.cancel()
                        Log.d("FAIL", e.message.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val str_response = response.body!!.string()
                            val jsonstr = JSONObject(str_response)
                            val ToutsLesEntree: JSONArray = jsonstr.getJSONArray("resultat")
                            try {
                                for (i in 0 until ToutsLesEntree.length()) {
                                    val Actual = ToutsLesEntree.getJSONArray(i)
                                    val Parent: Int = Actual.getInt(0)
                                    val Fils: Int = Actual.getInt(1)
                                    val Contenu = Actual.getString(2)
                                    val Rang: Int = Actual.getInt(3)
                                    val Username: String? = Actual.getString(4)
                                    listQuestion.add(
                                        Question(
                                            Parent,
                                            Fils,
                                            Contenu,
                                            Rang,
                                            Username
                                        )
                                    )

                                }
                                ret.retour = 1
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        } catch (e: Exception) {
                            ret.retour = 1
                            Log.e("reseau", "echec in requestSynchro",e)
                            }
                    }
                })
            }
            while (ret.retour != 1) {
                Thread.sleep(300)
            }
        return listQuestion
    }

    private fun requestNumberC(ret:RetourApi) :Int{
        ret.retour=0
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
                ret.retour=1
            }
            override fun onResponse(call: Call, response: Response) {
                var toutesLesEntree: JSONArray? =null
                Log.e("Response nombre", response.toString())
                try {
                    val strResponse : String = response.body!!.string()

                    val jsonstr = JSONObject(strResponse)
                    toutesLesEntree = jsonstr.getJSONArray("nombre")
                    Thread.sleep(3_000)
                }catch (e:Exception){
                    Log.e("Erreur nombre", e.toString())
                }
                try {
                    val actuel = toutesLesEntree?.getJSONArray(0)
                    if (actuel != null) {
                        numero= actuel.getInt(0)
                    }
                    Thread.sleep(3_000)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                ret.retour=1
            }

        })
        while(ret.retour!=1){
            Thread.sleep(300)
        }
        return numero
    }
}