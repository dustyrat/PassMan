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
import android.widget.Switch;

import java.util.Map;

public class AddAccount extends Activity implements View.OnClickListener {
    private String password;
    private Switch enableNotifications;
    private EditText address, user_name, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        if (EnDecrypt.password == null){
            startActivityForResult(new Intent("com.mycompany.passman.EnterPass"), 4);
        }
        else {
            runActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 4: runActivity();
                break;
            case 5: finish();
                break;
        }
    }

    private void runActivity() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        password = EnDecrypt.password;

        ImageButton submit = (ImageButton) findViewById(R.id.submit),
                cancel = (ImageButton) findViewById(R.id.cancel);
        enableNotifications = (Switch)findViewById(R.id.enableNotifications);
        address = (EditText) findViewById(R.id.address);
        user_name = (EditText) findViewById(R.id.username);
        pwd = (EditText) findViewById(R.id.pwd);

        enableNotifications.setEnabled(settings.getBoolean("notifications", false));
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void submitClick() {
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

        SharedPreferences accounts = getApplicationContext().getSharedPreferences("accounts", MODE_PRIVATE);
        Map<String, ?> keys = accounts.getAll();
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

        if (enableNotifications.isChecked()){
            Notifications.setAlarm(getApplicationContext(), key);
        }

        save(key, EnDecrypt.encrypt(EnDecrypt.password.getBytes(), value));
        setResult(1, new Intent().putExtra("password", password));
        finish();
    }

    private void save(String key, String value) {
        SharedPreferences accounts = getApplicationContext().getSharedPreferences("accounts", MODE_PRIVATE),
                notifications = getApplicationContext().getSharedPreferences("notifications", MODE_PRIVATE);
        accounts.edit().putString(key, value).apply();
        notifications.edit().putBoolean(key, enableNotifications.isChecked()).apply();
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
