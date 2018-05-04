package com.example.penguinn.hugproject;

/**
 * Created by delaroy on 3/27/17.
 */
public class User {

    private String stu_fname;
    private String stu_lname;
    private String phone;
    private String password;

    private String parent_fname;
    private String parent_lname;
    private String parent_phone;
    private String parent_email;
    private double latitude;
    private double longitude;

    private double school_latitude;
    private double school_longitude;
    private double h_status;
    private double s_status;

    public User() {

    }

    public User(String stu_fname, String stu_lname, String phone,String password,String parent_fname,String parent_lname,String parent_phone,String parent_email) {
        this.stu_fname = stu_fname;
        this.stu_lname = stu_lname;
        this.phone = phone;
        this.password = password;
        this.parent_fname = parent_fname;
        this.parent_lname = parent_lname;
        this.parent_phone = parent_phone;
        this.parent_email = parent_email;
    }


    public String getStu_lname() {
        return stu_lname;
    }

    public void setStu_lname(String stu_lname) {
        this.stu_lname = stu_lname;
    }

    public String getStu_fname(){
        return stu_fname;
    }

    public void setStu_fname(String stu_fname){
        this.stu_fname = stu_fname;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }


    public String getParent_fname() {
        return parent_fname;
    }

    public void setParent_fname(String parent_fname) {
        this.parent_fname = parent_fname;
    }

    public String getParent_lname() {
        return parent_lname;
    }

    public void setParent_lname(String parent_lname) {
        this.parent_lname = parent_lname;
    }

    public String getParent_phone() {
        return parent_phone;
    }

    public void setParent_phone(String parent_phone) {
        this.parent_phone = parent_phone;
    }

    public String getParent_email() {
        return parent_email;
    }

    public void setParent_email(String parent_email) {
        this.parent_email = parent_email;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSchool_latitude(){
        return school_latitude;
    }

    public void setSchool_latitude(double school_latitude){
        this.school_latitude = school_latitude;
    }

    public double getSchool_longitude() {
        return school_longitude;
    }

    public void setSchool_longitude(double school_longitude) {
        this.school_longitude = school_longitude;
    }

    public double getH_status() {
        return h_status;
    }

    public void setH_status(double h_status) {
        this.h_status = h_status;
    }

    public double getS_status() {
        return s_status;
    }

    public void setS_status(double s_status) {
        this.s_status = s_status;
    }
}
