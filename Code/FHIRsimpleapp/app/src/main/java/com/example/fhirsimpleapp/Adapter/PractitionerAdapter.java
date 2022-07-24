package com.example.fhirsimpleapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fhirsimpleapp.PatientSample;
import com.example.fhirsimpleapp.PractitionerChatting2;
import com.example.fhirsimpleapp.PractitionerSample;
import com.example.fhirsimpleapp.R;

import java.util.ArrayList;

public class PractitionerAdapter extends RecyclerView.Adapter<PractitionerAdapter.ViewHolder> {

    Context mContext;
    ArrayList<PatientSample> data;
    PractitionerSample practitioner;

    public PractitionerAdapter(Context _mContext, PractitionerSample _practitioner, ArrayList<PatientSample> _data) {
        super();
        mContext = _mContext;
        data = _data;
        practitioner = _practitioner;

        Log.d("patient list", "got data with size=" + _data.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.practitioner_chatting_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PractitionerAdapter.ViewHolder holder, int position) {
        final PatientSample receiver = data.get(position);
        holder.username.setText(receiver.getGivenName()+" "+ receiver.getFamilyName());
        holder.userId.setText(receiver.getPatientid());
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PractitionerChatting2.class);
                i.putExtra("receiver", receiver);
                i.putExtra("sender", practitioner.getId());
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
            username = itemView.findViewById(R.id.Name);
            userId = itemView.findViewById(R.id.PatientId);

        }
    }

}
