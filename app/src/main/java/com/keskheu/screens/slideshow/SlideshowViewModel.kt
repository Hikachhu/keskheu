package com.keskheu.screens.slideshow

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.keskheu.database.AccesLocal
import com.keskheu.api.Question

class SlideshowViewModel(application:Application) : AndroidViewModel(application) {


    private var accesLocal : AccesLocal
    private var question : Question

    @SuppressLint("StaticFieldLeak")
    private val applicationContext: Context = application.applicationContext

    private val _lu = MutableLiveData<String>().apply {
        accesLocal= AccesLocal(applicationContext)
        question= accesLocal.rcmpDenied()!!
        value="Derniere question posée: "+question.Contenu
    }
    val lu: LiveData<String> = _lu


}