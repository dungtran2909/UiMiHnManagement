package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.model.ItemNhap;
import com.example.model.SanPham;
import com.example.uimihnmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class SanPhamNhapMoiAdapter extends ArrayAdapter<SanPham> {
    Activity context = null;
    List<SanPham> objects;
    int resource;

    public SanPhamNhapMoiAdapter(Context context, int resource, ArrayList<SanPham> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.resource = resource;
        this.objects = objects;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LayoutInflater layoutInflater = this.context.getLayoutInflater();
        if(convertView==null){
            convertView=layoutInflater.inflate(this.resource,null);
            viewHolder=new ViewHolder();
            viewHolder.txtTen=convertView.findViewById(R.id.txtTenSanPham);
            viewHolder.txtSoLuong=convertView.findViewById(R.id.txtSoLuong);
            viewHolder.txtDonGia=convertView.findViewById(R.id.txtDonGia);
            viewHolder.position=position;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        SanPham sanPham=objects.get(position);


        viewHolder.txtTen.setText(sanPham.getTenSP());
        viewHolder.txtSoLuong.setText(sanPham.getSoLuongTon()+"");
        viewHolder.txtDonGia.setText(sanPham.getDonGia()+"");

        return convertView;
    }
    public static class ViewHolder{
        TextView txtTen;
        TextView txtSoLuong;
        TextView txtDonGia;
        int position;
    }
}
