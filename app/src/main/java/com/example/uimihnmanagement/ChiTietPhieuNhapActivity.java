package com.example.uimihnmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.adapter.SanPhamNhapMoiAdapter;
import com.example.model.ChiTietPhieuNhap;
import com.example.model.ItemNhap;
import com.example.model.NhanVien;
import com.example.model.PhieuNhap;
import com.example.model.SanPham;
import com.example.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietPhieuNhapActivity extends AppCompatActivity {
    ImageView imgBack, imgAdd, imgSave;
    EditText edtNhanVienNhap, edtNgayNhap;
    ListView lvSanPham;
    SanPhamNhapMoiAdapter sanPhamNhapMoiAdapter;
    ArrayList<SanPham> sanPhams;
    PhieuNhap phieuNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phieu_nhap);
        addcontrols();
        addEvents();
    }

    private void addEvents() {
        lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= new Intent(ChiTietPhieuNhapActivity.this,ChiTietSanPhamActivity.class);
                intent.putExtra("SANPHAM",sanPhams.get(i));
                startActivity(intent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addcontrols() {
        Intent intent= getIntent();
        phieuNhap= (PhieuNhap) intent.getSerializableExtra("PHIEUNHAP");
        imgBack=findViewById(R.id.iv_backChiTiet);
        edtNhanVienNhap=findViewById(R.id.edtMaNhanVienNhap);
        edtNgayNhap=findViewById(R.id.edtNgayNhapHang);
        sanPhams= new ArrayList<>();
        lvSanPham=findViewById(R.id.lvSanPham);
        sanPhamNhapMoiAdapter= new SanPhamNhapMoiAdapter(ChiTietPhieuNhapActivity.this,R.layout.item_san_pham_nhap_moi,sanPhams);
        lvSanPham.setAdapter(sanPhamNhapMoiAdapter);
        String []ngay=phieuNhap.getNgayNhap().split("T");
        edtNgayNhap.setText(ngay[0]);
        getChoTietNhanVienTheoMa(phieuNhap.getMaNV());

        getDataDetailPN(phieuNhap);
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
    private void getDataDetailPN(final PhieuNhap pn) {
        ApiService.getInstance().getDetailPhieuNhap(pn.getMaPhieuNhap(), new Callback<List<ChiTietPhieuNhap>>() {
            @Override
            public void onResponse(Call<List<ChiTietPhieuNhap>> call, Response<List<ChiTietPhieuNhap>> response) {
                if (response.isSuccessful()) {
                    List<ChiTietPhieuNhap> arrChiTietPN = response.body();
                    for (ChiTietPhieuNhap ctpn : arrChiTietPN) {
                        getDataSanPham(ctpn, pn);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietPhieuNhap>> call, Throwable t) {

            }
        });
    }
    private void getDataSanPham(final ChiTietPhieuNhap ctpn, final PhieuNhap pn) {
        ApiService.getInstance().getSanPham(ctpn.getMaSP(), new Callback<SanPham>() {
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
}
