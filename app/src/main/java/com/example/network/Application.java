package com.example.network;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiService.init("http://apiuimihn.somee.com/api/");
    }
}