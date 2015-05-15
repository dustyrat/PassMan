package com.mycompany.passman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import java.util.Date;

//TODO edit notification/extend
public class Edit extends Activity implements View.OnClickListener {
    private Account data;
    private int pos;
    private EditText address, user, pass;
    private Button gen;
    private Intent intent;
    private Switch enableNotifications;
    private Boolean notificationChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (EnDecrypt.password == null){
            startActivityForResult(new Intent("com.mycompany.passman.EnterPass"), 4);
        }
        else {
            runActivity();
        }
    }

    private void runActivity() {
        SharedPreferences notifications = getApplicationContext().getSharedPreferences("notifications", Context.MODE_PRIVATE),
                settings = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);

        intent = getIntent();
        data = new Account(intent.getStringExtra("data"));
        pos = intent.getIntExtra("pos", 0);

        address = (EditText)findViewById(R.id.webAddress);
        user = (EditText)findViewById(R.id.userName);
        pass = (EditText)findViewById(R.id.Password);

        ImageButton editAddress = (ImageButton) findViewById(R.id.editAddress),
                editUser = (ImageButton) findViewById(R.id.editUser),
                editPass = (ImageButton) findViewById(R.id.editPassword),
                submit = (ImageButton) findViewById(R.id.submit),
                cancel = (ImageButton) findViewById(R.id.cancel),
                delete = (ImageButton) findViewById(R.id.delete);
        enableNotifications = (Switch)findViewById(R.id.enableNotifications);

        gen = (Button)findViewById(R.id.generate);

        address.setText(data.getAddress());
        address.setHint(data.getAddress());
        user.setText(data.getUser_name());
        user.setHint(data.getUser_name());
        pass.setText(data.getCurPwd());
        pass.setHint(data.getCurPwd());

        editAddress.setOnClickListener(this);
        editUser.setOnClickListener(this);
        editPass.setOnClickListener(this);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        delete.setOnClickListener(this);
        gen.setOnClickListener(this);

        enableNotifications.setEnabled(settings.getBoolean("notifications", false));
        enableNotifications.setChecked(notifications.getBoolean(data.getAddress() + " " + data.getUser_name(), false));
        notificationChanged = notifications.getBoolean(data.getAddress() + " " + data.getUser_name(), false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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
        switch (v.getId()){
            case R.id.editAddress: AddressClick();
                break;
            case R.id.editUser: UserClick();
                break;
            case R.id.editPassword: PassClick();
                break;
            case R.id.submit: SubmitClick();
                break;
            case R.id.cancel: setResult(1, new Intent());
                finish();
                break;
            case R.id.delete: DeleteClick();
                break;
            case R.id.generate: GenerateClick();
                break;
        }
    }

    private void AddressClick() {
        address.setEnabled(!address.isEnabled());
        if (address.isEnabled()){
            address.setText("");
        }
        else {
            address.setText(address.getHint());
        }
    }

    private void UserClick() {
        user.setEnabled(!user.isEnabled());
        if (user.isEnabled()){
            user.setText("");
        }
        else {
            user.setText(user.getHint());
        }
    }

    private void PassClick() {
        pass.setEnabled(!pass.isEnabled());
        gen.setEnabled(!gen.isEnabled());
        if (pass.isEnabled()){
            pass.setText("");
        }
        else {
            pass.setText(pass.getHint());
        }
    }

    private void SubmitClick() {
        String newkey, oldkey, value;
        newkey = address.getText().toString() + " " + user.getText().toString();
        oldkey = data.getAddress() + " " + data.getUser_name();
        if (user.getText().toString().equals("")
                || address.getText().toString().equals("")
                || pass.getText().toString().equals("")){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Fields can not be blank")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        else if (user.getText().toString().contains(" ")
                || address.getText().toString().contains(" ")
                || pass.getText().toString().contains(" ")){
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
        else if(data.getPwdsString().contains(pass.getText().toString())){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("New password matches pasword in history")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        if((newkey.equals(oldkey) && !data.getCurPwd().equals(pass.getText().toString()))){
            data.add_pwd(pass.getText().toString());
            value = data.get_date() + " " + data.getCurPwd() + " " + data.getPwdsString();
            save(newkey, EnDecrypt.encrypt(EnDecrypt.password.getBytes(), value));
        }
        else if((newkey.equals(oldkey) && (data.getCurPwd().equals(pass.getText().toString()) || data.getPwdsString().equals(pass.getText().toString())))
                && notificationChanged == enableNotifications.isChecked()){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Old password matches new password")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        else{
            delete(oldkey);
            data.setAddress(address.getText().toString());
            data.setUser_name(user.getText().toString());
            data.setCurPwd(pass.getText().toString());
            data.setDate(new Date().toString());
            value = data.get_date() + " " + data.getCurPwd() + " " + data.getPwdsString();

            save(newkey, EnDecrypt.encrypt(EnDecrypt.password.getBytes(), value));
        }

        setResult(2, new Intent().putExtra("data", data.toString()).putExtra("pos", pos));
        finish();
    }

    private void save(String key, String value) {
        SharedPreferences accounts = getApplicationContext().getSharedPreferences("accounts", MODE_PRIVATE);
        accounts.edit().putString(key, value).apply();
        if (enableNotifications.isChecked()){
            Notifications.setAlarm(getApplicationContext(), key);
        }
        else {
            Notifications.cancelAlarm(getApplicationContext(), key);
        }
    }

    private void delete(String key){
        SharedPreferences accounts = getApplicationContext().getSharedPreferences("accounts", MODE_PRIVATE);
        accounts.edit().remove(key).apply();
        Notifications.cancelAlarm(getApplicationContext(), key);
    }

    private void DeleteClick() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String key = data.getAddress() + " " + data.getUser_name();
                        delete(key);
                        setResult(3, intent.putExtra("pos", pos));
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void GenerateClick() {
        startActivityForResult(new Intent("com.mycompany.passman.Generate").putExtra("data", data.getCurPwd() + " " + data.getPwdsString()), 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2){
            // fetch the message String
            String password = data.getStringExtra("data");
            pass.setText(password);
        }
    }
}
