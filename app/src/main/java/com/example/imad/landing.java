package com.example.imad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class landing extends AppCompatActivity {
    int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        Button bt = (Button) findViewById(R.id.existing);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),existingOrders.class);
                startActivity(i);
            }
        });
        Button lg = (Button)findViewById(R.id.logout);
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth mauth = FirebaseAuth.getInstance();
                mauth.signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        String UID = mauth.getUid();
        dbr.child("Users");
        TextView name = (TextView)findViewById(R.id.nme);
        name.setText("Failed To Retrieve Name");
        dbr.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn : snapshot.getChildren()) {
                    User u = sn.getValue(User.class);
                    if (UID.equals(String.valueOf(sn.getKey()))) {
                        name.setText("Your Name: "+u.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        TextView counter = (TextView) findViewById(R.id.ordercnt);
        counter.setText("Failed To Retrive Order Count");
        dbr.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cnt=0;
                for(DataSnapshot sn:snapshot.getChildren()){
                    Orders order =sn.getValue(Orders.class);
                    if(order.getUid().equals(UID)){
                        cnt++;
                    }

                }
                counter.setText("Number Of Orders : "+String.valueOf(cnt));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createNew(View view) {
        Intent i = new Intent(getApplicationContext(),CameraActivity.class);
        startActivity(i);
//        Intent i = new Intent(getApplicationContext(),Gallery.class);
//        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent intent = new Intent(getApplicationContext(), login.class);
        //startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}