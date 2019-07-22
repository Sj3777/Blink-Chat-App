package com.example.chatzone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    List<Helper> messageList=new ArrayList<>();

    public MessageAdapter(Context applicationContext, final DatabaseReference conversationRef, final RecyclerView recyclerView) {
        conversationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists())
                {
                    conversationRef.keepSynced(true);
                    Helper helper=dataSnapshot.getValue(Helper.class);
                    messageList.add(helper);
                    notifyItemInserted(messageList.size()-1);
                    recyclerView.scrollToPosition(messageList.size()-1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case 1:
                View view1=layoutInflater.inflate(R.layout.right_message,parent,false);
                viewHolder=new Right_chat(view1);
                break;
            case 2:
                View view=layoutInflater.inflate(R.layout.left_message,parent,false);
                viewHolder=new Left_Chat(view);

                break;
                default:
                    View view3=layoutInflater.inflate(R.layout.right_message,parent,false);
                    viewHolder=new Right_chat(view3);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType())
        {
            case 1:
                Right_chat right_chat=(Right_chat)holder;
                right_chat.configViewHolder(right_chat,messageList,position);
                break;

            case 2:

                Left_Chat left_chat=(Left_Chat)holder;
                left_chat.configViewHolder(left_chat,messageList,position);

            break;
        }

    }

    @Override
    public int getItemCount()
    {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderId().equals(BeginActivity.Currentuser))
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }
}
