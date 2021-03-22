package com.example.projet.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet.*
import com.example.projet.ui.gallery.GalleryFragment
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.String as String1

@Suppress("NAME_SHADOWING", "DEPRECATION", "LocalVariableName")
class HomeFragment : Fragment() {



    private lateinit var accesLocal : AccesLocal
    private lateinit var recyclerView: RecyclerView
    private var parentActuel:Int =0
    private var enfantActuel:Int =0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        accesLocal = AccesLocal(activity?.applicationContext)
        accesLocal.suppressionTotal()
        val buttonRefresh: Button = root.findViewById(R.id.Refresh_list)
        val buttonNumber: Button = root.findViewById(R.id.BoutonNombre)
        val reponseQuestion: Button = root.findViewById(R.id.ReponseQuestion)

        val textView: TextView = root.findViewById(R.id.text_home)

        val registrationForm1 =  JSONObject()
        try {registrationForm1.put("subject", "lire_tous");}
        catch (e: JSONException) {e.printStackTrace();}

        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            registrationForm1.toString()
        )
        requestSynchro("http://ns328061.ip-37-187-112.eu:5000", body)
        if(accesLocal.number ==0) {accesLocal.ajout(Question(0, 1, "Quest ce que cette app ?", 0,"ADMINN"))}
        recyclerView = root.findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.itemAnimator = SlideUpItemAnimator()
        recyclerView.adapter = activity?.applicationContext?.let { RandomNumListAdapter(it) }
        this.configureOnClickRecyclerView(textView)
        reponseQuestion.setOnClickListener {
            val fragment = GalleryFragment()
            val arguments = Bundle()
            arguments.putInt("NumeroQuestion", enfantActuel)
            val registrationForm1 =  JSONObject()
            try {registrationForm1.put("subject", "lire_tous");}
            catch (e: JSONException) {e.printStackTrace();}

            val body: RequestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                registrationForm1.toString()
            )
            requestSynchro("http://ns328061.ip-37-187-112.eu:5000", body)
            fragment.arguments = arguments
            requireFragmentManager().beginTransaction()
                .replace((requireView().parent as ViewGroup).id, fragment)
                .addToBackStack(null)
                .commit()

        }


        buttonRefresh.setOnClickListener {

                view ->Snackbar.make(
            view,
            "En beta",
            Snackbar.LENGTH_LONG
        ).setAction("Action", null).show()
        val registrationForm1  =  JSONObject()
         try {registrationForm1.put("subject", "lire_tous");}
         catch (e: JSONException) {e.printStackTrace();}

         val body: RequestBody = RequestBody.create(
             MediaType.parse("application/json; charset=utf-8"),
             registrationForm1.toString()
         )
            requestSynchro("http://ns328061.ip-37-187-112.eu:5000", body)     //<-Refresh my local database
         Thread.sleep(1_000)
         recyclerView.adapter=null
         recyclerView.layoutManager = LinearLayoutManager(root.context)
         recyclerView.adapter = activity?.applicationContext?.let { RandomNumListAdapter(it) }
         recyclerView.adapter?.notifyDataSetChanged()
         Snackbar.make(view, "Base de données rafraichit", Snackbar.LENGTH_LONG).setAction(
             "Action",
             null
         ).show()

        }

        buttonNumber.setOnClickListener {
            val registrationForm1 =  JSONObject()
            try {registrationForm1.put("subject", "nombre");}
            catch (e: JSONException) {e.printStackTrace();}

            val body: RequestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                registrationForm1.toString()
            )

            view?.let { it1 -> Snackbar.make(it1, "Post creation body, wait", Snackbar.LENGTH_LONG).setAction(
                "Action",
                null
            ).show() }
            requestNumber("http://ns328061.ip-37-187-112.eu:5000", body)
        }
        return root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun configureOnClickRecyclerView(textView: TextView) {
        ItemClickSupport.addTo(recyclerView, R.layout.frame_textview).setOnItemClickListener { recyclerView, position, v ->
            Snackbar.make(
                v,
                "Numéro $position",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()
            recyclerView.adapter=null
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = activity?.applicationContext?.let { RandomNumListAdapter(it) }
            (recyclerView.adapter as RandomNumListAdapter?)?.Etat=1
            (recyclerView.adapter as RandomNumListAdapter?)?.positionnement=position
            (recyclerView.adapter as RandomNumListAdapter?)?.parentActuel=parentActuel
            recyclerView.adapter?.notifyDataSetChanged()


            Log.e(
                "HomeChangementTexte", "Enfant actuel" + accesLocal.NumFils(
                    parentActuel,
                    position + 1
                )
            )
            textView.text=accesLocal.rcmpNumbers(accesLocal.NumFils(parentActuel, position + 1)).toString()
            parentActuel=accesLocal.NumFils(parentActuel, position + 1)
            enfantActuel=parentActuel
        }
    }

    private fun requestSynchro(postUrl: String1, postBody: RequestBody) {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(postUrl)
            .post(postBody)
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
                    val str_response = response.body()!!.string()
                    val jsonstr = JSONObject(str_response)
                    val ToutsLesEntree: JSONArray = jsonstr.getJSONArray("resultat")
                    accesLocal = AccesLocal(activity?.applicationContext)
                    try {
                        for (i in 0 until ToutsLesEntree.length()) {
                            val Actual = ToutsLesEntree.getJSONArray(i)
                            val Parent: Int = Actual.getInt(0)
                            val Fils: Int = Actual.getInt(1)
                            val Contenu = Actual.getString(2)
                            val Rang: Int = Actual.getInt(3)
                            val Username: kotlin.String? = Actual.getString(4)

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

    private fun requestNumber(postUrl: kotlin.String, postBody: RequestBody) {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(postUrl)
            .post(postBody)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                Log.d("FAIL", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val str_response = response.body()!!.string()
                val jsonstr = JSONObject(str_response)
                val ToutesLesEntree: JSONArray = jsonstr.getJSONArray("nombre")
                Thread.sleep(3_000)
                try {
                    val Actuel = ToutesLesEntree.getJSONArray(0)
                    val numero: Int = Actuel.getInt(0)
                    view?.let {
                        Snackbar.make(it, "Nombre d'entrée=$numero", Snackbar.LENGTH_LONG)
                            .setAction(
                                "Action",
                                null
                            ).show()
                    }
                    Thread.sleep(3_000)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

}