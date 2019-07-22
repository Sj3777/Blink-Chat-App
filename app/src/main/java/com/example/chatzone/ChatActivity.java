package com.example.chatzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    CircleImageView chatImage;
    TextView chatName;
    RecyclerView recyclerView;
    ImageView sendMessage;
    EditText textMessage;


    Toolbar toolbar;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    String userId;

    List<Helper> userlist;
    Helper userdata=new Helper();

    UserHolder userHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView=findViewById(R.id.ChatrecyclerView);
        chatImage=findViewById(R.id.chat_profileImage);
        chatName=findViewById(R.id.chat_username);
        sendMessage=findViewById(R.id.sendImage);
        textMessage=findViewById(R.id.textmessage);


        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();


        getandsetUser();
        OnetoOneChatExecution();

    }

    private void OnetoOneChatExecution() {
        final String userId = getIntent().getStringExtra("Id");
        final String userName = getIntent().getStringExtra("Name");
        final String imgLink = getIntent().getStringExtra("Image");
        Log.d(TAG, "onCreate: Users ID " + userId);
        Log.d(TAG, "onCreate: Users Name " + userName);
        Log.d(TAG, "onCreate: Users Photo " + imgLink);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //For Recycler View and writing message
        final DatabaseReference myChatLink = FirebaseDatabase.getInstance().getReference()
                .child("ChatData").child("UserChat").child(currentUser.getUid())
                .child(userId);

        // Only for writing message
        final DatabaseReference userChatLink = FirebaseDatabase.getInstance().getReference()
                .child("ChatData").child("UserChat").child(userId).child(currentUser.getUid());


        MessageAdapter messageAdapter=new MessageAdapter(getApplicationContext(),myChatLink.child("Conversation"),recyclerView);
        recyclerView.setAdapter(messageAdapter);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=textMessage.getText().toString().trim();
                if (!message.equals(""))
                {
                    sendMessage121(message,myChatLink,userChatLink,userId,userName,imgLink);
                }
            }
        });




    }

    private void sendMessage121(String message, DatabaseReference myChatLink,
                                DatabaseReference userChatLink, String userId, String userName, String imgLink) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().push();
        String key = ref.getKey();
        Map<String, Object> map = new HashMap<>();
        map.put("textMsg", message);
        map.put("key", key);
        map.put("senderId", currentUser.getUid());
        map.put("senderName", currentUser.getDisplayName());
        map.put("timeStamp", ServerValue.TIMESTAMP);
        map.put("userImage",imgLink);
        Log.d(TAG, "sendMessage121: MyChatLink" + myChatLink.toString());
        Log.d(TAG, "sendMessage121: UserChatLink" + userChatLink.toString());
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("userId", userId);
        map1.put("userName", userName);
        map1.put("userImg", imgLink);
        map1.put("lastMsg",message);
        map1.put("msgStamp",ServerValue.TIMESTAMP);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("userId", ProfileImageActivity.Currentuser);
        map2.put("userName", "");
        map2.put("userImg", currentUser.getPhotoUrl());
        map2.put("lastMsg",message);
        map2.put("msgStamp",ServerValue.TIMESTAMP);

        //For Updating User Detail in User Chat Node
        myChatLink.updateChildren(map1);
        userChatLink.updateChildren(map2);

        //For Message Uploading
        assert key != null;
        myChatLink.child("Conversation").child(key).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        textMessage.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });
        userChatLink.child("Conversation").child(key).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // editTextMsg.setText("");
                    }
                });
    }

    private void getandsetUser() {
        userId=getIntent().getStringExtra("Id");
        mRef= FirebaseDatabase.getInstance().getReference()
                .child("ChatZone").child("Users").child(userId);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required info is received
                if (dataSnapshot.exists())
                {
                    Helper helper =dataSnapshot.getValue(Helper.class);
                    String username= helper.getName();
                    String image= helper.getImage();
                    String id= helper.getId();

                    chatName.setText(username);

                    Glide.with(getApplicationContext())
                            .load(image)
                            .centerCrop()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .into(chatImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
