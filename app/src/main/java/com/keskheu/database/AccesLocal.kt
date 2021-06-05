package com.keskheu.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.keskheu.api.Question
import java.util.*

class AccesLocal(context: Context) {
    private val accesBD: MySQLiteOpenHelper
    private lateinit var bd: SQLiteDatabase
    @SuppressLint("Recycle")
    fun ajout(question: Question) {
        bd = accesBD.writableDatabase
        val recherche="select * from Questions where fils=${question.Fils}"
        val cursor = bd.rawQuery(recherche, null)
        if(cursor.count==0){
            val req = ("insert into Questions(Parent,Fils,Contenu,rang,Username) values "+
                    "(" + question.Parent + "," + question.Fils + ", \"" + question.Contenu + "\"," + question.Rang + ",\"" + question.Username + "\");")
            bd.execSQL(req)
            Log.e("Ajout","succes ajout de $question")
        }else{
            Log.e("ajout","Valeur deja existante")
        }
    }

    fun numFils(NumParent: Int, position: Int): Int {
        var question: Question? = null
        val req = "SELECT  * FROM Questions where Parent=$NumParent"
        bd = accesBD.readableDatabase
        val cursor = bd.rawQuery(req, null)
        cursor.move(position)
        if (!cursor.isAfterLast) {
            val parent = cursor.getInt(0)
            val fils = cursor.getInt(1)
            val contenu = cursor.getString(2)
            val rang = cursor.getInt(3)
            val username = cursor.getString(4)
            question = Question(parent, fils, contenu, rang, username)
        }
        assert(question != null)
        Log.e("NumFils", "Numéro de fils:" + question!!.Fils + " position = " + position)
        cursor.close()
        return question.Fils
    }

    fun listeFils(NumParent: Int): ArrayList<Question> {
        val listeQuestion = ArrayList<Question>()
        var question: Question
        val req = "SELECT  * FROM Questions where Parent=$NumParent"
        bd = accesBD.readableDatabase
        val cursor = bd.rawQuery(req, null)
        cursor.move(1)
        Log.e("ListeFils", "Taille =" + cursor.count + " position = " + cursor.position)
        while (!cursor.isAfterLast) {
            Log.e("TAG", "Taille =" + cursor.position)
            val parent = cursor.getInt(0)
            val fils = cursor.getInt(1)
            val contenu = cursor.getString(2)
            val rang = cursor.getInt(3)
            val username = cursor.getString(4)
            question = Question(parent, fils, contenu, rang, username)
            listeQuestion.add(question)
            cursor.moveToNext()
        }
        cursor.close()
        return listeQuestion
    }

    fun nombreDeRep(NumActuel: Int): Int {
        val req = "SELECT  * FROM Questions where Parent=$NumActuel"
        bd = accesBD.readableDatabase
        val courser = bd.rawQuery(req, null)
        val quantite = courser.count
        Log.e("NombreDeRep", quantite.toString())
        courser.close()
        return quantite
    }

    fun rcmpDenied(): Question? {
        bd = accesBD.readableDatabase
        var question: Question? = null
        val req = "select * from Questions"
        val cursor = bd.rawQuery(req, null)
        cursor.moveToLast()
        if (!cursor.isAfterLast) {
            val parent = cursor.getInt(0)
            val fils = cursor.getInt(1)
            val contenu = cursor.getString(2)
            val rang = cursor.getInt(3)
            val username = cursor.getString(4)
            question = Question(parent, fils, contenu, rang, username)
        }
        cursor.close()
        return question
    }

    fun rcmpNumbers(number: Int?): String? {
        bd = accesBD.readableDatabase
        var question: Question? = null
        val req = "select * from Questions"
        val courser = bd.rawQuery(req, null)
        courser.move(number!!)
        if (!courser.isAfterLast) {
            val parent = courser.getInt(0)
            val fils = courser.getInt(1)
            val contenu = courser.getString(2)
            val rang = courser.getInt(3)
            val username = courser.getString(4)
            question = Question(parent, fils, contenu, rang, username)
        }
        courser.close()
        return if (question == null) "" else question.Contenu
    }

    val number: Int
        get() {
            val countQuery = "SELECT  * FROM Questions"
            bd = accesBD.readableDatabase
            val cursor = bd.rawQuery(countQuery, null)
            val count = cursor.count
            cursor.close()
            return count
        }

    fun getNumber(Position: Int): Int {
        val countQuery = "SELECT  * FROM Questions where parent=$Position"
        bd = accesBD.readableDatabase
        val cursor = bd.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()
        return count
    }

    fun suppressionTotal() {
        val req = "delete from Questions"
        bd = accesBD.readableDatabase
        bd.execSQL(req)
    }

    init {
        val nomBase = "bdKeskheu.sqlite"
        val versionBase = 1
        accesBD = MySQLiteOpenHelper(context, nomBase, null, versionBase)
    }
}