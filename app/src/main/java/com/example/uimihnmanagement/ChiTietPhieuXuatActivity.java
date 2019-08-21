package com.example.uimihnmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.adapter.SanPhamNhapMoiAdapter;
import com.example.model.ChiTietPhieuXuat;
import com.example.model.NhanVien;
import com.example.model.PhieuNhap;
import com.example.model.PhieuXuat;
import com.example.model.SanPham;
import com.example.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietPhieuXuatActivity extends AppCompatActivity {
    PhieuXuat phieuXuat;

    ImageView imgBack, imgAdd, imgSave;
    EditText edtNhanVienNhap, edtNgayNhap;
    ListView lvSanPham;
    SanPhamNhapMoiAdapter sanPhamNhapMoiAdapter;
    ArrayList<SanPham> sanPhams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phieu_xuat);
        addControl();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControl() {
        Intent intent=getIntent();
        phieuXuat= (PhieuXuat) intent.getSerializableExtra("PHIEUXUAT");
        imgBack=findViewById(R.id.iv_backChiTiet);
        edtNhanVienNhap=findViewById(R.id.edtMaNhanVienNhap);
        edtNgayNhap=findViewById(R.id.edtNgayNhapHang);
        sanPhams= new ArrayList<>();
        lvSanPham=findViewById(R.id.lvSanPham);
        sanPhamNhapMoiAdapter= new SanPhamNhapMoiAdapter(ChiTietPhieuXuatActivity.this,R.layout.item_san_pham_nhap_moi,sanPhams);
        lvSanPham.setAdapter(sanPhamNhapMoiAdapter);
        String []ngay=phieuXuat.getNgayXuat().split("T");
        edtNgayNhap.setText(ngay[0]);
        getChoTietNhanVienTheoMa(phieuXuat.getMaNV());
        getChiTietPhieuXuat();
    }

    private void getChiTietPhieuXuat() {
        ApiService.getInstance().getPhieuXuatTheoMa(phieuXuat.getMaPhieuXuat(), new Callback<List<ChiTietPhieuXuat>>() {
            @Override
            public void onResponse(Call<List<ChiTietPhieuXuat>> call, Response<List<ChiTietPhieuXuat>> response) {
                if (response.isSuccessful()){
                    ArrayList<ChiTietPhieuXuat> chiTietPhieuXuats= (ArrayList<ChiTietPhieuXuat>) response.body();
                    for (ChiTietPhieuXuat chiTietPhieuXuat : chiTietPhieuXuats){
                        getChiTietSanPham(chiTietPhieuXuat.getMaSP());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietPhieuXuat>> call, Throwable t) {

            }
        });
    }

    private void getChiTietSanPham(int maSP) {
        ApiService.getInstance().getSanPham(maSP, new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                if (response.isSuccessful()) {
                    SanPham sp = response.body();
                    sanPhams.add(sp);
                    sanPhamNhapMoiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable t) {

            }
        });
    }


    private void getChoTietNhanVienTheoMa(int maNV) {
        ApiService.getInstance().getNhanVienTheoMa(maNV, new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.isSuccessful()){
                    NhanVien nhanVien=response.body();
                    edtNhanVienNhap.setText(nhanVien.getTenNhanVien());
                }
            }

            @Override
            public void onFailure(Call<NhanVien> call, Throwable t) {

            }
        });
    }
}
