package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChiTietPhieuNhap implements Serializable {
    @SerializedName("MaPhieuNhap")
    private int MaPhieuNhap;

    @SerializedName("MaSP")
    private int MaSP;

    @SerializedName("SoLuong")
    private int SoLuong;

    @SerializedName("GiaNhap")
    private int GiaNhap;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(int maPhieuNhap, int maSP, int soLuong, int giaNhap) {
        MaPhieuNhap = maPhieuNhap;
        MaSP = maSP;
        SoLuong = soLuong;
        GiaNhap = giaNhap;
    }

    public int getMaPhieuNhap() {
        return MaPhieuNhap;
    }

    public void setMaPhieuNhap(int maPhieuNhap) {
        MaPhieuNhap = maPhieuNhap;
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

    public int getGiaNhap() {
        return GiaNhap;
    }

    public void setGiaNhap(int giaNhap) {
        GiaNhap = giaNhap;
    }
}
