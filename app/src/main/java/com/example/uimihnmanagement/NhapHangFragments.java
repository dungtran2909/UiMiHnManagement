package com.example.uimihnmanagement;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adapter.PhieuNhapAdapter;
import com.example.adapter.SanPhamAdapter;
import com.example.model.ChiTietPhieuNhap;
import com.example.model.DanhMuc;
import com.example.model.ItemNhap;
import com.example.model.PhieuNhap;
import com.example.model.SanPham;
import com.example.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NhapHangFragments extends Fragment {
    private static final int REQUEST_CODE_ADD = 123;
    static View view;
    ListView lvPhieuNhap;
    ArrayList<PhieuNhap> phieuNhaps;
    PhieuNhapAdapter phieuNhapAdapter;


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
    }

    private void addControls() {
        lvPhieuNhap=view.findViewById(R.id.lvPhieuNhap);
        phieuNhaps= new ArrayList<>();
        phieuNhapAdapter= new PhieuNhapAdapter(view.getContext(),R.layout.item_phieu_nhap,phieuNhaps);
        lvPhieuNhap.setAdapter(phieuNhapAdapter);
        getAllPhieuNhap();
    }

    private void getAllPhieuNhap() {
        ApiService.getInstance().getListPhieuNhap(new Callback<List<PhieuNhap>>() {
            @Override
            public void onResponse(Call<List<PhieuNhap>> call, Response<List<PhieuNhap>> response) {
                if (response.isSuccessful()){
                    ArrayList<PhieuNhap> phieuNhapArrayList= (ArrayList<PhieuNhap>) response.body();
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
