package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.model.ItemNhap;
import com.example.uimihnmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends ArrayAdapter<ItemNhap> {
    Activity context = null;
    List<ItemNhap> objects;
    int resource;

    public SanPhamAdapter(Context context, int resource, ArrayList<ItemNhap> objects) {
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
            viewHolder.txtNgayNhap=convertView.findViewById(R.id.txtNgayNhap);
            viewHolder.position=position;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        ItemNhap itemNhap = objects.get(position);


        viewHolder.txtTen.setText(itemNhap.getSanPham().getTenSP());
        viewHolder.txtSoLuong.setText(itemNhap.getSanPham().getSoLuongTon() + "");
        viewHolder.txtNgayNhap.setText(itemNhap.getNgayNhap());

        return convertView;
    }
    public static class ViewHolder{
        TextView txtTen;
        TextView txtSoLuong;
        TextView txtNgayNhap;
        int position;
    }
}
