package com.mycompany.passman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Accounts extends Activity implements View.OnClickListener {
    private ArrayList<Account> listData;
    private AccountItemAdapter itemAdapter;
    private ImageButton add_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        if (getIntent().getBooleanExtra("refresh", false)){
            runActivity();
        }
        else if (!pref.getBoolean("passSet", false)) {
            startActivityForResult(new Intent("com.mycompany.passman.SetPass"), 4);
        }
        else {
            startActivityForResult(new Intent("com.mycompany.passman.EnterPass"), 4);
        }
    }

    private void loadSavedPreferences() {
        Account account;
        listData = new ArrayList<>();
        String dateString, addressString, usernameString, curPwd;
        ArrayList<String> pwds;

        String[] temp;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("data", MODE_PRIVATE);
        Map<String, ?> keys = pref.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            temp = entry.getKey().split("\\s+");
            addressString = temp[0];
            usernameString = temp[1];
            temp = EnDecrypt.decrypt(EnDecrypt.password.getBytes(), entry.getValue().toString()).split("\\s+");

            dateString = temp[0];
            curPwd = temp[1];
            pwds = new ArrayList<>();
            pwds.addAll(Arrays.asList(temp).subList(2, temp.length));
            account = new Account(dateString, addressString, usernameString, curPwd, pwds);
            listData.add(account);
        }
    }


    private void add_accountClick() {
        startActivityForResult(new Intent("com.mycompany.passman.AddAccount"), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accounts, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add: add_accountClick();
                break;
            case R.id.cancel: finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1: startActivity(new Intent("com.mycompany.passman.Accounts").putExtra("refresh", true));
                finish();
                break;
            case 2: listData.get(data.getIntExtra("pos", 0)).fromString(data.getStringExtra("data"));
                itemAdapter.notifyDataSetChanged();
                break;
            case 3: listData.remove(data.getIntExtra("pos", 0));
                itemAdapter.notifyDataSetChanged();
                break;
            case 4: runActivity();
                break;
            case 5: finish();
                break;
        }
    }

    private void runActivity() {
        setContentView(R.layout.activity_accounts);
        add_account = (ImageButton) findViewById(R.id.add);
        add_account.setOnClickListener(this);
        ImageButton back = (ImageButton) findViewById(R.id.cancel);
        back.setOnClickListener(this);
        loadSavedPreferences();
        itemAdapter = new AccountItemAdapter(this, R.layout.account_list, listData);
        ListView listView = (ListView) this.findViewById(R.id.accountList);
        listView.setAdapter(itemAdapter);
    }
}
