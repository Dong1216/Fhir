package com.example.fhirsimpleapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fhirsimpleapp.PatientChatting2;
import com.example.fhirsimpleapp.PatientSample;
import com.example.fhirsimpleapp.PractitionerSample;
import com.example.fhirsimpleapp.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context mContext;
    ArrayList<PractitionerSample> data;
    PatientSample patient;

    public UserAdapter(Context _mContext, PatientSample _patient, ArrayList<PractitionerSample> _data) {
        super();
        mContext = _mContext;
        data = _data;
        patient = _patient;

        Log.d("practitioner list", "got data with size=" + _data.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_chatting_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PractitionerSample receiver = data.get(position);
        holder.username.setText(receiver.getGivenName()+" "+ receiver.getFamilyName());
        holder.userId.setText(receiver.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PatientChatting2.class);
                i.putExtra("receiver", receiver);
                i.putExtra("sender", patient.getPatientid());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView username;
        public TextView userId;

        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;
            username = itemView.findViewById(R.id.name);
            userId = itemView.findViewById(R.id.PractitionerId);
        }
    }

}
