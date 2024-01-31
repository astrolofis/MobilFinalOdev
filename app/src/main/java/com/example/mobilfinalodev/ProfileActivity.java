package com.example.mobilfinalodev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    DrawerLayout drawer;
    Button btn_Guncelle;
    EditText edit_isim, edit_Soyisim, edit_Acıklama, edit_Email;
    DatabaseReference mReference;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    HashMap<String, Object> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawer= findViewById(R.id.drawer_background);

        btn_Guncelle= findViewById(R.id.btn_Guncelle);
        edit_isim= findViewById(R.id.edit_isim);
        edit_Soyisim= findViewById(R.id.edit_Soyisim);
        edit_Acıklama= findViewById(R.id.edit_Acıklama);
        edit_Email= findViewById(R.id.edit_Email);

        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if(mUser==null){
            Intent intent= new Intent(ProfileActivity.this, Login.class);
            startActivity(intent);
            finish();
        }else{
            edit_Email.setText(mUser.getEmail());
            verileriGetir(mUser.getUid());
        }

        btn_Guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData = new HashMap<>();
                String isim, soyisim, acıklama;
                isim= String.valueOf(edit_isim.getText());
                soyisim= String.valueOf(edit_Soyisim.getText());
                acıklama= String.valueOf(edit_Acıklama.getText());

                mData.put("isim",isim);
                mData.put("soyisim",soyisim);
                mData.put("acıklama",acıklama);

                verileriGuncelle(mData, mUser.getUid());
            }
        });
    }

    public void verileriGuncelle(HashMap<String, Object> data, String uid){
        mReference= FirebaseDatabase.getInstance().getReference("kullanıcı").child(uid);
        mReference.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this,"Güncelleme Başarılı",Toast.LENGTH_SHORT);
                    verileriGetir(uid);
                }
            }
        });

    }
    public void verileriGetir(String uid){
        mReference= FirebaseDatabase.getInstance().getReference("kullanıcı").child(uid).child("isim");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value= snapshot.getValue(String.class);
                edit_isim.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mReference= FirebaseDatabase.getInstance().getReference("kullanıcı").child(mUser.getUid()).child("soyisim");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value= snapshot.getValue(String.class);
                edit_Soyisim.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mReference= FirebaseDatabase.getInstance().getReference("kullanıcı").child(mUser.getUid()).child("acıklama");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value= snapshot.getValue(String.class);
                edit_Acıklama.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mReference= FirebaseDatabase.getInstance().getReference("kullanıcı").child(uid).child("email");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value= snapshot.getValue(String.class);
                edit_Email.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







    public void menuClick(View view){
        drawerOpen(drawer);
    }

    public void drawerOpen(DrawerLayout drawer) {
        drawer.openDrawer(GravityCompat.START);
    }

    public void ImageClick(View view){
        drawerClose(drawer);
    }

    public void drawerClose(DrawerLayout drawer) {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void anasayfaClick(View view){
        Intent intent= new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void profileClick(View view){
        drawerClose(drawer);
        recreate();
    }

    public void logoutClick(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent= new Intent(ProfileActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}