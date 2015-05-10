package com.mycompany.passman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Switch;


public class Settings extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Switch notificationSet;
    private NumberPicker days;
    private Button changePwd, clear;
    private ImageButton back;
    private Boolean passSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notificationSet = (Switch)findViewById(R.id.enableNot);
        days = (NumberPicker)findViewById(R.id.days);
        changePwd = (Button)findViewById(R.id.changePwd);
        clear = (Button)findViewById(R.id.clear);
        back = (ImageButton)findViewById(R.id.cancel);

        notificationSet.setOnCheckedChangeListener(this);
        changePwd.setOnClickListener(this);
        clear.setOnClickListener(this);
        back.setOnClickListener(this);
        loadSavedPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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

    private void loadSavedPreferences() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        notificationSet.setChecked(settings.getBoolean("notifications", false));
        passSet = settings.getBoolean("passSet", false);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.clear: clearClick();
                break;
            case R.id.changePwd: changePwdClick();
                break;
            case R.id.cancel: finish();
                break;
        }

    }

    private void changePwdClick() {
        startActivity(new Intent("com.mycompany.passman.SetPass"));
    }

    private void clearClick() {
        //TODO confermation
        getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE).edit().clear().commit();
        getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).edit().clear().commit();
        loadSavedPreferences();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE).edit().putBoolean("notifications", isChecked).commit();
    }
}
