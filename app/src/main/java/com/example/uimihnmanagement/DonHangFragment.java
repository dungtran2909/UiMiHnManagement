package com.example.uimihnmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.adapter.DonHangAdapter;
import com.example.adapter.SanPhamAdapter;
import com.example.model.ChiTietPhieuXuat;
import com.example.model.DonHang;
import com.example.model.ItemNhap;
import com.example.model.PhieuXuat;
import com.example.model.SanPham;
import com.example.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonHangFragment extends Fragment {
    static View view;
    ListView lvDonHang;
    DonHangAdapter donHangAdapter;
    ArrayList<DonHang> donHangs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_don_hang, container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        lvDonHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= new Intent(view.getContext(),ChiTietDonHangActivity.class);
                intent.putExtra("DONHANG",donHangs.get(i));
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        lvDonHang=view.findViewById(R.id.lvDonHang);
        donHangs= new ArrayList<>();
        donHangAdapter=new DonHangAdapter(getActivity(),R.layout.item_don_hang,donHangs);
        lvDonHang.setAdapter(donHangAdapter);
        getAllDonHang();
    }

    private void getAllDonHang() {
        ApiService.getInstance().getAllDonHang(new Callback<List<DonHang>>() {
            @Override
            public void onResponse(Call<List<DonHang>> call, Response<List<DonHang>> response) {
                if (response.isSuccessful()){
                    ArrayList<DonHang> donHangArrayList= (ArrayList<DonHang>) response.body();
                    donHangs.addAll(donHangArrayList);
                    donHangAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<DonHang>> call, Throwable t) {

            }
        });
    }
}
