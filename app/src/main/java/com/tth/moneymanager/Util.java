package com.tth.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tth.moneymanager.Model.User;
import com.tth.moneymanager.Security.AES;
import com.tth.moneymanager.Security.RSAUtil;

import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Util {
    private Context context;

    public Util(Context context) {
        this.context = context;
    }

    public void addUserToPreference(User user) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String input = gson.toJson(user);
        String publicKey = getPublicKey();
        byte[] output = RSAUtil.RSAEncrypt(input, RSAUtil.stringToPublicKey(publicKey));
        String kq = RSAUtil.base64Encode(output);
        editor.putString("user", kq);
        editor.apply();
    }

    //sau khi xac thuc van tay thanh cong, lay ra user
    public User userAuthenLogin() throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {
        }.getType();
        String private_Key = getPrivateKey();
        String privateKey = RSAUtil.base64Encode(AES.decryptBinary(RSAUtil.base64Decode(private_Key), "helloworld"));
        String input = pre.getString("user", null);
        byte[] kq = RSAUtil.base64Decode(input);
        String output = RSAUtil.RSADecrypt(kq, RSAUtil.stringToPrivateKey(privateKey));
        return gson.fromJson(output, type);
    }

    public void signOutUser() {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        pre.edit().remove("user").apply();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public String getExitsUser() {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        return pre.getString("userexist", "no");
    }

    public void setAESKey(String key) {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        pre.edit().putString("aeskey", key).apply();
    }

    public String getAESKey() {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        return pre.getString("aeskey", "");
    }

    public boolean getRSAKey() {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        return pre.getBoolean("rsa", false);
    }

    public void setRSAKey() {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        pre.edit().putBoolean("rsa", true).apply();
    }

    public void setPublicKey(String key) {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        pre.edit().putString("publickey", key).apply();
    }

    public String getPublicKey() {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        return pre.getString("publickey", "");
    }

    public void setPrivateKey(String key) {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        pre.edit().putString("privatekey", key).apply();
    }

    public String getPrivateKey() {
        SharedPreferences pre = context.getSharedPreferences("pre_info_user", Context.MODE_PRIVATE);
        return pre.getString("privatekey", "");
    }
}
