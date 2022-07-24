package com.example.fhirsimpleapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fhirsimpleapp.Adapter.PractitionerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PractitionerChatting extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference mRef;
    DatabaseReference usersRef;
    DatabaseReference patientsRef;
    DatabaseReference practitionersRef;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    public PractitionerSample practitioner;
    PatientSample patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);

        Intent intent = getIntent();
        practitioner = (PractitionerSample) intent.getSerializableExtra("practitioner");

        recyclerView= findViewById(R.id.user_recycler_view);
        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        recyclerView.setLayoutManager(layoutManager);

        mRef = FirebaseDatabase.getInstance().getReference();
    }

    protected void onStart() {
        super.onStart();

        usersRef = mRef.child("users");
        patientsRef = usersRef.child("patients");
        practitionersRef = usersRef.child("practitioners");
        patientsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<PatientSample> data = new ArrayList<>();
                data.clear();
                for (DataSnapshot userSnap:dataSnapshot.getChildren())
                {
                    patient= userSnap.getValue(PatientSample.class);
                    data.add(patient);
                }
                adapter = new PractitionerAdapter(PractitionerChatting.this, practitioner, data);
                // assign the adapter to the recycler view
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
