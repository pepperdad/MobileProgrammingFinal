package com.example.mpfinal.ui.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.example.mpfinal.LoginActivity;
import com.example.mpfinal.MainActivity;
import com.example.mpfinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    String name, email, profileUrl;
    Dialog dialog;
    TextView userName, userEmail, tv_point;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color='#000000'>   ??? ?????????</font>"));
        ab.setElevation(0);
        ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.outline_arrow_back_black_24dp);

        Intent intent=getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        profileUrl=intent.getStringExtra("profileUrl");

        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("MadCampWeek4");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("UserProfile");

        FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();

        userName=findViewById(R.id.userName);
        userEmail=findViewById(R.id.userEmail);
        tv_point=findViewById(R.id.tv_point);

        userName.setText(name);
        userEmail.setText(email);

        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    tv_point.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });



        ImageView iv_profileUrl=findViewById(R.id.iv_profileUrl);

        dialog = new Dialog(this);

        if (profileUrl!=null&&!profileUrl.equals("")){
            Glide.with(this).load(profileUrl).into(iv_profileUrl);
        } else{
            // ????????? ?????? ??? ????????????
            String uid=firebaseUser.getUid();
            StorageReference profileRef = storageReference.child(uid);
            Log.d("profileRef ????1", profileRef.toString());

            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri).into(iv_profileUrl);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d("????????????", " ");
                }
            });

        }


        final FloatingActionButton btn_update_pwd = findViewById(R.id.btn_update_pwd);
        final FloatingActionButton btn_update_name=findViewById(R.id.btn_create_group);
        final FloatingActionButton btn_logout = findViewById(R.id.btn_logout);
        final FloatingActionButton btn_signout = findViewById(R.id.btn_signout);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ????????????
                mFirebaseAuth.signOut();
                Toast.makeText(ProfileActivity.this, "???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ????????????
                mFirebaseAuth.getCurrentUser().delete();
                Toast.makeText(ProfileActivity.this, "?????? ??????" , Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName();
            }
        });
        btn_update_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePwd();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent3 = new Intent(ProfileActivity.this, MainActivity.class);
                intent3.putExtra("email", email);
                intent3.putExtra("name", name);
                startActivity(intent3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateName() {
        dialog.setContentView(R.layout.dialog_name_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_update_name = dialog.findViewById(R.id.btn_create_group);
        final EditText et_name = dialog.findViewById(R.id.et_name);
        ImageView iv_closeDialog = dialog.findViewById(R.id.iv_closeDialog);

        btn_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ?????? ?????? ????????? ????????? setValue
                String EtName = et_name.getText().toString();
                FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser(); // ?????? ??????

                userName.setText(EtName);

                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("name").setValue(EtName);
                Toast.makeText(ProfileActivity.this, "?????? ??????!", Toast.LENGTH_SHORT).show();

                Log.d("new Name", "name : " + EtName);
                //ProfileActivity.this.recreate();

                dialog.dismiss();
            }
        });
        iv_closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    private void updatePwd() {
        View view = getLayoutInflater().inflate(R.layout.dialog_pwd_update, null);

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(view).show();

        dialog.setContentView(R.layout.dialog_pwd_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText et_current_pwd=dialog.findViewById(R.id.et_current_pwd);
        final EditText et_new_pwd = dialog.findViewById(R.id.et_new_pwd);
        final EditText et_new_pwd_val = dialog.findViewById(R.id.et_new_pwd_val);
        Button btn_update_pwd = dialog.findViewById(R.id.btn_update_pwd);
        ImageView iv_closeDialog2 = dialog.findViewById(R.id.iv_closeDialog2);

        btn_update_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser(); // ?????? ??????

                //  ?????? ?????? ????????? ????????? setValue
                String EtCurrentPwd=et_current_pwd.getText().toString();
                String EtNewPwd=et_new_pwd.getText().toString();
                String EtNewPwdVal=et_new_pwd_val.getText().toString();
                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("password").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            if (String.valueOf(task.getResult().getValue()).equals(EtCurrentPwd)){
                                if(EtNewPwd.equals(EtNewPwdVal)){
                                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("password").setValue(EtNewPwd);
                                    Toast.makeText(ProfileActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                    Log.d("?????? ?????? ???", EtNewPwd);

                                    //LoginActivity??? ?????? Intent ???????????? ??????, ??? ?????? ??????

                                } else {
                                    Toast.makeText(ProfileActivity.this, "??????????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                                    Log.d("?????? ?????? ??? ???", EtNewPwd+", "+EtNewPwdVal);
                                }

                            } else {
                                Toast.makeText(ProfileActivity.this, "?????? ???????????? ??????!", Toast.LENGTH_SHORT).show();
                                Log.d("?????? ?????? ??????", String.valueOf(task.getResult().getValue()) +", "+EtCurrentPwd);
                            }

                        }
                    }
                });
            }
        });
        iv_closeDialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
