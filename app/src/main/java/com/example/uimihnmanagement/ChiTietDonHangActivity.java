package com.example.uimihnmanagement;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adapter.SanPhamAdapter;
import com.example.adapter.SanPhamNhapMoiAdapter;
import com.example.model.ChiTietDonHang;
import com.example.model.ChiTietPhieuXuat;
import com.example.model.DonHang;
import com.example.model.PhieuXuat;
import com.example.model.SanPham;
import com.example.model.User;
import com.example.network.ApiService;
import com.google.android.gms.common.api.Api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    ImageView imgBack, imgSave, imgEdit;
    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");

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
                int trangthai=spinnerTrangThai.getSelectedItemPosition();
                if (trangthai>donHang.getTrangThai()){
                    updateTrangThaiDonHang(trangthai);
                }
                else
                    Toast.makeText(ChiTietDonHangActivity.this,"Không thể lưu",Toast.LENGTH_LONG).show();
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerTrangThai.setEnabled(true);
                imgSave.setVisibility(View.VISIBLE);
                imgEdit.setVisibility(View.GONE);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    private void updateTrangThaiDonHang(int trangthai) {
        ApiService.getInstance().editTrangThaiDonHang(donHang.getMaDonHang(), trangthai, new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean kq=response.body();
                    if (kq==true){
                        Toast.makeText(ChiTietDonHangActivity.this,"Lưu thành công",Toast.LENGTH_LONG).show();
                        imgEdit.setVisibility(View.VISIBLE);
                        imgSave.setVisibility(View.GONE);
                        //tạo mới phiếu xuất
                        if (spinnerTrangThai.getSelectedItemPosition()==1){
                            createNewPhieuXuat();
                        }
                    }
                    else
                        Toast.makeText(ChiTietDonHangActivity.this,"Lưu thất bại, vui lòng thử lại",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void createNewPhieuXuat() {
        PhieuXuat phieuXuat= new PhieuXuat();
        phieuXuat.setMaDonHang(donHang.getMaDonHang());
        phieuXuat.setMaNV(MainActivity.nhanVienLogin.getMaNV());
        phieuXuat.setNgayXuat(simpleDateFormat.format(new Date(System.currentTimeMillis())));
        ApiService.getInstance().createNewPhieuXuat(phieuXuat, new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean kq=response.body();
                    if (kq==true){
                        Toast.makeText(ChiTietDonHangActivity.this,"Tạo phiếu xuất thành công",Toast.LENGTH_LONG).show();

                        //lấy chi tiết phiếu xuất mới tạo
                        getPhieuXuat();
                    }
                    else
                        Toast.makeText(ChiTietDonHangActivity.this,"Tạo phiếu xuất thất bại, vui lòng kiểm tra lại",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void getPhieuXuat() {
        ApiService.getInstance().getPhieuXuatTheoMaDonHang(donHang.getMaDonHang(), new Callback<List<PhieuXuat>>() {
            @Override
            public void onResponse(Call<List<PhieuXuat>> call, Response<List<PhieuXuat>> response) {
                if (response.isSuccessful()){
                    ArrayList<PhieuXuat> phieuXuatArrayList= (ArrayList<PhieuXuat>) response.body();
                    createChiTietPhieuXuat(phieuXuatArrayList.get(0).getMaPhieuXuat());
                }
            }

            @Override
            public void onFailure(Call<List<PhieuXuat>> call, Throwable t) {

            }
        });
    }

    private void createChiTietPhieuXuat(int maPhieuXuat) {
        for (final SanPham sanPham : sanPhams){
            ApiService.getInstance().createNewChiTietPhieuXuat(new ChiTietPhieuXuat(maPhieuXuat, sanPham.getMaSP(), sanPham.getSoLuongTon()), new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()){
                        boolean kq=response.body();
                        if (kq==true){
                            System.out.println(sanPham.getTenSP()+" thêm cho tiết phiếu nhập thành công");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });
        }
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
        imgSave.setVisibility(View.GONE);
        imgEdit=findViewById(R.id.imgEdit);
        imgEdit.setVisibility(View.VISIBLE);

        ArrayList<String> trangthais=new ArrayList<>();
        trangthais.add("Chờ duyệt");//0
        trangthais.add("Đã duyệt");//1
        trangthais.add("Đang giao");//2
        trangthais.add("Hoàn thành");//3

        trangthaiAdapter= new ArrayAdapter(ChiTietDonHangActivity.this,android.R.layout.simple_spinner_item);
        trangthaiAdapter.addAll(trangthais);
        spinnerTrangThai.setAdapter(trangthaiAdapter);

        spinnerTrangThai.setSelection(donHang.getTrangThai());
        spinnerTrangThai.setEnabled(false);
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
