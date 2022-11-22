package com.example.mangaapp.display;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mangaapp.R;
import com.example.mangaapp.api.ApiService;
import com.example.mangaapp.function.SignIn;
import com.example.mangaapp.mainscreen.MainScreen;
import com.example.mangaapp.model.TaiKhoan;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongTinTaiKhoan extends AppCompatActivity {
    private TextView tvHoVaTen;
    private EditText tvHoVaTen1;
    private TextView tvEmail;
    private TextView tvEmail1;
    private TextView tvCSHoTen;
    private TextView tvCSMatKhau;
    private ImageView img_home;
    private TaiKhoan user;
    private Button btnXacNhanHoTen, btn_LogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);
        init();
        Intent intent = getIntent();
        String value = intent.getStringExtra("lấy thông tin tài khoản");
        Log.e("Lấy thông tin tài khoản", "" + value);
        getThongTinTaiKhoan(value);
        tvCSHoTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnXacNhanHoTen.setVisibility(View.VISIBLE);
                tvHoVaTen1.setEnabled(true);
            }
        });
        btnXacNhanHoTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChinhSuaHoTen(value);
            }
        });
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongTinTaiKhoan.this, MainScreen.class);
                intent.putExtra("lấy thông tin tài khoản", user.get_id());
                startActivity(intent);
            }
        });
        btn_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongTinTaiKhoan.this, SignIn.class);
                //intent.putExtra("lấy thông tin tài khoản", user.get_id());
                startActivity(intent);
            }
        });
    }

    private void ChinhSuaHoTen(String s) {
        String newHoTen = tvHoVaTen1.getText().toString();
        TaiKhoan taiKhoan = new TaiKhoan(newHoTen);
        ApiService.apiService.updateHoTen(s, taiKhoan).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
            }

            @Override
            public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {

            }
        });
        btnXacNhanHoTen.setVisibility(View.INVISIBLE);
    }

    private void getThongTinTaiKhoan(String s) {
        ApiService.apiService.thongtintaikhoan(s).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
                TaiKhoan taiKhoan = response.body();
                Log.e("Lấy thông tin tài khoản", "" + taiKhoan.toString());
                user = taiKhoan;
                if (taiKhoan != null && taiKhoan.isTrangThai()) {
                    tvHoVaTen.setText(taiKhoan.getHoTen());
                    tvHoVaTen1.setText(taiKhoan.getHoTen());
                    tvEmail.setText(taiKhoan.getEmail());
                    tvEmail1.setText(taiKhoan.getEmail());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {
                Log.e("Thông tin tài khoản: ", t.toString());
            }
        });
    }

    public void init() {
        tvHoVaTen = findViewById(R.id.ttcn_tv_HoVaTen);
        tvHoVaTen1 = findViewById(R.id.ttcn_tv_HoVaTen1);
        tvEmail = findViewById(R.id.ttcn_tv_Email);
        tvEmail1 = findViewById(R.id.ttcn_tv_Email1);
        tvCSHoTen = findViewById(R.id.ttcn_tv_ChinhSuaHoTen);
        btnXacNhanHoTen = findViewById(R.id.ttcn_btn_XacnhanHovaTen);
        btn_LogOut = findViewById(R.id.btn_LogOut);
        img_home = findViewById(R.id.img_home);
    }
}