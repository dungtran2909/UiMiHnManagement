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
import com.example.model.PhieuXuat;
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
    TextView txt_totalALlX,txt_totalDTX, txt_totalTVX, txt_totalLTX, txt_totalMTBX, txt_totalDHX, txt_totalPKX;

    ArrayList<SanPham> sanPhamTong;
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
        sanPhamTong= new ArrayList<>();
        txt_totalALlN = view.findViewById(R.id.txt_totalALlN);
        txt_totalDTN = view.findViewById(R.id.txt_totalDTN);
        txt_totalLTN = view.findViewById(R.id.txt_totalLTN);
        txt_totalMTBN = view.findViewById(R.id.txt_totalMTBN);
        txt_totalDHN = view.findViewById(R.id.txt_totalDHN);
        txt_totalPKN = view.findViewById(R.id.txt_totalPKN);
        txt_totalTVN=view.findViewById(R.id.txt_totalTVN);

        txt_totalALlX = view.findViewById(R.id.txt_totalALlX);
        txt_totalDTX = view.findViewById(R.id.txt_totalDTX);
        txt_totalLTX = view.findViewById(R.id.txt_totalLTX);
        txt_totalMTBX = view.findViewById(R.id.txt_totalMTBX);
        txt_totalDHX = view.findViewById(R.id.txt_totalDHX);
        txt_totalPKX = view.findViewById(R.id.txt_totalPKX);
        txt_totalTVX=view.findViewById(R.id.txt_totalTVX);


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
                        sanPham.setMaDanhMuc(danhMuc.getMaDanhMuc());
                        soLuongTon+=sanPham.getSoLuongTon();

                    }
                    updateSoLuong(danhMuc,soLuongTon);
                    sanPhamTong.addAll(sanPhams);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });

    }

    private void getDataXuat(final ArrayList<SanPham> sanPhams) {
        final int[] xuatPhone = {0};
        final int[] xuatTiVi = {0};
        final int[] xuatMayTinh = {0};
        final int[] xuatMayTinhBang = {0};
        final int[] xuatDongHo = {0};
        final int[] xuatPhuKien = {0};

        ApiService.getInstance().getAllCTPhieuXuat(new Callback<List<ChiTietPhieuXuat>>() {
            @Override
            public void onResponse(Call<List<ChiTietPhieuXuat>> call, Response<List<ChiTietPhieuXuat>> response) {
                if (response.isSuccessful()){
                    ArrayList<ChiTietPhieuXuat> chiTietPhieuXuats= (ArrayList<ChiTietPhieuXuat>) response.body();
                    for (ChiTietPhieuXuat chiTietPhieuXuat : chiTietPhieuXuats){
                        for (SanPham sanPham : sanPhams){
                            if (chiTietPhieuXuat.getMaSP()==sanPham.getMaSP()){
                                if (sanPham.getMaDanhMuc()==1){
                                    xuatPhone[0] +=chiTietPhieuXuat.getSoLuong();
                                }
                                if (sanPham.getMaDanhMuc()==2){
                                   xuatTiVi[0] +=chiTietPhieuXuat.getSoLuong();
                                }
                                if (sanPham.getMaDanhMuc()==3){
                                    xuatMayTinh[0] +=chiTietPhieuXuat.getSoLuong();
                                }
                                if (sanPham.getMaDanhMuc()==4){
                                    xuatMayTinhBang[0] +=chiTietPhieuXuat.getSoLuong();
                                }
                                if (sanPham.getMaDanhMuc()==5){
                                    xuatDongHo[0] +=chiTietPhieuXuat.getSoLuong();
                                }
                                if (sanPham.getMaDanhMuc()==6){
                                    xuatPhuKien[0] +=chiTietPhieuXuat.getSoLuong();
                                }
                            }
                        }
                        txt_totalALlX.setText( xuatPhone[0]+xuatTiVi[0]+xuatMayTinh[0]+xuatMayTinhBang[0]+xuatDongHo[0]+xuatPhuKien[0]+"");
                        txt_totalDTX.setText(xuatPhone[0]+"");
                        txt_totalTVX.setText(xuatTiVi[0]+"");
                        txt_totalLTX.setText(xuatMayTinh[0]+"");
                        txt_totalMTBX.setText(xuatMayTinhBang[0]+"");
                        txt_totalDHX.setText(xuatDongHo[0]+"");
                        txt_totalPKX.setText(xuatPhuKien[0]+"");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietPhieuXuat>> call, Throwable t) {

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
            getDataXuat(sanPhamTong);
        }
    }
}
