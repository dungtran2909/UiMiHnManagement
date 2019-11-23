package com.example.uimihnmanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HoTroFragment  extends Fragment {
    View view;

    TextView txt_call, txt_send;
    EditText edt_send;
    Button btn_send;
    LinearLayout lnl_send;

    public static String so = "0356484803";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hotro, container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        txt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(so)) {
                    String dial = "tel:" + so;
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                } else {
                    Toast.makeText(getContext(), "Nhap vao so dien thoai", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_send.setOnClickListener(new View.OnClickListener() {
            boolean visible;

            @Override
            public void onClick(View v) {
                visible = !visible;
                lnl_send.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tin = edt_send.getText().toString();
                String send = "smsto:" + so;
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(send));
                intent.putExtra("sms_body", tin);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        txt_call = view.findViewById(R.id.txt_call);
        txt_send = view.findViewById(R.id.txt_send);
        edt_send = view.findViewById(R.id.edt_send);
        btn_send = view.findViewById(R.id.btn_send);
        lnl_send = view.findViewById(R.id.lnl_send);
    }
}
