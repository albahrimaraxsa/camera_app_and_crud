package com.example.lenovo.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 26/08/2018.
 */

public class Ci_DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Photo_db.db";
    private static final int DATABASE_VERSION = 1;

    public Ci_DataHelper(Context context) {
        super(context, Environment.getExternalStoragePublicDirectory("DCIM").getAbsolutePath()+"/" + DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "create table photos(no integer primary key, nama text null)";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public void insertData(String no,String nama,String tgl,String jk,String alamat){
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("insert into biodata(no, nama, tgl, jk, alamat) values('" +
//                no + "','" +
//                nama + "','" +
//                tgl + "','" +
//                jk + "','" +
//                alamat + "')");
//        db.close();
//    }



    public void insertData(String nama){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into photos(nama) values('" + nama +"')");
        db.close();
    }

    public void insertVideoName(String nama){
        try {
            Log.d("nameInDb", nama);
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("insert into photos(nama) values('" +
                    nama + "')");
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public List<String> getData() {
        List<String > list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT nama FROM photos";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
            do{
                list.add(cursor.getString(cursor.getColumnIndex("nama")));
            }while(cursor.moveToNext());
        }

        return list;
    }



    public void insertDatawithContentValue(String no,String nama,String tgl,String jk,String alamat){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("no",no);
        values.put("nama",nama);
        values.put("tgl",tgl);
        values.put("jk",jk);
        values.put("alamat",alamat);

        db.insert("biodata",null,values);
        db.close();
    }


    public boolean checkNo(String no){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean check=true;
        String checkSql="Select * from biodata where no = "+no+" ";
        Cursor cursor = db.rawQuery(checkSql,null);

        if(cursor.moveToFirst()){//check ada data no itu engga
            check=false;
        }

        db.close();
        return check;
    }
}
