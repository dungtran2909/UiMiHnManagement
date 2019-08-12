package com.example.uimihnmanagement;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.SanPham;
import com.example.network.ApiService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD = 123;
    Intent intent;
    SanPham sanPham;
    ImageView iv_back;
    EditText et_Ma, et_Ten, et_Gia, et_SoLuong, et_mota;
    Button bt_Sua, bt_Xoa, bt_luu;
    TextView txtTen;
    private Dialog dialog;
    ImageView imgSanPham;
    TextView txtTenSanPham;
    String urlImage = "";
    int REQUEST_CODE_IMAGE = 1;
    int REQUEST_CODE_IMAGE_STORAGE = 2;
    Bitmap bitmapCamera;
    ProgressDialog progressDialog;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://uimihnmanagement.appspot.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        addcontrols();
        addEvents();
    }

    private void addEvents() {
        bt_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLySua();
            }
        });
        bt_Xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyXoa();
            }
        });
        bt_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyLuu();
            }
        });
        imgSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyDoi();
            }
        });
    }
    private void xuLyDoi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietSanPhamActivity.this);
        builder.setTitle("Ảnh từ");
        builder.setNegativeButton("Mở máy ảnh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        }).setPositiveButton("Bộ sưu tập", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQUEST_CODE_IMAGE_STORAGE);
            }
        }).show();
    }

    private void xuLyLuu() {
        if (bitmapCamera!=null){
            xuLyUpload();
        }
        ApiService.getInstance().suaTenSanPham(sanPham.getMaSP(), et_Ten.getText().toString(), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean kq=response.body();
                    if (kq==true){
                        Toast.makeText(ChiTietSanPhamActivity.this,"Sửa thành công",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(ChiTietSanPhamActivity.this,"Sửa thất bại",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
        ApiService.getInstance().suaDonGiaSanPham(sanPham.getMaSP(), Integer.parseInt(et_Gia.getText().toString()), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean kq=response.body();
                    if (kq==true){
                        Toast.makeText(ChiTietSanPhamActivity.this,"Sửa thành công",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else{
                        Toast.makeText(ChiTietSanPhamActivity.this,"Sửa thất bại",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
        ApiService.getInstance().suaMoTaSanPham(sanPham.getMaSP(), et_mota.getText().toString(), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean kq=response.body();
                    if (kq==true){
                        Toast.makeText(ChiTietSanPhamActivity.this,"Sửa thành công",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(ChiTietSanPhamActivity.this,"Sửa thất bại",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent returnIntent = new Intent();
                returnIntent.putExtra("result",1);
                setResult(Activity.RESULT_OK,returnIntent);*/
                finish();
            }
        });
    }

    private void xuLyXoa() {
        dialog = new Dialog(ChiTietSanPhamActivity.this);
        dialog.setTitle("Xác nhận xóa");
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.show();
    }

    private void xuLySua() {
        et_Ten.setEnabled(true);
        et_Gia.setEnabled(true);
        et_SoLuong.setEnabled(true);
        et_mota.setEnabled(true);

    }
    private void addcontrols() {
        intent=getIntent();
        sanPham= (SanPham) intent.getSerializableExtra("SANPHAM");

        iv_back=findViewById(R.id.iv_backChiTiet);

        et_Ma=findViewById(R.id.edtMaSpChiTiet);

        txtTen=findViewById(R.id.txtTen);
        et_Ten=findViewById(R.id.edtTenSpChiTiet);
        et_Gia=findViewById(R.id.edtDonGiaSpChiTiet);
        et_SoLuong=findViewById(R.id.edtSoLuongSpChiTiet);
        et_mota=findViewById(R.id.et_MoTa);



        bt_luu=findViewById(R.id.btnLuuChiTiet);
        bt_Sua=findViewById(R.id.btnSuaChiTiet);
        bt_Xoa=findViewById(R.id.btnXoaChiTiet);

        et_Ma.setText(sanPham.getMaSP()+"");
        txtTen.setText(sanPham.getTenSP());
        et_Ten.setText(sanPham.getTenSP());
        et_Gia.setText(sanPham.getDonGia()+"");
        et_SoLuong.setText(sanPham.getSoLuongTon()+"");
        et_mota.setText(sanPham.getMoTa());


        et_Ma.setEnabled(false);
        et_Ten.setEnabled(false);
        et_Gia.setEnabled(false);
        et_SoLuong.setEnabled(false);

        et_mota.setEnabled(false);

        imgSanPham=findViewById(R.id.imgSanPham);
       /* ApiService.getInstance().suaHinhSanPham(sanPham.getMaSP(), "https://firebasestorage.googleapis.com/v0/b/uimihnmanagement.appspot.com/o/download.jpg?alt=media&token=62202454-39d4-4e5d-91c1-76eee9dcd9ff", new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
        ApiService.getInstance().suaMoTaSanPham(sanPham.getMaSP(), "Mô tả ở đây", new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });*/
        Picasso.with(ChiTietSanPhamActivity.this).load(sanPham.getHinhSP()).into(imgSanPham);
    }


    public void XacNhanXoa(View view) {
        ApiService.getInstance().xoaSanPham(sanPham.getMaSP(), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean kq=response.body();
                    if (kq==true){
                        Toast.makeText(ChiTietSanPhamActivity.this,"Xóa thành công",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(ChiTietSanPhamActivity.this,"Sản phẩm còn tồn, Không thể xóa",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public void XacNhanHuy(View view) {
        dialog.dismiss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            bitmapCamera = (Bitmap) data.getExtras().get("data");

        }
        else if (requestCode == REQUEST_CODE_IMAGE_STORAGE && resultCode == RESULT_OK && data != null) {
            Uri uri=data.getData();
            String path=getRealPathFromURI(uri);
            bitmapCamera=getThumbnail(path);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public Bitmap getThumbnail(String pathHinh)
    {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathHinh, bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
            return null;
        int originalSize = (bounds.outHeight > bounds.outWidth) ?
                bounds.outHeight
                : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / 500;
        return BitmapFactory.decodeFile(pathHinh, opts);
    }
    private void xuLyUpload() {
        progressDialog= new ProgressDialog(ChiTietSanPhamActivity.this);
        progressDialog.setTitle("Đang xử lý");
        progressDialog.setMessage("Vui lòng chờ...");
        progressDialog.show();
        String child = sanPham.getMaSP()+sanPham.getTenSP();
        final StorageReference mountainsRef = storageRef.child(child);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapCamera.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Toast.makeText(SanPhamNangCaoActivity.this, "Thất bại", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(SanPhamNangCaoActivity.this, "Thành công", Toast.LENGTH_LONG).show();
            }
        });
        final StorageReference ref = storageRef.child(child);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    final Uri downloadUri = task.getResult();
                    urlImage=downloadUri.toString();
                    Picasso.with(ChiTietSanPhamActivity.this).load(urlImage).into(imgSanPham);
                    ApiService.getInstance().suaHinhSanPham(sanPham.getMaSP(), urlImage, new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()){
                                boolean kq=response.body();
                                if (kq==true){
                                    Toast.makeText(ChiTietSanPhamActivity.this,"Lưu ảnh thành công",Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(ChiTietSanPhamActivity.this,"Lưu ảnh thất bại",Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });

                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}
