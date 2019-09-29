package com.google.firebase.samples.apps.mlkit.java;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.samples.apps.mlkit.R;

import java.util.ArrayList;
import java.util.List;

public class ReportDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_display);
        Bundle bundle = getIntent().getExtras();
        ArrayList<DataEntry.Info> lstObj = (ArrayList<DataEntry.Info>) bundle.getSerializable("transactions");
        System.out.println(lstObj);
//        ReportDisplayAdapter adapter = new ReportDisplayAdapter(this, lstObj);
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.tablereport);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(100,100,100,100);
        tableLayout.setLayoutParams(params);
        TableRow header= new TableRow(ReportDisplay.this);
        TextView dateHeader = new TextView(ReportDisplay.this);
        dateHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
        dateHeader.setText("DATE");
        dateHeader.setTextSize(20);
        dateHeader.setBackgroundColor(Color.parseColor("#9E9797"));
        header.addView(dateHeader);
        TextView amountHeader = new TextView(ReportDisplay.this);
        amountHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
        amountHeader.setText("AMOUNT");
        amountHeader.setTextSize(20);
        amountHeader.setBackgroundColor(Color.parseColor("#9E9797"));
        header.addView(amountHeader);
        TextView billNoHeader = new TextView(ReportDisplay.this);
        billNoHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
        billNoHeader.setText("BILL.NO");
        billNoHeader.setTextSize(20);
        billNoHeader.setBackgroundColor(Color.parseColor("#9E9797"));
        header.addView(billNoHeader);
        tableLayout.addView(header);
        for (int i = 0; i < lstObj.size(); i++) {
            TableRow row= new TableRow(ReportDisplay.this);
            TextView date = new TextView(ReportDisplay.this);
            date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            date.setText(lstObj.get(i).date);
            date.setTextSize(20);
            row.addView(date);
            TextView amount = new TextView(ReportDisplay.this);
            amount.setText(lstObj.get(i).amount);
            amount.setTextSize(20);
            amount.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            row.addView(amount);
            TextView billNo = new TextView(ReportDisplay.this);
            billNo.setText(lstObj.get(i).billNo);
            billNo.setTextSize(20);
            billNo.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            row.addView(billNo);
            tableLayout.addView(row);
        }
        TableRow end= new TableRow(ReportDisplay.this);
        Button button = new Button(ReportDisplay.this);
        button.setText("Return");
        end.addView(button);
        tableLayout.addView(end);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.removeAllViews();
                Intent myIntent = new Intent(ReportDisplay.this, StillImageActivity.class);
                ReportDisplay.this.startActivity(myIntent);
            }
        });



    }
}
