package com.example.mangaapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangaapp.R;
import com.example.mangaapp.function.GetTruyen;
import com.example.mangaapp.model.Truyen;

import java.util.ArrayList;
import java.util.List;

public class TruyenTranhAdapter extends RecyclerView.Adapter<TruyenTranhAdapter.TruyenTranhViewHolder> {
    private Context context;
    private List<Truyen> mListTruyenTranh;

    @SuppressLint("NotifyDataSetChanged")
    public TruyenTranhAdapter(Context context, List<Truyen> mListTruyenTranh) {
        this.context = context;
        this.mListTruyenTranh = mListTruyenTranh;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TruyenTranhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_truyen, parent, false);
        return new TruyenTranhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TruyenTranhViewHolder holder, int position) {
        Truyen truyen = mListTruyenTranh.get(position);
        if (truyen != null) {
            holder.tenTruyebTranh.setText(truyen.getTenTruyen());
            Glide.with(this.context).load(truyen.getAnhBia()).into(holder.imgAnhBia);
            holder.crvTruyen.setOnClickListener(v -> {
                //Click vào chi tiết truyện
                Intent intent = new Intent(context, GetTruyen.class);
                intent.putExtra("clickTruyen", truyen);
                //updateLuotXem(truyen);
                context.startActivity(intent);
            });
        }
    }

//    public void updateLuotXem(@NonNull Truyen truyen) {
//        int luotxem = truyen.getLuotXem();
//        int luotxemThang = truyen.getLuotXemThang();
//        Date ngayXepHang = truyen.getNgayXepHang();
//        int thang = ngayXepHang.getMonth() + 1;
//        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
//        int currentMonth = calendar.get(Calendar.MONTH) + 1;
//        Date currentTime = Calendar.getInstance().getTime();
//        if (thang == currentMonth) {
//            luotxemThang += 1;
//        } else {
//            luotxemThang = 1;
//            ngayXepHang = currentTime;
//        }
//        luotxem += 1;
//        Truyen truyen1 = new Truyen(truyen.isTrangThai(), truyen.isTinhTrang(), luotxem, luotxemThang, ngayXepHang);
//        ApiService.apiService.UpdateTruyen(truyen.get_id(), truyen1).enqueue(new Callback<Truyen>() {
//            @Override
//            public void onResponse(Call<Truyen> call, Response<Truyen> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<Truyen> call, Throwable t) {
//
//            }
//        });
//    }

    public void release() {
        context = null;
    }

    @Override
    public int getItemCount() {
        if (mListTruyenTranh != null)
            return mListTruyenTranh.size();
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<Truyen> filteredList) {
        mListTruyenTranh = filteredList;
        notifyDataSetChanged();
    }

    public static class TruyenTranhViewHolder extends RecyclerView.ViewHolder {
        private final CardView crvTruyen;
        private final TextView tenTruyebTranh;
        private final ImageView imgAnhBia;


        public TruyenTranhViewHolder(@NonNull View itemView) {
            super(itemView);
            crvTruyen = itemView.findViewById(R.id.crv_TruyenTranh);
            tenTruyebTranh = itemView.findViewById(R.id.tv_TenTruyen);
            imgAnhBia = itemView.findViewById(R.id.imgv_AnhBia);
        }
    }
}

