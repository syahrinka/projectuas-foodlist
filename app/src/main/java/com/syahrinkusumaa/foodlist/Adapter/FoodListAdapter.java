package com.syahrinkusumaa.foodlist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.syahrinkusumaa.foodlist.Class.FoodList;
import com.syahrinkusumaa.foodlist.Class.FoodList;
import com.syahrinkusumaa.foodlist.DetailActivity;
import com.syahrinkusumaa.foodlist.DetailActivity;
import com.syahrinkusumaa.foodlist.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.MyViewHolder> {

    private List<FoodList> mFoodList;
    private Context mContext;

    FirebaseStorage storage;
    StorageReference storageReference;

    public FoodListAdapter(List<FoodList> mFoodList, Context mContext) {
        this.mFoodList = mFoodList;
        this.mContext = mContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView nama_makanan, waktu, keterangan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgViewFoodList);
            nama_makanan = itemView.findViewById(R.id.tvnama_makananFoodList);
            waktu = itemView.findViewById(R.id.tvWaktuFoodList);
            keterangan = itemView.findViewById(R.id.tvKeteranganFoodList);
        }
    }

    @NonNull
    @Override
    public FoodListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_foodlist, parent, false);
        FoodListAdapter.MyViewHolder myViewHolder = new FoodListAdapter.MyViewHolder(mView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodListAdapter.MyViewHolder holder, final int position) {
        holder.nama_makanan.setText(mFoodList.get(position).getnama_makanan());
        holder.waktu.setText(mFoodList.get(position).getWaktu());
        holder.keterangan.setText(mFoodList.get(position).getKeterangan());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final StorageReference ref = storageReference.child("images/"+ mFoodList.get(position).getImg());

        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(ref)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(mContext, DetailActivity.class);
                //nama img sama dengan id
                detail.putExtra("id",mFoodList.get(position).getId_foodlist());
                detail.putExtra("nama_makanan",mFoodList.get(position).getnama_makanan());
                detail.putExtra("waktu",mFoodList.get(position).getWaktu());
                detail.putExtra("keterangan",mFoodList.get(position).getKeterangan());
                detail.putExtra("image",mFoodList.get(position).getImg());
                mContext.startActivity(detail);
            }
        });




    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
}

