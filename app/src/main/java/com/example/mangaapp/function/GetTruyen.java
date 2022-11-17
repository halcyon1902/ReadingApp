package com.example.mangaapp.function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaapp.R;
import com.example.mangaapp.adapter.ChapterAdapter;
import com.example.mangaapp.adapter.TacGiaAdapter;
import com.example.mangaapp.adapter.TheLoaiAdapter;
import com.example.mangaapp.api.ApiService;
import com.example.mangaapp.model.Chapter;
import com.example.mangaapp.model.TacGia;
import com.example.mangaapp.model.TheLoai;
import com.example.mangaapp.model.Truyen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetTruyen extends AppCompatActivity {
    private final Context context = this;
    private TextView tvTenTruyen, tvTinhTrang, tvFollow, tvLike, tvNoiDung, tvTongChuong;
    private ImageView imgAnhBia, imgAnhNen;
    private RecyclerView rcvTheLoai, rcvTacGia, rcvChapter;
    private List<String> mListIDTheLoai, mListIDTacGia;
    private List<TheLoai> mlistTheLoai;
    private List<TacGia> mlistTacGia;
    private ChapterAdapter chapterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_get_truyen);

        // lấy intent
        Intent intent = getIntent();
        Truyen truyen = (Truyen) intent.getSerializableExtra("clickTruyen");
        init();
        mListIDTheLoai = new ArrayList<>();
        mlistTheLoai = new ArrayList<>();
        mListIDTacGia = new ArrayList<>();
        mlistTacGia = new ArrayList<>();
        initLinearLayout();
        hienThiTruyen(truyen);
    }

    private void initLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        rcvTheLoai.setLayoutManager(linearLayoutManager);
        rcvTacGia.setLayoutManager(linearLayoutManager1);
        rcvChapter.setLayoutManager(linearLayoutManager2);
    }

    @SuppressLint("SetTextI18n")
    private void hienThiTruyen(Truyen truyen) {
        if (truyen != null && truyen.isTrangThai()) {
            mListIDTheLoai = Arrays.asList(truyen.getTheLoais());
            mListIDTacGia = Arrays.asList(truyen.getTacGias());
            List<Chapter> mlistChapter = Arrays.asList(truyen.getChapters());
            //Hiển thị thể loại
            for (int i = 0; i < mListIDTheLoai.size(); i++) {
                ApiService.apiService.GetTheLoai(mListIDTheLoai.get(i)).enqueue(new Callback<TheLoai>() {
                    @Override
                    public void onResponse(@NonNull Call<TheLoai> call, @NonNull Response<TheLoai> response) {
                        TheLoai theLoai = response.body();
                        if (theLoai != null && theLoai.isTrangThai()) {
                            mlistTheLoai.add(theLoai);
                        }
                        TheLoaiAdapter theLoaiAdapter = new TheLoaiAdapter(mlistTheLoai, GetTruyen.this);
                        rcvTheLoai.setAdapter(theLoaiAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<TheLoai> call, @NonNull Throwable t) {

                    }
                });
            }

            //Hiển thị tác giả
            for (int i = 0; i < mListIDTacGia.size(); i++) {
                ApiService.apiService.GetTacGia(mListIDTacGia.get(i)).enqueue(new Callback<TacGia>() {
                    @Override
                    public void onResponse(@NonNull Call<TacGia> call, @NonNull Response<TacGia> response) {
                        TacGia tacGia = response.body();
                        if (tacGia != null && tacGia.isTrangThai()) {
                            mlistTacGia.add(tacGia);
                        }
                        TacGiaAdapter tacGiaAdapter = new TacGiaAdapter(mlistTacGia, context);
                        rcvTacGia.setAdapter(tacGiaAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<TacGia> call, @NonNull Throwable t) {

                    }
                });
            }

            //Hiển thị chapter
            Collections.reverse(mlistChapter);
            chapterAdapter = new ChapterAdapter(mlistChapter, context);
            rcvChapter.setAdapter(chapterAdapter);
            tvTenTruyen.setText(truyen.getTenTruyen());
            if (truyen.isTrangThai()) {
                tvTinhTrang.setText("Tình trạng: Hoàn thành");
            } else {
                tvTinhTrang.setText("Tình trạng: Đang tiến thành");
            }
            tvFollow.setText("" + truyen.getLuotXem());
            tvLike.setText("" + truyen.getLuotThich());
            tvNoiDung.setText(truyen.getGioiThieu());
            tvTongChuong.setText("Tổng chapter: " + mlistChapter.size());
            Picasso.get().load(truyen.getAnhBia()).into(imgAnhBia);
            Picasso.get().load(truyen.getAnhBia()).into(imgAnhNen);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chapterAdapter != null)
            chapterAdapter.release();
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void init() {
        tvTenTruyen = findViewById(R.id.tv_ten_truyen);
        tvTinhTrang = findViewById(R.id.tv_tinhtrang_truyen);
        tvFollow = findViewById(R.id.tv_follow);
        tvLike = findViewById(R.id.tv_like);
        tvNoiDung = findViewById(R.id.tv_noi_dung_truyen);
        tvTongChuong = findViewById(R.id.tv_tong_chapter);
        imgAnhBia = findViewById(R.id.manga_cover);
        imgAnhNen = findViewById(R.id.backdrop);
        rcvTheLoai = findViewById(R.id.rcv_The_Loai);
        rcvTacGia = findViewById(R.id.rcv_tac_gia);
        rcvChapter = findViewById(R.id.rcv_chapter);
    }
}