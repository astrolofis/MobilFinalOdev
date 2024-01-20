package com.example.mobilfinalodev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btn_Logout,btn_Guncelle;
    EditText editName, editSurname, editAcıklama;
    DatabaseReference mReference;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    HashMap<String, Object> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_Logout= findViewById(R.id.btn_Logout);
        btn_Guncelle= findViewById(R.id.btn_Guncelle);
        editName= findViewById(R.id.editName);
        editSurname= findViewById(R.id.editSurname);
        editAcıklama= findViewById(R.id.editAcıklama);

        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if(mUser==null){
            Intent intent= new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }else{
            verileriGetir(mUser.getUid());
        }

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btn_Guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData = new HashMap<>();
                String isim, soyisim, acıklama;
                isim= String.valueOf(editName.getText());
                soyisim= String.valueOf(editSurname.getText());
                acıklama= String.valueOf(editAcıklama.getText());

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
                    Toast.makeText(MainActivity.this,"Güncelleme Başarılı",Toast.LENGTH_SHORT);
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
                editName.setText(value);
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
                editSurname.setText(value);
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
                editAcıklama.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}