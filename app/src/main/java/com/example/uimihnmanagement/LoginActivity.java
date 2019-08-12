package com.example.uimihnmanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.NhanVien;
import com.example.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edtUserName, edtPassword;
    Button btnLogin;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;
    CheckBox chkRemember;
    SharedPreferences sharedPreferences;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    public static final String REMEMBER = "remember";

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
        addEvents();
        loadData();
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkRemember.isChecked())
                    //lưu lại thông tin đăng nhập
                    saveData(edtUserName.getText().toString(),edtPassword.getText().toString());
                else
                    clearData();
               checkUsername(edtUserName.getText().toString());
            }
        });
    }

    private void checkUsername(String username) {
        ApiService.getInstance().getNhanVienTheoUserName(username, new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if (response.isSuccessful()){
                    ArrayList<NhanVien> nhanViens= (ArrayList<NhanVien>) response.body();
                    if (nhanViens.size()>0){
                        if ((nhanViens.get(0)).getPassword().equals(edtPassword.getText().toString())){
                            Toast.makeText(LoginActivity.this,"Đăng nhập thành công", Toast.LENGTH_LONG).show();
                            Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("NHANVIEN",nhanViens.get(0));
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(LoginActivity.this,"Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(LoginActivity.this,"Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

            }
        });
    }

    private void addControls() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        edtUserName=findViewById(R.id.edtUserName);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        chkRemember=findViewById(R.id.chkRemember);

    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void saveData(String username, String Pass) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.putBoolean(REMEMBER,chkRemember.isChecked());
        editor.commit();
    }
    private void loadData() {
        if(sharedPreferences.getBoolean(REMEMBER,false)) {
            edtUserName.setText(sharedPreferences.getString(USERNAME, ""));
            edtPassword.setText(sharedPreferences.getString(PASS, ""));
            chkRemember.setChecked(true);
        }
        else
            chkRemember.setChecked(false);
    }
}
