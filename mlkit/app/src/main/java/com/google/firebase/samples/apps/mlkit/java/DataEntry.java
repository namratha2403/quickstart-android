package com.google.firebase.samples.apps.mlkit.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.samples.apps.mlkit.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import static com.google.firebase.samples.apps.mlkit.java.cloudtextrecognition.CloudDocumentTextRecognitionProcessor.date;
import static com.google.firebase.samples.apps.mlkit.java.cloudtextrecognition.CloudDocumentTextRecognitionProcessor.finalAmount;

public class DataEntry extends AppCompatActivity {

    public static class Info implements Serializable {
        String date;
        String amount;
        String billNo;


        public Info(String date, String amount, String billNo){
            this.date = date;
            this.amount = amount;
            this.billNo = billNo;
        }

        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("date", date);
            result.put("amount", amount);
            result.put("billNo", billNo);

            return result;
        }
    }
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_entry);
        Bundle bundle = getIntent().getExtras();
        final TextView dateEntry = findViewById(R.id.EditText04);
        dateEntry.setText(bundle.getString("date"));
        final TextView amountEntry = findViewById(R.id.EditText06);
        amountEntry.setText(bundle.getString("amount"));

        Button returnToCamera  = findViewById(R.id.button2);
        returnToCamera.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.dataentry);
                        linearLayout.removeAllViews();
                        date = "";
                        finalAmount ="";
                        Intent myIntent = new Intent(DataEntry.this, StillImageActivity.class);
                        DataEntry.this.startActivity(myIntent);
                    }
                }
        );

        Button save = findViewById(R.id.button3);
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Info info = new Info(date, amounttext,"8633");
                        Info info = new Info(dateEntry.getText().toString(), amountEntry.getText().toString(),"8633");
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        String key = mDatabase.child("users").child("usr1").push().getKey();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/users/usr1/" +  key, info.toMap());
                        mDatabase.updateChildren(childUpdates);
                        EditText date = (EditText)  findViewById(R.id.EditText04);
                        date.setText("");
                        EditText billNo = (EditText)  findViewById(R.id.EditText05);
                        billNo.setText("");
                        EditText amount = (EditText)  findViewById(R.id.EditText06);
                        amount.setText("");
                    }
                }
        );
    }
}
