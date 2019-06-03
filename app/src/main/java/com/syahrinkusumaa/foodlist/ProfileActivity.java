package com.syahrinkusumaa.foodlist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView Nama, Email, Phone;
    private Button btnLogout;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();

            Intent mn = new Intent(this,LoginActivity.class);
            startActivity(mn);
        }

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        Nama = findViewById(R.id.tvProfileNama);
        Email = findViewById(R.id.tvProfileEmail);
        Phone = findViewById(R.id.tvProfilePhone);

        btnLogout = findViewById(R.id.buttonLogout);
        btnLogout.setOnClickListener(this);

        loadProfile();

    }

    private void loadProfile(){
        databaseReference.child("User").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Nama.setText(dataSnapshot.child("nama").getValue().toString());
                Email.setText(dataSnapshot.child("email").getValue().toString());
                Phone.setText(dataSnapshot.child("telepon").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLogout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                break;
        }
    }
}

