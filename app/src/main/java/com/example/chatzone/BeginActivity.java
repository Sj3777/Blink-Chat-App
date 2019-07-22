package com.example.chatzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BeginActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Helper> helpers;

    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public static String Currentuser;

    FirebaseRecyclerOptions<Helper> options;
    FirebaseRecyclerAdapter<Helper, UserHolder.UserViewHolder> adapter;
    Toolbar toolbar;

    String  userId;

    Helper helper;

    private static final String TAG="BeginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        recyclerView = findViewById(R.id.recyclerView);
        helpers = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        Currentuser=currentUser.getUid();


        userId= getIntent().getStringExtra("UserId");
        Helper helper =new Helper();
        helper.setId(userId);
        Log.d(TAG,"BeginActivity : helper Id" +userId);

        mRef=FirebaseDatabase.getInstance().getReference("ChatZone").child("Users");

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_list);


        one2onceChatMode();


    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            one2onceChatMode();

        }

    }

    private void one2onceChatMode() {

        {
            mRef = FirebaseDatabase.getInstance().getReference("ChatZone").child("Users");
            mRef.keepSynced(true);
            Query query = mRef.orderByChild("Name");
            options = new FirebaseRecyclerOptions.Builder<Helper>().setQuery(query, Helper.class).build();

            adapter = new FirebaseRecyclerAdapter<Helper, UserHolder.UserViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull UserHolder.UserViewHolder userViewHolder, int i, @NonNull Helper helper) {
                    String receiveruid=getRef(i).getKey();
                    userViewHolder.ConfigureViewHolder(userViewHolder,i, helper,receiveruid);
                }
                @NonNull
                @Override
                public UserHolder.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view;
                    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                    view = layoutInflater.inflate(R.layout.userlayout, parent, false);
                    return new UserHolder.UserViewHolder(view);
                }
            };

        }

        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.logout) {
            mAuth.signOut();
            sendtoLoginActivity();

        }


        return true;
    }

    private void sendtoLoginActivity() {
        Intent intent=new Intent(BeginActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
