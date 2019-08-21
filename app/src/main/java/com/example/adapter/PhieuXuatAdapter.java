package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.model.PhieuNhap;
import com.example.model.PhieuXuat;
import com.example.uimihnmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class PhieuXuatAdapter extends ArrayAdapter<PhieuXuat> {
    Activity context = null;
    List<PhieuXuat> objects;
    int resource;

    public PhieuXuatAdapter(Context context, int resource, ArrayList<PhieuXuat> objects) {
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

       PhieuXuat phieuXuat = objects.get(position);


       viewHolder.txtMaPN.setText(phieuXuat.getMaPhieuXuat()+"");
       viewHolder.txtMaNV.setText(phieuXuat.getMaNV()+"");
       String [] ngay=phieuXuat.getNgayXuat().split("T");
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
