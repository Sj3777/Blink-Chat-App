package com.example.chatzone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileImageActivity extends AppCompatActivity {

    private static final String TAG ="Profile Intent Data" ;
    CircleImageView signupImage;
    Uri ImageUri;
    Button ContinueBegin;
    StorageReference mStore;

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mRef;
    public static String Currentuser;
    public ProgressDialog progressDialog;

    String  currentusername;
    String currentuseremail;
    String currentuserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        signupImage=findViewById(R.id.signup_image);
        ContinueBegin=findViewById(R.id.continueBegin);

        progressDialog = new ProgressDialog(ProfileImageActivity.this);

        mStore= FirebaseStorage.getInstance().getReference("ProfileImages");
        mRef= FirebaseDatabase.getInstance().getReference("ChatZone").child("Users");
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        signupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        ContinueBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileImageActivity.this,BeginActivity.class));
                finish();
            }
        });

        currentusername=getIntent().getStringExtra("Name");
        currentuserId=getIntent().getStringExtra("Id");
        currentuseremail=getIntent().getStringExtra("Email");

    }

    private void openGallery() {
        Intent galleryIntent =new Intent ();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        //startActivityForResult(galleryIntent,GalleryPick);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ImageUri= result.getUri();
                Picasso.get().load(ImageUri).into(signupImage);
                Toast.makeText(this, "Image is Captured", Toast.LENGTH_SHORT).show();
                uploadImage(ImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error=result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void uploadImage(Uri imageUri) {
        if(imageUri!=null)
        {
            final StorageReference filePath=mStore.child(user.getUid()+".jpg");

            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setMax(100);
                    progressDialog.setMessage("Uploading Profile Pic");
                    progressDialog.setTitle("Uploading");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    final Handler handle = new Handler();
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                while (progressDialog.getProgress() <= progressDialog.getMax()) {
                                    Thread.sleep(200);
                                    handle.sendMessage(handle.obtainMessage());
                                    if (progressDialog.getProgress() == progressDialog.getMax()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                Toast.makeText(ProfileImageActivity.this, "Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();

                    HashMap<String ,Object> map=new HashMap<>();
                    map.put("Image",String.valueOf(ImageUri));

                    mRef.child(user.getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProfileImageActivity.this, " Profile updated in Database.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            ContinueBegin.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usernew=mAuth.getCurrentUser();
        Currentuser=usernew.getUid();
        Log.d(TAG,"user id" + user);
        HashMap<String ,Object > hashMap=new HashMap<>();
        hashMap.put("Id",ProfileImageActivity.Currentuser);
        hashMap.put("Name",currentusername);
        hashMap.put("Email",currentuseremail);
        hashMap.put("Image","");

        String id=mRef.push().getKey();


        mRef.child(user.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProfileImageActivity.this, "Data Updated to Firebase", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
