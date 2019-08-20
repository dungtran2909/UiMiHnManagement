package com.example.network;

import com.example.model.ChiTietDonHang;
import com.example.model.ChiTietPhieuNhap;
import com.example.model.ChiTietPhieuXuat;
import com.example.model.DanhMuc;
import com.example.model.DonHang;
import com.example.model.NhanHieu;
import com.example.model.NhanVien;
import com.example.model.PhieuNhap;
import com.example.model.PhieuXuat;
import com.example.model.SanPham;
import com.example.model.User;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {
    @GET("danhmuc")
    Call<List<DanhMuc>> getDanhMuc();

    @GET("nhanhieu")
    Call<List<NhanHieu>> getNhanHieu();

    @GET("phieunhap")
    Call<List<PhieuNhap>> getListPhieuNhap();

    @GET("ctphieunhap")
    Call<List<ChiTietPhieuNhap>> getAllPN();

    @GET("ctphieunhap/{id}")
    Call<List<ChiTietPhieuNhap>> getDetailPhieuNhap(@Path("id") int id);

    @GET("phieuxuat")
    Call<List<PhieuXuat>> getListPhieuXuat();

    @GET("ctphieuxuat/{id}")
    Call<List<ChiTietPhieuXuat>> getDetailPhieuXuat(@Path("id") int id);

    @GET("sanpham")
    Call<List<SanPham>> getAllSanPham();

    @GET("sanpham/{id}")
    Call<SanPham> getSanPham(@Path("id") int id);

    @GET("sanpham")
    Call<List<SanPham>> getListSanPhamTheoDanhMuc(@Query("madm") int madm);

    @GET("sanpham")
    Call<SanPham> getSanPhamTheoTen(@Query("tenSanPhamTim") String tensp);

    @POST("sanpham")
    Call<Boolean> xoaSanPham(@Query("maSp") int masp);

    @POST("sanpham")
    Call<Boolean> suaTenSanPham(@Query("maSpSua") int masp, @Query("tenSp") String tensp);

    @POST("sanpham")
    Call<Boolean> suaDonGiaSanPham(@Query("maSpSua") int masp, @Query("giaSp") int giaSp);

    @POST("sanpham")
    Call<Boolean> suaMoTaSanPham(@Query("maSpSua") int masp, @Query("moTa") String mota);

    @POST("sanpham")
    Call<Boolean> suaHinhSanPham(@Query("maSpSua") int masp, @Query("hinh") String hinh);

    @POST("sanpham")
    Call<Boolean> luuSanPhamMoi(@Query("tensp") String tensp, @Query("giasp") int giaSp, @Query("maDm") int maDm, @Query("tinhTrang") boolean tinhtrang,
                                @Query("moTa") String mota, @Query("hinh") String hinh, @Query("manhanhieu") int manhanhieu, @Query("soLuong") int soluong);

    @POST("phieunhap")
    Call<Boolean> luuMoiPhieuNhap(@Query("maNv") int maNv, @Query("ngaynhap") String ngayNhap);

    @POST("CTPhieuNhap")
    Call<Boolean> luuMoiCTPhieuNhap(@Query("mapn") int mapn, @Query("masp") int masp, @Query("soluong") int soluong, @Query("gianhap") int gianhap);

    @GET("PhieuNhap")
    Call<List<PhieuNhap>> layPhieuNhapTheoMaNV(@Query("manv") int manv);

    @GET("nhanvien")
    Call<List<NhanVien>> getNhanVienTheoUsername(@Query("userName") String username);


    @GET("nhanvien")
    Call<NhanVien> getNhanVienTheoTen(@Query("ten") String ten);

    @GET("nhanvien")
    Call<List<NhanVien>> getAllNhanVien();

    @POST("nhanvien")
    Call<Boolean> luuMoiNhanVien(@Query("tennv") String tenNV, @Query("diachi") String diaChi, @Query("phone") String phone, @Query("email") String email, @Query("username") String userName, @Query("password") String password, @Query("role") int role);

    @GET("nhanvien/{id}")
    Call<NhanVien> getNhanVienTheoMa(@Path("id") int id);

    @GET("donhang")
    Call<List<DonHang>> getAllDonHang();

    @GET("khachhang/{id}")
    Call<User> getUsertheoMa(@Query("id") int id);

    @GET("ctdonhang/{id}")
    Call<List<ChiTietDonHang>> getCTDonHangTheoMa(@Query("id") int id);
}