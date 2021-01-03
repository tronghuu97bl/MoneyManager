package com.tth.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tth.moneymanager.Model.User;

import java.lang.reflect.Type;

public class Util {
    private static final String TAG = "Utils";
    private Context context;

    public Util(Context context) {
        this.context = context;
    }

    public void addUserToPreference(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString("user", gson.toJson(user));
        editor.apply();
    }


    //sau khi xac thuc van tay thanh cong, lay ra user
    public User userAuthenLogin() {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {}.getType();
        return gson.fromJson(pre.getString("user", null), type);
    }

    public void signOutUser() {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        pre.edit().remove("user").apply();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
