package com.example.projet.ui.slideshow

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projet.AccesLocal
import com.example.projet.Question

class SlideshowViewModel(application:Application) : AndroidViewModel(application) {


    private lateinit var accesLocal : AccesLocal
    private lateinit var question : Question
    private lateinit var mContext: Context
    private val applicationContext: Context = application.applicationContext

    private val _text = MutableLiveData<String>().apply {
        accesLocal= AccesLocal(applicationContext)
        value = "Gestion de compte"
    }
    val NbrQuestion: LiveData<String> = _text

    private val _Demande_question = MutableLiveData<String>().apply {
        accesLocal= AccesLocal(applicationContext)
        value = "Quelle question souhaitez vous consulter ?"
    }
    val DemandeQuestion: LiveData<String> = _Demande_question

    private val _lu = MutableLiveData<String>().apply {
        accesLocal= AccesLocal(applicationContext)
        question=accesLocal.rcmpDenied()
        value="Derniere question posée: "+question.Contenu;
    }
    val lu: LiveData<String> = _lu


}