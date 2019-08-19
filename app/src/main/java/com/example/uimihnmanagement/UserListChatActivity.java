package com.example.uimihnmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.model.NhanVien;
import com.example.network.ApiService;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListChatActivity extends AppCompatActivity {
    ListView lvUserChat;
    ArrayAdapter<NhanVien> nhanVienArrayAdapter;
    ArrayList<NhanVien> nhanViens;
    ImageView imgBack;
    Button btnChatNhom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_chat);
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
        nhanVienArrayAdapter= new ArrayAdapter<>(UserListChatActivity.this,android.R.layout.simple_list_item_1,nhanViens);
        lvUserChat.setAdapter(nhanVienArrayAdapter);
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
                        if (nhanVien.getMaNV()==MainActivity.nhanVienLogin.getMaNV()){
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
}
