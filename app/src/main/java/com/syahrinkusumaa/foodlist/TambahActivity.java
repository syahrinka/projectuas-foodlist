package com.syahrinkusumaa.foodlist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.syahrinkusumaa.foodlist.Class.FoodList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class TambahActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTambah,btnUpload;
    private EditText editTextNama, editTextWaktu, editTextKeterangan;
    private ImageView imageView;
    private String nama_img;
    private ProgressBar progressBar;

    private Uri filPath;

    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_foodlist);

        //firebase init
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();

            Intent mn = new Intent(this,LoginActivity.class);
            startActivity(mn);
        }

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("FoodList");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnTambah = findViewById(R.id.buttonTambah);
        btnUpload = findViewById(R.id.buttonImage);

        editTextNama = findViewById(R.id.edtTambahNama);
        editTextKeterangan = findViewById(R.id.edtTambahKeterangan);
        editTextWaktu = findViewById(R.id.edtTambahWaktu);

        imageView = findViewById(R.id.imgFoodList);
        progressBar = findViewById(R.id.PBTambah);
        progressBar.setVisibility(View.GONE);

        btnTambah.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonImage:
                chooseImage();
                break;
            case R.id.buttonTambah:
                btnTambah.setEnabled(false);
                tambahData();
                //uploadImage();
                break;
        }
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/+");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"),PICK_IMAGE_REQUEST);
    }

    private void tambahData(){
        String nama_makanan = editTextNama.getText().toString();
        String keterangan = editTextKeterangan.getText().toString();
        String waktu = editTextWaktu.getText().toString();
        nama_img = UUID.randomUUID().toString();
        String idLap = UUID.randomUUID().toString();

        if(TextUtils.isEmpty(nama_makanan)){
            editTextNama.setError("Masukkan Nama Makanan");
            editTextNama.requestFocus();
            btnTambah.setEnabled(true);
            return;
        }

        if(TextUtils.isEmpty(keterangan)){
            editTextKeterangan.setError("Masukkan Keterangan");
            editTextKeterangan.requestFocus();
            btnTambah.setEnabled(true);
            return;
        }

        if(TextUtils.isEmpty(waktu)){
            editTextWaktu.setError("Masukkan Waktu");
            editTextWaktu.requestFocus();
            btnTambah.setEnabled(true);
            return;
        }

        FoodList foodList = new FoodList(nama_makanan,waktu,keterangan,nama_img);

        databaseReference.child(idLap).setValue(foodList)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            uploadImage();
                        }else{
                            Toast.makeText(TambahActivity.this,"Gagal Menambahkan Nama Makanan!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }


    private void uploadImage(){
        progressBar.setVisibility(View.VISIBLE);

        if(filPath != null){
            StorageReference ref = storageReference.child("images/"+ nama_img);
            ref.putFile(filPath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(TambahActivity.this,"Berhasil",Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(TambahActivity.this,MainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(TambahActivity.this,"Gagal",Toast.LENGTH_LONG).show();
                        }
                    });

        }else{
            progressBar.setVisibility(View.GONE);
            Toast.makeText(TambahActivity.this,"Berhasil",Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(TambahActivity.this,MainActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filPath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filPath);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}

