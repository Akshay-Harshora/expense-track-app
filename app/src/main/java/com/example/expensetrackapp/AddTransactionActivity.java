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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddTransactionActivity extends AppCompatActivity {
    EditText e1,e2;
    Button b1;
    CheckBox c1,c2;
    String type = "";
    FirebaseFirestore fstore;
    FirebaseAuth firebaseauth;
    FirebaseUser firebaseuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        fstore = FirebaseFirestore.getInstance();
        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        e1 = (EditText)findViewById(R.id.useramount);
        e2 = (EditText)findViewById(R.id.usernote);
        c1 = (CheckBox)findViewById(R.id.expense);
        c2 = (CheckBox)findViewById(R.id.income);
        b1 = (Button)findViewById(R.id.btntransaction);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = e1.getText().toString().trim();
                String note = e2.getText().toString().trim();
                if(amount.length()<=0)
                {
                    return;
                }
                if(type.length()<=0)
                {
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy_HH:mm");
                String currentdatetime = sdf.format(new Date());
                String id = UUID.randomUUID().toString();
                Map<String,Object> transaction = new HashMap<>();
                transaction.put("id",id);
                transaction.put("amount",amount);
                transaction.put("note",note);
                transaction.put("type",type);
                transaction.put("date",currentdatetime);
                fstore.collection("Expenses").document(firebaseauth.getUid()).collection("Notes").document(id).set(transaction)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddTransactionActivity.this,"Added",Toast.LENGTH_SHORT).show();
                                e1.setText("");
                                e2.setText("");
                                c1.setChecked(false);
                                c2.setChecked(false);
                                onBackPressed();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTransactionActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "Expense";
                 c1.setChecked(true);
                c2.setChecked(false);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "Income";
                c1.setChecked(false);
                c2.setChecked(true);
            }
        });

    }
}