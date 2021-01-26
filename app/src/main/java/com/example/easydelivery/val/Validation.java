package com.example.easydelivery.val;

import android.text.TextUtils;

public class Validation {

    public String ValidarCamposBuisnes(String email, String password,String name,String buisname,String ident, boolean ConfimPass )
    {
        String mensage;
        //Verificamos que las cajas de texto no esten vacías
        if(TextUtils.isEmpty(name)){
            return "Nombre";
        }
        if(TextUtils.isEmpty(buisname)){
            return "Apellido";
        }
        if(TextUtils.isEmpty(ident)){
            return "Usuario";
        }
        if(TextUtils.isEmpty(email)){
            return "Email";
        }
        if(TextUtils.isEmpty(password)){
            return "Contraseña";
        }
        if (!ConfimPass){
            return "Las contraseñas no coinciden";
        }

        return "";
    }
    public String ValidarCamposClient(String email, String password,String name,String apellido, boolean ConfimPass)
    {
        String mensage;
        //Verificamos que las cajas de texto no esten vacías
        if(TextUtils.isEmpty(name)){
            return "Nombre";
        }
        if(TextUtils.isEmpty(apellido)){
            return "Apellido";
        }
        if(TextUtils.isEmpty(email)){
            return "Email";
        }
        if(TextUtils.isEmpty(password)){
            return "Contraseña";
        }
        if (!ConfimPass){
            return "Las contraseñas no coinciden";
        }

        return "";
    }
    public String ValidarCamposDelivery(String email, String password,String companyname,String ident, boolean ConfimPass )
    {
        String mensage;
        //Verificamos que las cajas de texto no esten vacías
        if(TextUtils.isEmpty(companyname)){
            return "Nombre";
        }
        if(TextUtils.isEmpty(ident)){
            return "Usuario";
        }
        if(TextUtils.isEmpty(email)){
            return "Email";
        }
        if(TextUtils.isEmpty(password)){
            return "Contraseña";
        }
        if (!ConfimPass){
            return "Las contraseñas no coinciden";
        }

        return "";
    }
}
