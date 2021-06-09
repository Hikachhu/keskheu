package com.keskheu.screens.home

import android.R.layout
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.keskheu.R
import com.keskheu.api.Question
import com.keskheu.api.Recuperation
import com.keskheu.database.AccesLocal
import com.keskheu.recyclerView.ItemClickSupport
import com.keskheu.recyclerView.RandomNumListAdapter
import com.keskheu.screens.formulaire_question.FormulaireQuestionFragment
import com.squareup.picasso.Picasso
import drewcarlson.coingecko.CoinGeckoClient
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


class HomeFragment : Fragment() {

    private lateinit var accesLocal : AccesLocal
    private lateinit var recyclerView: RecyclerView
    private var parentActuel:Int =0
    private var enfantActuel:Int =0
    private var systemApi=Recuperation
    private lateinit var reponseQuestion: Button
    private var listeQuestion= arrayListOf<Question>()

    @SuppressLint("ResourceType", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("Affichage","Debut affichage Home")

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        accesLocal = context?.let { AccesLocal(it) }!!
        accesLocal.suppressionTotal()

        val buttonRefresh: Button = root.findViewById(R.id.Refresh_list)
        val buttonNumber: Button = root.findViewById(R.id.BoutonNombre)
        val imageAffiche: ImageView = root.findViewById(R.id.imageActuel)
        reponseQuestion = root.findViewById(R.id.ReponseQuestion)
        val textView: TextView = root.findViewById(R.id.text_home)
        textView.text="Bienvenu sur Kesheu"
        listeQuestion=systemApi.requestSynchro()
        listeQuestion.forEach { accesLocal.ajout(it) }

        if(accesLocal.number ==0) {accesLocal.ajout(Question(0, 1, "Quest ce que cette app ?", 0,"ADMINN"))}
        recyclerView = root.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        var adapter =context?.applicationContext?.let { RandomNumListAdapter(it) }
        recyclerView.adapter = adapter
        val resId: Int =R.anim.layout_animation_fall_down
        val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation( context, resId)
        recyclerView.layoutAnimation = animation

        lifecycleScope.launch {
            val data = CoinGeckoClient.create()

            try {
                Picasso.with(context).load("").into(imageAffiche);
            } catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
        }
        this.configureOnClickRecyclerView(textView)
        reponseQuestion.setOnClickListener {
            arguments?.putInt("NumeroQuestion", enfantActuel)
            val registrationForm1 =  JSONObject()
            try {registrationForm1.put("subject", "lire_tous");}
            catch (e: JSONException) {e.printStackTrace();}
            listeQuestion=systemApi.requestSynchro()
            listeQuestion.forEach { accesLocal.ajout(it) }
            val fragment = FormulaireQuestionFragment()
            val arguments = Bundle()
            changeScreen(fragment, arguments)

        }


        buttonRefresh.setOnClickListener {
            view ->Snackbar.make(view,"En beta",Snackbar.LENGTH_LONG).setAction("Action", null).show()
            refreshRecyclerView()
            Snackbar.make(view, "Base de données rafraichit", Snackbar.LENGTH_LONG).setAction("Action",null).show()

        }

        buttonNumber.setOnClickListener {

            view?.let { it1 -> Snackbar.make(it1, "Post creation body, wait", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
            listeQuestion=systemApi.requestSynchro()
            listeQuestion.forEach { accesLocal.ajout(it) }
        }
        return root
    }
    private fun changeScreen(fragment: Fragment, arguments : Bundle){
        fragment.arguments = arguments
        parentFragmentManager.beginTransaction()
            .replace((requireView().parent as ViewGroup).id, fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun refreshRecyclerView() {
        listeQuestion=systemApi.requestSynchro()
        listeQuestion.forEach { accesLocal.ajout(it) }
        recyclerView.adapter?.notifyDataSetChanged()

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun configureOnClickRecyclerView(textView: TextView) {
        ItemClickSupport.addTo(recyclerView, R.layout.frame_textview).setOnItemClickListener { recyclerView, position, _ ->

            (recyclerView.adapter as RandomNumListAdapter?)?.Etat=1
            (recyclerView.adapter as RandomNumListAdapter?)?.positionnement=position
            (recyclerView.adapter as RandomNumListAdapter?)?.parentActuel=parentActuel
            recyclerView.adapter?.notifyDataSetChanged()

            textView.text=accesLocal.rcmpNumbers(accesLocal.numFils(parentActuel, position + 1)).toString()
            parentActuel=accesLocal.numFils(parentActuel, position + 1)
            enfantActuel=parentActuel
            reponseQuestion.text="Répondre à la question"
        }
    }



}