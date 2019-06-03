package com.syahrinkusumaa.foodlist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.syahrinkusumaa.foodlist.Adapter.FoodListAdapter;
import com.syahrinkusumaa.foodlist.Class.FoodList;
import com.syahrinkusumaa.foodlist.Class.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.syahrinkusumaa.foodlist.Adapter.FoodListAdapter;
import com.syahrinkusumaa.foodlist.Class.FoodList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogout;
    private TextView tvEmail,tvNama;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<FoodList> DataFoodList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();

            Intent mn = new Intent(this,LoginActivity.class);
            startActivity(mn);
        }

        FirebaseUser Fuser = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        btnLogout = findViewById(R.id.buttonTambahFoodList);
        btnLogout.setOnClickListener(this);

        getData();

        //init RecylecrView
        mRecyclerView = findViewById(R.id.rvFoodList);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        tvEmail = findViewById(R.id.tvWelcome);


        tvEmail.setText("Selamat Datang "+Fuser.getEmail());
        tvEmail.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData(){
        databaseReference.child("FoodList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataFoodList = new ArrayList<>();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String nama_makanan = dataSnapshot1.child("nama_makanan").getValue().toString();
                    String waktu = dataSnapshot1.child("waktu").getValue().toString();
                    String keterangan = dataSnapshot1.child("keterangan").getValue().toString();
                    String img = dataSnapshot1.child("img").getValue().toString();
                    String id = dataSnapshot1.getKey();

                    FoodList foodList = new FoodList(id,nama_makanan,waktu,keterangan,img);


                    Log.e("asdasda",foodList.getId_foodlist());
                    DataFoodList.add(foodList);
                }

                mAdapter = new FoodListAdapter(DataFoodList,MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w( "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buttonTambahFoodList:
//                firebaseAuth.signOut();
//                finish();
//                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                startActivity(new Intent(MainActivity.this,TambahActivity.class));
                break;
            case R.id.tvWelcome:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                break;
        }

    }
}
