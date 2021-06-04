package com.keskheu.screens.formulaire_question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FormulaireQuestionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Formulaire de question:"
    }
    val text: LiveData<String> = _text
}