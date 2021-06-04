package com.keskheu.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

//room pour
class MySQLiteOpenHelper(context: Context?, name: String?, factory: CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {
    private val creation = ("create table Questions("
            + "Parent integer,"
            + "Fils integer primary key,"
            + "Contenu varchar(40) not null ,"
            + "rang integer,"
            + "Username varchar(100) not null);")

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(creation)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}