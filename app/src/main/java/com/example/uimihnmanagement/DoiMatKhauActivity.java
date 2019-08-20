package com.example.uimihnmanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.model.NhanVien;
import com.example.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DoiMatKhauActivity extends AppCompatActivity {
    EditText edtOldPassword, edtNewPassword, getEdtNewPasswordRepeat;
    Button btnChangePass;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = edtOldPassword.getText().toString();
                String s2 = MainActivity.nhanVienLogin.getPassword().toString();
                if (s1.equals(s2) == true) {
                    String s3 = edtNewPassword.getText().toString();
                    String s4 = getEdtNewPasswordRepeat.getText().toString();
                    if (s3.equals(s4) == true) {
                        xuLyDoiMatKhau();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DoiMatKhauActivity.this);
                        alertDialog.setTitle("Lỗi");
                        alertDialog.setMessage("Mật khẩu mới không trùng khớp");
                        alertDialog.setIcon(R.drawable.ic_error);
                        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                    }
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DoiMatKhauActivity.this);
                    alertDialog.setTitle("Lỗi");
                    alertDialog.setMessage("Sai mật khẩu");
                    alertDialog.setIcon(R.drawable.ic_error);
                    alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void xuLyDoiMatKhau() {
        ApiService.getInstance().doiMatKhauNhanVien(MainActivity.nhanVienLogin.getMaNV(), edtNewPassword.getText().toString(), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean kq=response.body();
                    if (kq==true){
                        Toast.makeText(DoiMatKhauActivity.this,"Đổi mật thành công, vui lòng đăng nhập lại",Toast.LENGTH_LONG).show();
                        Intent intent= new Intent(DoiMatKhauActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(DoiMatKhauActivity.this,"Lưu thất bại, vui lòng kiểm tra lại",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
    private void addControls() {
        edtOldPassword = findViewById(R.id.et_old_password);
        edtNewPassword = findViewById(R.id.et_new_password);
        getEdtNewPasswordRepeat = findViewById(R.id.et_new_password_repeat);
        btnChangePass = findViewById(R.id.bt_change_pass);
        iv_back = findViewById(R.id.iv_back);
    }
}
