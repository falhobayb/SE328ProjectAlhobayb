package com.example.se328projectalhobayb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireBaseActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref;

    EditText inp_fname;
    EditText inp_lname;
    EditText inp_phone;
    EditText inp_email;
    EditText inp_uid;

    Button btn_insert;
    Button btn_delete;
    Button btn_update;
    Button btn_select;

    RequestQueue rq;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

        rq= Volley.newRequestQueue(this);
        rq.add(Helper.weather(this));

        btn_insert=findViewById(R.id.bttn_insert);
        btn_delete=findViewById(R.id.bttn_delete);
        btn_select=findViewById(R.id.bttnx_select);
        btn_update=findViewById(R.id.bttnx_update);

//        inp_email=findViewById(R.id.xemail);
//        inp_uid=findViewById(R.id.xuid);
//        btn_select=findViewById(R.id.xselect);
//        btn_update=findViewById(R.id.xupdate);

        inp_fname=findViewById(R.id.inp_fire_fname);
        inp_lname=findViewById(R.id.inp_fire_lname);
        inp_phone=findViewById(R.id.inp_fire_phone);
        inp_email=findViewById(R.id.inp_fire_email);
        inp_uid=findViewById(R.id.inp_fire_uid);

        user = new User();

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FireBaseActivity.this,FirebaseList.class));
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid=inp_uid.getText()+"";
                if (uid.isEmpty()){
                    Toast.makeText(FireBaseActivity.this,"University ID field is required.",Toast.LENGTH_SHORT).show();
                    return;
                }

                delete(Integer.valueOf(uid));
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=inp_fname.getText()+"";
                String lname=inp_lname.getText()+"";
                String phone=inp_phone.getText()+"";
                String email=inp_email.getText()+"";
                String uid=inp_uid.getText()+"";

                if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || email.isEmpty() || uid.isEmpty()){
                    Toast.makeText(FireBaseActivity.this,"All fields are required.",Toast.LENGTH_SHORT).show();
                    return; }

                insertUser(fname,email,lname,phone,Integer.valueOf(uid));

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=inp_fname.getText()+"";
                String lname=inp_lname.getText()+"";
                String phone=inp_phone.getText()+"";
                String email=inp_email.getText()+"";
                String uid=inp_uid.getText()+"";

                HashMap<String,Object> myMap = new HashMap<>();

                if (uid.isEmpty()){
                    Toast.makeText(FireBaseActivity.this,"University ID field is required.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fname.isEmpty()){
                    myMap.put("firstName",fname);
                }
                if (!lname.isEmpty()){
                    myMap.put("lastName",lname);
                }
                if (!phone.isEmpty()){
                    myMap.put("phoneNumber",phone);
                }
                if (!email.isEmpty()){
                    myMap.put("emailAddress",email);
                }
                if (!uid.isEmpty()){
                    myMap.put("userId",Integer.valueOf(uid));
                }
                update(Integer.valueOf(uid),myMap);
            }
        });

    }

    private void insertUser(String fName,String email,String lName,String phone,int userId){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){

                    if (child.child("userId").getValue(Integer.class)==userId){

                        Toast.makeText(FireBaseActivity.this,"A record with UID "+userId+" was found.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                int counterx = (int)snapshot.getChildrenCount();

                while(snapshot.hasChild(counterx+"")){
                    counterx++;
                }

                DatabaseReference insertRef = ref.child(counterx+"");

                insertRef.child("emailAddress").setValue(email);
                insertRef.child("firstName").setValue(fName);
                insertRef.child("lastName").setValue(lName);
                insertRef.child("phoneNumber").setValue(phone);
                insertRef.child("userId").setValue(userId);
                Toast.makeText(FireBaseActivity.this,"Data inserted successfully.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateUserAllDetails(String email,String fName,String lName,String phone,int userId){

        Map<String,Object> stringObjMap = new HashMap<>();
        stringObjMap.put("firstName",fName);
        stringObjMap.put("lastName",lName);
        stringObjMap.put("emailAddress",email);
        stringObjMap.put("phoneNumber",phone);
        stringObjMap.put("userId",userId);
        update(userId,stringObjMap);
    }


    private void update(int uid,Map<String,Object> keyValMap){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==uid){

                        ref.child(child.getKey()).updateChildren(keyValMap).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FireBaseActivity.this,"Record updated successfully.",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FireBaseActivity.this,"Error Detected: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }
                Toast.makeText(FireBaseActivity.this,"No such record found.",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void delete(int uid){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==uid){
                        ref.child(child.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FireBaseActivity.this,"Record deleted successfully.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FireBaseActivity.this,"Error Detected: "+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                        return;
                    }
                }
                Toast.makeText(FireBaseActivity.this,"No such record found.",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}

@IgnoreExtraProperties
class User{


    int userId;
    String emailAddress;
    String phoneNumber;
    String firstName;
    String lastName;

    public User(){
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}