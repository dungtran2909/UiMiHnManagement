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
    FloatingActionButton fabBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thongke, container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void getAllCTN(){
        ApiService.getInstance().getAllPN(new Callback<List<ChiTietPhieuNhap>>() {
            @Override
            public void onResponse(Call<List<ChiTietPhieuNhap>> call, Response<List<ChiTietPhieuNhap>> response) {
                if (response.isSuccessful()){
                    ArrayList<ChiTietPhieuNhap> ctpn = (ArrayList<ChiTietPhieuNhap>) response.body();
                    int length = ctpn.size();
                    ChiTietPhieuXuat ctpns;
                    int total = 0;
                    for(int i=0;i<length;i++){
                        int s = ctpn.get(i).getSoLuong();
                        total = total +s;

                    }
                    txt_totalALlN.setText(total+"");
                }
            }
            @Override
            public void onFailure(Call<List<ChiTietPhieuNhap>> call, Throwable t) {

            }

        });
    }

    private void getDataSPNhap() {
        ApiService.getInstance().getListPhieuNhap(new Callback<List<PhieuNhap>>() {
            @Override
            public void onResponse(Call<List<PhieuNhap>> call, Response<List<PhieuNhap>> response) {
                ArrayList<PhieuNhap> arrPhieuNhap = (ArrayList<PhieuNhap>) response.body();
                for (PhieuNhap pn : arrPhieuNhap) {
                    getDataDetailPN(pn);
                }
            }

            @Override
            public void onFailure(Call<List<PhieuNhap>> call, Throwable t) {

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
                        getDataSanPham(ctpn);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietPhieuNhap>> call, Throwable t) {

            }
        });
    }

    private void getDataSanPham(final ChiTietPhieuNhap ctpn) {
        ApiService.getInstance().getSanPham(ctpn.getMaSP(), new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                if (response.isSuccessful()) {
                    SanPham sp = response.body();


                }
            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable t) {

            }
        });
    }





    private void addControls() {
        getAllCTN();
        fabBack = view.findViewById(R.id.fabBack);

        txt_totalALlN = view.findViewById(R.id.txt_totalALlN);
        txt_totalDTN = view.findViewById(R.id.txt_totalDTN);
        txt_totalLTN = view.findViewById(R.id.txt_totalLTN);
        txt_totalMTBN = view.findViewById(R.id.txt_totalMTBN);
        txt_totalDHN = view.findViewById(R.id.txt_totalDHN);
        txt_totalPKN = view.findViewById(R.id.txt_totalPKN);



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
    }
}
