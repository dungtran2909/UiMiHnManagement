package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.model.ItemNhap;
import com.example.model.PhieuNhap;
import com.example.uimihnmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class PhieuNhapAdapter extends ArrayAdapter<PhieuNhap> {
    Activity context = null;
    List<PhieuNhap> objects;
    int resource;

    public PhieuNhapAdapter(Context context, int resource, ArrayList<PhieuNhap> objects) {
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
            viewHolder.txtMaPN=convertView.findViewById(R.id.txtMaPhieuNhap);
            viewHolder.txtMaNV=convertView.findViewById(R.id.txtMaNhanVien);
            viewHolder.txtNgayNhap=convertView.findViewById(R.id.txtNgayNhap);
            viewHolder.position=position;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        PhieuNhap phieuNhap = objects.get(position);


       viewHolder.txtMaPN.setText(phieuNhap.getMaPhieuNhap()+"");
       viewHolder.txtMaNV.setText(phieuNhap.getMaNV()+"");
       String [] ngay=phieuNhap.getNgayNhap().split("T");
       viewHolder.txtNgayNhap.setText(ngay[0]);

        return convertView;
    }
    public static class ViewHolder{
        TextView txtMaPN;
        TextView txtMaNV;
        TextView txtNgayNhap;
        int position;
    }
}
