package com.example.projet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//room pour
public class MySQLiteOpenHelper extends SQLiteOpenHelper{

    private String creation ="create table Questions("
            +"Parent integer,"
            +"Fils integer primary key,"
            +"Contenu varchar(40) not null ,"
            +"rang integer,"
            +"Username varchar(100) not null);";

    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
