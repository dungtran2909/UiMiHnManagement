package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SanPham implements Serializable {
    @SerializedName("MaSP")
    private int MaSP;

    @SerializedName("TenSP")
    private String TenSP;

    @SerializedName("DonGia")
    private int DonGia;

    @SerializedName("MaDanhMuc")
    private int MaDanhMuc;

    @SerializedName("MoTa")
    private String MoTa;

    @SerializedName("HinhSP")
    private String HinhSP;

    @SerializedName("TinhTrang")
    private boolean TinhTrang;

    @SerializedName("MaNhanHieu")
    private int MaNhanHieu;

    @SerializedName("SoLuongTon")
    private int SoLuongTon;

    public SanPham() {
    }

    public SanPham(int maSP, String tenSP, int donGia, int maDanhMuc, String moTa, String hinhSP, boolean tinhTrang, int maNhanHieu, int soLuongTon) {
        MaSP = maSP;
        TenSP = tenSP;
        DonGia = donGia;
        MaDanhMuc = maDanhMuc;
        MoTa = moTa;
        HinhSP = hinhSP;
        TinhTrang = tinhTrang;
        MaNhanHieu = maNhanHieu;
        SoLuongTon = soLuongTon;
    }

    public int getMaSP() {
        return MaSP;
    }

    public void setMaSP(int maSP) {
        MaSP = maSP;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public int getDonGia() {
        return DonGia;
    }

    public void setDonGia(int donGia) {
        DonGia = donGia;
    }

    public int getMaDanhMuc() {
        return MaDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        MaDanhMuc = maDanhMuc;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getHinhSP() {
        return HinhSP;
    }

    public void setHinhSP(String hinhSP) {
        HinhSP = hinhSP;
    }

    public boolean isTinhTrang() {
        return TinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        TinhTrang = tinhTrang;
    }

    public int getMaNhanHieu() {
        return MaNhanHieu;
    }

    public void setMaNhanHieu(int maNhanHieu) {
        MaNhanHieu = maNhanHieu;
    }

    public int getSoLuongTon() {
        return SoLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        SoLuongTon = soLuongTon;
    }
}
