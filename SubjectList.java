package com.rusta.gpa3;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.rustam.gpa3.R;


import java.util.List;

public class SubjectList extends ArrayAdapter<Subject> {
    private Activity context;
    public List<Subject> subjectList;
    TextView textViewName;
    TextView textViewID;
    TextView textViewGrade;
    TextView textViewCredits;


    public SubjectList(Activity context, List<Subject> subjectList) {
        super(context, R.layout.subjectlist_layout, subjectList);
        this.context = context;
        this.subjectList = subjectList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listViewItem = layoutInflater.inflate(R.layout.subjectlist_layout, null, true);
        textViewName = listViewItem.findViewById(R.id.textview_subjectname);
        textViewGrade = listViewItem.findViewById(R.id.textview_subjectgrade);
        textViewCredits = listViewItem.findViewById(R.id.textview_subjectcredits);
        Subject subject = subjectList.get(position);
        textViewName.setText(subject.getSubjectName());
        textViewGrade.setText(String.valueOf(subject.getSpinnerGrade()));
        textViewCredits.setText(String.valueOf(subject.getSubjectCredits()));
        return listViewItem;
    }
}
