package com.example.se328projectalhobayb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SQLITEActivity extends AppCompatActivity {


    EditText inp_fname;
    EditText inp_lname;
    EditText inp_phone;
    EditText inp_email;
    EditText inp_uid;

    Button bttn_insert;
    Button bttn_update;
    Button bttn_delete;
    Button bttn_insert_fire;
    Button bttn_select_options;

    FirebaseDatabase database;
    DatabaseReference ref;

    private void setup() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_q_l_i_t_e);
        setup();
        DBHelper dbHelper = new DBHelper(this);

        inp_fname = findViewById(R.id.inp_sql_fname);
        inp_lname = findViewById(R.id.inp_sql_lname);
        inp_phone = findViewById(R.id.inp_sql_phone);
        inp_email = findViewById(R.id.inp_sql_email);
        inp_uid = findViewById(R.id.inp_sql_uid);

        bttn_insert = findViewById(R.id.bttn_sql_insert);
        bttn_update = findViewById(R.id.bttn_sql_update);
        bttn_delete = findViewById(R.id.btn_sql_delete);
        bttn_select_options = findViewById(R.id.btn_sql_select_options);
        bttn_insert_fire = findViewById(R.id.bttn_sql_fire);


        bttn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = inp_fname.getText() + "";
                String lname = inp_lname.getText() + "";
                String phone = inp_phone.getText() + "";
                String email = inp_email.getText() + "";
                String uid = inp_uid.getText() + "";

                if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty() || uid.isEmpty()) {

                    Toast.makeText(SQLITEActivity.this, "All field required.", Toast.LENGTH_SHORT).show();

                }

                int x = dbHelper.insert(fname, lname, phone, email, Integer.valueOf(uid));

                if (x != -1) {
                    Toast.makeText(SQLITEActivity.this, "Record inserted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SQLITEActivity.this, "Error inserting .", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bttn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = inp_fname.getText() + "";
                String lname = inp_lname.getText() + "";
                String phone = inp_phone.getText() + "";
                String email = inp_email.getText() + "";
                String uid = inp_uid.getText() + "";

                if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty() || uid.isEmpty()) {

                    Toast.makeText(SQLITEActivity.this, "All field required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int y = dbHelper.updateUser(fname, lname, phone, email, Integer.valueOf(uid));
                if (y > 0) {
                    Toast.makeText(SQLITEActivity.this, "Record updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SQLITEActivity.this, "Error updating", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bttn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = inp_uid.getText() + "";

                if (uid.isEmpty()) {
                    Toast.makeText(SQLITEActivity.this, "University ID field required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int z = dbHelper.deleteByUID(Integer.valueOf(uid));

                if (z > 0) {
                    Toast.makeText(SQLITEActivity.this, "Record deleted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SQLITEActivity.this, "Error in deletion", Toast.LENGTH_SHORT).show();

                }
            }
        });

        bttn_insert_fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = inp_uid.getText() + "";

                if (uid.isEmpty()) {
                    Toast.makeText(SQLITEActivity.this, "University ID field required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childData : snapshot.getChildren()) {
                            if (childData.child("userId").getValue(Integer.class) == Integer.valueOf(uid)) {
                                User u = snapshot.child(childData.getKey()).getValue(User.class);
                                int k = dbHelper.insert(u);

                                if (k != -1) {
                                    Toast.makeText(SQLITEActivity.this, "Record inserted successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SQLITEActivity.this, "Error inserting.", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                        }
                        Toast.makeText(SQLITEActivity.this, "No such University ID found.", Toast.LENGTH_SHORT).show();
                        return;

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



        bttn_select_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SQLITEActivity.this, SQLSelect.class));
            }
        });



    }


}