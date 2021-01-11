package com.example.easydelivery.ado;

import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class InternalFile {

    public void createFile(String filename, String directoryname)
    {
        try {
            File nuevaCarpeta = new File(Environment.getExternalStorageDirectory(), directoryname);
            if (!nuevaCarpeta.exists()) {
                nuevaCarpeta.mkdir();
            }
            try {
                File file = new File(nuevaCarpeta, filename + ".txt");
                file.createNewFile();
            } catch (Exception ex) {
                Log.e("Error", "ex: " + ex);
            }
        } catch (Exception e) {
            Log.e("Error", "e: " + e);
        }
    }

    public void writerFile(String filename, String directoryname, JSONObject data)
    {
        try {
            File fileuser = new File(Environment.getExternalStorageDirectory()+"/"+directoryname,filename+".txt");

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileuser));
            outputStreamWriter.write(data.toString());
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("Error", "e: " + e);
        }
    }
    public JSONObject readerFile(String filename, String directoryname) throws IOException, JSONException {
        File fileuser = new File(Environment.getExternalStorageDirectory()+"/"+directoryname,filename+".txt");
        BufferedReader leer = new BufferedReader(new InputStreamReader(new FileInputStream(fileuser)));
        String dat =  leer.readLine();
        JSONObject jsonObject = new JSONObject(dat);
        leer.close();
        return jsonObject;
    }
}
