package com.projet.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.projet.R
import com.google.android.material.snackbar.Snackbar
import com.projet.USERNAME
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel = ViewModelProvider(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        val textView: TextView = root.findViewById(R.id.Text_informatif)
        slideshowViewModel.lu.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val Username = root.findViewById<EditText>(R.id.Username)
        val Mdp = root.findViewById<EditText>(R.id.Mdp)

        val CreationAccount = root.findViewById<Button>(R.id.Creation)
        val ConnectionAccount = root.findViewById<Button>(R.id.Connection)

        ConnectionAccount.setOnClickListener {
            var username = Username.text.toString().trim()
            var mdp= Mdp.text.toString().trim()
            val registrationForm1 = JSONObject()
            try {
                registrationForm1.put("subject", "Connection");
                registrationForm1.put("Username", username);
                registrationForm1.put("Mdp", mdp);
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val body:RequestBody  = registrationForm1.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull());

            postRequest("http://ns328061.ip-37-187-112.eu:5000", body, username);

        }

        CreationAccount.setOnClickListener {
            var username = Username.text.toString().trim()
            var mdp= Mdp.text.toString().trim()
            val registrationForm1 = JSONObject()
            try {
                registrationForm1.put("subject", "Creation");
                registrationForm1.put("Username", username);
                registrationForm1.put("Mdp", mdp);
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val body:RequestBody  = registrationForm1.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            postRequest("http://ns328061.ip-37-187-112.eu:5000", body, username);

        }

        return root
    }
    private fun postRequest(postUrl: String?, postBody: RequestBody?, username: String?) {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(postUrl.toString())
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

                        view?.let {
                            Snackbar.make(it, "Mot de passe invalide", Snackbar.LENGTH_LONG)
                                .setAction(
                                    "Action",
                                    null
                                ).show()
                        }
                    } else {

                        view?.let {
                            if (responseString != null) {
                                Snackbar.make(it, "Mot de passe Valide", Snackbar.LENGTH_LONG).setAction("Action",null).show()}
                        }
                        if (username != null) {
                            USERNAME = username
                        }

                    }
                } catch (e: Exception) {

                }
            }
        })
    }
}