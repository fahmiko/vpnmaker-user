package com.uas.fahmiko.vpnuser.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private Context context;

    public Preference(Context context) {
        this.context = context;
    }

    public void logout(){
        SharedPreferences sf = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.remove("id_user");
        editor.remove("name");
        editor.remove("username");
        editor.remove("password");
        editor.remove("photo");
        editor.apply();
    }

    public void saveCredentials(String id, String name,String username, String password, String photo){
        SharedPreferences sf = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("id_user",id);
        editor.putString("name",name);
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("photo",photo);

        editor.apply();
    }

    public boolean checkSavedCredetential(){
        SharedPreferences sf = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        String id_user = sf.getString("id_user","");
        if (id_user.equals("")){
            return false;
        }else {
            return true;
        }
    }
}

