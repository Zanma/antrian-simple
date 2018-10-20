package com.example.q.antrian_simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvNoNow;
    private EditText etNoKu;
    private Button btnIncrement, btnEditNoKu;

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
