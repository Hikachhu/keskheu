package com.projet.ui.formulaire_question

import android.os.Bundle
import android.text.InputType
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
import com.projet.AccesLocal
import com.projet.Question
import com.projet.R
import com.projet.USERNAME
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class FormulaireQuestionFragment : Fragment() {

    private lateinit var galleryViewModel: FormulaireQuestionViewModel
    private lateinit var accesLocal : AccesLocal
    private lateinit var question : Question
    private var NumeroQuestion:Int = 0
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(FormulaireQuestionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_formulaire_question, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val username = root.findViewById<EditText>(R.id.Question_utilisateur)
        val boutton = root.findViewById<Button>(R.id.button_entree_utlisateur)

        username.inputType = InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

        val bundle = this.arguments
        if (bundle != null) {
             NumeroQuestion = bundle.getInt("NumeroQuestion")
            Log.e("Recup Bundle", "NumeroQuestion:" + NumeroQuestion)
        }

        boutton.setOnClickListener {
                view ->Snackbar.make(view,"Test de connection",Snackbar.LENGTH_LONG).setAction("Action", null).show()
            if(USERNAME !="Unconnected"){
                accesLocal = AccesLocal(activity?.applicationContext)
                val QuestionUtilisation: Question = Question(
                    NumeroQuestion,
                    accesLocal.getNumber() + 1,
                    username.text.toString(),
                    1,
                    USERNAME
                )
                val registrationForm1: JSONObject = JSONObject()
                try {
                    registrationForm1.put("subject", "ecrire");
                    registrationForm1.put("Contenu", QuestionUtilisation.Contenu);
                    registrationForm1.put("Parent", QuestionUtilisation.Parent);
                    registrationForm1.put("Fils", QuestionUtilisation.Fils);
                    registrationForm1.put("Rang", QuestionUtilisation.Rang);
                    registrationForm1.put("Username", USERNAME);
                } catch (e: JSONException) {
                    e.printStackTrace();
                }

                val body: RequestBody = registrationForm1.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull());
                postRequest("http://ns328061.ip-37-187-112.eu:5000", body);
                Snackbar.make(view,"Question ajoutée avec succès en tant que "+ USERNAME,Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }else{
                Snackbar.make(view,"Veuillez vous connecter",Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }

        }
        return root
    }
    fun postRequest(postUrl: String?, postBody: RequestBody?) {
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
                //    responseText.text = "Failed to Connect to Server. Please Try Again."
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseString: String? = response.body?.string()

                    when (responseString) {
                        "success" -> {
                            view?.let {
                                Snackbar.make(
                                        it,
                                        "yes|" + responseString,
                                        Snackbar.LENGTH_LONG
                                ).setAction("Action", null).show()
                            }
                        }
                        "failure" -> {
                            view?.let {
                                Snackbar.make(
                                        it,
                                        "fail|" + responseString,
                                        Snackbar.LENGTH_LONG
                                ).setAction("Action", null).show()
                            }
                        }
                        else -> {
                            view?.let {
                                Snackbar.make(
                                        it,
                                        "other|" + responseString,
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