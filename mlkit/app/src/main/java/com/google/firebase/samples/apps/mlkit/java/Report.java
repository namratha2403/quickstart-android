package com.google.firebase.samples.apps.mlkit.java;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.DatePickerDialog;


import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.samples.apps.mlkit.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

public class Report extends AppCompatActivity {

    static EditText datePicked = null;
    final Calendar myCalendar = Calendar.getInstance();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        final EditText editText1 = findViewById(R.id.reportEditText1);
        final EditText editText2 = findViewById(R.id.reportEditText2);
        final Button getdata = findViewById(R.id.getdata);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicked = editText1;
                // TODO Auto-generated method stub
                new DatePickerDialog(Report.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicked = editText2;
                // TODO Auto-generated method stub
                new DatePickerDialog(Report.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final ArrayList<Object> listOfDates = new ArrayList<>();

        getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("users").child("usr1")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                SimpleDateFormat
                                        sdfo
                                        = new SimpleDateFormat("dd/MM/yyyy");
                               if(dataSnapshot.hasChildren()){
                                   Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                                   while (iter.hasNext()){
                                       DataSnapshot snap = iter.next();
                                       String nodId = snap.getKey();
                                       String date = (String) dataSnapshot.child(nodId).child("date").getValue();
                                       try {
                                           Date d1 = sdfo.parse(date);
                                           Date before = sdfo.parse(editText1.getText().toString());
                                           Date after = sdfo.parse(editText2.getText().toString());
                                           if(d1.after(before) && d1.before(after)){
                                               HashMap<String,String> dataMap =  (HashMap<String,String>)dataSnapshot.child(nodId).getValue();
                                               DataEntry.Info transaction = new DataEntry.Info(dataMap.get("date"),dataMap.get("amount"),dataMap.get("billNo"));
                                               listOfDates.add(transaction);
                                           }
                                       } catch (ParseException e) {
                                           e.printStackTrace();
                                       }

                                   }
                                   System.out.println(listOfDates);
                                   Bundle bundle = new Bundle();
                                   bundle.putSerializable("transactions", listOfDates);
                                   Intent myIntent = new Intent(Report.this, ReportDisplay.class);
                                   myIntent.putExtras(bundle);
                                   Report.this.startActivity(myIntent);
                               }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });


    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datePicked.setText(sdf.format(myCalendar.getTime()));
    }

}
