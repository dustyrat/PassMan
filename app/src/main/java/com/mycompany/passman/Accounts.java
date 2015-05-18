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
/* Class: Accounts
 * Purpose: Display all accounts
*/
public class Accounts extends Activity implements View.OnClickListener {
    private ArrayList<Account> listData;
    private AccountItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for password/reload
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        if(EnDecrypt.password != null || getIntent().getBooleanExtra("refresh", false)){
            runActivity();
        }
        else if (!settings.getBoolean("passSet", false)) {
            startActivityForResult(new Intent("com.mycompany.passman.SetPass"), 4);
        }
        else {
            startActivityForResult(new Intent("com.mycompany.passman.EnterPass"), 4);
        }
    }

    // Initialize UI
    private void runActivity() {
        setContentView(R.layout.activity_accounts);
        ImageButton add_account = (ImageButton) findViewById(R.id.add), back = (ImageButton) findViewById(R.id.cancel);
        add_account.setOnClickListener(this);
        back.setOnClickListener(this);
        loadSavedPreferences();
        itemAdapter = new AccountItemAdapter(this, R.layout.account_list, listData);
        ListView listView = (ListView) this.findViewById(R.id.accountList);
        listView.setAdapter(itemAdapter);
    }

    // Load data from SharedPreferences into ArrayList<Account>
    private void loadSavedPreferences() {
        Account account;
        listData = new ArrayList<>();
        String dateString, addressString, usernameString, curPwd;
        ArrayList<String> pwds;

        String[] temp;
        SharedPreferences accounts = getApplicationContext().getSharedPreferences("accounts", MODE_PRIVATE);
        Map<String, ?> keys = accounts.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            temp = entry.getKey().split("\\s+");
            addressString = temp[0];
            usernameString = temp[1];
            temp = EnDecrypt.decrypt(EnDecrypt.password, entry.getValue().toString()).split("\\s+");

            dateString = temp[0];
            curPwd = temp[1];
            pwds = new ArrayList<>();
            pwds.addAll(Arrays.asList(temp).subList(2, temp.length));
            account = new Account(dateString, addressString, usernameString, curPwd, pwds);
            listData.add(account);
        }
    }

    // Load add accounts page
    private void add_accountClick() {
        startActivityForResult(new Intent("com.mycompany.passman.AddAccount"), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accounts, menu);
        return true;
    }

    // Handle button clicks
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
            case 1: // Refresh page
                startActivity(new Intent("com.mycompany.passman.Accounts").putExtra("refresh", true));
                finish();
                break;
            case 2: // Update ListView after editing
                listData.get(data.getIntExtra("pos", 0)).fromString(data.getStringExtra("data"));
                itemAdapter.notifyDataSetChanged();
                break;
            case 3: // Remove view from ListView
                listData.remove(data.getIntExtra("pos", 0));
                itemAdapter.notifyDataSetChanged();
                break;
            case 4: // Run activity after login
                runActivity();
                break;
            case 5: // Back out on cancel login
                finish();
                break;
        }
    }
}
