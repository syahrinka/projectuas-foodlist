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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private EditText editTextEmail, editTextPassword;
    private TextView BelumPunyaAkun;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();

            Intent mn = new Intent(this,MainActivity.class);
            startActivity(mn);
        }


        btnLogin = findViewById(R.id.buttonLogin);

        editTextEmail = findViewById(R.id.edtLoginEmail);
        editTextPassword = findViewById(R.id.edtLoginPass);

        BelumPunyaAkun = findViewById(R.id.tvBelumPunya);

        progressBar = findViewById(R.id.PBlogin);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(this);
        BelumPunyaAkun.setOnClickListener(this);

    }

    private void Daftar(){
        Intent dftr = new Intent(this,RegisterActivity.class);
        startActivity(dftr);
    }

    private void Menu(){
        Intent mn = new Intent(this,MainActivity.class);
        startActivity(mn);
    }

    private void Login(){
        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();

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

        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if(task.isSuccessful()){
                            finish();
                            Menu();
                        }else{
                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogin:
                Login();
                break;

            case R.id.tvBelumPunya:
                Daftar();
                break;
        }
    }
}
