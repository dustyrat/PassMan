package com.mycompany.passman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button gen_page = (Button) findViewById(R.id.gen_page),
                log_page = (Button) findViewById(R.id.log_page),
                settings = (Button)findViewById(R.id.settings);
        gen_page.setOnClickListener(this);
        log_page.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    private void gen_pageClick(){
        startActivity(new Intent("com.mycompany.passman.Generate"));
    }

    private void log_pageClick(){
        startActivity(new Intent("com.mycompany.passman.Accounts"));
    }

    private void settingsClick(){
        startActivity(new Intent("com.mycompany.passman.Settings"));
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.gen_page: gen_pageClick();
                break;
            case R.id.log_page: log_pageClick();
                break;
            case R.id.settings: settingsClick();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
