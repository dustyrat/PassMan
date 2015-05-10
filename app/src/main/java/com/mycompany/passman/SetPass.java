package com.mycompany.passman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


public class SetPass extends Activity implements View.OnClickListener {
    private ImageButton submit, cancel;
    private EditText curPwd, newPwd, conPwd;
    private Boolean passSet;
    private int password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass);
        loadSavedPreferences();

        submit = (ImageButton)findViewById(R.id.submit);
        cancel = (ImageButton)findViewById(R.id.cancel);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);

        curPwd = (EditText)findViewById(R.id.curPwd);
        if (!passSet){
            curPwd.setVisibility(View.GONE);
        }
        newPwd = (EditText)findViewById(R.id.newPwd);
        conPwd = (EditText)findViewById(R.id.confirmPwd);
    }

    private void loadSavedPreferences() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        passSet = settings.getBoolean("passSet", false);
        if (!passSet) {
            password = settings.getInt("password", 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_pass, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.submit: submitClick();
                break;
            case R.id.cancel: setResult(5);
                finish();
                break;
        }
    }

    private void submitClick() {
        if (passSet && curPwd.hashCode() != password){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("You have entered the wrong current password")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        if (passSet && curPwd.getText().toString().equals(newPwd.getText().toString())) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("New password can not match current password")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        if (!newPwd.getText().toString().equals(conPwd.getText().toString())) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("New password does not match")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.putInt("password", newPwd.getText().hashCode()); //TODO need better hash
        editor.putBoolean("passSet", true).apply();
        setResult(4, new Intent().putExtra("password", newPwd.getText())); //TODO decrypt/encrypt current user data
        finish();
    }
}
