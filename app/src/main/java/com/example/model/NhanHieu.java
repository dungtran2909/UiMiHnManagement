package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NhanHieu implements Serializable {
    @SerializedName("MaNhanHieu")
    private int MaNhanHieu;

    @SerializedName("TenNhanHieu")
    private String TenNhanHieu;

    @SerializedName("HinhNhanHieu")
    private int HinhNhanHieu;

    public NhanHieu() {
    }

    public NhanHieu(int maNhanHieu, String tenNhanHieu, int hinhNhanHieu) {
        MaNhanHieu = maNhanHieu;
        TenNhanHieu = tenNhanHieu;
        HinhNhanHieu = hinhNhanHieu;
    }

    public int getMaNhanHieu() {
        return MaNhanHieu;
    }

    public void setMaNhanHieu(int maNhanHieu) {
        MaNhanHieu = maNhanHieu;
    }

    public String getTenNhanHieu() {
        return TenNhanHieu;
    }

    public void setTenNhanHieu(String tenNhanHieu) {
        TenNhanHieu = tenNhanHieu;
    }

    public int getHinhNhanHieu() {
        return HinhNhanHieu;
    }

    public void setHinhNhanHieu(int hinhNhanHieu) {
        HinhNhanHieu = hinhNhanHieu;
    }

    @Override
    public String toString() {
        return TenNhanHieu;
    }
}
