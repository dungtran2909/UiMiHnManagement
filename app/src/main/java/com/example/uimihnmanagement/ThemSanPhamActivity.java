package com.example.uimihnmanagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.DanhMuc;
import com.example.model.NhanHieu;
import com.example.model.PhieuNhap;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemSanPhamActivity extends AppCompatActivity {
    ImageView imgSanPham, iv_back;
    EditText edtMaNV, edtNgayNhap, edtTenSanPham, edtDonGia, edtSoLuong, edtMoTa;
    Button bt_luu;
    TextView txtTenSanPham;

    Spinner spinner_danhMuc;

    ArrayAdapter<DanhMuc> danhMucAdapter;


    ArrayList<DanhMuc> arrDM;
    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-dd-MM");


    Spinner spinner_NhanHieu;
    ArrayAdapter<NhanHieu> nhanHieuAdapter;
    ArrayList<NhanHieu> arrNH;

    SanPham sp;
    PhieuNhap nhap;
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
        setContentView(R.layout.activity_them_san_pham);

        addControls();
        addEvents();
    }

    private void addEvents() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",100);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        bt_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void xuLyLuu() {
        ApiService.getInstance().LuuPhieuNhap(MainActivity.nhanVienLogin.getMaNV(), edtNgayNhap.getText().toString(), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    layChiTietPhieuNhap();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void layChiTietPhieuNhap() {
        ApiService.getInstance().layPhieuNhapTheoMaNV(MainActivity.nhanVienLogin.getMaNV(), new Callback<List<PhieuNhap>>() {
            @Override
            public void onResponse(Call<List<PhieuNhap>> call, Response<List<PhieuNhap>> response) {
                if (response.isSuccessful()){
                    ArrayList<PhieuNhap> phieuNhaps= (ArrayList<PhieuNhap>) response.body();
                    nhap= new PhieuNhap();
                    for (PhieuNhap phieuNhap : phieuNhaps){
                        if (phieuNhap.getNgayNhap().equals(edtNgayNhap.getText().toString()));
                        {
                            nhap.setMaPhieuNhap(phieuNhap.getMaPhieuNhap());
                        }
                    }
                    luuSanPham();

                }
            }

            @Override
            public void onFailure(Call<List<PhieuNhap>> call, Throwable t) {

            }
        });
    }

    private void luuSanPham() {
        DanhMuc dm= (DanhMuc) spinner_danhMuc.getSelectedItem();
        NhanHieu nhanHieu= (NhanHieu) spinner_NhanHieu.getSelectedItem();
        String hinh="";
        ApiService.getInstance().luuMoiSanPham(edtTenSanPham.getText().toString(), Integer.parseInt(edtDonGia.getText().toString()), dm.getMaDanhMuc(), true, edtMoTa.getText().toString(), hinh, nhanHieu.getMaNhanHieu(), Integer.parseInt(edtSoLuong.getText().toString()), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    getChiTietSanPham(edtTenSanPham.getText().toString());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void getChiTietSanPham(String tenSp) {
        ApiService.getInstance().getSanPhamTheoTen(tenSp, new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                if (response.isSuccessful()){
                    sp=response.body();
                    luuChiTietPhieuNhap(nhap);
                }
            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable t) {

            }
        });
    }

    private void luuChiTietPhieuNhap(PhieuNhap nhap) {
        ApiService.getInstance().LuuCTPhieuNhap(nhap.getMaPhieuNhap(), sp.getMaSP(), Integer.parseInt(edtSoLuong.getText().toString()), Integer.parseInt(edtDonGia.getText().toString()), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    upDateAnh();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void upDateAnh() {
        xuLyUpload(sp);
    }

    private void xuLyDoi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ThemSanPhamActivity.this);
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
    private void addControls() {
        iv_back=findViewById(R.id.iv_backChiTiet);

        edtMaNV=findViewById(R.id.edtMaNhanVienNhap);
        edtNgayNhap=findViewById(R.id.edtNgayNhapHang);
        edtDonGia=findViewById(R.id.edtDonGiaSpChiTiet);

        edtSoLuong=findViewById(R.id.edtSoLuongSpChiTiet);
        edtTenSanPham=findViewById(R.id.edtTenSpNhap);
        edtMoTa=findViewById(R.id.edtMotaSpChiTiet);
        bt_luu=findViewById(R.id.btnLuuChiTiet);

        imgSanPham=findViewById(R.id.imgSanPham);


        arrDM = new ArrayList<>();
        layDanhSachDM();
        spinner_danhMuc=findViewById(R.id.spinner_DanhMuc);
        danhMucAdapter= new ArrayAdapter<>(ThemSanPhamActivity.this,android.R.layout.simple_spinner_item);
        danhMucAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_danhMuc.setAdapter(danhMucAdapter);

        arrNH = new ArrayList<>();
        layDanhSachNH();
        spinner_NhanHieu = findViewById(R.id.spinner_NhanHieu);
        nhanHieuAdapter = new ArrayAdapter<>(ThemSanPhamActivity.this, android.R.layout.simple_spinner_item);
        nhanHieuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_NhanHieu.setAdapter(nhanHieuAdapter);

        edtMaNV.setText(MainActivity.nhanVienLogin.getMaNV()+"-"+MainActivity.nhanVienLogin.getTenNhanVien());
        edtNgayNhap.setText(simpleDateFormat.format(new Date(System.currentTimeMillis())));

        Picasso.with(ThemSanPhamActivity.this).load("https://firebasestorage.googleapis.com/v0/b/uimihnmanagement.appspot.com/o/download.jpg?alt=media&token=62202454-39d4-4e5d-91c1-76eee9dcd9ff").into(imgSanPham);
    }

    private void layDanhSachDM(){
        ApiService.getInstance().getDanhMuc(new Callback<List<DanhMuc>>() {
            @Override
            public void onResponse(Call<List<DanhMuc>> call, Response<List<DanhMuc>> response) {
                Log.d("AAAAA", response.toString());
                if(response.isSuccessful()){
                    List<DanhMuc> list = response.body();

                    danhMucAdapter.addAll(list);
                    danhMucAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<DanhMuc>> call, Throwable t) {
                Log.d("aaaaaa", t.toString());
            }
        });
    }

    private void layDanhSachNH(){
        ApiService.getInstance().getNhanHieu(new Callback<List<NhanHieu>>() {
            @Override
            public void onResponse(Call<List<NhanHieu>> call, Response<List<NhanHieu>> response) {
                if (response.isSuccessful()){
                    List<NhanHieu> list = response.body();
                    nhanHieuAdapter.addAll(list);
                    nhanHieuAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<NhanHieu>> call, Throwable t) {

            }
        });
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
    private void xuLyUpload(final SanPham sanPham) {
        progressDialog = new ProgressDialog(ThemSanPhamActivity.this);
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
                    urlImage = downloadUri.toString();
                    Picasso.with(ThemSanPhamActivity.this).load(urlImage).into(imgSanPham);
                    ApiService.getInstance().suaHinhSanPham(sanPham.getMaSP(), urlImage, new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                boolean kq = response.body();
                                if (kq == true) {
                                    Toast.makeText(ThemSanPhamActivity.this, "Lưu ảnh thành công", Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(ThemSanPhamActivity.this, "Lưu ảnh thất bại", Toast.LENGTH_LONG).show();
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
