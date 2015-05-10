package com.mycompany.passman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Details extends Activity implements View.OnClickListener {

    //TODO decrypt data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Account data = new Account(intent.getStringExtra("data"));

        TextView web_address, user_name, cur_pass, date;

        ListView passList = (ListView)findViewById(R.id.passView);
        Button back = (Button)findViewById(R.id.cancel);
        web_address = (TextView)findViewById(R.id.web_address);
        user_name = (TextView)findViewById(R.id.username);
        cur_pass = (TextView)findViewById(R.id.cur_pass);
        date = (TextView)findViewById(R.id.date);

        if(data.getPwds().isEmpty()) {
            ArrayList<String> notFound = new ArrayList<>();
            notFound.add("No Entries Found");
            data.setPwds(notFound);
        }

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data.getPwds());
        passList.setAdapter(itemsAdapter);

        web_address.setText(data.getAddress());
        user_name.setText(data.getUser_name());
        cur_pass.setText(data.getCurPwd());
        date.setText(data.get_Date());
        back.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
