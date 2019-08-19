package com.example.uimihnmanagement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.model.ChiTietPhieuNhap;
import com.example.model.ChiTietPhieuXuat;
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

public class ThongKeFragment extends Fragment {
    View view;

    TextView txt_totalALlN,txt_totalDTN, txt_totalTVN, txt_totalLTN, txt_totalMTBN, txt_totalDHN, txt_totalPKN;

    ArrayList<SanPham> sanPhams;
    TabHost tabHost;
    int total=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thongke, container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {

    }


    private void addControls() {
        txt_totalALlN = view.findViewById(R.id.txt_totalALlN);
        txt_totalDTN = view.findViewById(R.id.txt_totalDTN);
        txt_totalLTN = view.findViewById(R.id.txt_totalLTN);
        txt_totalMTBN = view.findViewById(R.id.txt_totalMTBN);
        txt_totalDHN = view.findViewById(R.id.txt_totalDHN);
        txt_totalPKN = view.findViewById(R.id.txt_totalPKN);
        txt_totalTVN=view.findViewById(R.id.txt_totalTVN);


        tabHost = view.findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setIndicator("Nhập");
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setIndicator("Xuất");
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);

        getSanPham();
    }

    private void getSanPham() {
        ApiService.getInstance().getDanhMuc(new Callback<List<DanhMuc>>() {
            @Override
            public void onResponse(Call<List<DanhMuc>> call, Response<List<DanhMuc>> response) {
                if (response.isSuccessful()){
                    ArrayList<DanhMuc> danhMucs= (ArrayList<DanhMuc>) response.body();
                    for (DanhMuc danhMuc : danhMucs){
                        getListSanPhamTheoDanhMuc(danhMuc);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DanhMuc>> call, Throwable t) {

            }
        });
    }

    private void getListSanPhamTheoDanhMuc(final DanhMuc danhMuc) {
        ApiService.getInstance().getListSanPhamTheoDanhMuc(danhMuc.getMaDanhMuc(), new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                if (response.isSuccessful()){
                    int soLuongTon=0;
                    ArrayList<SanPham> sanPhams= (ArrayList<SanPham>) response.body();
                    for (SanPham sanPham : sanPhams){
                        soLuongTon+=sanPham.getSoLuongTon();

                    }
                    updateSoLuong(danhMuc,soLuongTon);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });

    }

    private void updateSoLuong(DanhMuc danhMuc, int soLuongTon) {
        total+=soLuongTon;
        txt_totalALlN.setText(total+"");
        if (danhMuc.getMaDanhMuc()==1){
            txt_totalDTN.setText(soLuongTon+"");
        }
        else if (danhMuc.getMaDanhMuc()==2){
            txt_totalTVN.setText(soLuongTon+"");
        }
        else if (danhMuc.getMaDanhMuc()==3){
            txt_totalLTN.setText(soLuongTon+"");
        }
        else if (danhMuc.getMaDanhMuc()==4){
            txt_totalMTBN.setText(soLuongTon+"");
        }
        else if (danhMuc.getMaDanhMuc()==5){
            txt_totalDHN.setText(soLuongTon+"");
        }
        else if (danhMuc.getMaDanhMuc()==6){
            txt_totalPKN.setText(soLuongTon+"");
        }
    }
}
