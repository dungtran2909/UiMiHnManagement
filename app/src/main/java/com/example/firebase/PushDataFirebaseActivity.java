package com.example.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.model.NhanVien;
import com.example.network.ApiService;
import com.example.uimihnmanagement.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushDataFirebaseActivity extends AppCompatActivity {
    DatabaseReference mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_data_firebase);
        mData= FirebaseDatabase.getInstance().getReference();
        ApiService.getInstance().getAllNhanVien(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if (response.isSuccessful()){
                    ArrayList<NhanVienFirebase> nhanVienFirebases= new ArrayList<>();
                    ArrayList<NhanVien> nhanViens= (ArrayList<NhanVien>) response.body();
                    for (NhanVien nv : nhanViens){
                        nhanVienFirebases.add(new NhanVienFirebase(nv.getMaNV(),nv.getTenNhanVien(),nv.getEmail(),nv.getUsername(),nv.getPassword(),"https://firebasestorage.googleapis.com/v0/b/uimihnmanagement.appspot.com/o/download.jpg?alt=media&token=62202454-39d4-4e5d-91c1-76eee9dcd9ff"));
                    }
                    for (NhanVienFirebase firebase : nhanVienFirebases){
                        mData.child("status").child(firebase.getUserName()).setValue(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

            }
        });
    }
}
