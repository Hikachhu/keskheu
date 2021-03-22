package com.example.projet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AccesLocal {
    private String nomBase="bdKeskheu.sqlite";
    private Integer versionBase=1;
    private MySQLiteOpenHelper accesBD;
    private SQLiteDatabase bd;

    public AccesLocal(Context context){
        accesBD=new MySQLiteOpenHelper(context,nomBase,null,versionBase);
        this.accesBD=accesBD;
        this.bd=bd;
    }

    public void ajout(@NotNull Question question){
        bd=accesBD.getWritableDatabase();
        String req = "insert into Questions(Parent,Fils,Contenu,rang,Username) values ";
        req+=("("+question.getParent()+","+question.getFils()+", \""+question.getContenu()+"\","+question.getRang()+",\""+question.getUsername()+"\");");
        bd.execSQL(req);
    }

    public int NumFils(int NumParent,int position) {
        Question question =null;
        String req = "SELECT  * FROM Questions where Parent=" + NumParent;
        bd = accesBD.getReadableDatabase();
        Cursor courser = bd.rawQuery(req, null);
        courser.move(position);

        if (!courser.isAfterLast()) {
            Integer Parent = courser.getInt(0);
            Integer Fils = courser.getInt(1);
            String Contenu = courser.getString(2);
            Integer rang = courser.getInt(3);
            String Username = courser.getString(4);
            question = new Question(Parent, Fils, Contenu, rang,Username);
        }
        Log.e("NumFils","Numéro de fils:"+question.getFils()+" position = "+position);
        courser.close();
        return question.getFils();
    }

    public ArrayList<Question> ListeFils(int NumParent) {
        ArrayList<Question> ListeQuestion = new ArrayList<Question>();
        Question question =null;
        String req = "SELECT  * FROM Questions where Parent=" + NumParent;
        bd = accesBD.getReadableDatabase();
        Cursor courser = bd.rawQuery(req, null);
        courser.move(1);
        Log.e("ListeFils","Taille ="+courser.getCount()+" position = "+courser.getPosition());

        while(!(courser.isAfterLast())) {
            Log.e("TAG","Taille ="+courser.getPosition());
            Integer Parent = courser.getInt(0);
            Integer Fils = courser.getInt(1);
            String Contenu = courser.getString(2);
            Integer rang = courser.getInt(3);
            String Username = courser.getString(4);
            question = new Question(Parent, Fils, Contenu, rang,Username);
            ListeQuestion.add(question);
            courser.moveToNext();
        }

        courser.close();
        return ListeQuestion;
    }

    @SuppressLint("WrongConstant")
    public ArrayList<Question> All() {
        ArrayList<Question> ListeQuestion = new ArrayList<Question>();
        Question question =null;
        String req = "SELECT  * FROM Questions";
        bd = accesBD.getReadableDatabase();
        Cursor courser = bd.rawQuery(req, null);
        courser.move(1);
        Log.e("TAG","Taille ="+courser.getCount()+" position = "+courser.getPosition());

        while((courser!=null)) {
            Log.e("TAG","Taille ="+courser.getPosition());
            Integer Parent = courser.getInt(0);
            Integer Fils = courser.getInt(1);
            String Contenu = courser.getString(2);
            Integer rang = courser.getInt(3);
            String Username = courser.getString(4);
            question = new Question(Parent, Fils, Contenu, rang,Username);
            ListeQuestion.add(question);
            courser.moveToNext();
        }

        courser.close();
        return ListeQuestion;
    }


    public Question rcmpDenied(){
        bd=accesBD.getReadableDatabase();
        Question question =null;
        String req ="select * from Questions";
        Cursor curseur = bd.rawQuery(req,null);
        curseur.moveToLast();
        if(!curseur.isAfterLast()){
            Integer Parent=curseur.getInt(0);
            Integer Fils=curseur.getInt(1);
            String Contenu=curseur.getString(2);
            Integer rang=curseur.getInt(3);
            String Username = curseur.getString(4);
            question=new Question(Parent,Fils,Contenu,rang,Username);
        }
        curseur.close();
        return question;
    }

    public String rcmpNumbers(Integer number){
        bd=accesBD.getReadableDatabase();
        Question question =null;
        String req ="select * from Questions";
        Cursor courser = bd.rawQuery(req,null);
        courser.move(number);
        if(!courser.isAfterLast()){
            Integer Parent=courser.getInt(0);
            Integer Fils=courser.getInt(1);
            String Contenu=courser.getString(2);
            Integer rang=courser.getInt(3);
            String Username=courser.getString(4);
            question=new Question(Parent,Fils,Contenu,rang,Username);
        }
        courser.close();
        if(question==null)return "";
        return question.getContenu();
    }

    public int getNumber() {
        String countQuery = "SELECT  * FROM Questions";
        bd = accesBD.getReadableDatabase();
        Cursor cursor = bd.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getNumber(int Position){
        String countQuery = "SELECT  * FROM Questions where parent="+Position;
        bd = accesBD.getReadableDatabase();
        Cursor cursor = bd.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public void suppressionTotal(){
        String req = "delete from Questions";
        bd = accesBD.getReadableDatabase();
        bd.execSQL(req);
    }
}
