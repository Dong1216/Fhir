package com.example.fhirsimpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PractitionerProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.practitioner_profile);

        Intent intent =getIntent();
        final PractitionerSample practitioner = (PractitionerSample) intent.getSerializableExtra("practitioner");

        String name;
        String family = practitioner.getFamilyName();//.substring(0,practitioner.getFamilyName().length()-3);
        String given = practitioner.getGivenName();//.substring(0,practitioner.getGivenName().length()-3);
        name = given + " " + family;
        TextView nameTv = findViewById(R.id.prac_name);
        nameTv.setText(name);

        String gender = practitioner.getGender();
        TextView genderTv = findViewById(R.id.prac_gender);
        genderTv.setText(gender);


        String address = practitioner.getCountry();
        TextView addressTv= findViewById(R.id.prac_address);
        addressTv.setText(address);


    }
}
