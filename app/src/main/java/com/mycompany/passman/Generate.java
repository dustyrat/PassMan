package com.mycompany.passman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Generate extends Activity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    private CheckBox alpha, numeric, special;
    private RadioGroup radioSpec;
    private RadioButton spec_all, spec_custom;
    private EditText custom;
    private TextView maxView;
    private TextView minView;
    int Max = 32, Min = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        Button gen_pwd = (Button) findViewById(R.id.gen_pwd);
        Button back = (Button) findViewById(R.id.back);

        radioSpec = (RadioGroup)findViewById(R.id.radioSpec);

        spec_all = (RadioButton)findViewById(R.id.spec_all);
        spec_custom = (RadioButton)findViewById(R.id.spec_custom);

        alpha = (CheckBox)findViewById(R.id.alpha);
        numeric = (CheckBox)findViewById(R.id.numeric);
        special = (CheckBox)findViewById(R.id.special);

        custom = (EditText)findViewById(R.id.custom);

        gen_pwd.setOnClickListener(this);
        back.setOnClickListener(this);

        radioSpec.setOnCheckedChangeListener(this);

        alpha.setOnCheckedChangeListener(this);
        numeric.setOnCheckedChangeListener(this);
        special.setOnCheckedChangeListener(this);

        RangeSeekBar<Integer> seekBar = new RangeSeekBar<>(1, 32, this);
        seekBar.setNotifyWhileDragging(true);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
                maxView = (TextView)findViewById(R.id.maxView);
                minView = (TextView)findViewById(R.id.minView);
                Max = maxValue;
                Min = minValue;
                maxView.setText("" + Max);
                minView.setText("" + Min);
            }
        });

// add RangeSeekBar to pre-defined layout
        ViewGroup layout = (ViewGroup) findViewById(R.id.range);
        layout.addView(seekBar);
    }

    private void gen_pwdClick(){
        StringBuilder buffer = new StringBuilder();
        String characters = "";
        int length;

        if (alpha.isChecked()){
            characters += "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if (numeric.isChecked()){
            characters += "1234567890";
        }
        if (special.isChecked()){
            if (spec_all.isChecked()){
                characters += "~!@#$%^&*()_+`-=[]\\{}|;':\",./<>?";
            }
            else if (spec_custom.isChecked()){
                characters += custom.getText().toString();
            }
        }

        if (characters.equals("") || characters.contains(" ")){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please select an option")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        int charactersLength = characters.length();
        length = Min + (int)(Math.random() * ((Max - Min) + 1));
        for (int i = 0; i < length; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }
        TextView pwd = (TextView) findViewById(R.id.pwd);
        pwd.setText(buffer.toString());
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.gen_pwd:
                gen_pwdClick();
                break;
            case R.id.back: finish();
                break;
            case R.id.copy:
                //TODO
                break;
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        switch (buttonView.getId()){
            case R.id.special:
                for (int i = 0; i < radioSpec.getChildCount(); i++){
                    radioSpec.getChildAt(i).setEnabled(isChecked);
                }
                custom.setEnabled(isChecked);
                break;
        }
    }

    public void onCheckedChanged(RadioGroup group, int checkedId){
        switch (checkedId){
            case R.id.spec_all:
                break;
            case R.id.spec_custom:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generate, menu);
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
