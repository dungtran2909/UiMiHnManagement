package com.example.uimihnmanagement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.adapter.SanPhamAdapter;
import com.example.model.ChiTietPhieuNhap;
import com.example.model.ChiTietPhieuXuat;
import com.example.model.ItemNhap;
import com.example.model.PhieuNhap;
import com.example.model.PhieuXuat;
import com.example.model.SanPham;
import com.example.network.ApiService;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XuatHangFragment extends Fragment {
    static View view;

    ListView lvSanPham;
    ArrayList<ItemNhap> arritemXuat;
    SanPhamAdapter sanPhamAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_xuathang, container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
    }

    private void addControls() {
        lvSanPham = view.findViewById(R.id.lvSanPham);
        arritemXuat = new ArrayList<>();
        sanPhamAdapter= new SanPhamAdapter(view.getContext(),R.layout.item_sanphamnhap,arritemXuat);
        lvSanPham.setAdapter(sanPhamAdapter);
        getDataSPXuat();

    }

    private void getDataSPXuat() {
        ApiService.getInstance().getListPhieuXuat(new Callback<List<PhieuXuat>>() {
            @Override
            public void onResponse(Call<List<PhieuXuat>> call, Response<List<PhieuXuat>> response) {
                if (response.isSuccessful()) {
                    ArrayList<PhieuXuat> arrPhieuXuat = (ArrayList<PhieuXuat>) response.body();
                    for (PhieuXuat px : arrPhieuXuat) {
                        getDataDetailPX(px);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PhieuXuat>> call, Throwable t) {

            }
        });


    }

    private void getDataDetailPX(final PhieuXuat px) {
        ApiService.getInstance().getDetailPhieXuat(px.getMaPhieuXuat(), new Callback<List<ChiTietPhieuXuat>>() {
            @Override
            public void onResponse(Call<List<ChiTietPhieuXuat>> call, Response<List<ChiTietPhieuXuat>> response) {
                if (response.isSuccessful()) {
                    List<ChiTietPhieuXuat> arrChiTietPX = response.body();
                    for (ChiTietPhieuXuat ctpx : arrChiTietPX) {
                        getDataSanPham(ctpx, px);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietPhieuXuat>> call, Throwable t) {

            }
        });
    }

    private void getDataSanPham(final ChiTietPhieuXuat ctpx, final PhieuXuat px) {
        ApiService.getInstance().getSanPham(ctpx.getMaSP(), new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                if(response.isSuccessful()){
                    SanPham sp = response.body();

                    String[] ngays = px.getNgayXuat().split("T");
                    arritemXuat.add(new ItemNhap(sp, ngays[0]));
                    sanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable t) {

            }
        });

    }





}
