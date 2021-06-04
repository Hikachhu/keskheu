package com.projet;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AccesLocal {
    private final MySQLiteOpenHelper accesBD;
    private SQLiteDatabase bd;

    public AccesLocal(Context context){
        String nomBase = "bdKeskheu.sqlite";
        int versionBase = 1;
        accesBD=new MySQLiteOpenHelper(context, nomBase,null, versionBase);
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
            int Parent = courser.getInt(0);
            int Fils = courser.getInt(1);
            String Contenu = courser.getString(2);
            int rang = courser.getInt(3);
            String Username = courser.getString(4);
            question = new Question(Parent, Fils, Contenu, rang,Username);
        }
        assert question != null;
        Log.e("NumFils","Numéro de fils:"+question.getFils()+" position = "+position);
        courser.close();
        return question.getFils();
    }

    public ArrayList<Question> ListeFils(int NumParent) {
        ArrayList<Question> ListeQuestion = new ArrayList<>();
        Question question;
        String req = "SELECT  * FROM Questions where Parent=" + NumParent;
        bd = accesBD.getReadableDatabase();
        Cursor courser = bd.rawQuery(req, null);
        courser.move(1);
        Log.e("ListeFils","Taille ="+courser.getCount()+" position = "+courser.getPosition());

        while(!(courser.isAfterLast())) {
            Log.e("TAG","Taille ="+courser.getPosition());
            int Parent = courser.getInt(0);
            int Fils = courser.getInt(1);
            String Contenu = courser.getString(2);
            int rang = courser.getInt(3);
            String Username = courser.getString(4);
            question = new Question(Parent, Fils, Contenu, rang,Username);
            ListeQuestion.add(question);
            courser.moveToNext();
        }

        courser.close();
        return ListeQuestion;
    }

    public int NombreDeRep(int NumActuel){
        Question question;
        String req = "SELECT  * FROM Questions where Parent=" + NumActuel;
        bd = accesBD.getReadableDatabase();
        Cursor courser = bd.rawQuery(req, null);
        int quantite=courser.getCount();
        Log.e("NombreDeRep", String.valueOf(quantite));
        courser.close();
        return quantite;
    }

    public Question rcmpDenied(){
        bd=accesBD.getReadableDatabase();
        Question question =null;
        String req ="select * from Questions";
        Cursor cursor = bd.rawQuery(req,null);
        cursor.moveToLast();
        if(!cursor.isAfterLast()){
            int Parent=cursor.getInt(0);
            int Fils=cursor.getInt(1);
            String Contenu=cursor.getString(2);
            int rang=cursor.getInt(3);
            String Username = cursor.getString(4);
            question=new Question(Parent,Fils,Contenu,rang,Username);
        }
        cursor.close();
        return question;
    }

    public String rcmpNumbers(Integer number){
        bd=accesBD.getReadableDatabase();
        Question question =null;
        String req ="select * from Questions";
        Cursor courser = bd.rawQuery(req,null);
        courser.move(number);
        if(!courser.isAfterLast()){
            int Parent=courser.getInt(0);
            int Fils=courser.getInt(1);
            String Contenu=courser.getString(2);
            int rang=courser.getInt(3);
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
