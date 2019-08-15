package com.example.uimihnmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.NhanVienAdapter;
import com.example.adapter.SanPhamAdapter;
import com.example.firebase.NhanVienFirebase;
import com.example.model.ChiTietPhieuXuat;
import com.example.model.ItemNhap;
import com.example.model.NhanVien;
import com.example.model.PhieuXuat;
import com.example.model.SanPham;
import com.example.network.ApiService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.uimihnmanagement.MainActivity.nhanVienLogin;

public class NhanSuFragment extends Fragment {
    View view;
    ListView lv_NhanVien;
    ImageView  iv_add;
    NhanVienAdapter nhanVienAdapter;
    ArrayList<NhanVien> dsNhanVien;
    LinearLayout linearLayout_tim;
    TextView txtTen, txtChucVu;
    Button btnDong,btnTim;
    EditText edtNhapVao;
    Spinner spinner_ChucVu;
    ArrayAdapter<String> chucVuAdapter;

    AlertDialog.Builder dialog;
    boolean chucNangTim=true;
    int viTri=0;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;
    TextView txtThem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nhan_su, container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        txtTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_tim.setVisibility(View.VISIBLE);
                edtNhapVao.setVisibility(View.VISIBLE);
                spinner_ChucVu.setVisibility(View.GONE);
                txtTen.setBackgroundResource(R.drawable.border_txt_select);
                txtChucVu.setBackgroundResource(R.drawable.border_edittext);
                txtTen.setTextColor(Color.WHITE);
                txtChucVu.setTextColor(Color.BLACK);
                chucNangTim=true;
            }
        });
        txtChucVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout_tim.setVisibility(View.VISIBLE);
                edtNhapVao.setVisibility(View.GONE);

                spinner_ChucVu.setVisibility(View.VISIBLE);
                txtChucVu.setBackgroundResource(R.drawable.border_txt_select);
                txtTen.setBackgroundResource(R.drawable.border_edittext);
                txtChucVu.setTextColor(Color.WHITE);
                txtTen.setTextColor(Color.BLACK);
                chucNangTim=false;
            }
        });
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chucNangTim==true){
                    xuLyTimTheoTen();
                }
                else if(chucNangTim==false){
                    xuLyTimTheoChucVu();
                }
            }
        });
        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_tim.setVisibility(View.GONE);
                txtChucVu.setBackgroundResource(R.drawable.border_edittext);
                txtTen.setBackgroundResource(R.drawable.border_edittext);
                txtChucVu.setTextColor(Color.BLACK);
                txtTen.setTextColor(Color.BLACK);
                layDanhSachNhanVien();
            }
        });
        lv_NhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(view.getContext(),ChiTietNhanSu.class);
                NhanVien nhanVien=nhanVienAdapter.getItem(position);
                intent.putExtra("NHANVIEN",nhanVien);
                intent.putExtra("NHANVIENLOGIN",nhanVienLogin);
                startActivity(intent);
            }
        });
        txtThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyThemNhanVien();
            }
        });
    }

    private void xuLyTimTheoChucVu() {
        if(spinner_ChucVu.getSelectedItemPosition()==0){
            ApiService.getInstance().getAllNhanVien(new Callback<List<NhanVien>>() {
                @Override
                public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                    if (response.isSuccessful()){
                        ArrayList<NhanVien> nhanViens= (ArrayList<NhanVien>) response.body();
                        dsNhanVien.clear();
                        for (NhanVien vien :nhanViens){
                            if (vien.getRole()==0){
                                dsNhanVien.add(vien);
                            }
                        }
                        nhanVienAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<List<NhanVien>> call, Throwable t) {

                }
            });
        }
        else  if(spinner_ChucVu.getSelectedItemPosition()==1){
            ApiService.getInstance().getAllNhanVien(new Callback<List<NhanVien>>() {
                @Override
                public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                    if (response.isSuccessful()){
                        ArrayList<NhanVien> nhanViens= (ArrayList<NhanVien>) response.body();
                        dsNhanVien.clear();
                        for (NhanVien vien :nhanViens){
                            if (vien.getRole()==1){
                                dsNhanVien.add(vien);
                            }
                        }
                        nhanVienAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<List<NhanVien>> call, Throwable t) {

                }
            });
        }
    }

    private void xuLyThemNhanVien() {
        Intent intent= new Intent(view.getContext(),ThemNhanVienActivity.class);
        startActivityForResult(intent,1);
    }

    private void xuLyTimTheoTen() {
        String ten= String.valueOf(edtNhapVao.getText());
        if(ten!=null){
            ApiService.getInstance().getNhanVienTheoTen(ten, new Callback<NhanVien>() {
                @Override
                public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                    NhanVien nhanVien= (NhanVien) response.body();
                    if (response.isSuccessful()){
                        if (nhanVien!=null){
                            dsNhanVien.clear();
                            dsNhanVien.add(nhanVien);
                            nhanVienAdapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(view.getContext(),"Không tìm thấy nhân viên",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<NhanVien> call, Throwable t) {

                }
            });
        }
        else
            Toast.makeText(view.getContext(), "Vui lòng nhập thông tin cần tìm", Toast.LENGTH_LONG).show();
    }

    private void addControls() {
        dsNhanVien = new ArrayList<>();
        lv_NhanVien = view.findViewById(R.id.lv_NhanVien);
        nhanVienAdapter = new NhanVienAdapter(getActivity(), R.layout.item_row_nhanvien, dsNhanVien);
        layDanhSachNhanVien();
        lv_NhanVien.setAdapter(nhanVienAdapter);
        iv_add=view.findViewById(R.id.iv_addNhanSu);
        linearLayout_tim=view.findViewById(R.id.ll_seach);
        linearLayout_tim.setVisibility(View.GONE);
        txtChucVu=view.findViewById(R.id.txtChucVuNS);
        txtTen=view.findViewById(R.id.txtTenNS);
        btnDong=view.findViewById(R.id.btnDongNhanSu);
        btnTim=view.findViewById(R.id.btnTimNhanSu);
        edtNhapVao=view.findViewById(R.id.edtNhapVaoNhanSu);

        spinner_ChucVu= view.findViewById(R.id.spiner_ChucVuNhanSu);
        chucVuAdapter=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_spinner_item);
        chucVuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayList<String> dsChucVu= new ArrayList<>();
        dsChucVu.add("Quản lý");
        dsChucVu.add("Nhân viên");
        chucVuAdapter.addAll(dsChucVu);
        spinner_ChucVu.setAdapter(chucVuAdapter);
        
        txtThem=view.findViewById(R.id.txtThemNV);
    }

    private void layDanhSachNhanVien() {
        progressDialog= new ProgressDialog(view.getContext());
        progressDialog.setTitle("Đang lấy danh sách nhân viên");
        progressDialog.setMessage("Vui lòng chờ");
        progressDialog.show();
        ApiService.getInstance().getAllNhanVien(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if (response.isSuccessful()){
                    ArrayList<NhanVien> nhanViens= (ArrayList<NhanVien>) response.body();
                    dsNhanVien.clear();
                    dsNhanVien.addAll(nhanViens);
                    nhanVienAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                layDanhSachNhanVien();
            }
        }
    }
}
