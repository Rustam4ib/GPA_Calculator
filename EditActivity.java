package com.rusta.gpa3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.rustam.gpa3.R;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity {
    EditText editText_editsubjectname;
    Spinner spinner_editsubjectgrade;
    EditText editText_editsubjectcredits;
    Button btn_editsave;
    Button btn_editcancel;
    DatabaseReference databaseSubjects;
    String grade;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userID = mAuth.getCurrentUser().getUid();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btn_editsave = findViewById(R.id.btn_editsave);
        btn_editcancel = findViewById(R.id.btn_editcancel);
        editText_editsubjectname = findViewById(R.id.edittext_editname);
        editText_editsubjectcredits = findViewById(R.id.edittext_editcredit);
        spinner_editsubjectgrade = findViewById(R.id.spinner_editgrade);
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.Grade, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_editsubjectgrade.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spinner_editsubjectgrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] choose = getResources().getStringArray(R.array.Grade);
                grade = choose[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Setting initial values
        String key = "";
        Bundle bundle= getIntent().getExtras();
        if (bundle != null){
            String intentname = String.valueOf(getIntent().getStringExtra("name"));
            String intentgrade = String.valueOf(getIntent().getStringExtra("grade"));
            String intentcredits = String.valueOf(getIntent().getStringExtra("credits"));
            key = bundle.get("key").toString();
            databaseSubjects = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child(key);
            editText_editsubjectname.setText(intentname);
            setSpinText(spinner_editsubjectgrade, intentgrade);
            editText_editsubjectcredits.setText(intentcredits);
        }

        btn_editsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseSubjects.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            dataSnapshot.getRef().child("subjectName").setValue(String.valueOf(editText_editsubjectname.getText().toString()));
                            dataSnapshot.getRef().child("spinnerGrade").setValue(spinner_editsubjectgrade.getSelectedItem().toString());
                            if (TextUtils.isEmpty(editText_editsubjectcredits.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "Enter credits!", Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                dataSnapshot.getRef().child("subjectCredits").setValue(Integer.parseInt(editText_editsubjectcredits.getText().toString()));
                            }
                            dataSnapshot.getRef().child("subjectKey").setValue(getIntent().getStringExtra("key"));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                if (TextUtils.isEmpty(editText_editsubjectname.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter a subject name!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(editText_editsubjectcredits.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter credits!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Subject updated successfully!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_editcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //Log.d("Tag", key);
            }
        });
    }

    //Set text for Spinner
    public void setSpinText(Spinner spin, String text){
        for(int i = 0; i < spin.getAdapter().getCount(); i++){
            if(spin.getAdapter().getItem(i).toString().contains(text)){
                spin.setSelection(i);
            }
        }
    }
    @Override
    public void onBackPressed() {
        return;
    }
}


