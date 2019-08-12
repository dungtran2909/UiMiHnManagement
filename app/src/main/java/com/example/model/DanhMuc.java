package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DanhMuc implements Serializable {
    @SerializedName("MaDanhMuc")
    private int MaDanhMuc;

    @SerializedName("TenDanhMuc")
    private String TenDanhMuc;

    public DanhMuc() {
    }

    public DanhMuc(String tenDanhMuc) {
        TenDanhMuc = tenDanhMuc;
    }

    public DanhMuc(int maDanhMuc, String tenDanhMuc) {
        MaDanhMuc = maDanhMuc;
        TenDanhMuc = tenDanhMuc;
    }

    public int getMaDanhMuc() {
        return MaDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        MaDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return TenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        TenDanhMuc = tenDanhMuc;
    }


    @Override
    public String toString() {
        return TenDanhMuc;
    }
}
