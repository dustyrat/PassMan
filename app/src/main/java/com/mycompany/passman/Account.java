package com.mycompany.passman;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/* Class: Account
 * Purpose: To get and store an account's data
 * Private Variables: address - String that holds the URL
 *                    user_name - String that holds the User Name
 *                    curPwd - String that holds the Current Password
 *                    pwds - ArrayList<String> that holds the Password History
 *                    last_update - Date that holds the late date the account was updated
 */
public class Account {
    private String address, // URL
            user_name, // User Name
            curPwd; // Current Password
    private ArrayList<String> pwds; // Password history
    private Date last_update; // Last time the Account was updated

    /* Constructor: Account
     * Purpose: Initializes class by separating a string by whitespace
    * Parameters: data - String containing date, address,username, current password and password history
    */
    public Account(String data){
        String[] temp = data.split(" ");
        setDate(temp[0]);
        setAddress(temp[1]);
        setUser_name(temp[2]);
        setCurPwd(temp[3]);
        setPwds(new ArrayList<>(Arrays.asList(temp).subList(4, temp.length)));
    }
    
    /* Constructor: Account
     * Purpose: Initializes class
     * Parameters: date - String to set the date - format: yyyyMMdd
     * 			   address - String to set the URL
     * 			   user_name - String to set User Name
     * 			   curPwd - String to set th Current Password
     * 			   pwds - ArrayList<String> to set the password history
     */
    public Account(String date, String address, String user_name, String curPwd, ArrayList<String> pwds){
        setDate(date);
        setAddress(address);
        setUser_name(user_name);
        setCurPwd(curPwd);
        setPwds(pwds);
    }

    /* Constructor: Account
     * Purpose: Initializes class
     * Parameters: address - String to set the URL
     * 			   user_name - String to set the User Name
     * 			   pwd - String to set Password
     */
    public Account(String address, String user_name, String pwd){
        this.setAddress(address);
        this.setUser_name(user_name);
        this.setCurPwd(pwd);
        this.last_update = new Date();
        this.pwds = new ArrayList<>();
    }

    /* Method: fromString
     * Purpose: convert String into Account
     * Parameters: data - String containing date, address,username, current password and password history
     * Return value: void
     */
    public void fromString(String data){
        String[] temp = data.split(" ");
        setDate(temp[0]);
        setAddress(temp[1]);
        setUser_name(temp[2]);
        setCurPwd(temp[3]);
        setPwds(new ArrayList<>(Arrays.asList(temp).subList(4, temp.length)));
    }

    /* Method: setDate
     * Purpose: converts String into Date and sets last_update
     * Parameters: date - String format: yyyyMMdd
     * Return value: void
     */
    public void setDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            this.last_update = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /* Method: setAddress
     * Purpose: sets address
     * Parameters: address - String
     * Return value: void
     */
    public void setAddress(String address){
        this.address = address;
    }

    /* Method: setUser_name
     * Purpose: sets user_name
     * Parameters: user_name - String
     * Return value: void
     */
    public void setUser_name(String user_name){
        this.user_name = user_name;
    }

    /* Method: setCurPwd
     * Purpose: sets curPwd
     * Parameters: curPwd - String
     * Return value: void
     */
    public void setCurPwd(String curPwd) {
        this.curPwd = curPwd;
    }

    /* Method: setPwds
     * Purpose: sets password history
     * Parameters: pwds - ArrayList<String>
     * Return value: void
     */
    public void setPwds(ArrayList<String> pwds) {
        this.pwds = pwds;
    }

    /* Method: add_pwd
     * Purpose: adds current password to password history and sets current password
     * Parameters: pwd - String
     * Return value: void
     */
    public void add_pwd(String pwd){
        this.pwds.add(this.curPwd);
        this.curPwd = pwd;
        this.last_update = new Date();

        while (this.pwds.size() > 10){
            this.pwds.remove(0);
        }
    }

    /* Method: get_date
     * Purpose: returns last_update format: yyyyMMdd
     * Return value: String
     */
    public String get_date(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(this.last_update);
    }

    /* Method: get_Date
     * Purpose: returns last_update format: MM/dd/yyyy
     * Return value: String
     */
    public String get_Date(){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(this.last_update);
    }

    /* Method: getAddress
     * Purpose: returns address
     * Return value: String
     */
    public String getAddress(){
        return this.address;
    }

    /* Method: getUser_name
     * Purpose: returns user_name
     * Return value: String
     */
    public String getUser_name(){
        return this.user_name;
    }

    /* Method: getCurPwd
     * Purpose: returns curPwd
     * Return value: String
     */
    public String getCurPwd(){
        return this.curPwd;
    }

    /* Method: getPwds
     * Purpose: returns pwds
     * Return value: ArrayList<String>
     */
    public ArrayList<String> getPwds(){
        return this.pwds;
    }

    /* Method: getPwdsString
     * Purpose: returns pwds separated by whitespace
     * Return value: String
     */
    public String getPwdsString(){
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < this.pwds.size(); i++){
            buffer.append(this.pwds.get(i)).append(" ");
        }
        return buffer.toString();
    }

    /* Method: toString
     * Purpose: returns last_update, address, user_name, curPwd and pwds separated by whitespace
     * Return value: void
     */
    @Override
    public String toString(){
        return get_date() + " " + getAddress() + " " + getUser_name() + " " + getCurPwd() + " " + getPwdsString();
    }
}
