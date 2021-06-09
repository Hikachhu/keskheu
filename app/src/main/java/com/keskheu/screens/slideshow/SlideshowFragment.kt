package com.keskheu.screens.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.keskheu.MainActivity
import com.keskheu.R
import com.keskheu.USERNAME
import com.keskheu.Utils.toMD5
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var ip="http://ns328061.ip-37-187-112.eu:5000"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel = ViewModelProvider(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        val textView: TextView = root.findViewById(R.id.Text_informatif)
        slideshowViewModel.lu.observe(viewLifecycleOwner, {
            textView.text = it
        })

        val username = root.findViewById<EditText>(R.id.Username)
        val mdp = root.findViewById<EditText>(R.id.Mdp)

        val creationAccount = root.findViewById<Button>(R.id.Creation)
        val connectionAccount = root.findViewById<Button>(R.id.Connection)

        connectionAccount.setOnClickListener {
            val nomUtilisateur = username.text.toString().trim()
            val motdepasse= mdp.text.toString().trim().toMD5()
            val registrationForm1 = JSONObject()
            try {
                registrationForm1.put("subject", "Connection")
                registrationForm1.put("Username", nomUtilisateur)
                registrationForm1.put("Mdp", motdepasse)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val body:RequestBody  = registrationForm1.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            postRequest(body, nomUtilisateur)


        }

        creationAccount.setOnClickListener {
            val nomUtilisateur = username.text.toString().trim()
            val motdepasse= mdp.text.toString().trim()
            Log.e("MD5",motdepasse)
            val registrationForm1 = JSONObject()
            try {
                registrationForm1.put("subject", "Creation")
                registrationForm1.put("Username", nomUtilisateur)
                registrationForm1.put("Mdp", motdepasse)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val body:RequestBody  = registrationForm1.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            postRequest(body, nomUtilisateur)

        }

        return root
    }

    private fun postRequest(postBody: RequestBody?, username: String?) {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(ip)
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
                    view?.let {
                        if (responseString != null) {
                            Snackbar.make(it, responseString, Snackbar.LENGTH_LONG).setAction(
                                "Action",
                                null
                            ).show()
                        }
                    }
                    if (responseString == "Invalide") {

                        view?.let {Snackbar.make(it, "Mot de passe invalide", Snackbar.LENGTH_LONG).setAction("Action",null).show()}
                    } else {
                        view?.let {
                            if (responseString != null) {
                                Snackbar.make(it, "Mot de passe Valide", Snackbar.LENGTH_LONG).setAction("Action",null).show()}
                        }
                        if (username != null) {
                            USERNAME = username
                            (activity as MainActivity).changeUsername()
                        }

                    }
                } catch (e: Exception) {

                }
            }
        })
    }
}