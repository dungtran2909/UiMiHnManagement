package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhieuXuat implements Serializable {
    @SerializedName("MaDonHang")
    private int MaDonHang;

    @SerializedName("MaPhieuXuat")
    private int MaPhieuXuat;

    @SerializedName("MaNV")
    private int MaNV;

    @SerializedName("NgayXuat")
    private String NgayXuat;

    public PhieuXuat() {
    }

    public PhieuXuat(int maDonHang, int maPhieuXuat, int maNV, String ngayXuat) {
        MaDonHang = maDonHang;
        MaPhieuXuat = maPhieuXuat;
        MaNV = maNV;
        NgayXuat = ngayXuat;
    }

    public int getMaDonHang() {
        return MaDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        MaDonHang = maDonHang;
    }

    public int getMaPhieuXuat() {
        return MaPhieuXuat;
    }

    public void setMaPhieuXuat(int maPhieuXuat) {
        MaPhieuXuat = maPhieuXuat;
    }

    public int getMaNV() {
        return MaNV;
    }

    public void setMaNV(int maNV) {
        MaNV = maNV;
    }

    public String getNgayXuat() {
        return NgayXuat;
    }

    public void setNgayXuat(String ngayXuat) {
        NgayXuat = ngayXuat;
    }
}
