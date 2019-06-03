package com.syahrinkusumaa.foodlist;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.syahrinkusumaa.foodlist.Class.FoodList;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnUpdate,btnUpload;
    private EditText editTextNama, editTextWaktu, editTextKeterangan;
    private ImageView imageView;
    private ProgressBar progressBar;

    private String idFoodList, image;

    FirebaseStorage storage;
    StorageReference storageReference;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    private Uri filPath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_foodlist);

        //firebase init
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();

            Intent mn = new Intent(this,LoginActivity.class);
            startActivity(mn);
        }

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("FoodList");

        editTextNama = findViewById(R.id.edtEditNama);
        editTextWaktu = findViewById(R.id.edtEditWaktu);
        editTextKeterangan = findViewById(R.id.edtEditKeterangan);

        btnUpdate = findViewById(R.id.buttonUpdateData);
        btnUpload = findViewById(R.id.buttonUpdateImage);

        imageView = findViewById(R.id.imgUpdateFoodList);
        progressBar = findViewById(R.id.PBUpdate);
        progressBar.setVisibility(View.GONE);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Intent i = getIntent();
        idFoodList = i.getStringExtra("idFoodList");
        image = i.getStringExtra("image");

        editTextNama.setText(i.getStringExtra("nama_makanan"));
        editTextWaktu.setText(i.getStringExtra("waktu"));
        editTextKeterangan.setText(i.getStringExtra("keterangan"));
        LoadData();

        btnUpdate.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

    }

    private void LoadData(){

        StorageReference ref = storageReference.child("images/"+image);

        Glide.with(UpdateActivity.this)
                .using(new FirebaseImageLoader())
                .load(ref)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(imageView);
    }

    private void updateData(){
        String nama_makanan = editTextNama.getText().toString();
        String keterangan = editTextKeterangan.getText().toString();
        String waktu = editTextWaktu.getText().toString();
        //image = UUID.randomUUID().toString();
        String idFood = idFoodList;

        if(TextUtils.isEmpty(nama_makanan)){
            editTextNama.setError("Masukkan Nama Makanan");
            editTextNama.requestFocus();
            btnUpdate.setEnabled(true);
            return;
        }

        if(TextUtils.isEmpty(keterangan)){
            editTextKeterangan.setError("Masukkan Keterangan");
            editTextKeterangan.requestFocus();
            btnUpdate.setEnabled(true);
            return;
        }

        if(TextUtils.isEmpty(waktu)){
            editTextWaktu.setError("Masukkan Waktu");
            editTextWaktu.requestFocus();
            btnUpdate.setEnabled(true);
            return;
        }

        FoodList foodList = new FoodList(nama_makanan,waktu,keterangan,image);

        databaseReference.child(idFood).setValue(foodList)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            uploadImage();
                        }else{
                            Toast.makeText(UpdateActivity.this,"Gagal",Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/+");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    private void uploadImage(){
        progressBar.setVisibility(View.VISIBLE);

        if(filPath != null){
            StorageReference ref = storageReference.child("images/"+ image);
            ref.putFile(filPath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(UpdateActivity.this,"Berhasil",Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(UpdateActivity.this,MainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(UpdateActivity.this,"Gagal",Toast.LENGTH_LONG).show();
                        }
                    });

        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(UpdateActivity.this,"Berhasil",Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(UpdateActivity.this,MainActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonUpdateImage:
                chooseImage();
                break;
            case R.id.buttonUpdateData:
                btnUpdate.setEnabled(false);
                updateData();
                break;
        }
    }
}

