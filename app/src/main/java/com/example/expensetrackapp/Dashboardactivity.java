package com.example.expensetrackapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Dashboardactivity extends AppCompatActivity {
    CardView c1;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;
    RecyclerView r1;
    int sumexpense = 0, sumincome = 0;
    TextView t1,t2,t3;
    ImageView im1,im2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardactivity);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        r1 = (RecyclerView)findViewById(R.id.history);
        c1 = (CardView)findViewById(R.id.addfloatingbutton);
        t1 = (TextView)findViewById(R.id.totalincome);
        t2 = (TextView)findViewById(R.id.totalexpense);
        t3 = (TextView)findViewById(R.id.totalbalance);
        im2 = (ImageView)findViewById(R.id.signout);
       firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(Dashboardactivity.this,MainActivity.class));
                    finish();
                }
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSignOutDialog();
            }
        });
        transactionModelArrayList = new ArrayList<>();
        r1.setLayoutManager(new LinearLayoutManager(this));
        r1.setHasFixedSize(true);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboardactivity.this,AddTransactionActivity.class));
            }
        });
        loadData();
    }

    private void createSignOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboardactivity.this);
        builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
         firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
        .collection("Notes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {

                 transactionModelArrayList.clear();
                 sumexpense = 0;
                 sumincome = 0;
                 for(DocumentSnapshot ds:task.getResult())
                 {
                    TransactionModel model = new TransactionModel(
                            ds.getString("id"),
                            ds.getString("note"),
                            ds.getString("amount"),
                            ds .getString("type"),
                            ds.getString("date"));
                            transactionModelArrayList.add(model);
                            int amount = Integer.parseInt(ds.getString("amount"));
                            if(ds.getString("type").equals("Expense"))
                            {
                                sumexpense = sumexpense+amount;
                            }
                            else
                            {
                                sumincome = sumincome+amount;
                            }


                 }
                 t2.setText(String.valueOf(sumexpense));
                 t1.setText(String.valueOf(sumincome));
                 t3.setText(String.valueOf(sumincome-sumexpense));

                 transactionAdapter = new TransactionAdapter(Dashboardactivity.this,transactionModelArrayList);
                 r1.setAdapter(transactionAdapter);

             }
         });
    }
}