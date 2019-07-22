package com.example.chatzone;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

class Right_chat extends RecyclerView.ViewHolder {

    CircleImageView userimage;
    TextView message;
    Right_chat(@NonNull View itemView) {
        super(itemView);
        message=itemView.findViewById(R.id.msg);
        userimage=itemView.findViewById(R.id.image);
    }

    void configViewHolder(Right_chat right_chat, List<Helper> messageList, int position) {
        message.setText(messageList.get(position).getTextMsg());

        Glide.with(itemView.getContext())
                .load(messageList.get(position).getImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.man)
                .into(userimage);

        //Picasso.get().load(messageList.get(position).getImage()).centerCrop().into(userimage);

        Log.d(TAG,"User Image :" +messageList.get(position).getImage());
    }
}
