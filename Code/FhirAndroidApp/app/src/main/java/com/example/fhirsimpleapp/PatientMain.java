package com.example.fhirsimpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PatientMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.patient_main);

        Intent intent = getIntent();
        final PatientSample patient = (PatientSample) intent.getSerializableExtra("patient");

        Button informationBtn = findViewById(R.id.personalInformation_btn);
        informationBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent2 = new Intent(PatientMain.this, PatientInformation.class);
                intent2.putExtra("patient", patient);
                startActivity(intent2);
            }
        });

        final Button consultingBtn = findViewById(R.id.consulting_btn);
        consultingBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent3 = new Intent(PatientMain.this, PatientChatting.class);
                intent3.putExtra("patient", patient);
                startActivity(intent3);
            }
        });

        final Button predictionBtn = findViewById(R.id.prediction_btn);
        predictionBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent4 = new Intent(PatientMain.this, PatientPredict.class);
                intent4.putExtra("patient", patient);
                startActivity(intent4);
            }
        });



    }


}
