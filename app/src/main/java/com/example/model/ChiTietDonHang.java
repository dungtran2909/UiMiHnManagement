package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChiTietDonHang implements Serializable {
    @SerializedName("MaDonHang")
    private int maDonHang;
    @SerializedName("MaSP")
    private int maSanPham;
    @SerializedName("SoLuong")
    private int soLuong;

    public ChiTietDonHang(int maDonHang, int maSanPham, int soLuong) {
        this.maDonHang = maDonHang;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
    }

    public ChiTietDonHang() {
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
