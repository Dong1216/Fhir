package com.example.fhirsimpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fhirsimpleapp.Adapter.MessageAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientChatting2 extends AppCompatActivity {

    TextView username;
    ImageButton btn_send;
    ImageButton btn_map;
    EditText text_send;
    MessageAdapter messageAdapter;
    ArrayList<ChatSample> chatList;
    RecyclerView recyclerView;
    DatabaseReference databaseReference,informationReference;
    PractitionerSample practitioner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);

        Intent intent = getIntent();
        final String senderId = intent.getStringExtra("sender");
        practitioner = (PractitionerSample) intent.getSerializableExtra("receiver");
        final String receiverId = practitioner.getId();


        String address=intent.getStringExtra("address");
        if (address!=null&&address.length()>0)
        {
            sendMessage(senderId,receiverId,address);
        }


        recyclerView=findViewById(R.id.chats_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_send=findViewById(R.id.send_btn);
        text_send=findViewById(R.id.send_text);
        username=findViewById(R.id.chat_username);

        btn_map = findViewById(R.id.map_btn);

        username.setText(practitioner.getGivenName()+" "+practitioner.getFamilyName());


        informationReference= FirebaseDatabase.getInstance().getReference().child("users").child("practitioners");

        informationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessage(senderId,receiverId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=text_send.getText().toString();
                if (!msg.equals(""))
                {
                    sendMessage(senderId,receiverId,msg);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "can't send an empty message ", Toast.LENGTH_LONG).show();
                }
                text_send.setText("");
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(PatientChatting2.this, PatientMap.class);
                i.putExtra("receiver", practitioner);
                i.putExtra("sender", senderId);
                startActivity(i);
            }
        });

    }
    private void sendMessage(String sender, String receiver ,String message)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        reference.child("messages").push().setValue(hashMap);
    }
    private  void readMessage(final String myid, final String userid)
    {

        databaseReference=FirebaseDatabase.getInstance().getReference("messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList = new ArrayList<>();
                chatList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    ChatSample newchat= snapshot.getValue(ChatSample.class);

                    if (newchat.getReceiver().equals(myid)&&newchat.getSender().equals(userid)||
                            newchat.getReceiver().equals(userid)&&newchat.getSender().equals(myid)
                    )
                    {
                        chatList.add(newchat);
                    }
                }
                messageAdapter= new MessageAdapter(PatientChatting2.this, myid, chatList);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


}



















