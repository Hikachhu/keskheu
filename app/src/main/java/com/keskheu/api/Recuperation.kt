package com.keskheu.api

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.keskheu.IdUser
import com.keskheu.MainActivity
import com.keskheu.R
import com.keskheu.USERNAME
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.notify
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

@SuppressLint("StaticFieldLeak")
object Recuperation {

    private var ipServer="http://ns328061.ip-37-187-112.eu:5000"
    private var DernierAppel=(System.currentTimeMillis()/1000).toInt()

    fun requestListPM(account1:Int,account2: Int): ArrayList<PrivateMessage>{
        val recupAPI = RetourApi(0)
        var list=requestListPMC(account1,account2,recupAPI)
        Log.e("listAccount", list.toString())
        return list
    }

    fun listAccount(): ArrayList<String>{
        val recupAPI = RetourApi(0)
        return listAccountC(recupAPI)
    }

    fun demandeConnection(postBody: RequestBody?, username: String?, view: View): Int {
        val recupAPI=RetourApi(0)
        demandeConnectionC(postBody,username,view,recupAPI)
        while(recupAPI.retour==0){
            Thread.sleep(300)
        }
        return if(recupAPI.retour==2){
            0
        } else {
            1
        }
    }

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

    private fun  listAccountC(ret:RetourApi): ArrayList<String> {
        ret.retour=0
        val listAccount = arrayListOf<String>()
        val registrationForm1  =  JSONObject()
        try {registrationForm1.put("subject", "listAccount");}
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
                        val listeEntrees: JSONArray = jsonstr.getJSONArray("account")
                        try {
                            for (i in 0 until listeEntrees.length()) {
                                val actuel = listeEntrees.getJSONArray(i)
                                listAccount.add(actuel.getString(0))

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
        return listAccount
    }
     private fun  requestListPMC(account1:Int, account2: Int, ret:RetourApi): ArrayList<PrivateMessage> {

        val listPrivateMessage = arrayListOf<PrivateMessage>()
        val registrationForm1  =  JSONObject()
        try {
            registrationForm1.put("subject", "RecupPM");
            registrationForm1.put("Account1", account1)
            registrationForm1.put("Account2", account2)
        }
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
                                val account1: String = actuel.getString(0)
                                val account2: String = actuel.getString(1)
                                val message = actuel.getString(2)
                                listPrivateMessage.add(PrivateMessage(account1,account2,message))
                                Log.e("Liste private message:",
                                    "account1 =$account1 account2 =$account2 message=$message"
                                )

                            }
                            ret.retour=1
                        } catch (e: JSONException) {
                            e.printStackTrace()

                            ret.retour=1
                        }


                    } catch (e: Exception) {

                        ret.retour=1
                        Log.e("reseau", "echec in requestSynchro",e)
                    }
                }
            })
        }
         while (ret.retour != 1) {
             Thread.sleep(300)
         }
        return listPrivateMessage
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

    private fun demandeConnectionC(
        postBody: RequestBody?,
        username: String?,
        view: View,
        ret: RetourApi
    ) {
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

            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call, response: Response) {

                try {
                    val responseString: Int? = response.body?.string()?.toInt()
                    view.let {
                        if (responseString != null) {
                            Snackbar.make(it, responseString.toString(), Snackbar.LENGTH_LONG).setAction("Action",null).show()
                        }
                    }
                    if (responseString != null) {
                        when {
                            responseString==-1 -> {

                                view.let { Snackbar.make(it, "Mot de passe invalide", Snackbar.LENGTH_LONG).setAction("Action",null).show()}
                                ret.retour=1

                            }
                            responseString>0 -> {
                                view.let {
                                    Snackbar.make(it, "Mot de passe Valide", Snackbar.LENGTH_LONG).setAction("Action",null).show()
                                    if (username != null) {
                                        USERNAME = username
                                        IdUser=responseString
                                        Log.e("Changement IdUser","Nouvel id = $IdUser")
                                        ret.retour=2
                                    }
                                }


                            }
                            responseString==-2 -> {
                                view.let { Snackbar.make(it, "Username deja utilisé", Snackbar.LENGTH_LONG).setAction("Action",null).show()}
                                ret.retour=1
                            }
                            else -> {
                                view.let { Snackbar.make(it, responseString.toString(), Snackbar.LENGTH_LONG).setAction("Action",null).show()}
                                ret.retour=1

                            }
                        }
                    }


                } catch (e: Exception) {
                    Log.e("Erreur dans la connection",e.toString())
                    ret.retour=1
                }
            }
        })
    }



     fun insertPM(account1:String,account2: Int,message:String,view: View) {
        val registrationForm1 = JSONObject()
        try {
            registrationForm1.put("subject", "InsertPM")
            registrationForm1.put("Account1", account1)
            registrationForm1.put("Account2", account2)
            registrationForm1.put("Message", message)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body: RequestBody = registrationForm1.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(ipServer)
            .post(body)
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

                    when (val responseString: String? = response.body?.string()) {
                        "OK" -> {
                            view?.let {
                                Snackbar.make(
                                    it,
                                    "yes|$responseString",
                                    Snackbar.LENGTH_LONG
                                ).setAction("Action", null).show()
                            }
                        }

                        else -> {
                            view?.let {
                                Snackbar.make(
                                    it,
                                    "other|$responseString",
                                    Snackbar.LENGTH_LONG
                                ).setAction("Action", null).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }
}