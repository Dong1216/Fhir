package com.example.fhirsimpleapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    // reference to the database
    DatabaseReference mRef;
    DatabaseReference usersRef;
    DatabaseReference patientsRef;
    DatabaseReference practitionersRef;

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRef= FirebaseDatabase.getInstance().getReference();
        usersRef = mRef.child("users");
        patientsRef = usersRef.child("patients");
        practitionersRef = usersRef.child("practitioners");

        radioGroup = findViewById(R.id.radioGroup);
        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                //get id from edit text
                EditText patientId = findViewById(R.id.userId);
                String id = patientId.getText().toString();
                int radioId = radioGroup.getCheckedRadioButtonId();
                if(radioId == R.id.radioPatient){
                    new GetPatientDetails().execute(id);
                }else{
                    new GetPractitionerDetails().execute(id);
                }
            }
        });
    }

    private class GetPatientDetails extends AsyncTask<String, String, PatientSample>{
        protected PatientSample doInBackground(String... params){
            PatientSample patient = null;
            try{
                String selectedPatient = params[0];
                URL webServiceEndPoint = new URL("http://hapi-fhir.erc.monash.edu:8080/baseDstu3/Patient/" + selectedPatient);
                //create connection
                HttpURLConnection myConnection = (HttpURLConnection) webServiceEndPoint.openConnection();
                if(myConnection.getResponseCode() == 200) {
                    //open a stream to it and get a reader
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    //now use a JSON parser to decode data
                    JsonReader jsonReader = new JsonReader(responseBodyReader);

                    patient = new PatientSample();
                    patient.setPatientid(selectedPatient);
                    String keyName;
                    String familyName;
                    String givenName;

                    jsonReader.beginObject(); //consume array's opening JSON brace
                    while(jsonReader.hasNext()){
                        //process key/value pairs inside the current object
                        keyName =jsonReader.nextName();
                        if(keyName.equals("name")){
                            jsonReader.beginArray();
                            while(jsonReader.hasNext()){
                                jsonReader.beginObject();
                                while(jsonReader.hasNext()){
                                    keyName = jsonReader.nextName();
                                    if(keyName.equals("family")){
                                        familyName = jsonReader.nextString();
                                        patient.setFamilyName(familyName.substring(0,familyName.length()-3));
                                    }else if(keyName.equals("given")){
                                        jsonReader.beginArray();
                                        while(jsonReader.hasNext()){
                                            givenName = jsonReader.nextString();
                                            patient.setGivenName(givenName.substring(0,givenName.length()-3));
                                        }jsonReader.endArray();
                                    }else{
                                        jsonReader.skipValue();
                                    }
                                }jsonReader.endObject();
                            }jsonReader.endArray();
                        }
                        else if(keyName.equals("gender")){
                            patient.setGender(jsonReader.nextString());
                        }
                        else if(keyName.equals("birthDate")){
                            String birthDate = jsonReader.nextString();//1939-08-08
                            Date today = new Date();
                            int age = today.getYear()+1900 - Integer.parseInt(birthDate.substring(0,4));
                            patient.setAge((double) age);
                        }
                        else if(keyName.equals("maritalStatus")){
                            jsonReader.beginObject();
                            while(jsonReader.hasNext()){
                                keyName = jsonReader.nextName();
                                if(keyName.equals("text")){
                                    patient.setMaritualStatus(jsonReader.nextString());
                                }else{
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
                        }
                        else if(keyName.equals("telecom")){
                            jsonReader.beginArray();
                            while(jsonReader.hasNext()){
                                jsonReader.beginObject();
                                while(jsonReader.hasNext()){
                                    keyName = jsonReader.nextName();
                                    if(keyName.equals("value")){
                                        patient.setPhone(jsonReader.nextString());
                                    }else{
                                        jsonReader.skipValue();
                                    }
                                }jsonReader.endObject();
                            }jsonReader.endArray();
                        }
                        else if(keyName.equals("address")){
                            jsonReader.beginArray();
                            while(jsonReader.hasNext()){
                                jsonReader.beginObject();
                                while(jsonReader.hasNext()){
                                    keyName=jsonReader.nextName();
                                    if(keyName.equals("country")){
                                        patient.setAddress(jsonReader.nextString());
                                    }else{
                                        jsonReader.skipValue();
                                    }
                                }jsonReader.endObject();
                            }jsonReader.endArray();
                        }
                        else{
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                }else{
                    Log.i("info", "no response" );
                }
            }catch(Exception e){
                Log.i("info", "error" + e.toString());
            }
            return patient;
        }

        @Override
        protected void onPostExecute(PatientSample patient){
            super.onPostExecute(patient);
            patientsRef.child(patient.getPatientid()).setValue(patient);
            Intent intent = new Intent(MainActivity.this, PatientMain.class);
            intent.putExtra("patient", patient);
            startActivity(intent);
        }
    }

    private class GetAllPractitioners extends AsyncTask<PatientSample, String, PatientSample>{
        protected  PatientSample doInBackground(PatientSample... params){
            List<String> practitionersList = null;
            PatientSample patient = params[0];
            String practitionerId;
            try{
                String selectedPatient = String.valueOf(patient.getPatientid());
                URL webServiceEndPoint = new URL("http://hapi-fhir.erc.monash.edu:8080/baseDstu3/Encounter?patient=" + selectedPatient);
                //create connection
                HttpURLConnection myConnection = (HttpURLConnection) webServiceEndPoint.openConnection();
                if(myConnection.getResponseCode() == 200){
                    practitionersList = new ArrayList<>();
                    //open a stream to it and get a reader
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                    //now use a JSON parser to decode data
                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginObject();//consume array's opening JSON brace
                    String keyName;
                    while(jsonReader.hasNext()) {//process key/value pairs inside the current object
                        keyName = jsonReader.nextName();
                        if (keyName.equals("entry")) {
                            jsonReader.beginArray();
                            while(jsonReader.hasNext()){
                                //read array
                                jsonReader.beginObject();
                                while(jsonReader.hasNext()){
                                    keyName = jsonReader.nextName();
                                    if(keyName.equals("resource")){
                                        jsonReader.beginObject();
                                        while(jsonReader.hasNext()){
                                            keyName= jsonReader.nextName();
                                            if(keyName.equals("participant")){
                                                jsonReader.beginArray();
                                                while(jsonReader.hasNext()){
                                                    jsonReader.beginObject();
                                                    while(jsonReader.hasNext()){
                                                        keyName = jsonReader.nextName();
                                                        if(keyName.equals("individual")){
                                                            jsonReader.beginObject();
                                                            while(jsonReader.hasNext()){
                                                                keyName = jsonReader.nextName();
                                                                if(keyName.equals("reference")){
                                                                    practitionerId = jsonReader.nextString().substring(13);
                                                                    if(!practitionersList.contains(practitionerId)){
                                                                        practitionersList.add(practitionerId);
                                                                    }
                                                                }else{
                                                                    jsonReader.skipValue();}
                                                            }
                                                            jsonReader.endObject();
                                                        }else{
                                                            jsonReader.skipValue();
                                                        }

                                                    }
                                                    jsonReader.endObject();

                                                }
                                                jsonReader.endArray();

                                            }else{
                                                jsonReader.skipValue();
                                            }
                                        }
                                        jsonReader.endObject();
                                    }else{
                                        jsonReader.skipValue();
                                    }

                                }
                                jsonReader.endObject();
                            }
                            jsonReader.endArray();
                        }else{
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                    patient.setPractitionersList(practitionersList);
                }
                else{
                    Log.i("info", "no respond" );
                }
            }catch(Exception e){
                Log.i("info", "error" + e.toString());
            }
            return patient;
        }
        protected void onPostExecute(PatientSample patient){
            super.onPostExecute(patient);
            patientsRef.child(patient.getPatientid()).setValue(patient);
            Intent intent = new Intent(MainActivity.this, PatientMain.class);
            intent.putExtra("patient", patient);
            startActivity(intent);}
    }


    private class GetPractitionerDetails extends AsyncTask<String, String, PractitionerSample>{

        @Override
        protected PractitionerSample doInBackground(String... params) {
            PractitionerSample practitioner = null;
            try{
                String selectedPractitioner = params[0];
                URL webServiceEndPoint = new URL("http://hapi-fhir.erc.monash.edu:8080/baseDstu3/Practitioner/" + selectedPractitioner);

                //create connection
                HttpURLConnection myConnection = (HttpURLConnection) webServiceEndPoint.openConnection();

                if(myConnection.getResponseCode()==200){

                    practitioner = new PractitionerSample();
                    practitioner.setId(selectedPractitioner);

                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginObject();
                    String keyName;
                    String familyName;
                    String givenName;
                    while(jsonReader.hasNext()){
                        keyName = jsonReader.nextName();
                        if(keyName.equals("name")){
                            jsonReader.beginArray();
                            while(jsonReader.hasNext()){
                                jsonReader.beginObject();
                                while(jsonReader.hasNext()){
                                    keyName = jsonReader.nextName();
                                    if(keyName.equals("family")){
                                        familyName = jsonReader.nextString();
                                        practitioner.setFamilyName(familyName.substring(0,familyName.length()-3));
                                    }else if(keyName.equals("given")){
                                        jsonReader.beginArray();
                                        while(jsonReader.hasNext()){
                                            givenName = jsonReader.nextString();
                                            practitioner.setGivenName(givenName.substring(0,givenName.length()-3));
                                        }jsonReader.endArray();
                                    }else{
                                        jsonReader.skipValue();
                                    }
                                }jsonReader.endObject();
                            }jsonReader.endArray();

                        }else if(keyName.equals("address")){
                            jsonReader.beginArray();
                            while(jsonReader.hasNext()){
                                jsonReader.beginObject();
                                while(jsonReader.hasNext()){
                                    keyName = jsonReader.nextName();
                                    if(keyName.equals("country")){
                                        practitioner.setCountry(jsonReader.nextString());
                                    }else{
                                        jsonReader.skipValue();
                                    }
                                }jsonReader.endObject();
                            }jsonReader.endArray();

                        }else if(keyName.equals("gender")){
                            practitioner.setGender(jsonReader.nextString());

                        }else{
                            jsonReader.skipValue();
                        }
                    }jsonReader.endObject();
                }else{
                    Log.i("info", "no response");
                }

            }catch(Exception e){
                Log.i("info", "error"+e.toString());
            }
            return practitioner;
        }
        protected void onPostExecute(PractitionerSample practitioner){
            super.onPostExecute(practitioner);
            practitionersRef.child(practitioner.getId()).setValue(practitioner);
            Intent intent = new Intent(MainActivity.this, PractitionerMain.class);
            intent.putExtra("practitioner", practitioner);
            startActivity(intent);
        }
    }

}
