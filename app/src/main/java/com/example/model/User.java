package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("MaKH")
    private int MaKH;

    @SerializedName("TenKH")
    private String TenKH;

    @SerializedName("Email")
    private String Email;

    @SerializedName("Phone")
    private int Phone;

    @SerializedName("DiaChi")
    private String DiaChi;

    @SerializedName("Password")
    private String Password;

    public User() {
    }

    public User(int maKH, String tenKH, String email, int phone, String diaChi, String password) {
        MaKH = maKH;
        TenKH = tenKH;
        Email = email;
        Phone = phone;
        DiaChi = diaChi;
        Password = password;
    }

    public int getMaKH() {
        return MaKH;
    }

    public void setMaKH(int maKH) {
        MaKH = maKH;
    }

    public String getTenKH() {
        return TenKH;
    }

    public void setTenKH(String tenKH) {
        TenKH = tenKH;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getPhone() {
        return Phone;
    }

    public void setPhone(int phone) {
        Phone = phone;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return getEmail();
    }
}
