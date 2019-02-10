package com.example.GPA_pierrelouim5_Calculator;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Sources stackoverflow.com, developer.android.com and youTube.com

    ViewGroup parentLayout;
    ScrollView mainLayout;
    Button addCourseBtn;
    Button calculateGPABtn;
    TextView GPAResults;
    int counter = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = findViewById(R.id.linearLayout);
        mainLayout = findViewById(R.id.MainLayout);
        addCourseBtn = findViewById(R.id.addCourseBtn);
        calculateGPABtn = findViewById(R.id.CalculateGPABtn);
        GPAResults = findViewById(R.id.GPAResults);

        //add courses when the add course button is clicked
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditTextView();
            }
        });

        //calculate GPA and clear the form
        calculateGPABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calculateGPABtn.getText().toString().equals(getResources().getString(R.string.clearForm))) {
                    clearForm();
                    calculateGPABtn.setText(getResources().getString(R.string.calculateGPA));
                } else {
                    validate();
                }
            }
        });

        //add at least five textViews and editViews
        for (int i = 0; i < 5; i++)
            addEditTextView();
    }

    //Edit textViews
    protected void addEditTextView() {

        View view = LayoutInflater.from(this).inflate(R.layout.course_item, null);

        TextInputLayout textInputLayout = view.findViewById(R.id.gpa_text_input_layout);
        textInputLayout.setId(R.id.gpa_text_input_layout + counter);

        TextInputEditText textInputEditText = view.findViewById(R.id.editFinalGrade);
        textInputEditText.setId(R.id.editFinalGrade + counter);

        TextView textView = view.findViewById(R.id.course);
        textView.setId(R.id.course + counter);

        Spinner spinner = view.findViewById(R.id.spinnerCredits);
        spinner.setId(R.id.spinnerCredits + counter);
        spinner.setSelection(2);

        parentLayout.addView(view);
        counter++;
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    //validate entries
    public void validate() {
        int error = 0;

        for (int x = 5000; x < counter; x++) {
            try {
                TextInputEditText textInputEditText = findViewById(R.id.editFinalGrade + x);
                TextInputLayout textInputLayout = findViewById(R.id.gpa_text_input_layout + x);

                String gradeValidator = textInputEditText.getText().toString();

                if (TextUtils.isEmpty(gradeValidator)) {
                    textInputLayout.setError("Please enter a grade");
                    error++;
                } else if (!isNumeric(gradeValidator)) {
                    textInputLayout.setError("Please enter a number");
                    error++;
                } else {
                    int grade = Integer.parseInt(gradeValidator);
                    if (grade < 0 || grade > 100) {
                        textInputLayout.setError("Please enter a number between 0 and 100");
                        error++;
                    } else {
                        textInputLayout.setError(null);
                    }
                }
            } catch (Exception e) {
                Log.e(getPackageName(), "Exception: " + Log.getStackTraceString(e));
            }
        }

        if (error == 0) {
            calculateGPA();
            calculateGPABtn.setText(getResources().getString(R.string.clearForm));
            addCourseBtn.setVisibility(View.INVISIBLE);
        }
    }

    //delete textViews
    public void deleteView(View view) {
        parentLayout.removeView((View) view.getParent());
    }

    //calculate GPA
    public void calculateGPA() {

        double total = 0;
        int howManyCredits = 0;

        for (int x = 5000; x < counter; x++) {
            try {
                TextInputEditText textInputEditText = findViewById(R.id.editFinalGrade + x);
                Spinner spinner = findViewById(R.id.spinnerCredits + x);

                int grade = Integer.parseInt(textInputEditText.getText().toString());
                char c = spinner.getSelectedItem().toString().charAt(0);
                int credits = (int) c;

                total += grade * credits;
                howManyCredits += credits;

            } catch (Exception e) {
                Log.e(getPackageName(), "Exception: " + Log.getStackTraceString(e));
            }
        }

        double gpa = total / howManyCredits;
        double finalGPA = convertGradetoGPA(gpa);
        String answer = "GPA: " + String.format(Locale.getDefault(), "%.2f", finalGPA);

        GPAResults.setText(answer);

        //Set red, yellow, or green background based on GPA
        if (gpa <= 60)
            mainLayout.setBackgroundColor(ContextCompat.getColor(this.getApplicationContext(), R.color.redBackground));
        else if (gpa < 80)
            mainLayout.setBackgroundColor(ContextCompat.getColor(this.getApplicationContext(), R.color.yellowBackground));
        else
            mainLayout.setBackgroundColor(ContextCompat.getColor(this.getApplicationContext(), R.color.greenBackground));
    }

    //Clear data
    public void clearForm() {
        GPAResults.setText(null);
        for (int x = 5000; x < counter; x++) {
            try {
                TextInputEditText textInputEditText = findViewById(R.id.editFinalGrade + x);
                Spinner spinner = findViewById(R.id.spinnerCredits + x);

                textInputEditText.getText().clear();
                spinner.setSelection(2);
            } catch (Exception e) {
                Log.e(getPackageName(), "Exception: " + Log.getStackTraceString(e));
            }
        }
        mainLayout.setBackgroundColor(ContextCompat.getColor(this.getApplicationContext(), android.R.color.transparent));
        addCourseBtn.setVisibility(View.VISIBLE);
    }

    //converting final grades out of 100 to 4.0 GPA system
    public double convertGradetoGPA(double grade) {

        if (grade > 94)
            return 4.0;
        else if (grade > 89)
            return 3.7;
        else if (grade > 86)
            return 3.33;
        else if (grade > 83)
            return 3.0;
        else if (grade > 79)
            return 2.7;
        else if (grade > 76)
            return 2.3;
        else if (grade > 73)
            return 2.0;
        else if (grade > 69)
            return 1.7;
        else if (grade > 66)
            return 1.3;
        else if (grade > 63)
            return 1.0;
        else if (grade > 59)
            return 0.7;
        else
            return 0.0;
    }
}