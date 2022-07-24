package com.example.fhirsimpleapp;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class PatientPredict extends AppCompatActivity {
    Interpreter tflite;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_prediction);

        TextView cholesterolTv = (TextView) findViewById(R.id.preTv);
        Intent intent = getIntent();
        final PatientSample patient = (PatientSample) intent.getSerializableExtra("patient");

        String gender = patient.getGender();
        String marital = patient.getMaritualStatus();
        double age = patient.getAge();

        float female = 1;
        float male = 1;
        if( gender == "male"){
            female = 0;
        }else{
            male = 0;
        }
        float single = 1;
        float married = 1;
        if(marital == "M"){
            single = 0;
        }else{
            married = 0;
        }

        try{
            tflite = new Interpreter(loadModelFile());
            float[][] inputVal = new float[1][5];

            inputVal[0][0] = female;
            inputVal[0][1] = male;
            inputVal[0][2] = single;
            inputVal[0][3] = married;
            inputVal[0][4] = (float) age;

            float[][] outVal = new float[1][1];
            tflite.run(inputVal,outVal);
            final String cholesterolString = Float.toString(outVal[0][0]);

            cholesterolTv.setText(cholesterolString);

        }catch(Exception ex){
            ex.printStackTrace();
            Log.i("error", ex+"");
        }


    }
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("no_normalized_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffet,declaredLength);
    }

}
