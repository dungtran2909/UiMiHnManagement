package com.example.uimihnmanagement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.adapter.ChatAdapter;
import com.example.firebase.ChatMessage;
import com.example.model.NhanVien;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.HttpHeaders;
import cz.msebera.android.httpclient.entity.StringEntity;

import static com.example.uimihnmanagement.MainActivity.nhanVienLogin;


public class ChatActivity extends AppCompatActivity {
    FloatingActionButton fabSend;
    EditText edtInput;
    RecyclerView rcyMessege;
    ImageView imgBack;
    ChatAdapter adapter;
    ArrayList<ChatMessage> dsMessages;
    ArrayList<String> dsKEY = new ArrayList<>();
    NhanVien nhanVien=MainActivity.nhanVienLogin;
    private DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    String urlImage = "https://github.com/quoccuong151197/AppAndroid/blob/master/app/src/main/res/drawable/ic_fist_sub.png";
    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FirebaseDatabase.getInstance().getReference().child("status").child(nhanVienLogin.getUsername()).setValue(1);
        addControls();
        addEvents();
    }

    private void addEvents() {
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aaaaa = urlImage;
                mData.child("chats")
                        .push()
                        .setValue(new ChatMessage(edtInput.getText().toString(), nhanVien.getUsername(), urlImage));
                sendNotification(edtInput.getText().toString());
                edtInput.setText("");
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls() {
        dsMessages = new ArrayList<>();
        fabSend = findViewById(R.id.fab);
        edtInput = findViewById(R.id.edtInput);
        imgBack = findViewById(R.id.iv_back);
        rcyMessege = findViewById(R.id.lvMessages);
        rcyMessege.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        adapter = new ChatAdapter(ChatActivity.this, dsMessages, nhanVien);
        rcyMessege.setAdapter(adapter);
        displayChatMessages();
    }

    private void displayChatMessages() {
        mData.child("chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                dsMessages.add(chatMessage);
                dsKEY.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
                rcyMessege.smoothScrollToPosition(rcyMessege.getAdapter().getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String s) {
        try {
            String url = "https://fcm.googleapis.com/fcm/send";
            AsyncHttpClient client = new AsyncHttpClient();


            client.addHeader(HttpHeaders.AUTHORIZATION, "key=AIzaSyCVDJVhV8cOSqKs8_n6SPx0a2wxJ0u6FfA");
            client.addHeader(HttpHeaders.CONTENT_TYPE, RequestParams.APPLICATION_JSON);
            JSONObject params = new JSONObject();

            params.put("to", "/topics/ThongBao");

            JSONObject notificationObject = new JSONObject();
            notificationObject.put("body", URLEncoder.encode(s));
            notificationObject.put("title", URLEncoder.encode( "Gủi từ "+ removeAccent(nhanVien.getUsername())));

            params.put("notification", notificationObject);

            StringEntity entity = new StringEntity(params.toString());

            client.post(getApplicationContext(), url, entity, RequestParams.APPLICATION_JSON, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    Log.i(TAG, responseString);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    Log.i(TAG, responseString);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child("status").child(nhanVienLogin.getUsername()).setValue(0);
    }
}