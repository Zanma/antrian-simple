package com.example.q.antrian_simple;

import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvNoNow;
    private EditText etNoKu;
    private Button btnIncrement, btnEditNoKu;
    
    Gson gson = new GsonBuilder().create();

    String TAG = "hmm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvNoNow = (TextView) findViewById(R.id.tv_no_now);
        etNoKu = (EditText) findViewById(R.id.et_no_ku);
        btnEditNoKu = (Button) findViewById(R.id.btn_edit_no_ku);
        btnEditNoKu.setOnClickListener(this);
        btnIncrement = (Button) findViewById(R.id.btn_increment);
        btnIncrement.setOnClickListener(this);

        setNoNow();
    }

    public void setNoNow() {

        AndroidNetworking.get("https://antrian-simple.glitch.me/antrian")
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
                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.btn_edit_no_ku:
                Toast.makeText(this, "Nomormu sudah terupdate", Toast.LENGTH_SHORT).show();
        }
    }
}
