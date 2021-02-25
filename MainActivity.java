package com.rusta.gpa3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.rustam.gpa3.R;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Button btn_add;
    Button btn_show;
    private FirebaseAuth mAuth;
    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference databaseSubjects = FirebaseDatabase.getInstance().getReference("users").child(userID);
    DatabaseReference database_one_subject;
    List<Subject> subjectList;
    private final int list_of_act = 1;
    Subject subject;
    TextView textView_gpa;
    int credits;
    double unitgpa;
    int position1;
    Toolbar toolbar;


    @Override
    protected void onStart() {
        super.onStart();
        databaseSubjects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjectList.clear();

                for (DataSnapshot subjectSnapShot : dataSnapshot.getChildren()) {
                    subject = subjectSnapShot.getValue(Subject.class);
                    subjectList.add(subject);
                    position1 = subjectList.size();
                }

                SubjectList adapter = new SubjectList(MainActivity.this, subjectList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        btn_add = findViewById(R.id.btn_add);
        btn_show = findViewById(R.id.btn_showgpa);
        subjectList = new ArrayList<>();
        textView_gpa = findViewById(R.id.textview_gpa);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("GPA Calculator");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_gpa.setText("" + calculateGPA());
            }
        });

        //Dialog: Редактировать, удалить
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                subject = subjectList.get(position);
                Log.d("Mytag ", String.valueOf(subject.getSubjectCredits() + " --- " + position1 + "; GPA: " + position));
                showDialog(list_of_act);
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //Toolbar Menu "Log out"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        return true;
    }

    public double calculateGPA() {
        for (int position = 0; position < subjectList.size(); position++) {
            Subject subject = subjectList.get(position);
            unitgpa += subject.getCgpa()*subject.getSubjectCredits();
            credits += subject.getSubjectCredits();
        }
        double gpa = Math.round((unitgpa/credits)*100.0)/100.0;
        return gpa;
    }


    @Override
    protected Dialog onCreateDialog(int id){
        String[] acts = {"Edit", "Delete"};
        switch (id){
            case list_of_act:
                AlertDialog.Builder builder = new AlertDialog.Builder(this); // ??
                builder.setTitle("Choose option");
                builder.setItems(acts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                                intent.putExtra("name", subject.getSubjectName());
                                intent.putExtra("grade", String.valueOf(subject.getSpinnerGrade()));
                                intent.putExtra("credits", String.valueOf(subject.getSubjectCredits()));
                                intent.putExtra("key", subject.getSubjectKey());
                                startActivity(intent);
                                break;
                            case 1:
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Do you want to delete a subject?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteSubject();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                                break;
                        }
                    }
                });
                builder.setCancelable(true);
                return builder.create();
            default:
                return null;
        }
    }

    public void deleteSubject() {
        database_one_subject = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child(String.valueOf(subject.getSubjectKey()));
        database_one_subject.setValue(null);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}

