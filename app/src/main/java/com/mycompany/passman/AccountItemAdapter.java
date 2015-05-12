package com.mycompany.passman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;


public class AccountItemAdapter extends ArrayAdapter<Account> {
    private Activity myContext;
    private ArrayList<Account> data;

    static class ViewHolder{
        TextView WebAddress, UserName, Password, LastDate;
        Button Edit, Details;
        CheckBox Show;
    }

    public AccountItemAdapter(Context context, int textViewResourceId, ArrayList<Account> objects) {
        super(context, textViewResourceId, objects);
        myContext = (Activity) context;
        data = objects;
    }
    
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.account_list, null);
            viewHolder = new ViewHolder();

            viewHolder.WebAddress = (TextView)convertView.findViewById(R.id.Web_Address);
            viewHolder.UserName = (TextView)convertView.findViewById(R.id.User_Name);
            viewHolder.Password = (TextView)convertView.findViewById(R.id.Password);
            viewHolder.LastDate = (TextView)convertView.findViewById(R.id.Date);

            viewHolder.Edit = (Button)convertView.findViewById(R.id.Edit);
            viewHolder.Details = (Button)convertView.findViewById(R.id.Details);

            viewHolder.Show = (CheckBox)convertView.findViewById(R.id.Show_Password);

            viewHolder.Details.setTag(position);
            viewHolder.Edit.setTag(position);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.WebAddress.setText(data.get(position).getAddress());
        viewHolder.UserName.setText(data.get(position).getUser_name());
        viewHolder.Password.setText(data.get(position).getCurPwd());
        viewHolder.LastDate.setText(data.get(position).get_Date());

        viewHolder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur_pos = (Integer) v.getTag();
                myContext.startActivityForResult(new Intent("com.mycompany.passman.Edit").putExtra("data", data.get(cur_pos).toString()).putExtra("pos", cur_pos), 2);
            }
        });

        viewHolder.Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur_pos = (Integer) v.getTag();
                v.getContext().startActivity(new Intent("com.mycompany.passman.Details").putExtra("data", data.get(cur_pos).toString()));
            }
        });

        viewHolder.Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!viewHolder.Show.isChecked()) {
                    // show password
                    viewHolder.Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    viewHolder.Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        return convertView;
    }
}
