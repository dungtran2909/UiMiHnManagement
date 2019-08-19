package com.example.uimihnmanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.adapter.NhanVienChatAdapter;
import com.example.model.NhanVien;
import com.example.network.ApiService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.uimihnmanagement.MainActivity.nhanVienLogin;

public class UserListChatActivity extends AppCompatActivity {
    ListView lvUserChat;
    NhanVienChatAdapter nhanVienArrayAdapter;
    ArrayList<NhanVien> nhanViens;
    ImageView imgBack;
    Button btnChatNhom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_chat);
        FirebaseDatabase.getInstance().getReference().child("status").child(nhanVienLogin.getUsername()).setValue(1);
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvUserChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                NhanVien nhanVienChat=nhanViens.get(i);
                Intent intent= new Intent(UserListChatActivity.this,ChatSingelActivity.class);
                intent.putExtra("NHANVIENCHAT",nhanVienChat);
                startActivity(intent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnChatNhom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(UserListChatActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        });
    }



    private void addControls() {
        imgBack=findViewById(R.id.iv_backNhanVien);
        btnChatNhom=findViewById(R.id.btnChatNhom);
        nhanViens= new ArrayList<>();
        lvUserChat=findViewById(R.id.lvUserChat);
        nhanVienArrayAdapter= new NhanVienChatAdapter(UserListChatActivity.this,R.layout.item_list_chat,nhanViens);
        lvUserChat.setAdapter(nhanVienArrayAdapter);
        FirebaseDatabase.getInstance().getReference().child("status").child("change").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=dataSnapshot.getValue(Integer.class);
                if (i==1){
                    getAllUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getAllUser();
    }

    private void getAllUser() {
        ApiService.getInstance().getAllNhanVien(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if (response.isSuccessful()){
                    ArrayList<NhanVien> nhanVienArrayList= (ArrayList<NhanVien>) response.body();
                    for (int i=0; i<nhanVienArrayList.size();i++){
                        NhanVien nhanVien=nhanVienArrayList.get(i);
                        if (nhanVien.getMaNV()== nhanVienLogin.getMaNV()){
                            nhanVienArrayList.remove(i);
                        }
                    }
                    nhanViens.clear();
                    nhanViens.addAll(nhanVienArrayList);
                    nhanVienArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().child("status").child(nhanVienLogin.getUsername()).setValue(1);
    }
    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child("status").child(nhanVienLogin.getUsername()).setValue(0);
    }
}
