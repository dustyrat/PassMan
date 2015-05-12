package com.mycompany.passman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AddAccount extends Activity implements View.OnClickListener {
    private String password;

    //TODO set notifications
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        password = this.getIntent().getStringExtra("password");
        setContentView(R.layout.activity_add_account);
        ImageButton submit = (ImageButton) findViewById(R.id.submit);
        ImageButton cancel = (ImageButton) findViewById(R.id.cancel);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void submitClick() {
        EditText address = (EditText) findViewById(R.id.address);
        EditText user_name = (EditText) findViewById(R.id.username);
        EditText pwd = (EditText) findViewById(R.id.pwd);
        if (user_name.getText().toString().equals("")
                || address.getText().toString().equals("")
                || pwd.getText().toString().equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please fill in all fields")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        else if (user_name.getText().toString().contains(" ")
                || address.getText().toString().contains(" ")
                || pwd.getText().toString().contains(" ")){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Fields can not contain spaces")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        String key, value;
        Account newAccount = new Account(address.getText().toString(), user_name.getText().toString(), pwd.getText().toString());
        key = newAccount.getAddress() + " " + newAccount.getUser_name();

        SharedPreferences data = getApplicationContext().getSharedPreferences("Accounts", MODE_PRIVATE);
        Map<String, ?> keys = data.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            if (key.equals(entry.getKey())){
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Account already exists")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;
            }
        }
        if (!newAccount.getPwds().isEmpty()) {
            value = newAccount.get_date() + " " + newAccount.getCurPwd() + " " + newAccount.getPwdsString();
        }
        else {
            value = newAccount.get_date() + " " + newAccount.getCurPwd();
        }

        save(key, EnDecrypt.encrypt(EnDecrypt.password.getBytes(), value));
        setResult(1, new Intent().putExtra("password", password));
        finish();
    }

    private void save(String key, String value) {
        SharedPreferences data = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE);
        Editor editor = data.edit();
        editor.putString(key, value).apply();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.submit: submitClick();
                break;
            case R.id.cancel: setResult(1, new Intent().putExtra("password", password));
                finish();
                break;
        }
    }
}
