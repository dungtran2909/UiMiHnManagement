package com.example.uimihnmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.model.ChiTietPhieuXuat;
import com.example.model.DanhMuc;
import com.example.model.NhanHieu;
import com.example.model.SanPham;
import com.example.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongKeTheoNHActivity extends AppCompatActivity {

    TextView txt_totalVSN, txt_totalHPN, txt_totalDLN, txt_totalLGN, txt_totalNKN, txt_totalPNN;
    TextView txt_totalHTN, txt_totalHWN, txt_totalSNN, txt_totalOPN, txt_totalSSN, txt_totalAPN;
    TextView txt_totalALLN;

    TextView txt_totalVSX, txt_totalHPX, txt_totalDLX, txt_totalLGX, txt_totalNKX, txt_totalPNX;
    TextView txt_totalHTX, txt_totalHWX, txt_totalSNX, txt_totalOPX, txt_totalSSX, txt_totalAPX;
    TextView txt_totalALLX;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke_theo_nh);

        addControls();
        addEvents();
    }

    private void addEvents() {
        getSanPhamNH();
    }

    private void addControls() {
        txt_totalVSN = findViewById(R.id.txt_totalVSN);
        txt_totalHPN = findViewById(R.id.txt_totalHPN);
        txt_totalDLN = findViewById(R.id.txt_totalDLN);
        txt_totalLGN = findViewById(R.id.txt_totalLGN);
        txt_totalNKN = findViewById(R.id.txt_totalNKN);
        txt_totalPNN = findViewById(R.id.txt_totalPNN);
        txt_totalHTN = findViewById(R.id.txt_totalHTN);
        txt_totalHWN = findViewById(R.id.txt_totalHWN);
        txt_totalSNN = findViewById(R.id.txt_totalSNN);
        txt_totalOPN = findViewById(R.id.txt_totalOPN);
        txt_totalSSN = findViewById(R.id.txt_totalSSN);
        txt_totalAPN = findViewById(R.id.txt_totalAPN);
        txt_totalALLN = findViewById(R.id.txt_totalALlN);

        txt_totalVSX = findViewById(R.id.txt_totalVSX);
        txt_totalHPX = findViewById(R.id.txt_totalHPX);
        txt_totalDLX = findViewById(R.id.txt_totalDLX);
        txt_totalLGX = findViewById(R.id.txt_totalLGX);
        txt_totalNKX = findViewById(R.id.txt_totalNKX);
        txt_totalPNX = findViewById(R.id.txt_totalPNX);
        txt_totalHTX = findViewById(R.id.txt_totalHTX);
        txt_totalHWX = findViewById(R.id.txt_totalHWX);
        txt_totalSNX = findViewById(R.id.txt_totalSNX);
        txt_totalOPX = findViewById(R.id.txt_totalOPX);
        txt_totalSSX = findViewById(R.id.txt_totalSSX);
        txt_totalAPX = findViewById(R.id.txt_totalAPX);
        txt_totalALLX = findViewById(R.id.txt_totalALlX);
    }



    private void getSanPhamNH() {
        ApiService.getInstance().getNhanHieu(new Callback<List<NhanHieu>>() {
            @Override
            public void onResponse(Call<List<NhanHieu>> call, Response<List<NhanHieu>> response) {
                if (response.isSuccessful()) {
                    ArrayList<NhanHieu> nhanHieus = (ArrayList<NhanHieu>) response.body();
                    for (NhanHieu nhanHieu : nhanHieus) {
                        getListSanPhamTheoNhanHieu(nhanHieu);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NhanHieu>> call, Throwable t) {

            }
        });
    }

    private void getListSanPhamTheoNhanHieu(final NhanHieu nhanHieu) {
        ApiService.getInstance().getListSanPhamTheoNhanHieu(nhanHieu.getMaNhanHieu(), new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                if (response.isSuccessful()) {
                    int soLuongTon = 0;
                    ArrayList<SanPham> sanPhams = (ArrayList<SanPham>) response.body();
                    for (SanPham sanPham : sanPhams) {
                        sanPham.setMaNhanHieu(nhanHieu.getMaNhanHieu());
                        soLuongTon += sanPham.getSoLuongTon();

                    }
                    updateSoLuong(nhanHieu, soLuongTon);
//                    sanPhamTong.addAll(sanPhams);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }

//    private void getDataXuat(final ArrayList<SanPham> sanPhams) {
//        final int[] xuatVsmat = {0};
//        final int[] xuatHP = {0};
//        final int[] xuatDell = {0};
//        final int[] xuatLG = {0};
//        final int[] xuatNOKIA = {0};
//        final int[] xuatPanasonic = {0};
//        final int[] xuatHtc = {0};
//        final int[] xuatHuawei = {0};
//        final int[] xuatSony= {0};
//        final int[] xuatOppo = {0};
//        final int[] xuatSamsung = {0};
//        final int[] xuatApple = {0};
//
//
//        ApiService.getInstance().getAllCTPhieuXuat(new Callback<List<ChiTietPhieuXuat>>() {
//            @Override
//            public void onResponse(Call<List<ChiTietPhieuXuat>> call, Response<List<ChiTietPhieuXuat>> response) {
//                if (response.isSuccessful()){
//                    ArrayList<ChiTietPhieuXuat> chiTietPhieuXuats= (ArrayList<ChiTietPhieuXuat>) response.body();
//                    for (ChiTietPhieuXuat chiTietPhieuXuat : chiTietPhieuXuats){
//                        for (SanPham sanPham : sanPhams){
//                            if (chiTietPhieuXuat.getMaSP()==sanPham.getMaSP()){
//                                if (sanPham.getMaNhanHieu()==1){
//                                    xuatVsmat[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==2){
//                                    xuatHP[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==3){
//                                    xuatDell[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==4){
//                                    xuatLG[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==5){
//                                    xuatNOKIA[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==6){
//                                    xuatPanasonic[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==7){
//                                    xuatHtc[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==8){
//                                    xuatHuawei[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==9){
//                                    xuatSony[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==10){
//                                    xuatOppo[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==11){
//                                    xuatSamsung[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//                                if (sanPham.getMaNhanHieu()==12){
//                                    xuatApple[0] +=chiTietPhieuXuat.getSoLuong();
//                                }
//
//                            }
//                        }
//                        txt_totalALLX.setText( xuatVsmat[0]+xuatHP[0]+xuatDell[0]+xuatLG[0]+xuatNOKIA[0]+xuatPanasonic[0]+xuatHtc[0]+xuatHuawei[0]+xuatSony[0]+xuatOppo[0]+xuatSamsung[0]+xuatApple[0]+"");
//                        txt_totalVSX.setText(xuatVsmat[0]+"");
//                        txt_totalHPX.setText(xuatVsmat[0]+"");
//                        txt_totalDLX.setText(xuatVsmat[0]+"");
//                        txt_totalLGX.setText(xuatVsmat[0]+"");
//                        txt_totalNKX.setText(xuatVsmat[0]+"");
//                        txt_totalPNX.setText(xuatVsmat[0]+"");
//                        txt_totalHTX.setText(xuatVsmat[0]+"");
//                        txt_totalHWX.setText(xuatVsmat[0]+"");
//                        txt_totalSNX.setText(xuatVsmat[0]+"");
//                        txt_totalOPX.setText(xuatVsmat[0]+"");
//                        txt_totalSSX.setText(xuatVsmat[0]+"");
//                        txt_totalAPX.setText(xuatVsmat[0]+"");
//
//
////                        txt_totalDTX.setText(xuatPhone[0]+"");
////                        txt_totalTVX.setText(xuatTiVi[0]+"");
////                        txt_totalLTX.setText(xuatMayTinh[0]+"");
////                        txt_totalMTBX.setText(xuatMayTinhBang[0]+"");
////                        txt_totalDHX.setText(xuatDongHo[0]+"");
////                        txt_totalPKX.setText(xuatPhuKien[0]+"");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ChiTietPhieuXuat>> call, Throwable t) {
//
//            }
//        });
//
//    }


    private void updateSoLuong(NhanHieu nhanHieu, int soLuongTon) {
        total += soLuongTon;
        txt_totalALLN.setText(total + "");
        if (nhanHieu.getMaNhanHieu() == 1) {
            txt_totalVSN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 2) {
            txt_totalHPN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 3) {
            txt_totalDLN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 4) {
            txt_totalLGN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 5) {
            txt_totalNKN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 6) {
            txt_totalPNN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 7) {
            txt_totalHTN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 8) {
            txt_totalHWN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 9) {
            txt_totalSNN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 10) {
            txt_totalOPN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 11) {
            txt_totalSSN.setText(soLuongTon + "");
        } else if (nhanHieu.getMaNhanHieu() == 12) {
            txt_totalAPN.setText(soLuongTon + "");
        }
    }


}
