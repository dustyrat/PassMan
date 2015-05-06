package com.mycompany.passman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Accounts extends Activity implements View.OnClickListener {
    private ArrayList<Account> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        Button add_account = (Button) findViewById(R.id.add_account);
        add_account.setOnClickListener(this);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        loadSavedPreferences();

        ListView listView = (ListView) this.findViewById(R.id.accountList);
        AccountItemAdapter itemAdapter = new AccountItemAdapter(this, R.layout.account_list, listData);
        listView.setAdapter(itemAdapter);
    }

    private void loadSavedPreferences(){
        Account account;
        listData = new ArrayList<>();
        String dateString, addressString, usernameString, curPwd;
        ArrayList<String> pwds;

        String[] temp;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Accounts", MODE_PRIVATE);
        Map<String, ?> keys = pref.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            temp = entry.getKey().split("\\s+");
            addressString = temp[0];
            usernameString = temp[1];
            temp = entry.getValue().toString().split("\\s+");
            dateString = temp[0];
            curPwd = temp[1];
            pwds = new ArrayList<>();
            pwds.addAll(Arrays.asList(temp).subList(2, temp.length));
            account = new Account(dateString, addressString, usernameString, curPwd, pwds);
            listData.add(account);
        }
    }


    private void add_accountClick(){
        startActivityForResult(new Intent("com.mycompany.passman.AddAccount"), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accounts, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            startActivity(new Intent("com.mycompany.passman.Accounts"));
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_account: add_accountClick();
                break;
            case R.id.back: finish();
                break;
        }
    }
}
