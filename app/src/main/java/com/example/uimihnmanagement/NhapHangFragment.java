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

public class NhapHangFragment extends Fragment {
    private static final int REQUEST_CODE_ADD = 123;
    static View view;

    ListView lvSanPham;
    ArrayList<ItemNhap> arritemNhap;
    SanPhamAdapter sanPhamAdapter;

    FloatingActionButton fabAdd;
    Spinner spinnerTraCuu;
    ArrayAdapter chucNangAdapter;
    EditText edtInput;
    Spinner spinnerDanhMuc;
    ArrayAdapter<DanhMuc> adapterDanhMuc;
    ImageView imgTim;
    ImageView imgAddProduct;

    ArrayList<ItemNhap> arrItemNhapStatic;
    ArrayList<SanPham> arrSanPhamTheoDanhMuc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nhaphang, container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
//        fabAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), ThemSanPhamActivity.class);
//                startActivity(intent);
//            }
//        });
        spinnerTraCuu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    spinnerDanhMuc.setEnabled(false);
                    edtInput.setEnabled(true);
                } else if (i == 1) {
                    spinnerDanhMuc.setEnabled(true);
                    edtInput.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerTraCuu.getSelectedItemPosition()==1){
                    DanhMuc danhMuc= (DanhMuc) spinnerDanhMuc.getSelectedItem();
                    if (danhMuc.getMaDanhMuc()==0){
                        sanPhamAdapter.clear();
                        getDataSPNhap();
                    }
                   else {
                        getListSanPhamTheoDanhMuc((DanhMuc) spinnerDanhMuc.getSelectedItem());
                    }
                }
                if (spinnerTraCuu.getSelectedItemPosition() == 0) {
                    if (edtInput.getText().toString().equals("")){
                        Toast.makeText(view.getContext(),"Vui lòng nhập tên sản phẩm",Toast.LENGTH_LONG).show();
                    }
                    else {
                        getSanPhamTheoTen(edtInput.getText().toString());
                    }
                }
            }
        });
        lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemNhap itemNhapSanPham=sanPhamAdapter.getItem(i);
                Intent intent= new Intent(view.getContext(),ChiTietSanPhamActivity.class);
                intent.putExtra("SANPHAM",itemNhapSanPham.getSanPham());
                startActivityForResult(intent,1);
            }
        });
        imgAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NhapHangMoiActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }


    private void getSanPhamTheoTen(String tenSP) {
        ApiService.getInstance().getSanPhamTheoTen(tenSP, new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                if (response.isSuccessful()) {
                    ArrayList<SanPham> arrayList=new ArrayList();
                    arrayList.add(response.body());
                    new getNgayNhapSanPhamTask().execute(arrayList);

                }
            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable t) {

            }
        });
    }

    private void getListSanPhamTheoDanhMuc(final DanhMuc dm) {
        int i=arritemNhap.size();
        System.out.println("==============="+i);

        ApiService.getInstance().getListSanPhamTheoDanhMuc(dm.getMaDanhMuc(), new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                if (response.isSuccessful()) {
                    ArrayList<SanPham>sanphamArrayList= (ArrayList<SanPham>) response.body();
                    new getNgayNhapSanPhamTask().execute(sanphamArrayList);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }
    class getNgayNhapSanPhamTask extends AsyncTask<ArrayList<SanPham>,Void,ArrayList<ItemNhap>>{
        @Override
        protected void onPostExecute(ArrayList<ItemNhap> itemNhaps) {
            super.onPostExecute(itemNhaps);
            if (itemNhaps.size()>0){
                sanPhamAdapter.clear();
                sanPhamAdapter.addAll(itemNhaps);
                sanPhamAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected ArrayList<ItemNhap> doInBackground(ArrayList<SanPham>... arrayLists) {
            ArrayList<ItemNhap>  nhaps= new ArrayList<>();
            for (SanPham sanPham : arrayLists[0]){
                for (ItemNhap itemNhap : arrItemNhapStatic){
                    if (itemNhap.getSanPham().getMaSP()==sanPham.getMaSP()){
                        nhaps.add(new ItemNhap(sanPham,itemNhap.getNgayNhap()));
                    }
                }
            }
            return nhaps;
        }
    }

    private void addControls() {
        //fabAdd = view.findViewById(R.id.fabAdd);
        imgAddProduct = view.findViewById(R.id.imgAddProduct);
        lvSanPham= view.findViewById(R.id.lvSanPham);
        arrSanPhamTheoDanhMuc= new ArrayList<>();
        arrItemNhapStatic= new ArrayList<>();

        arritemNhap = new ArrayList<>();

        sanPhamAdapter= new SanPhamAdapter(view.getContext(),R.layout.item_sanphamnhap,arritemNhap);
        lvSanPham.setAdapter(sanPhamAdapter);
        getDataSPNhap();

        spinnerTraCuu=view.findViewById(R.id.spinner_TraCuu);
        chucNangAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item);
        chucNangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayList<String> dsChucNang = new ArrayList<>();
        dsChucNang.add("Tên");
        dsChucNang.add("Loại");
        chucNangAdapter.addAll(dsChucNang);
        spinnerTraCuu.setAdapter(chucNangAdapter);
        spinnerTraCuu.setSelection(0);

        edtInput=view.findViewById(R.id.edtTim);

        spinnerDanhMuc=view.findViewById(R.id.spinner_ChungLoai);
        adapterDanhMuc = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item);
        adapterDanhMuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getListDanhMuc();


        imgTim=view.findViewById(R.id.imgSeach);
    }
    private void getListDanhMuc(){
        ApiService.getInstance().getDanhMuc(new Callback<List<DanhMuc>>() {
            @Override
            public void onResponse(Call<List<DanhMuc>> call, Response<List<DanhMuc>> response) {
                if (response.isSuccessful()) {
                  ArrayList<DanhMuc> arrayList= (ArrayList<DanhMuc>) response.body();
                  adapterDanhMuc.add(new DanhMuc(0,"ALL"));
                  adapterDanhMuc.addAll(arrayList);

                  spinnerDanhMuc.setAdapter(adapterDanhMuc);
                }
            }

            @Override
            public void onFailure(Call<List<DanhMuc>> call, Throwable t) {

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
                        getDataSanPham(ctpn, pn);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietPhieuNhap>> call, Throwable t) {

            }
        });
    }

    private void getDataSanPham(final ChiTietPhieuNhap ctpn, final PhieuNhap pn) {
        ApiService.getInstance().getSanPham(ctpn.getMaSP(), new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                if (response.isSuccessful()) {
                    SanPham sp = response.body();


                    String[] ngays = pn.getNgayNhap().split("T");
                    arritemNhap.add(new ItemNhap(sp, ngays[0]));
                    arrItemNhapStatic.add(new ItemNhap(sp, ngays[0]));
                    sanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable t) {

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                refeshData();
            }
        }
        if (requestCode == REQUEST_CODE_ADD) {
            if (resultCode == Activity.RESULT_OK) {
                refeshData();
            }
        }
    }
    private void refeshData() {
        sanPhamAdapter.clear();
        getDataSPNhap();
    }
}
