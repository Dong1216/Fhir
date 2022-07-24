package com.example.fhirsimpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PatientInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.patient_information);

        Intent intent =getIntent();
        final PatientSample patient = (PatientSample) intent.getSerializableExtra("patient");

        String name;
        String family = patient.getFamilyName();//.substring(0,practitioner.getFamilyName().length()-3);
        String given = patient.getGivenName();//.substring(0,practitioner.getGivenName().length()-3);
        name = given + " " + family;
        TextView nameTv = findViewById(R.id.patientName);
        nameTv.setText(name);

        String gender = patient.getGender();
        TextView gendertv = findViewById(R.id.gendertv);
        gendertv.setText(gender);

        Double age = patient.getAge();
        TextView agetv = findViewById(R.id.agetv);
        agetv.setText(Double.toString(age));

        String phone = patient.getPhone();
        TextView phonetv= findViewById(R.id.phonetv);
        phonetv.setText(phone);

        String address = patient.getAddress();
        TextView addresstv= findViewById(R.id.addresstv);
        addresstv.setText(address);


    }
}
