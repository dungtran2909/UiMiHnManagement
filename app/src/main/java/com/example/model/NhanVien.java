package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NhanVien implements Serializable {
    @SerializedName("MaNV")
    private int maNV;
    @SerializedName("Username")
    private String username;
    @SerializedName("Password")
    private String password;
    @SerializedName("TenNV")
    private String tenNhanVien;
    @SerializedName("Email")
    private String email;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("DiaChi")
    private String diaChi;
    @SerializedName("Role")
    private  int role;

    public NhanVien(int maNV, String username, String password, String tenNhanVien, String email, String phone, String diaChi, int role) {
        this.maNV = maNV;
        this.username = username;
        this.password = password;
        this.tenNhanVien = tenNhanVien;
        this.email = email;
        this.phone = phone;
        this.diaChi = diaChi;
        this.role = role;
    }

    public NhanVien() {
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
