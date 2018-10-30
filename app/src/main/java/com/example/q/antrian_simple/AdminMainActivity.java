package com.example.q.antrian_simple;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvNoNow;
//    private EditText etNoKu;
    private Button btnIncrement; //, btnEditNoKu;

    Gson gson = new GsonBuilder().create();

    String TAG = "hmm";

    private Socket mSocket;
    {
        try {
            Log.d(TAG, "instance initializer: connected");
            mSocket = IO.socket("https://antrian-simple.glitch.me/");
        } catch (URISyntaxException e) {
            Log.d(TAG, "instance initializer: "+e);
        }
    }

    private void attemptSend() {
        String message = tvNoNow.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Log.d(TAG, "attemptSend: kosong");
            return;
        }
        Log.d(TAG, "attemptSend: "+message);
        mSocket.emit("new message", message);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            AdminMainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        Log.d(TAG, "run: "+e);
                        return;
                    }

                    // add the message to view
//                    addMessage(username, message);
                    Log.d(TAG, "run: detail"+username + message);
                    tvNoNow.setText(message);

//                    if (!etNoKu.getText().toString().equals("")) {
//                        checkGoToStand(Integer.parseInt(message));
//                    }
                }
            });

        }
    };

//    private void checkGoToStand(int now) {
//
//        int current = Integer.parseInt(etNoKu.getText().toString()) - now;
//
//        if (current <= 2) {
//
//            NotificationCompat.Builder builer = new NotificationCompat.Builder(AdminMainActivity.this);
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://techsupportnep.blogspot.com"));
//            PendingIntent pendingIntent = PendingIntent.getActivity(AdminMainActivity.this, 01, intent,0);
//            builer.setContentIntent(pendingIntent);
//            builer.setDefaults(Notification.DEFAULT_ALL);
//            builer.setContentTitle("Boodonation 2018");
//            builer.setSmallIcon(R.mipmap.ic_launcher);
//            builer.setContentText("Giliranmu sudah dekat, kuy ke ged H1.6");
//            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//            notificationManager.notify(001, builer.build());
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mSocket.on("new message", onNewMessage);
        mSocket.connect();

        tvNoNow = (TextView) findViewById(R.id.tv_no_now);
        etNoKu = (EditText) findViewById(R.id.et_no_ku);
//        btnEditNoKu = (Button) findViewById(R.id.btn_edit_no_ku);
//        btnEditNoKu.setOnClickListener(this);
        btnIncrement = (Button) findViewById(R.id.btn_increment);
        btnIncrement.setOnClickListener(this);

        setNoNow();
    }

    public void incrementNo() {

        Log.d(TAG, "incrementNo: ");

        Antrian antrian = new Antrian();
        antrian.setNo(Integer.valueOf(tvNoNow.getText().toString()));

        String jsoon = gson.toJson(antrian);

        AndroidNetworking.put("http://mobileporos.nyamukterbang.com/antrian/1")
                .addApplicationJsonBody(antrian)
                //.addBodyParameter(jsoon)
                //.addApplicationJsonBody(jsoon)
                .setPriority(Priority.LOW).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: ressp"+response);
            }

            @Override
            public void onError(ANError anError) {
                Log.d(TAG, "onError: eeee"+anError);

            }
        });

    }

    public void setNoNow() {

        AndroidNetworking.get("http://mobileporos.nyamukterbang.com/antrian")
                .addQueryParameter("limit", "3")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Antrian antrian = null;
                        try {
                            antrian = gson.fromJson(response.getJSONObject(0).toString(), Antrian.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tvNoNow.setText(antrian.getNo().toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "onResponse error: "+error.toString());
                        // handle error
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_increment:
                Toast.makeText(this, "Nomor Antrian berhasil dinaikkan", Toast.LENGTH_SHORT).show();
                int newNoNow = Integer.valueOf(tvNoNow.getText().toString()) + 1;
                tvNoNow.setText(String.valueOf(newNoNow));
                attemptSend();
                incrementNo();
                break;
//            case R.id.btn_edit_no_ku:
//                Toast.makeText(this, "Nomormu sudah terupdate", Toast.LENGTH_SHORT).show();
        }
    }
}
