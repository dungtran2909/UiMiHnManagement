package com.example.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.model.NhanVien;
import com.example.model.User;
import com.example.uimihnmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class ChamSocKhachHangAdapter extends ArrayAdapter<User> {
    Activity context=null;
    List<User> objects;
    int resource;
    public ChamSocKhachHangAdapter(Activity context, int resource, ArrayList<User> objects){
        super(context,resource,objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);
        TextView txtTenKhachHang=row.findViewById(R.id.txtTenKhachHang);
        TextView txtEmail=row.findViewById(R.id.txtEmail);

        User user=objects.get(position);
        txtTenKhachHang.setText(user.getTenKH());
        txtEmail.setText(user.getEmail());

        return row;
    }
}
