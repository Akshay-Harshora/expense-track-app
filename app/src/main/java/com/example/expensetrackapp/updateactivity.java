package com.example.expensetrackapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class updateactivity extends AppCompatActivity {
    Button b1,b2;
    EditText e1,e2;
    CheckBox c1,c2;
    String newtype;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateactivity);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        b1 = (Button)findViewById(R.id.btnupdate);
        b2 = (Button)findViewById(R.id.btndelete);
        e1 = (EditText)findViewById(R.id.useramount);
        e2 = (EditText)findViewById(R.id.usernote);
        c1 = (CheckBox)findViewById(R.id.expense);
        c2 = (CheckBox)findViewById(R.id.income);
        String id = getIntent().getStringExtra("id");
        String amount = getIntent().getStringExtra("amount");
        String note  = getIntent().getStringExtra("note");
        String type = getIntent().getStringExtra("type");
        e1.setText(amount);
        e2.setText(note);
        switch(type)
        {
            case "Income":
                newtype ="Income";
                c2.setChecked(true);
                break;
            case "Expense":
                newtype = "Expense";
                c1.setChecked(true);
                break;


        }
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newtype = "Expense";
                c1.setChecked(true);
                c2.setChecked(false);
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newtype = "Income";
                c1.setChecked(false);
                c2.setChecked(true);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = e1.getText().toString();
                String note = e2.getText().toString();

                  firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes")
                         .document(id).update("amount", amount, "note", note, "type", newtype)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onBackPressed();
                                Toast.makeText(updateactivity.this, "Updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(updateactivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
                                .collection("Notes")
                                .document(id).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        onBackPressed();
                                        Toast.makeText(updateactivity.this, "deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(updateactivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

    }
}