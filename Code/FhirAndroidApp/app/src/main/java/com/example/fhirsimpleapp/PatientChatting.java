package com.example.fhirsimpleapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fhirsimpleapp.Adapter.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientChatting extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference mRef;
    DatabaseReference usersRef;
    DatabaseReference patientsRef;
    DatabaseReference practitionersRef;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    public PatientSample patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);

        Intent intent = getIntent();
        patient = (PatientSample) intent.getSerializableExtra("patient");

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
        practitionersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<PractitionerSample> data = new ArrayList<>();
                data.clear();
                for (DataSnapshot userSnap:dataSnapshot.getChildren())
                {
                    PractitionerSample practitioner= userSnap.getValue(PractitionerSample.class);
                    data.add(practitioner);
                }
                adapter = new UserAdapter(PatientChatting.this, patient, data);
                // assign the adapter to the recycler view
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
