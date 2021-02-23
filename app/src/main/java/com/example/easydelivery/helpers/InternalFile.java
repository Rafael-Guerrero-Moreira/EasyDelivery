package com.example.easydelivery.helpers;

import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class InternalFile {

    private String userDir = "edappdata";

    private void createFile(String directory, String filename, String extension)
    {
        try {
            File newFolder = new File(Environment.getExternalStorageDirectory(), directory);
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
            try {
                File file = new File(newFolder, filename + extension);
                file.createNewFile();
            } catch (Exception ex) {
                Log.e("Error", "Error al crear el archivo ex: " + ex);
            }
        } catch (Exception e) {
            Log.e("Error", "Error al crear la carpeta e: " + e);
        }
    }

    public void createUserFile()
    {
        createFile(userDir, "userdata", ".txt");
    }

    private void writeFile(String directory, String filename, String extension, JSONObject data)
    {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + directory,filename + extension);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(data.toString());
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("Error", "Error al escribir el archivo e: " + e);
        }
    }

    public void writeUserFile(JSONObject data) {
        writeFile(userDir, "userdata", ".txt", data);
    }

    public JSONObject readFile(String directory, String filename, String extension) {
        JSONObject jsonObject = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + directory,filename + extension);
            BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String dat =  read.readLine();
            jsonObject = new JSONObject(dat);
            read.close();
        } catch (Exception e) {
            Log.e("Error", "Error al leer el archivo e: " + e);
        }
        return jsonObject;
    }

    public JSONObject readUserFile() {
        return readFile(userDir, "userdata", ".txt");
    }

    public void deleteFile(String directory, String filename, String extension) {

            File file = new File(Environment.getExternalStorageDirectory() + "/" + directory,filename + extension);
            file.delete();
    }


    public void deleteUserFile() {
         deleteFile(userDir, "userdata", ".txt");
    }
}
