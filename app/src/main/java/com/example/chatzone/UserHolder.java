package com.example.chatzone;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

class UserHolder {


    static class UserViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "UserHolder";
        CircleImageView UserImage;
        TextView UserName;
        TextView lastmessage;
        FirebaseAuth mAuth;
        String currentUser;


        UserViewHolder(@NonNull final View itemView) {
            super(itemView);
            UserImage = itemView.findViewById(R.id.userImage);
            UserName = itemView.findViewById(R.id.userName);
            lastmessage = itemView.findViewById(R.id.lstmsg);


        }


        void ConfigureViewHolder(UserViewHolder userViewHolder, int i, final Helper helper, final String receiveruid) {
            userViewHolder.UserName.setText(helper.getName());
            Glide.with(userViewHolder.itemView.getContext())
                    .load(helper.getImage())
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.man)
                    .into(userViewHolder.UserImage);

//            Picasso.get().load(helper.getImage()).centerCrop().into(userViewHolder.UserImage);



                userViewHolder.lastmessage.setText(helper.getMessage());

                mAuth=FirebaseAuth.getInstance();
                currentUser=mAuth.getCurrentUser().getUid();


                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(!currentUser.equals(receiveruid))
                        {
                            itemView.getContext().startActivity(new Intent(itemView.getContext(), ChatActivity.class)
                                    .putExtra("Id", helper.getId())
                                    .putExtra("Name", helper.getName())
                                    .putExtra("Image", helper.getImage()));

                            Log.d(TAG, "Helper id :" + helper.getId());
                            Log.d(TAG, "Helper Name :" + helper.getName());
                            Log.d(TAG, "Helper Image :" + helper.getImage());
                            Log.d(TAG, "last message :" + helper.getMessage());
                        }

                        else
                        {
                            itemView.getContext().startActivity(new Intent(itemView.getContext(),MyProfile.class)
                            .putExtra("Id",helper.getId())
                            .putExtra("Name",helper.getName())
                            .putExtra("Image",helper.getImage()));

                            Log.d(TAG, "Helper id :" + helper.getId());
                            Log.d(TAG, "Helper Name :" + helper.getName());
                            Log.d(TAG, "Helper Image :" + helper.getImage());
                            Log.d(TAG, "last message :" + helper.getMessage());

                        }



                    }
                });
            }
        }
    }


