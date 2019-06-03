package com.syahrinkusumaa.foodlist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.syahrinkusumaa.foodlist.Class.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button btnDaftar;
    private EditText editTextNama, editTextTelepon, editTextEmail, editTextPassword;
    private TextView SudahPunyaAkun;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            finish();

            Intent mn = new Intent(this,MainActivity.class);
            startActivity(mn);
        }

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");



        btnDaftar = findViewById(R.id.buttonRegister);

        editTextNama = findViewById(R.id.edtRegisNama);
        editTextTelepon = findViewById(R.id.edtRegisPhone);
        editTextEmail = findViewById(R.id.edtRegisEmail);
        editTextPassword = findViewById(R.id.edtRegisPass);

        SudahPunyaAkun = findViewById(R.id.tvSudahPunya);

        progressBar = findViewById(R.id.PBRegister);
        progressBar.setVisibility(View.GONE);



        btnDaftar.setOnClickListener(this);
        SudahPunyaAkun.setOnClickListener(this);
    }

    private void registerUser(){
        final String nama = editTextNama.getText().toString().trim();
        final String phone = editTextTelepon.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(nama)){
            editTextNama.setError("Masukkan Nama");
            editTextNama.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(phone)){
            editTextTelepon.setError("Masukkan No Telepone");
            editTextTelepon.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(email)){
            editTextEmail.setError("Masukkan Email");
            editTextEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            editTextPassword.setError("Masukkan Password");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){

                            User mUser = new User(nama, email, phone);

                            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(mUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                firebaseAuth.signOut();
                                                Toast.makeText(RegisterActivity.this,"Berhasil Mendaftar",Toast.LENGTH_SHORT).show();
                                                Login();
                                            }else{
                                                Toast.makeText(RegisterActivity.this,"Gagal Mendaftar, Silahkan coba lagi",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void Login(){
        Intent login = new Intent(this,LoginActivity.class);
        startActivity(login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonRegister:
                btnDaftar.setEnabled(false);
                registerUser();
                break;

            case R.id.tvSudahPunya:
                Login();
                break;
        }
    }
}

