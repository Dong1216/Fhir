package com.example.fhirsimpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PractitionerMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.practitioner_main);

        Intent intent = getIntent();
        final PractitionerSample practitioner = (PractitionerSample) intent.getSerializableExtra("practitioner");

        Button profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent2 = new Intent(PractitionerMain.this, PractitionerProfile.class);
                intent2.putExtra("practitioner", practitioner);
                startActivity(intent2);
            }
        });

        final Button consultingBtn = findViewById(R.id.chatting_btn);
        consultingBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent3 = new Intent(PractitionerMain.this, PractitionerChatting.class);
                intent3.putExtra("practitioner", practitioner);
                startActivity(intent3);
            }
        });
    }
}
