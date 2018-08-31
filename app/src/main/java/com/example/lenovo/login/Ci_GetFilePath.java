package com.example.lenovo.login;

import android.os.Environment;

import java.io.File;

public class Ci_GetFilePath {

    public static String getFilePath(String filename){
        File filepath = new File(Environment.getExternalStoragePublicDirectory("DCIM").getAbsolutePath(),"AppsPhoto");
        if(!filepath.exists()) filepath.mkdir();
        return filepath.getAbsolutePath()+"/"+filename;
    }

    public static String getFilePathVideo (String filename){
        File filepath = new File(Environment.getExternalStoragePublicDirectory("DCIM").getAbsolutePath(),"AppsVideo");
        if(!filepath.exists()) filepath.mkdir();
        return filepath.getAbsolutePath()+"/"+filename;
    }
}
