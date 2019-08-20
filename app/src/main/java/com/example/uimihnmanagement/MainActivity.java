package com.example.uimihnmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.model.NhanVien;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    public static NhanVien nhanVienLogin;

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

        setStatus();
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
            toolbar.setTitle("Thống kê nhập xuất");

        }
        else if (id == R.id.nav_DonHang) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DonHangFragment()).commit();
            toolbar.setTitle("Đơn hàng");

        }else if (id == R.id.nav_tools) {
            Intent intent= new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_home) {
            Intent intent = new Intent(MainActivity.this, UserListChatActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_TaiKhoan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TaiKhoanFragment()).commit();
            toolbar.setTitle("Thông tin tài khoản");

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
