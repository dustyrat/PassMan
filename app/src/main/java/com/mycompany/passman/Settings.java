package com.mycompany.passman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

/* Class: Settings
 * Purpose: Edit settings, clear user data and change password
 * Extends: Activity
 * Implements: OnClickListener
 *             OnCheckedChangeListener
 *             OnValueChangeListener
*/
public class Settings extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, NumberPicker.OnValueChangeListener {
    private Switch enableNotifications;
    private NumberPicker days;
    private boolean notificationsChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);

        enableNotifications = (Switch)findViewById(R.id.enableNotifications);
        days = (NumberPicker)findViewById(R.id.days);
        Button changePwd = (Button) findViewById(R.id.changePwd),
                clear = (Button) findViewById(R.id.clear);
        ImageButton back = (ImageButton) findViewById(R.id.cancel);

        enableNotifications.setOnCheckedChangeListener(this);
        enableNotifications.setChecked(settings.getBoolean("notifications", false));
        days.setEnabled(enableNotifications.isChecked());
        days.setWrapSelectorWheel(false);

        days.setMinValue(1);
        days.setMaxValue(4);
        String[] dayValues = new String[4];
        for (int i = 0; i < dayValues.length; i++) {
            String number = Integer.toString((i + 1)*30);
            dayValues[i] = number;
        }

        days.setDisplayedValues(dayValues);
        days.setOnValueChangedListener(this);

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

    // Load settings
    private void loadSavedPreferences() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        enableNotifications.setChecked(settings.getBoolean("notifications", false));
        days.setValue(settings.getInt("days", 30) / 30);
        notificationsChange = settings.getBoolean("notifications", false);
    }

    // Handles button clicks
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.clear: clearClick();
                break;
            case R.id.changePwd: changePwdClick();
                break;
            case R.id.cancel: // Check if notification settings were changed and prompt user to confirm
                SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
                if (notificationsChange != enableNotifications.isChecked() && !settings.getBoolean("notifications", false)){
                    new AlertDialog.Builder(this)
                            .setTitle("Attention")
                            .setMessage("Turning notifications off will delete all notifications on exit")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Notifications.cancelAll(getApplicationContext());
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
                else{
                    finish();
                }
                break;
        }

    }

    // Call SetPass page
    private void changePwdClick() {
        startActivity(new Intent("com.mycompany.passman.SetPass"));
    }

    // Clears all user data
    private void clearClick() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("This will delete all saved data including resetting the password.\nAre you sure you want to delete?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences accounts = getApplicationContext().getSharedPreferences("accounts", MODE_PRIVATE),
                                settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE),
                                notifications = getApplicationContext().getSharedPreferences("notifications", MODE_PRIVATE);
                        settings.edit().clear().apply();
                        accounts.edit().clear().apply();
                        Notifications.cancelAll(getApplicationContext());
                        notifications.edit().clear().apply();
                        loadSavedPreferences();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        settings.edit().putBoolean("notifications", isChecked).apply();
        days.setEnabled(isChecked);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        settings.edit().putInt("days", newVal*30).apply();
    }
}
