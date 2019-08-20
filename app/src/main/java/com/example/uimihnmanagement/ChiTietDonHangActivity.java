package com.example.uimihnmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.adapter.SanPhamAdapter;
import com.example.adapter.SanPhamNhapMoiAdapter;
import com.example.model.ChiTietDonHang;
import com.example.model.DonHang;
import com.example.model.SanPham;
import com.example.model.User;
import com.example.network.ApiService;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietDonHangActivity extends AppCompatActivity {
    EditText edtTenKhachHang;
    Spinner spinnerTrangThai;
    ListView lvSanPham;
    SanPhamNhapMoiAdapter sanPhamAdapter;
    ArrayList<SanPham> sanPhams;
    DonHang donHang;
    ArrayAdapter trangthaiAdapter;
    ImageView imgBack, imgSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch_tiet_don_hang);
        addControls();
        addEvents();
    }

    private void addEvents() {
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int trangthai=spinnerTrangThai.getSelectedItemPosition()+1;

            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addControls() {
        Intent intent=getIntent();
        donHang= (DonHang) intent.getSerializableExtra("DONHANG");

        edtTenKhachHang=findViewById(R.id.edtTenKhachHang);
        spinnerTrangThai=findViewById(R.id.spinner_TrangThai);
        lvSanPham=findViewById(R.id.lvSanPham);
        sanPhams= new ArrayList<>();
        sanPhamAdapter= new SanPhamNhapMoiAdapter(ChiTietDonHangActivity.this,R.layout.item_san_pham_nhap_moi,sanPhams);
        lvSanPham.setAdapter(sanPhamAdapter);

        imgBack=findViewById(R.id.iv_backChiTiet);
        imgSave=findViewById(R.id.imgSave);

        ArrayList<String> trangthais=new ArrayList<>();
        trangthais.add("Chờ duyệt");
        trangthais.add("Đã duyệt");
        trangthais.add("Đang giao");
        trangthais.add("Hoàn thành");

        trangthaiAdapter= new ArrayAdapter(ChiTietDonHangActivity.this,android.R.layout.simple_spinner_item);
        trangthaiAdapter.addAll(trangthais);
        spinnerTrangThai.setAdapter(trangthaiAdapter);

        spinnerTrangThai.setSelection(donHang.getTrangThai()-1);
        getTenKhachHang(donHang.getMaKH());
        getCTDonHang(donHang.getMaDonHang());
    }

    private void getCTDonHang(int maDonHang) {
        ApiService.getInstance().getCTDonHangTheoMa(maDonHang, new Callback<List<ChiTietDonHang>>() {
            @Override
            public void onResponse(Call<List<ChiTietDonHang>> call, Response<List<ChiTietDonHang>> response) {
                if (response.isSuccessful()){
                    ArrayList<ChiTietDonHang> chiTietDonHangs= (ArrayList<ChiTietDonHang>) response.body();
                    for (ChiTietDonHang chiTietDonHang:chiTietDonHangs){
                        getSanPham(chiTietDonHang);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietDonHang>> call, Throwable t) {

            }
        });
    }

    private void getSanPham(final ChiTietDonHang chiTietDonHang) {
        ApiService.getInstance().getSanPham(chiTietDonHang.getMaSanPham(), new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                if (response.isSuccessful()){
                    SanPham sanPham=response.body();
                    sanPham.setSoLuongTon(chiTietDonHang.getSoLuong());
                    sanPhams.add(sanPham);
                    sanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable t) {

            }
        });
    }

    private void getTenKhachHang(int maKH) {
        ApiService.getInstance().getUserTheoMa(maKH, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user=response.body();
                    edtTenKhachHang.setText(user.getTenKH());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
