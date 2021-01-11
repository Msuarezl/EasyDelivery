package com.example.easydelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.easydelivery.ado.InternalFile;
import com.example.easydelivery.model.Person;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class SettingUser extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottonNavigation);
        bottomNavigationView.setSelectedItemId(R.id.fragmenUser);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        InicializarFirebase ();
    }
    private void InicializarFirebase (){
        // firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    public void CloseSession(View view)
    {
        InternalFile internal = new InternalFile();
        JSONObject jsonObject = null;
        try {
            jsonObject = internal.readerFile("data", "datausers");
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("User", jsonObject.getString("User"));
            jsonObject2.put("Token", "Null");
            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                        Person p = objSnaptshot.getValue(Person.class);
                        try {
                            // se pregunta por el usuario en la bd esto por el email
                            if (jsonObject2.getString("User").equals(p.getEmail())) {
                                p.setToken("  ");
                                databaseReference.child("Users").child(p.getIduser()).setValue(p);
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            internal.writerFile("data", "datausers", jsonObject2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, VerifyToken.class);
        startActivity(intent);
    }
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.fragmenStore:
                    intent = new Intent(SettingUser.this, Store.class);
                    startActivity(intent);
                    return true;

                case R.id.fragmenCategory:
                    intent = new Intent(SettingUser.this, Category.class);
                    startActivity(intent);
                    return true;

                case R.id.fragmenSearch:
                    intent = new Intent(SettingUser.this, Search.class);
                    startActivity(intent);
                    return true;
                case R.id.fragmenUser:
                    intent = new Intent(SettingUser.this, SettingUser.class);
                    startActivity(intent);
                    return true;
            }


            return false;
        }
    };
}