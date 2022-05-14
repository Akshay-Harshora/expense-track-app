package com.example.expensetrackapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signupactivity extends AppCompatActivity {
    Button b1;
    EditText t1,t2;
    TextView tv1;
    FirebaseAuth firebaseauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);
        firebaseauth = FirebaseAuth.getInstance();
        tv1 = (TextView)findViewById(R.id.gotologin);
        b1 = (Button)findViewById(R.id.btnsignup);
        t1 = (EditText)findViewById(R.id.email_for_signup);
        t2 = (EditText)findViewById(R.id.password_for_signup);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(signupactivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = t1.getText().toString();
                String password = t2.getText().toString();
                if(email.trim().length()<=0 || password.trim().length()<=0)
                {
                    return;
                }
            firebaseauth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(signupactivity.this,"User created",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(signupactivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            });
            }
        });
    }
}