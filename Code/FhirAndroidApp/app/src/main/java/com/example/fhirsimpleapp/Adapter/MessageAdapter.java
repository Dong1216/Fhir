package com.example.fhirsimpleapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fhirsimpleapp.ChatSample;
import com.example.fhirsimpleapp.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    Context mContext;
    ArrayList<ChatSample> data;
    String myId;


    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;


    public MessageAdapter(Context _mContext,String _myid, ArrayList<ChatSample> _data) {
        super();
        mContext = _mContext;
        data = _data;
        myId = _myid;
    }

    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(mContext).inflate(R.layout.my_message,parent,false);
            return new MessageAdapter.MyViewHolder(view);
        }

        else if(viewType==MSG_TYPE_LEFT){
            View view= LayoutInflater.from(mContext).inflate(R.layout.their_message,parent,false);
            return new MessageAdapter.MyViewHolder(view);
        }
        else {return null;}

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {

        ChatSample chat= data.get(position);
        holder.show_message.setText(chat.getMessage());
    }



    @Override
    public int getItemCount() {

        return data.size();
    }
    public class MyViewHolder   extends RecyclerView.ViewHolder
    {
        public TextView show_message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.message_body);

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (data.get(position).getSender().equals(myId))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }
}