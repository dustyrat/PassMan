package com.mycompany.passman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Map;

public class AddAccount extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Button submit = (Button) findViewById(R.id.submit);
        Button cancel = (Button) findViewById(R.id.cancel);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void submitClick(){
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

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Accounts", MODE_PRIVATE);
        Map<String, ?> keys = pref.getAll();
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

        value = newAccount.get_date() + " " + newAccount.getCurPwd() + " " + newAccount.getPwdsString();
        save(key, value);
        setResult(RESULT_OK, null);
        finish();
    }

    private void save(String key, String value) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Accounts", MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.submit: submitClick();
                break;
            case R.id.cancel: setResult(RESULT_OK, null);
                finish();
                break;
        }
    }
}
