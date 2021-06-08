package com.keskheu.screens.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
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
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment() {

    private lateinit var accesLocal : AccesLocal
    private lateinit var recyclerView: RecyclerView
    private var parentActuel:Int =0
    private var enfantActuel:Int =0
    private var systemApi=Recuperation
    private lateinit var reponseQuestion: Button

    private var listeQuestion= arrayListOf<Question>()

    @SuppressLint("ResourceType")
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
        reponseQuestion = root.findViewById(R.id.ReponseQuestion)
        val textView: TextView = root.findViewById(R.id.text_home)
        Thread.sleep(900)
        listeQuestion=systemApi.requestSynchro()
        listeQuestion.forEach { accesLocal.ajout(it) }

        if(accesLocal.number ==0) {accesLocal.ajout(Question(0, 1, "Quest ce que cette app ?", 0,"ADMINN"))}
        recyclerView = root.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        val resId: Int =R.anim.layout_animation_fall_down

        val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation( context, resId)
        recyclerView.layoutAnimation = animation
        recyclerView.adapter = context?.applicationContext?.let { RandomNumListAdapter(it) }

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
            reponseQuestion.text="Répondre à la question actuelle"
        }
    }



}