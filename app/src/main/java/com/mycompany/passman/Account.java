package com.mycompany.passman;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Account {
    private String address, user_name, curPwd;
    private ArrayList<String> pwds;
    private Date last_update;

    public Account(String data){
        String[] temp = data.split(" ");
        setDate(temp[0]);
        setAddress(temp[1]);
        setUser_name(temp[2]);
        setCurPwd(temp[3]);
        setPwds(new ArrayList<>(Arrays.asList(temp).subList(4, temp.length)));
    }

    public Account(String date, String address, String user_name, String curPwd, ArrayList<String> pwds){
        setDate(date);
        setAddress(address);
        setUser_name(user_name);
        setCurPwd(curPwd);
        setPwds(pwds);
    }

    public Account(String address, String user_name, String pwd){
        this.setAddress(address);
        this.setUser_name(user_name);
        this.setCurPwd(pwd);
        this.last_update = new Date();
        this.pwds = new ArrayList<>();
    }

    public void fromString(String data){
        String[] temp = data.split(" ");
        setDate(temp[0]);
        setAddress(temp[1]);
        setUser_name(temp[2]);
        setCurPwd(temp[3]);
        setPwds(new ArrayList<>(Arrays.asList(temp).subList(4, temp.length)));
    }

    public void setDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            this.last_update = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setUser_name(String user_name){
        this.user_name = user_name;
    }

    public void setCurPwd(String curPwd) {
        this.curPwd = curPwd;
    }

    public void setPwds(ArrayList<String> pwds) {
        this.pwds = pwds;
    }

    public void add_pwd(String pwd){
        this.pwds.add(this.curPwd);
        this.curPwd = pwd;
        this.last_update = new Date();

        while (this.pwds.size() > 10){
            this.pwds.remove(0);
        }
    }

    public String get_date(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String date = format.format(this.last_update);
        return date;
    }

    public String get_Date(){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String date = format.format(this.last_update);
        return date;
    }

    public String getAddress(){
        return this.address;
    }

    public String getUser_name(){
        return this.user_name;
    }

    public String getCurPwd(){
        return this.curPwd;
    }

    public ArrayList<String> getPwds(){
        return this.pwds;
    }

    public String getPwdsString(){
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < this.pwds.size(); i++){
            buffer.append(this.pwds.get(i)).append(" ");
        }
        return buffer.toString();
    }

    @Override
    public String toString(){
        return get_date() + " " + getAddress() + " " + getUser_name() + " " + getCurPwd() + " " + getPwdsString();
    }
}
