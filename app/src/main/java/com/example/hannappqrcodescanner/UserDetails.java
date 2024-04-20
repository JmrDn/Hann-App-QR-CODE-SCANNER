package com.example.hannappqrcodescanner;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDetails {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public  UserDetails(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public  void setEmail(String email){
       editor.putString("email", email);
       editor.apply();

    }

    public String getEmail(){
        return  sharedPreferences.getString("email", null);
    }

    public  void setPassword(String password){
        editor.putString("password", password);
        editor.apply();

    }
    public String getPassword(){
        return  sharedPreferences.getString("password", null);
    }
    public  void setAlis(String alias){
        editor.putString("alias", alias);
        editor.apply();
    }
    public String getAlias(){
        return  sharedPreferences.getString("alias", null);
    }

    public void logout(){
        editor.clear();
        editor.commit();
        editor.apply();
    }




}
