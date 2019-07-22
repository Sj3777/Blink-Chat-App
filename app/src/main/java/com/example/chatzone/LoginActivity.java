package com.example.chatzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {


    EditText loginEmail,loginPaswword;
    Button loginButton;

    FirebaseAuth mAuth;
    FirebaseUser currentuser;
    DatabaseReference mRef;
    public static String user;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        loginEmail=findViewById(R.id.login_email);
        loginPaswword=findViewById(R.id.login_password);
        loginButton=findViewById(R.id.login);

        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();
        mRef= FirebaseDatabase.getInstance().getReference("ChatZone").child("Users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=loginEmail.getText().toString();
                String password=loginPaswword.getText().toString();

                loginwithemailandpassword(email,password);
            }
        });



    }

    private void loginwithemailandpassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    //user=currentuser.getUid();
                    Intent intent=new Intent(LoginActivity.this,BeginActivity.class);
                    //intent.putExtra("UserId",user);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginActivity.this, "Log in Successfully" , Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
