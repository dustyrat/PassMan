package com.mycompany.passman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/* Class: Generate
 * Purpose: Generates a random String with given parameters
 * Extends: Activity
 * Implements: OnClickListener
 *             OnCheckedChangeListener
*/
public class Generate extends Activity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    private CheckBox alpha, numeric, special;
    private NumberPicker upper, lower, num, spec;
    private RadioGroup radioSpec;
    private RadioButton spec_all, spec_custom;
    private EditText custom;
    private TextView maxView;
    private TextView minView;
    private int Max = 32, Min = 1;
    private String data;

    // Initializes UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        Intent intent = getIntent();
        data = intent.getStringExtra("data");

        Button gen_pwd = (Button) findViewById(R.id.gen_pwd);
        ImageButton back = (ImageButton) findViewById(R.id.cancel);

        radioSpec = (RadioGroup)findViewById(R.id.radioSpec);
        spec_all = (RadioButton)findViewById(R.id.spec_all);
        spec_custom = (RadioButton)findViewById(R.id.spec_custom);

        alpha = (CheckBox)findViewById(R.id.alpha);
        numeric = (CheckBox)findViewById(R.id.numeric);
        special = (CheckBox)findViewById(R.id.special);

        upper = (NumberPicker)findViewById(R.id.upper);
        lower = (NumberPicker)findViewById(R.id.lower);
        num = (NumberPicker)findViewById(R.id.num);
        spec = (NumberPicker)findViewById(R.id.spec);

        custom = (EditText)findViewById(R.id.custom);

        gen_pwd.setOnClickListener(this);
        back.setOnClickListener(this);

        radioSpec.setOnCheckedChangeListener(this);

        alpha.setOnCheckedChangeListener(this);
        numeric.setOnCheckedChangeListener(this);
        special.setOnCheckedChangeListener(this);

        upper.setMaxValue(5);
        upper.setMinValue(0);
        upper.setWrapSelectorWheel(false);
        upper.setEnabled(false);
        lower.setMaxValue(5);
        lower.setMinValue(0);
        lower.setWrapSelectorWheel(false);
        lower.setEnabled(false);
        num.setMaxValue(5);
        num.setMinValue(0);
        num.setWrapSelectorWheel(false);
        num.setEnabled(false);
        spec.setMaxValue(5);
        spec.setMinValue(0);
        spec.setWrapSelectorWheel(false);
        spec.setEnabled(false);

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

    /* Method: gen_pwdClick
     * Purpose: Generates random String
     * Returns: void
    */
    private void gen_pwdClick(){
        StringBuilder buffer = new StringBuilder(), temp = new StringBuilder();
        String characters = "", low, up, number, other = "";
        int length, a = lower.getValue(), A = upper.getValue() , n = num.getValue(), s = spec.getValue();

        // Check for settings
        // generate random lowercase and uppercase Strings
        if (alpha.isChecked()){
            low = "abcdefghijklmnopqrstuvwxyz";
            up = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            for (int i = 0; i < a; i++) {
                int index = (int)(Math.random() * low.length());
                temp.append(low.charAt(index));
            }
            for (int i = 0; i < A; i++) {
                int index = (int)(Math.random() * up.length());
                temp.append(up.charAt(index));
            }
            characters += low + up;
        }
        // generate random numeric String
        if (numeric.isChecked()){
            number = "1234567890";
            for (int i = 0; i < n; i++) {
                double index = Math.random() * number.length();
                temp.append(number.charAt((int) index));
            }
            characters += number;
        }
        // generate random special character String
        if (special.isChecked()){
            if (spec_all.isChecked()){
                other = "~!@#$%^&*()_+`-=[]\\{}|;':\",./<>?";
            }
            else if (spec_custom.isChecked()){
                other = custom.getText().toString();
            }
            for (int i = 0; i < s; i++) {
                int index = (int)(Math.random() * other.length());
                temp.append(other.charAt(index));
            }
            characters += other;
        }

        // Check for no options selected or empty/whitespace in custom special characters
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

        // Generate random String
        length = Min + (int)(Math.random() * ((Max - Min) + 1));
        for (int i = 0; i < length; i++) {
            int index = (int)(Math.random() * characters.length());
            buffer.append(characters.charAt(index));
        }

        // Takes random character sets and overwrites random index
        if (temp.length() > 0) {
            int index;
            ArrayList<Integer> indexs = new ArrayList<>();
            for (int i = 0; i < temp.length(); i++) {
                do {
                    index = (int) (Math.random() * buffer.length());
                }while (indexs.contains(index));
                indexs.add(index);
                buffer.setCharAt(index, temp.charAt(i));
            }
        }

        // Check for duplicates in password history
        if (!(data == null)){
            while(data.contains(buffer.toString())) {
                gen_pwdClick();
            }
            setResult(2, new Intent().putExtra("data", buffer.toString()));
            finish();
        }
        TextView pwd = (TextView) findViewById(R.id.pwd);
        pwd.setText(buffer.toString());
    }

    // Handles button clicks
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.gen_pwd:
                gen_pwdClick();
                break;
            case R.id.cancel: finish();
                break;
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        switch (buttonView.getId()){
            case R.id.alpha: upper.setEnabled(isChecked);
                lower.setEnabled(isChecked);
                if (!lower.isEnabled()){
                    lower.setValue(0);
                }
                break;
            case R.id.numeric: num.setEnabled(isChecked);
                if (!num.isEnabled()){
                    num.setValue(0);
                }
                break;
            case R.id.special: spec.setEnabled(isChecked);
                for (int i = 0; i < radioSpec.getChildCount(); i++){
                    radioSpec.getChildAt(i).setEnabled(isChecked);
                }
                custom.setEnabled(isChecked);
                if (!spec.isEnabled()){
                    spec.setValue(0);
                }
                break;
        }
    }

    // Toggles special characters options
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
