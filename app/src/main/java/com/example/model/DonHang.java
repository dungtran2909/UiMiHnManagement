package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DonHang implements Serializable {
    @SerializedName("MaDonHang")
    private int MaDonHang;

    @SerializedName("MaKH")
    private int MaKH;

    @SerializedName("TrangThai")
    private int TrangThai;

    @SerializedName("NgayDat")
    private String NgayDat;

    @SerializedName("NgayGiao")
    private String NgayGiao;

    public DonHang() {
    }

    public DonHang(int maDonHang, int maKH, int trangThai, String ngayDat, String ngayGiao) {
        MaDonHang = maDonHang;
        MaKH = maKH;
        TrangThai = trangThai;
        NgayDat = ngayDat;
        NgayGiao = ngayGiao;
    }

    public int getMaDonHang() {
        return MaDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        MaDonHang = maDonHang;
    }

    public int getMaKH() {
        return MaKH;
    }

    public void setMaKH(int maKH) {
        MaKH = maKH;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    public String getNgayDat() {
        return NgayDat;
    }

    public void setNgayDat(String ngayDat) {
        NgayDat = ngayDat;
    }

    public String getNgayGiao() {
        return NgayGiao;
    }

    public void setNgayGiao(String ngayGiao) {
        NgayGiao = ngayGiao;
    }
}
