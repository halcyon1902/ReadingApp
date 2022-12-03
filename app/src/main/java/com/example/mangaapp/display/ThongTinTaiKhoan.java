package com.example.mangaapp.display;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private static final String MY_PREFERENCE_NAME = "USER_ID";
    private final String pass = "*********";
    String id = null;
    private EditText tvHoVaTen1;
    private TextView tvEmail, tvEmail1, tvCSHoTen, tvCSMatKhau, lichsu, tv_TaiKhoan, yeuthich, tv_MatKhau;
    private ImageView img_home;
    private Button btnXacNhanHoTen, btn_LogOut, btnXacNhanPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_tai_khoan);
        init();
        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFERENCE_NAME, MODE_PRIVATE);
        id = sharedPreferences.getString("value", "");
        check(id);
        getThongTinTaiKhoan(id);
        tvCSHoTen.setOnClickListener(v -> {
            btnXacNhanHoTen.setVisibility(View.VISIBLE);
            tvHoVaTen1.setEnabled(true);
        });
        tvCSMatKhau.setOnClickListener(v -> {
            btnXacNhanPass.setVisibility(View.VISIBLE);
            tv_MatKhau.setEnabled(true);
        });
        btnXacNhanHoTen.setOnClickListener(v -> ChinhSuaHoTen(id));
        btnXacNhanPass.setOnClickListener(v -> updatePassword(id));
        img_home.setOnClickListener(v -> {
            Intent intent = new Intent(ThongTinTaiKhoan.this, MainScreen.class);
            startActivity(intent);
            finish();
        });
        btn_LogOut.setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("value", "");
            editor.apply();
            Intent intent = new Intent(ThongTinTaiKhoan.this, SignIn.class);
            startActivity(intent);
            finish();
        });
        lichsu.setOnClickListener(v -> {
            Intent intent = new Intent(ThongTinTaiKhoan.this, LichSu.class);
            startActivity(intent);
            finish();
        });
        yeuthich.setOnClickListener(v -> {
            Intent intent = new Intent(ThongTinTaiKhoan.this, Favorite.class);
            startActivity(intent);
            finish();
        });

    }

    private void check(String id) {
        ApiService.apiService.thongtintaikhoan(id).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
                TaiKhoan taiKhoan = response.body();
                if (taiKhoan != null && !taiKhoan.isTrangThai()) {
                    startActivity(new Intent(ThongTinTaiKhoan.this, SignIn.class));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {
                Log.e("Thông tin tài khoản: ", t.toString());
            }
        });
    }

    //Full màn hình
    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void ChinhSuaHoTen(String s) {
        String newHoTen = tvHoVaTen1.getText().toString();
        TaiKhoan taiKhoan = new TaiKhoan(false, true, newHoTen);
        ApiService.apiService.updateTaiKhoan(s, taiKhoan).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
            }

            @Override
            public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {
                Log.e("lỗi ", "" + t);
            }
        });
        btnXacNhanHoTen.setVisibility(View.INVISIBLE);
        tvHoVaTen1.setEnabled(false);
    }

    private void updatePassword(String s) {
        String newpass = tv_MatKhau.getText().toString();
        TaiKhoan taiKhoan = new TaiKhoan(newpass);
        ApiService.apiService.updateMatKHau(s, taiKhoan).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
            }

            @Override
            public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {
            }
        });
        btnXacNhanPass.setVisibility(View.INVISIBLE);
        tv_MatKhau.setText(pass);
        tv_MatKhau.setEnabled(false);
    }

    private void getThongTinTaiKhoan(String s) {
        ApiService.apiService.thongtintaikhoan(s).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
                TaiKhoan taiKhoan = response.body();
                if (taiKhoan != null && taiKhoan.isTrangThai()) {
                    tv_TaiKhoan.setText(taiKhoan.getTaiKhoan());
                    tvHoVaTen1.setText(taiKhoan.getHoTen());
                    tvEmail.setText(taiKhoan.getEmail());
                    tvEmail1.setText(taiKhoan.getEmail());
                    tvEmail1.setText(taiKhoan.getEmail());
                    tv_MatKhau.setText(pass);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaiKhoan> call, @NonNull Throwable t) {
                Log.e("Thông tin tài khoản: ", t.toString());
            }
        });
    }

    public void init() {
        tv_TaiKhoan = findViewById(R.id.ttcn_tv_TaiKhoan);
        tvHoVaTen1 = findViewById(R.id.ttcn_tv_HoVaTen1);
        tv_MatKhau = findViewById(R.id.ttcn_tv_MatKhau);
        tvEmail = findViewById(R.id.ttcn_tv_Email);
        tvEmail1 = findViewById(R.id.ttcn_tv_Email1);
        tvCSHoTen = findViewById(R.id.ttcn_tv_ChinhSuaHoTen);
        tvCSMatKhau = findViewById(R.id.ttcn_tv_ChinhSuaMK);
        btnXacNhanHoTen = findViewById(R.id.ttcn_btn_XacnhanHovaTen);
        btnXacNhanPass = findViewById(R.id.ttcn_btn_XacnhanPass);
        btn_LogOut = findViewById(R.id.btn_LogOut);
        img_home = findViewById(R.id.img_home);
        yeuthich = findViewById(R.id.ttcn_tv_YeuThich);
        lichsu = findViewById(R.id.ttcn_tv_LichSu);
    }
}