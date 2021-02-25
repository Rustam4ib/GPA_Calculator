package com.rusta.gpa3;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {
    EditText editText_name;
    EditText editText_credits;
    Spinner spinner;
    //private FirebaseAuth mAuth;
    //String userID = mAuth.getCurrentUser().getUid();
    Button btn_save;
    Button btn_cancel;
    DatabaseReference databaseSubjects;
    String name;
    String grade;
    int credits;
    String key;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        databaseSubjects = FirebaseDatabase.getInstance().getReference("users");
        btn_cancel = findViewById(R.id.btn_editcancel);
        btn_save = findViewById(R.id.btn_editsave);
        editText_name = findViewById(R.id.edittext_name);
        editText_credits = findViewById(R.id.edittext_credits);
        spinner = findViewById(R.id.spinner_grade);
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.Grade, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] choose = getResources().getStringArray(R.array.Grade);
                grade = choose[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cred = editText_credits.getText().toString();
                String nameofsubj = editText_name.getText().toString();
                if (TextUtils.isEmpty(nameofsubj)) {
                    Toast.makeText(getApplicationContext(), "Enter a subject name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(cred)) {
                    Toast.makeText(getApplicationContext(), "Enter credits!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    addSubject();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private void addSubject(){
        String userID = mAuth.getCurrentUser().getUid();
        name = editText_name.getText().toString();
        credits = Integer.parseInt(editText_credits.getText().toString());
        key = databaseSubjects.push().getKey();
        if (!TextUtils.isEmpty(name)){
            Subject subject = new Subject(name, grade, credits, key);
            databaseSubjects.child(userID).child(key).setValue(subject);// change from key to userID // new child key
            Toast.makeText(this, "Subject is added!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Enter a subject!", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        return;
    }
}
