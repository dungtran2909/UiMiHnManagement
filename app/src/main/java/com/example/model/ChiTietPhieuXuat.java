package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChiTietPhieuXuat implements Serializable {
    @SerializedName("MaPhieuXuat")
    private int MaPhieuXuat;

    @SerializedName("MaSP")
    private int MaSP;

    @SerializedName("SoLuong")
    private int SoLuong;

    public ChiTietPhieuXuat() {
    }

    public ChiTietPhieuXuat(int maPhieuXuat, int maSP, int soLuong) {
        MaPhieuXuat = maPhieuXuat;
        MaSP = maSP;
        SoLuong = soLuong;
    }

    public int getMaPhieuXuat() {
        return MaPhieuXuat;
    }

    public void setMaPhieuXuat(int maPhieuXuat) {
        MaPhieuXuat = maPhieuXuat;
    }

    public int getMaSP() {
        return MaSP;
    }

    public void setMaSP(int maSP) {
        MaSP = maSP;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }
}
