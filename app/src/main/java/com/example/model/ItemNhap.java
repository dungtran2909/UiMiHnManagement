package com.example.model;

public class ItemNhap {
    SanPham sanPham;
    private String NgayNhap;

    public ItemNhap(SanPham sanPham, String ngayNhap) {
        this.sanPham = sanPham;
        NgayNhap = ngayNhap;
    }

    public ItemNhap() {
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public String getNgayNhap() {
        return NgayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        NgayNhap = ngayNhap;
    }
}
