package com.rusta.gpa3;

public class Subject {
    String subjectName;
    String spinnerGrade;
    int subjectCredits;
    String subjectKey;
    double cgpa;

    public Subject() {
    }

    public Subject(String subjectName, String spinnerGrade, int subjectCredits, String key) {
        this.subjectName = subjectName;
        this.spinnerGrade = spinnerGrade;
        this.subjectCredits = subjectCredits;
        this.subjectKey = key;
    }


    public String getSpinnerGrade() {
        return spinnerGrade;
    }

    public int getSubjectCredits() {
        return subjectCredits;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectKey() {
        return subjectKey;
    }

    public double getCgpa() {
        if (spinnerGrade.equals("A"))
            cgpa = 4.00;
        else if (spinnerGrade.equals("A-"))
            cgpa = 3.67;
        else if (spinnerGrade.equals("B+"))
            cgpa = 3.33;
        else if (spinnerGrade.equals("B"))
            cgpa = 3.00;
        else if (spinnerGrade.equals("B-"))
            cgpa = 2.67;
        else if (spinnerGrade.equals("C+"))
            cgpa = 2.33;
        else if (spinnerGrade.equals("C"))
            cgpa = 2.00;
        else if (spinnerGrade.equals("C-"))
            cgpa = 1.67;
        else if (spinnerGrade.equals("D+"))
            cgpa = 1.33;
        else if (spinnerGrade.equals("D"))
            cgpa = 1.00;
        else if (spinnerGrade.equals("F"))
            cgpa = 0.00;
        return cgpa;
    }
}

