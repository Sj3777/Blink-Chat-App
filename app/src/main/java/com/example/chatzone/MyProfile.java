package com.example.chatzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    TextView myName;
    CircleImageView myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        myImage=findViewById(R.id.myImage);
        myName=findViewById(R.id.myName);

        String userId=getIntent().getStringExtra("Id");

        mAuth=FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("ChatZone").child("Users").child(userId);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Helper helper=  dataSnapshot.getValue(Helper.class);
                    myName.setText(helper.getName());
                    /*Glide
                            .with(getApplicationContext())
                            .load(helper.getImage())
                            .centerCrop()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            //.placeholder(R.drawable.gif_spinner_move_forward)
                            .into(myImage);*/

                    Picasso.get().load(helper.getImage()).placeholder(R.drawable.man).into(myImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
