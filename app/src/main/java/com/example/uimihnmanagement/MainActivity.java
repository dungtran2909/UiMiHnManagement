package com.example.uimihnmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebase.NhanVienFirebase;
import com.example.model.NhanVien;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    public static NhanVien nhanVienLogin;

    ImageView img_avatar;
    TextView txt_tenHeader, txt_mailHeader;

    IImageLoader imageLoader;
    AvatarView avatarView;
    DatabaseReference mData= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NhapHangFragments()).commit();
        toolbar.setTitle("Quản lý nhập hàng");
        Intent intent=getIntent();
        nhanVienLogin= (NhanVien) intent.getSerializableExtra("NHANVIEN");

        View header = navigationView.getHeaderView(0);
        txt_tenHeader = header.findViewById(R.id.txt_tenHeader);
        txt_mailHeader = header.findViewById(R.id.txt_mailHeader);
        avatarView = header.findViewById(R.id.avatar_view_header);

        txt_tenHeader.setText(nhanVienLogin.getTenNhanVien());
        txt_mailHeader.setText(nhanVienLogin.getEmail());

        imageLoader= new PicassoLoader();
        imageLoader.loadImage(avatarView,"https://raw.githubusercontent.com/quoccuong151197/FirebaseStorage/master/app/src/main/res/drawable/ic.png","Image");
        getImage();

        setStatus();
    }

    private void getImage() {
        mData.child("NhanVien").child(MainActivity.nhanVienLogin.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NhanVienFirebase firebase=dataSnapshot.getValue(NhanVienFirebase.class);
                imageLoader.loadImage(avatarView,firebase.getUrlImage(),firebase.getTenNhanVien());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setStatus() {
        FirebaseDatabase.getInstance().getReference().child("status").child(nhanVienLogin.getUsername()).setValue(1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_NhapHang) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NhapHangFragments()).commit();
            toolbar.setTitle("Quản lý nhập hàng");
        } else if (id == R.id.nav_XuatHang) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new XuatHangFragment()).commit();
            toolbar.setTitle("Quản lý xuất hàng");

        } else if (id == R.id.nav_ThongKe) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ThongKeFragment()).commit();
            toolbar.setTitle("Thống kê nhập xuất");

        }
        else if (id == R.id.nav_NhanSu) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NhanSuFragment()).commit();
            toolbar.setTitle("Nhân sự");

        }
        else if (id == R.id.nav_DonHang) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DonHangFragment()).commit();
            toolbar.setTitle("Đơn hàng");

        }else if (id == R.id.nav_SignUp) {
            Intent intent= new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_chat) {
            Intent intent = new Intent(MainActivity.this, UserListChatActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_TaiKhoan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TaiKhoanFragment()).commit();
            toolbar.setTitle("Thông tin tài khoản");

        }
        else if (id == R.id.nav_changePassword) {
            Intent intent= new Intent(MainActivity.this,DoiMatKhauActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_CSKH) {
            Intent intent= new Intent(MainActivity.this,ChamSocKhachHangActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_infoApp) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HoTroFragment()).commit();
            toolbar.setTitle("Hỗ trợ người dùng");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child("status").child(nhanVienLogin.getUsername()).setValue(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().child("status").child(nhanVienLogin.getUsername()).setValue(1);
    }
}
