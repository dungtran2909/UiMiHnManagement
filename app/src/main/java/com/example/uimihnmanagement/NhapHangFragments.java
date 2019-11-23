package com.example.uimihnmanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.PhieuNhapAdapter;
import com.example.adapter.SanPhamAdapter;
import com.example.model.ChiTietPhieuNhap;
import com.example.model.DanhMuc;
import com.example.model.ItemNhap;
import com.example.model.NhanVien;
import com.example.model.PhieuNhap;
import com.example.model.SanPham;
import com.example.network.ApiService;
import com.google.android.gms.common.api.Api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NhapHangFragments extends Fragment {
    private static final int REQUEST_CODE_ADD = 123;
    static View view;
    ListView lvPhieuNhap;
    ArrayList<PhieuNhap> phieuNhaps;
    PhieuNhapAdapter phieuNhapAdapter;
    ImageView imgAdd;
    Spinner spinnerTraCuu;
    ArrayAdapter chucNangAdapter;
    LinearLayout llNgay;
    Spinner spinnerNhanVien;
    ArrayAdapter<NhanVien> nhanVienArrayAdapter;
    ArrayList<NhanVien> nhanViens;

    Button btn_test;

    TextView txtNgayFrom, txtNgayTo;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateFrom;
    DatePickerDialog.OnDateSetListener dateTo;
    String myFormat = "yyyy-MM-dd"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    
    TextView txtLoc;
    

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nhaphangs, container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        lvPhieuNhap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= new Intent(view.getContext(),ChiTietPhieuNhapActivity.class);
                intent.putExtra("PHIEUNHAP",phieuNhaps.get(i));
                startActivity(intent);
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NhapHangMoiActivity.class);
                startActivityForResult(intent,1);
            }
        });
        spinnerTraCuu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    spinnerNhanVien.setVisibility(View.VISIBLE);
                    llNgay.setVisibility(View.GONE);
                    getAllPhieuNhap();
                }
                if (i==1){
                    spinnerNhanVien.setVisibility(View.GONE);
                    llNgay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ThongKeTheoNHActivity.class);
                startActivity(intent);
            }
        });

        txtNgayFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), dateFrom, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        txtNgayTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), dateTo, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        txtLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyLoc();
            }
        });

    }

    private void xuLyLoc() {
        if (spinnerTraCuu.getSelectedItemPosition()==0){
            xuLyLocTheoNhanVien();
        }
        else if(spinnerTraCuu.getSelectedItemPosition()==1){
            xyLyLocTheoNgay();
        }
    }

    private void xyLyLocTheoNgay() {
       // getAllPhieuNhap();
        try {
            Date datefrom=sdf.parse(txtNgayFrom.getText().toString());
            Date dateto=sdf.parse(txtNgayTo.getText().toString());
            ArrayList<PhieuNhap> phieuNhapsTheoNgay= new ArrayList<>();
            for (PhieuNhap nhap : phieuNhaps){
                Date dateNhap=sdf.parse(nhap.getNgayNhap());
                if (dateNhap.after(datefrom)==true && dateNhap.before(dateto)==true){
                    phieuNhapsTheoNgay.add(nhap);
                }
            }
            phieuNhaps.clear();
            phieuNhapAdapter.clear();
            phieuNhaps.addAll(phieuNhapsTheoNgay);
            phieuNhapAdapter.notifyDataSetChanged();
            if (phieuNhapsTheoNgay.size()==0){
                Toast.makeText(view.getContext(),"Không có phiếu nhập nào",Toast.LENGTH_LONG).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void xuLyLocTheoNhanVien() {
        NhanVien nhanVien= (NhanVien) spinnerNhanVien.getSelectedItem();
        ApiService.getInstance().layPhieuNhapTheoMaNV(nhanVien.getMaNV(), new Callback<List<PhieuNhap>>() {
            @Override
            public void onResponse(Call<List<PhieuNhap>> call, Response<List<PhieuNhap>> response) {
                if (response.isSuccessful()){
                    ArrayList<PhieuNhap>phieuNhapArrayList= (ArrayList<PhieuNhap>) response.body();
                    phieuNhaps.clear();
                    phieuNhaps.addAll(phieuNhapArrayList);
                    phieuNhapAdapter.notifyDataSetChanged();

                    if (phieuNhapArrayList.size()==0){
                        Toast.makeText(view.getContext(),"Không có phiếu nhập nào",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PhieuNhap>> call, Throwable t) {

            }
        });
    }

    private void addControls() {
        lvPhieuNhap=view.findViewById(R.id.lvPhieuNhap);
        phieuNhaps= new ArrayList<>();
        phieuNhapAdapter= new PhieuNhapAdapter(view.getContext(),R.layout.item_phieu_nhap,phieuNhaps);
        lvPhieuNhap.setAdapter(phieuNhapAdapter);
        getAllPhieuNhap();
        btn_test = view.findViewById(R.id.btn_test);

        imgAdd=view.findViewById(R.id.imgAddProduct);

        spinnerTraCuu=view.findViewById(R.id.spinner_TraCuu);
        chucNangAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item);
        chucNangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayList<String> dsChucNang = new ArrayList<>();
        dsChucNang.add("Nhân viên nhập");
        dsChucNang.add("Ngày nhập");
        chucNangAdapter.addAll(dsChucNang);
        spinnerTraCuu.setAdapter(chucNangAdapter);
        spinnerTraCuu.setSelection(0);

        spinnerNhanVien=view.findViewById(R.id.spinner_NhanVien);
        spinnerNhanVien.setVisibility(View.GONE);
        nhanVienArrayAdapter= new ArrayAdapter<>(view.getContext(),android.R.layout.simple_spinner_item);
        nhanVienArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nhanViens= new ArrayList<>();
        nhanVienArrayAdapter.addAll(nhanViens);
        spinnerNhanVien.setAdapter(nhanVienArrayAdapter);
        getAllNhanVien();

        llNgay= view.findViewById(R.id.llNgay);
        llNgay.setVisibility(View.GONE);

        txtNgayFrom=view.findViewById(R.id.txtNgayFrom);
        txtNgayTo=view.findViewById(R.id.txtNgayTo);

         dateFrom = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelFrom();
            }

        };
        dateTo = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelTo();
            }

        };
        txtLoc=view.findViewById(R.id.txtLoc);
    }

    private void updateLabelTo() {


        txtNgayTo.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelFrom() {


        txtNgayFrom.setText(sdf.format(myCalendar.getTime()));
    }


    private void getAllNhanVien() {
        ApiService.getInstance().getAllNhanVien(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if (response.isSuccessful()){
                    ArrayList<NhanVien> nhanVienArrayList= (ArrayList<NhanVien>) response.body();
                    nhanVienArrayAdapter.addAll(nhanVienArrayList);
                    nhanVienArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

            }
        });
    }

    private void getAllPhieuNhap() {
        ApiService.getInstance().getListPhieuNhap(new Callback<List<PhieuNhap>>() {
            @Override
            public void onResponse(Call<List<PhieuNhap>> call, Response<List<PhieuNhap>> response) {
                if (response.isSuccessful()){
                    ArrayList<PhieuNhap> phieuNhapArrayList= (ArrayList<PhieuNhap>) response.body();
                    phieuNhaps.clear();
                    phieuNhaps.addAll(phieuNhapArrayList);
                    phieuNhapAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<PhieuNhap>> call, Throwable t) {

            }
        });
    }

}
