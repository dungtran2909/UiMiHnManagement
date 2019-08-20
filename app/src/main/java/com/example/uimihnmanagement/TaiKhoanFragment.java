package com.example.uimihnmanagement;

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
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.NhanVienFirebase;
import com.example.model.DanhMuc;
import com.example.model.NhanVien;
import com.example.model.SanPham;
import com.example.network.ApiService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.example.uimihnmanagement.MainActivity;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class TaiKhoanFragment extends Fragment {
    View view;
    EditText et_Ma, et_Ten, et_DiaChi, et_Sdt, et_Email, et_ChucVu, et_User;
    Button bt_Sua, bt_Luu, bt_DoiMK;
    ImageView ivBack;
    AvatarView avatarView;
    IImageLoader imageLoader;
    String KEY="";
    String urlImage="";
    TextView txtTen;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    int REQUEST_CODE_IMAGE = 1;
    int REQUEST_CODE_IMAGE_STORAGE = 2;
    StorageReference storageRef = storage.getReferenceFromUrl("gs://uimihnmanagement.appspot.com/");

    Bitmap bitmapCamera;

    ProgressDialog progressDialog;

    DatabaseReference mData= FirebaseDatabase.getInstance().getReference();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tai_khoan, container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        avatarView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                xuLyDoi();
                return false;
            }
        });
        bt_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLySua();
            }
        });
        bt_Luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                MainActivity.nhanVienLogin.setTenNhanVien(et_Ten.getText().toString());
                MainActivity.nhanVienLogin.setDiaChi(et_DiaChi.getText().toString());
                MainActivity.nhanVienLogin.setPhone(et_Sdt.getText().toString());
                MainActivity.nhanVienLogin.setEmail(et_Email.getText().toString());
                ApiService.getInstance().updateNhanVien(MainActivity.nhanVienLogin, new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()){
                            boolean kq=response.body();
                            if (kq==true){
                                Toast.makeText(view.getContext(),"Lưu thành công",Toast.LENGTH_LONG).show();
                                et_Ma.setEnabled(false);
                                et_Ten.setEnabled(false);
                                et_ChucVu.setEnabled(false);
                                et_User.setEnabled(false);
                                et_Email.setEnabled(false);
                                et_Sdt.setEnabled(false);
                                et_DiaChi.setEnabled(false);
                            }
                            else
                                Toast.makeText(view.getContext(),"Lưu thất bại, vui lòng kiểm tra lại",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
            }
        });
        bt_DoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(),DoiMatKhauActivity.class);
                startActivity(intent);
            }
        });
    }

    private void xuLyDoi() {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
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
        et_Ma = view.findViewById(R.id.edtMaNV);
        et_Ten = view.findViewById(R.id.edtTenNV);
        et_DiaChi = view.findViewById(R.id.edtDiaChi);
        et_Sdt = view.findViewById(R.id.edtSDT);
        et_Email = view.findViewById(R.id.edtEmail);
        et_ChucVu = view.findViewById(R.id.edtChucVu);
        et_User = view.findViewById(R.id.edtUserName);

        et_Ma.setText(MainActivity.nhanVienLogin.getMaNV()+"");
        et_Ten.setText(MainActivity.nhanVienLogin.getTenNhanVien());
        et_DiaChi.setText(MainActivity.nhanVienLogin.getDiaChi());
        et_Sdt.setText(MainActivity.nhanVienLogin.getPhone());
        et_Email.setText(MainActivity.nhanVienLogin.getEmail());
        et_User.setText(MainActivity.nhanVienLogin.getUsername());
        if (MainActivity.nhanVienLogin.getRole()==0){
            et_ChucVu.setText("Quản lý");
        }
        else if (MainActivity.nhanVienLogin.getRole()==1){
            et_ChucVu.setText("Nhân viên");
        }
        et_Ma.setEnabled(false);
        et_Ten.setEnabled(false);
        et_ChucVu.setEnabled(false);
        et_User.setEnabled(false);
        et_Email.setEnabled(false);
        et_Sdt.setEnabled(false);
        et_DiaChi.setEnabled(false);
        bt_DoiMK=view.findViewById(R.id.btnDoiMK);
        bt_Luu=view.findViewById(R.id.btnLuu);
        bt_Sua=view.findViewById(R.id.btnSua);

        ivBack=view.findViewById(R.id.iv_backTaiKhoan);
        txtTen=view.findViewById(R.id.txtTen);
        avatarView=view.findViewById(R.id.avatar_view);
        imageLoader= new PicassoLoader();
        imageLoader.loadImage(avatarView,"https://raw.githubusercontent.com/quoccuong151197/FirebaseStorage/master/app/src/main/res/drawable/ic.png","Image");
        getImage();
    }
    private void xuLySua() {
        et_Ten.setEnabled(true);
        et_Email.setEnabled(true);
        et_Sdt.setEnabled(true);
        et_DiaChi.setEnabled(true);
    }
    private void getImage() {
        mData.child("NhanVien").child(MainActivity.nhanVienLogin.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NhanVienFirebase firebase=dataSnapshot.getValue(NhanVienFirebase.class);
                imageLoader.loadImage(avatarView,firebase.getUrlImage(),firebase.getTenNhanVien());
                txtTen.setText(firebase.getTenNhanVien());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            bitmapCamera = (Bitmap) data.getExtras().get("data");
            xuLyUpload();
        }
        else if (requestCode == REQUEST_CODE_IMAGE_STORAGE && resultCode == RESULT_OK && data != null) {
            Uri uri=data.getData();
            String path=getRealPathFromURI(uri);
            xuLyUploadStroge(path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void xuLyUploadStroge(String path) {
        try{
            bitmapCamera=getThumbnail(path);
            xuLyUpload();
        }
        catch (Exception ex){
            Log.e("LOI",ex.toString());
        }
    }
    private void xuLyUpload() {
        progressDialog= new ProgressDialog(view.getContext());
        progressDialog.setTitle("Đang xử lý");
        progressDialog.setMessage("Vui lòng chờ...");
        progressDialog.show();
        final String child = MainActivity.nhanVienLogin.getUsername();
        final StorageReference mountainsRef = storageRef.child(child);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapCamera.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Toast.makeText(ChiTietNhanSu.this, "Thất bại", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(ChiTietNhanSu.this, "Thành công", Toast.LENGTH_LONG).show();
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
                    mData.child("NhanVien").child(child).child("urlImage").setValue((downloadUri.toString()), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                //Toast.makeText(ChiTietNhanSu.this, "Lưu databse Thành công", Toast.LENGTH_SHORT).show();
                                imageLoader.loadImage(avatarView, downloadUri.toString(), MainActivity.nhanVienLogin.getTenNhanVien());
                                progressDialog.dismiss();
                                android.app.AlertDialog.Builder alertDialog= new android.app.AlertDialog.Builder(view.getContext());
                                alertDialog.setTitle("Lưu thành công");
                                alertDialog.setIcon(R.drawable.ic_ok);
                                alertDialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                            } else {
                                // Toast.makeText(ChiTietNhanSu.this, "Lưu dadabase thất bại", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = view.getContext().getContentResolver().query(uri, null, null, null, null);
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
}
