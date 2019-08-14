package com.example.uimihnmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.SanPhamNhapMoiAdapter;
import com.example.callback.ConfirmDeleteCallBack;
import com.example.callback.ThemSanPhamOnclickListener;
import com.example.model.DanhMuc;
import com.example.model.NhanHieu;
import com.example.model.NhanVien;
import com.example.model.PhieuNhap;
import com.example.model.SanPham;
import com.example.network.ApiService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NhapHangMoiActivity extends AppCompatActivity {
    ImageView imgBack, imgAdd, imgSave;
    EditText edtNhanVienNhap, edtNgayNhap;
    ListView lvSanPham;
    SanPhamNhapMoiAdapter sanPhamNhapMoiAdapter;
    ArrayList<SanPham> sanPhams;
    int REQUEST_CODE_IMAGE = 1;
    int REQUEST_CODE_IMAGE_STORAGE = 2;
    DialogThemSanPham dialogThemSanPham;
    ConfirmDialog confirmDialog;
    int postionLuu;

    static Bitmap bitmapCamera;
    static ProgressDialog progressDialog;
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = storage.getReferenceFromUrl("gs://uimihnmanagement.appspot.com/");
    public static  String urlImage = "";
    ArrayList<String> arrUrlImage;
    SanPham sp;
    PhieuNhap nhap;
    int postionXoa;
    int positionImage;
    public static int status=0;

    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_hang_moi);
        addControls();
        addEvents();
    }

    private void addEvents() {
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemSanPhamOnclickListener themSanPhamOnclickListener= new ThemSanPhamOnclickListener() {
                    @Override
                    public void onButtonClick(SanPham sanPham) {
                        sanPhams.add(sanPham);
                        sanPhamNhapMoiAdapter.notifyDataSetChanged();
                        dialogThemSanPham.dismiss();
                    }
                };
                dialogThemSanPham= new DialogThemSanPham(NhapHangMoiActivity.this,NhapHangMoiActivity.this,themSanPhamOnclickListener);
                dialogThemSanPham.show();
            }
        });
        lvSanPham.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                registerForContextMenu(lvSanPham);
                postionXoa=i;
                positionImage=i;
                return false;
            }
        });
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyLuu();
            }
        });
    }
    //Lưu phiếu nhập mới
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
    //Lấy Chi tiết phiếu nhập vừa tạo để lấy được mã phiếu nhập
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
                    for (int i=0; i<sanPhams.size();i++){
                        SanPham sanPham=sanPhams.get(i);
                        sanPham.setHinhSP(arrUrlImage.get(i));
                        postionLuu=i;
                        luuSanPham(sanPham);
                    }


                }
            }

            @Override
            public void onFailure(Call<List<PhieuNhap>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
    //Lưu sản phẩm với ảnh mặc định
    private void luuSanPham(final SanPham sanPham) {
        ApiService.getInstance().luuMoiSanPham(sanPham.getTenSP(), sanPham.getDonGia(), sanPham.getMaDanhMuc(), true, sanPham.getMoTa(), sanPham.getHinhSP(), sanPham.getMaNhanHieu(),sanPham.getSoLuongTon(), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    getChiTietSanPham(sanPham.getTenSP());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
    //Lấy chi tiết sản phẩm vừa tạo để lấy được mã sản phẩm
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
    //Lưu chi tiết phiếu nhập mới
    private void luuChiTietPhieuNhap(PhieuNhap nhap) {
        ApiService.getInstance().LuuCTPhieuNhap(nhap.getMaPhieuNhap(), sp.getMaSP(), sp.getSoLuongTon(), sp.getDonGia(), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                   if (response.body()==true){
                       final AlertDialog.Builder builder= new AlertDialog.Builder(NhapHangMoiActivity.this);
                       builder.setTitle("Lưu thành công").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               Intent returnIntent = new Intent();
                               returnIntent.putExtra("result",1);
                               setResult(Activity.RESULT_OK,returnIntent);
                               finish();
                           }
                       }).show();
                       //Toast.makeText(NhapHangMoiActivity.this, "Lưu thành công", Toast.LENGTH_LONG).show();
                   }
                   else
                       Toast.makeText(NhapHangMoiActivity.this, "Lưu thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }


    private void addControls() {
        arrUrlImage=new ArrayList<String>();
        imgBack=findViewById(R.id.iv_backChiTiet);
        imgAdd=findViewById(R.id.imgAddProduct);
        imgSave=findViewById(R.id.imgSave);

        edtNhanVienNhap=findViewById(R.id.edtMaNhanVienNhap);
        edtNgayNhap=findViewById(R.id.edtNgayNhapHang);
        sanPhams= new ArrayList<>();
        lvSanPham=findViewById(R.id.lvSanPham);
        sanPhamNhapMoiAdapter= new SanPhamNhapMoiAdapter(NhapHangMoiActivity.this,R.layout.item_san_pham_nhap_moi,sanPhams);
        lvSanPham.setAdapter(sanPhamNhapMoiAdapter);

        edtNhanVienNhap.setText(MainActivity.nhanVienLogin.getMaNV()+"-"+MainActivity.nhanVienLogin.getTenNhanVien());
        edtNgayNhap.setText(simpleDateFormat.format(new Date(System.currentTimeMillis())));
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
        SanPham sanPham=sanPhams.get(positionImage);
        xuLyUpload(sanPham);
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemXoa:
                xuLyXacNhanXoaSanPham();
                break;
            case R.id.itemSua:
                xuLySuaSanPhamChitiet();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void xuLySuaSanPhamChitiet() {
        xuLyDoi();
    }

    private void xuLyXacNhanXoaSanPham() {
        AlertDialog.Builder builder= new AlertDialog.Builder(NhapHangMoiActivity.this);
        builder.setTitle("Bạn chắn chắn muốn xóa?").setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                xuLyXoaSanPham();
            }
        }).setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private void xuLyXoaSanPham() {
        sanPhams.remove(postionXoa);
        sanPhamNhapMoiAdapter.notifyDataSetChanged();
    }
    private void xuLyDoi() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NhapHangMoiActivity.this);
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
    private void xuLyUpload(final SanPham sanPham) {
        progressDialog = new ProgressDialog(NhapHangMoiActivity.this);
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
        final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    arrUrlImage.add(downloadUri.toString());
                    progressDialog.dismiss();

                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}
