package com.keskheu.api

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.keskheu.MainActivity
import com.keskheu.USERNAME
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
                            val strResponse = response.body!!.string()
                            val jsonstr = JSONObject(strResponse)
                            val listeEntrees: JSONArray = jsonstr.getJSONArray("resultat")
                            try {
                                for (i in 0 until listeEntrees.length()) {
                                    val actuel = listeEntrees.getJSONArray(i)
                                    val parent: Int = actuel.getInt(0)
                                    val fils: Int = actuel.getInt(1)
                                    val contenu = actuel.getString(2)
                                    val rang: Int = actuel.getInt(3)
                                    val username: String? = actuel.getString(4)
                                    listQuestion.add(Question(parent,fils,contenu,rang,username))

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

    fun demandeConnection(postBody: RequestBody?, username: String?, view: View) {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(ipServer)
            .post(postBody!!)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                Log.d("FAIL", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {

                try {
                    val responseString: String? = response.body?.string()
                    view.let {
                        if (responseString != null) {
                            Snackbar.make(it, responseString, Snackbar.LENGTH_LONG).setAction("Action",null).show()
                        }
                    }
                    if (responseString == "Invalide") {

                        view.let { Snackbar.make(it, "Mot de passe invalide", Snackbar.LENGTH_LONG).setAction("Action",null).show()}
                    } else if(responseString=="Valide"){
                        view.let {
                            Snackbar.make(it, "Mot de passe Valide", Snackbar.LENGTH_LONG).setAction("Action",null).show()
                        }
                        if (username != null) {
                            USERNAME = username
                            val main= MainActivity()
                            main.changeUsername()
                        }

                    }else if(responseString=="AlreadyUse"){
                        view.let { Snackbar.make(it, "Username deja utilisé", Snackbar.LENGTH_LONG).setAction("Action",null).show()}
                    }
                    else{
                        view.let { Snackbar.make(it, responseString.toString(), Snackbar.LENGTH_LONG).setAction("Action",null).show()}

                    }
                } catch (e: Exception) {

                }
            }
        })
    }
}