package com.example.mobilfinalodev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText regName, regSurname, regEmail, regPassword;
    Button btn_register;
    TextView textLogin;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mReference;
    HashMap<String, Object> mData;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent= new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regName = findViewById(R.id.regName);
        regSurname = findViewById(R.id.regSurname);
        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        btn_register = findViewById(R.id.btn_register);
        textLogin = findViewById(R.id.textLogin);

        mAuth = FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });




        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, isim, soyisim, acıklama;



                email= String.valueOf(regEmail.getText());
                password= String.valueOf(regPassword.getText());
                isim= String.valueOf(regName.getText());
                soyisim= String.valueOf(regSurname.getText());
                acıklama="Doldurunuz";

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this,"Email Giriniz", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this,"Password Giriniz", Toast.LENGTH_SHORT).show();
                }


                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mUser= mAuth.getCurrentUser();
                                    mData=new HashMap<>();
                                    mData.put("isim",isim);
                                    mData.put("soyisim",soyisim);
                                    mData.put("email",email);
                                    mData.put("sifre",password);
                                    mData.put("acıklama",acıklama);

                                    mReference.child("kullanıcı").child(mUser.getUid())
                                            .setValue(mData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(Register.this,"Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Intent intent= new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Register.this,"Kayıt Başarısız", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });






    }
}