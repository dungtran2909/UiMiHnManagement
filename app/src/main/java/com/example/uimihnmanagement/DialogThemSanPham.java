package com.example.uimihnmanagement;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.callback.ThemSanPhamOnclickListener;
import com.example.model.DanhMuc;
import com.example.model.NhanHieu;
import com.example.model.SanPham;
import com.example.network.ApiService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DialogThemSanPham extends Dialog {
    ArrayList<DanhMuc> arrDM;
    ArrayList<NhanHieu> arrNH;
    ArrayAdapter danhMucAdapter;
    ArrayAdapter nhanHieuAdapter;
    Activity activity;

    public DialogThemSanPham(Activity activity,Context context, ThemSanPhamOnclickListener themSanPhamOnclickListener) {
        super(context);
        this.themSanPhamOnclickListener=themSanPhamOnclickListener;
        this.activity=activity;
    }


    ThemSanPhamOnclickListener themSanPhamOnclickListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_them_san_pham);

        final EditText edtTenSanPham=findViewById(R.id.edtTenSanPham);
        final Spinner spinnerDanhMuc=findViewById(R.id.spinner_DanhMuc);
        final Spinner spinnerNhanHieu=findViewById(R.id.spinner_NhanHieu);
        final EditText edtDonGia=findViewById(R.id.edtDonGia);
        final EditText edtSoLuong=findViewById(R.id.edtSoLuong);
        final EditText edtMoTa=findViewById(R.id.edtMoTa);


        danhMucAdapter= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item);
        danhMucAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDanhMuc.setAdapter(danhMucAdapter);

        nhanHieuAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        nhanHieuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNhanHieu.setAdapter(nhanHieuAdapter);

        arrDM= new ArrayList<>();
        arrNH= new ArrayList<>();
        layDanhSachDM();
        layDanhSachNH();

        Button btnLuu = (Button) findViewById(R.id.btnLuu);
        btnLuu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DanhMuc dm= (DanhMuc) spinnerDanhMuc.getSelectedItem();
                NhanHieu nh= (NhanHieu) spinnerNhanHieu.getSelectedItem();
                SanPham sanPham= new SanPham();
                sanPham.setTenSP(edtTenSanPham.getText().toString());
                sanPham.setDonGia(Integer.parseInt(edtDonGia.getText().toString()));
                sanPham.setSoLuongTon(Integer.parseInt(edtSoLuong.getText().toString()));
                sanPham.setMoTa(edtMoTa.getText().toString());
                sanPham.setMaDanhMuc(dm.getMaDanhMuc());
                sanPham.setMaNhanHieu(nh.getMaNhanHieu());
                themSanPhamOnclickListener.onButtonClick(sanPham);
            }
        });

        Button btnHuy=findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    private void layDanhSachDM(){
        ApiService.getInstance().getDanhMuc(new Callback<List<DanhMuc>>() {
            @Override
            public void onResponse(Call<List<DanhMuc>> call, Response<List<DanhMuc>> response) {
                Log.d("AAAAA", response.toString());
                if(response.isSuccessful()){
                    List<DanhMuc> list = response.body();

                    danhMucAdapter.addAll(list);
                    danhMucAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<DanhMuc>> call, Throwable t) {
                Log.d("aaaaaa", t.toString());
            }
        });
    }

    private void layDanhSachNH(){
        ApiService.getInstance().getNhanHieu(new Callback<List<NhanHieu>>() {
            @Override
            public void onResponse(Call<List<NhanHieu>> call, Response<List<NhanHieu>> response) {
                if (response.isSuccessful()){
                    List<NhanHieu> list = response.body();
                    nhanHieuAdapter.addAll(list);
                    nhanHieuAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<NhanHieu>> call, Throwable t) {

            }
        });
    }
}
