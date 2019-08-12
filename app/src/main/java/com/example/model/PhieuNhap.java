package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhieuNhap implements Serializable {
    @SerializedName("MaPhieuNhap")
    private int MaPhieuNhap;

    @SerializedName("MaNV")
    private int MaNV;

    @SerializedName("NgayNhap")
    private String NgayNhap;

    public PhieuNhap() {
    }

    public PhieuNhap(int maPhieuNhap, int maNV, String ngayNhap) {
        MaPhieuNhap = maPhieuNhap;
        MaNV = maNV;
        NgayNhap = ngayNhap;
    }

    public int getMaPhieuNhap() {
        return MaPhieuNhap;
    }

    public void setMaPhieuNhap(int maPhieuNhap) {
        MaPhieuNhap = maPhieuNhap;
    }

    public int getMaNV() {
        return MaNV;
    }

    public void setMaNV(int maNV) {
        MaNV = maNV;
    }

    public String getNgayNhap() {
        return NgayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        NgayNhap = ngayNhap;
    }
}
