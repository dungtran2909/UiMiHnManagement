package com.example.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.model.DonHang;
import com.example.model.NhanVien;
import com.example.model.User;
import com.example.network.ApiService;
import com.example.uimihnmanagement.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonHangAdapter extends ArrayAdapter<DonHang> {
    Activity context=null;
    List<DonHang> objects;
    int resource;
    public DonHangAdapter(Activity context, int resource, ArrayList<DonHang> objects){
        super(context,resource,objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);
        TextView txtMaDonHang=row.findViewById(R.id.txtMaDonHang);
        final TextView txtTenKH=row.findViewById(R.id.txtTenKhachHang);
        TextView txtTrangThai=row.findViewById(R.id.txtTrangThai);

        DonHang donHang=objects.get(position);
        if (donHang.getTrangThai()==0){
            txtTrangThai.setText("Đang chờ duyệt");
            txtTrangThai.setTextColor(Color.RED);
        }
        else if (donHang.getTrangThai()==1){
            txtTrangThai.setText("Đã duyệt");
            txtTrangThai.setTextColor(Color.YELLOW);
        }
        else if (donHang.getTrangThai()==2){
            txtTrangThai.setText("Đang giao");
            txtTrangThai.setTextColor(Color.BLUE);
        }
        else if (donHang.getTrangThai()==3){
            txtTrangThai.setText("Hoàn thành");
            txtTrangThai.setTextColor(Color.GREEN);
        }
        txtMaDonHang.setText(donHang.getMaDonHang()+"");

        ApiService.getInstance().getUserTheoMa(donHang.getMaKH(), new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user=response.body();
                    txtTenKH.setText(user.getTenKH());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        return row;
    }
}
