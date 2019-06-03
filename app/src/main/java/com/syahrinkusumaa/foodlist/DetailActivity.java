package com.syahrinkusumaa.foodlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nama_makanan, waktu, keterangan;
    private ImageView imageView;
    private Button btnUpdate, btnDelete;

    FirebaseStorage storage;
    StorageReference storageReference;

    private String idFoodList, image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nama_makanan = findViewById(R.id.tvDetailnama_makanan);
        waktu = findViewById(R.id.tvDetailWaktu);
        keterangan = findViewById(R.id.tvDetailKeterangan);

        imageView = findViewById(R.id.imgDetail);

        btnUpdate = findViewById(R.id.buttonUpdate);
        btnDelete = findViewById(R.id.buttonDelete);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Intent i = getIntent();
        idFoodList = i.getStringExtra("id");
        image = i.getStringExtra("image");


        nama_makanan.setText(i.getStringExtra("nama_makanan"));
        waktu.setText(i.getStringExtra("waktu"));
        keterangan.setText(i.getStringExtra("keterangan"));

        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        LoadData();
    }

    private void LoadData(){

        StorageReference ref = storageReference.child("images/"+image);

        Glide.with(DetailActivity.this)
                .using(new FirebaseImageLoader())
                .load(ref)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(imageView);
    }

    private void DeleteData(){
        DatabaseReference del = FirebaseDatabase.getInstance().getReference("FoodList").child(idFoodList);

        del.removeValue();

        Toast.makeText(this,"Data Berhasil Dihapus!",Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(DetailActivity.this,MainActivity.class));
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Hapus");

        alertDialogBuilder
                .setMessage("Hapus Data Makanan?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteData();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void updateData(){
        Intent i = new Intent(DetailActivity.this,UpdateActivity.class);
        i.putExtra("idFoodList",idFoodList);
        i.putExtra("nama_makanan",nama_makanan.getText().toString());
        i.putExtra("waktu",waktu.getText().toString());
        i.putExtra("keterangan",keterangan.getText().toString());
        i.putExtra("image",image);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonUpdate:
                updateData();
                break;
            case R.id.buttonDelete:
                showDialog();
                break;
        }
    }
}

