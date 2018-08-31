package com.example.lenovo.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * Created by Lenovo on 26/08/2018.
 */

public class Login_DataHelperz extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "biodatadiri.db";
    private static final int DATABASE_VERSION = 1;

    public Login_DataHelperz(Context context) {
        super(context, Environment.getExternalStoragePublicDirectory("DCIM").getAbsolutePath()+"/" + DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "create table biodata(no integer primary key, nama text null, tgl text null, jk text null, alamat text null)";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(String no,String nama,String tgl,String jk,String alamat){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into biodata(no, nama, tgl, jk, alamat) values('" +
                no + "','" +
                nama + "','" +
                tgl + "','" +
                jk + "','" +
                alamat + "')");
        db.close();
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
