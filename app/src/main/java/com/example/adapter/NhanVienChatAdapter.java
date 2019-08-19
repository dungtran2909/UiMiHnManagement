package com.example.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.model.NhanVien;
import com.example.uimihnmanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NhanVienChatAdapter extends ArrayAdapter<NhanVien> {
    Activity context=null;
    List<NhanVien> objects;
    int resource;
    public NhanVienChatAdapter(Activity context, int resource, ArrayList<NhanVien> objects){
        super(context,resource,objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);
        TextView txtTen=row.findViewById(R.id.txtTenNhanVien);
        final TextView txtTrangThai=row.findViewById(R.id.txtTrangThai);

        NhanVien nhanVien=objects.get(position);
        txtTen.setText(nhanVien.getTenNhanVien());
        FirebaseDatabase.getInstance().getReference().child("status").child(nhanVien.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int status= dataSnapshot.getValue(Integer.class);
                if (status==0){
                    txtTrangThai.setText("Offline");
                    txtTrangThai.setTextColor(Color.RED);
                }
                else if (status==1){
                    txtTrangThai.setText("Online");
                    txtTrangThai.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return row;
    }
}
