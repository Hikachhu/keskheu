package com.keskheu.screens.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
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
import com.google.android.material.snackbar.Snackbar
import com.keskheu.*
import com.keskheu.database.AccesLocal
import com.keskheu.database.Question
import com.keskheu.recyclerView.RandomNumListAdapter
import com.keskheu.recyclerView.SlideUpItemAnimator
import com.keskheu.screens.formulaire_question.FormulaireQuestionFragment
import org.json.JSONException
import org.json.JSONObject
import com.keskheu.api.Recuperation.requestNumber
import com.keskheu.api.Recuperation.requestSynchro
import com.keskheu.recyclerView.ItemClickSupport


class HomeFragment : Fragment() {

    private lateinit var accesLocal : AccesLocal
    private lateinit var recyclerView: RecyclerView
    private var parentActuel:Int =0
    private var enfantActuel:Int =0

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        accesLocal = context?.let { AccesLocal(it) }!!
        accesLocal.suppressionTotal()
        val buttonRefresh: Button = root.findViewById(R.id.Refresh_list)
        val buttonNumber: Button = root.findViewById(R.id.BoutonNombre)
        val reponseQuestion: Button = root.findViewById(R.id.ReponseQuestion)

        val textView: TextView = root.findViewById(R.id.text_home)


        requestSynchro(requireContext())
        Thread.sleep(4_000)
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
        recyclerView.adapter = context?.applicationContext?.let { RandomNumListAdapter(it) }

        requestSynchro(requireContext())     //<-Refresh my local database
        recyclerView.adapter=null
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.adapter = context?.applicationContext?.let { RandomNumListAdapter(it) }
        recyclerView.adapter?.notifyDataSetChanged()

        this.configureOnClickRecyclerView(textView)
        reponseQuestion.setOnClickListener {
            arguments?.putInt("NumeroQuestion", enfantActuel)
            val registrationForm1 =  JSONObject()
            try {registrationForm1.put("subject", "lire_tous");}
            catch (e: JSONException) {e.printStackTrace();}
            requestSynchro(requireContext())
            val fragment = FormulaireQuestionFragment()
            val arguments = Bundle()
            changeScreen(fragment, arguments)

        }


        buttonRefresh.setOnClickListener {
            view ->Snackbar.make(view,"En beta",Snackbar.LENGTH_LONG).setAction("Action", null).show()
            refreshRecyclerView(root)
            Snackbar.make(view, "Base de données rafraichit", Snackbar.LENGTH_LONG).setAction("Action",null).show()

        }

        buttonNumber.setOnClickListener {

            view?.let { it1 -> Snackbar.make(it1, "Post creation body, wait", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
            requestNumber()
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
    private fun refreshRecyclerView(root:View){
        requestSynchro(requireContext())
        Thread.sleep(1_000)
        recyclerView.adapter=null
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.adapter = activity?.applicationContext?.let { RandomNumListAdapter(it) }
        recyclerView.adapter?.notifyDataSetChanged()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun configureOnClickRecyclerView(textView: TextView) {
        ItemClickSupport.addTo(recyclerView, R.layout.frame_textview).setOnItemClickListener { recyclerView, position, _ ->

            recyclerView.adapter=null
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = activity?.applicationContext?.let { RandomNumListAdapter(it) }
            (recyclerView.adapter as RandomNumListAdapter?)?.Etat=1
            (recyclerView.adapter as RandomNumListAdapter?)?.positionnement=position
            (recyclerView.adapter as RandomNumListAdapter?)?.parentActuel=parentActuel
            recyclerView.adapter?.notifyDataSetChanged()

            textView.text=accesLocal.rcmpNumbers(accesLocal.numFils(parentActuel, position + 1)).toString()
            parentActuel=accesLocal.numFils(parentActuel, position + 1)
            enfantActuel=parentActuel
        }
    }



}