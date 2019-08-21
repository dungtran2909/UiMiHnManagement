package com.example.uimihnmanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.adapter.ChamSocKhachHangAdapter;
import com.example.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChamSocKhachHangActivity extends AppCompatActivity {
    ListView lvKhachHangChat;
    ArrayList<User> dsKhachHang;
    ChamSocKhachHangAdapter khachHangChatAdapter;
    String child="ChamSocKhachHang";

    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cham_soc_khach_hang);
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvKhachHangChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= new Intent(ChamSocKhachHangActivity.this,ChamSocKhachHangChatActivity.class);
                intent.putExtra("KHACHHANG",dsKhachHang.get(i));
                startActivity(intent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addControls() {
        imgBack=findViewById(R.id.iv_backNhanVien);
        lvKhachHangChat=findViewById(R.id.lvKhachHangChat);
        dsKhachHang= new ArrayList<>();
        khachHangChatAdapter= new ChamSocKhachHangAdapter(ChamSocKhachHangActivity.this,R.layout.item_row_khach_hang,dsKhachHang);
        lvKhachHangChat.setAdapter(khachHangChatAdapter);

        getDanhSachKhachHang();
    }

    private void getDanhSachKhachHang() {
        FirebaseDatabase.getInstance().getReference().child(child).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user =dataSnapshot.getValue(User.class);
                dsKhachHang.add(user);
                khachHangChatAdapter.notifyDataSetChanged();
                khachHangChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
