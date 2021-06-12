package com.keskheu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keskheu.api.Recuperation
import com.keskheu.recyclerView.listPM.AdapterListPM


class ConnecteFragment : Fragment() {

    private var systemApi= Recuperation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_connecte, container, false)
        val usernameConnecte=root.findViewById(R.id.usernameDisplay) as TextView
        val textMessage=root.findViewById(R.id.textMessage) as EditText
        val boutonEnvoiMessage=root.findViewById(R.id.boutonEnvoiPM) as Button
        val recyclerView:RecyclerView=root.findViewById(R.id.recyclerviewListPM)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        val listPrivateMessage=systemApi.requestListPM(IdUser)
        Log.e("Apres api",listPrivateMessage.toString())
        val adapter =context?.applicationContext?.let { AdapterListPM(it,listPrivateMessage) }
        recyclerView.adapter = adapter
        val resId: Int =R.anim.layout_animation_fall_down
        val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation( context, resId)
        recyclerView.layoutAnimation = animation
        usernameConnecte.text= USERNAME
        var account2="ERROR"
        val spinnerRegion = root.findViewById(R.id.listDestinataire) as Spinner
        val lRegion = Recuperation.listAccount()
        val dataAdapterR: ArrayAdapter<String>? =
            context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, lRegion) }
        dataAdapterR?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRegion.adapter = dataAdapterR
        spinnerRegion.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                account2=lRegion[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        boutonEnvoiMessage.setOnClickListener {

            view?.let { it1 ->
                systemApi.insertPM(account2,IdUser,textMessage.text.toString(),
                    it1
                )
            }
        }
        return root
    }

    private fun changeScreen(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace((requireView().parent as ViewGroup).id, fragment)
            .addToBackStack(null)
            .commit()
    }
}