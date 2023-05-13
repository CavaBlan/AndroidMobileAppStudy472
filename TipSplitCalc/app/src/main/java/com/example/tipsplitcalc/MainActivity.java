package com.example.tipsplitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private EditText inputBill;
    private TextView tipAns;
    private TextView totalAns;
    private float totalVal;
    private EditText inputPeople;
    private TextView totalPer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputBill = findViewById(R.id.inputBill);
        tipAns = findViewById(R.id.tipAns);
        totalAns = findViewById(R.id.totalAns);
        inputPeople = findViewById(R.id.inputPeople);
        totalPer = findViewById(R.id.totalPer);

    }

    //Calculate the tip and total with tip
    public void setRatio(View v){
        String inputNum = inputBill.getText().toString();       //Input the bill amount.
        if(inputNum.isEmpty()) return;
        float billVal = Float.parseFloat(inputNum);          //Translate the String to float.
        float tipVal;

        if(v.getId() == R.id.radio_12) {                               //15%
            tipVal = billVal / 100 * 12;
            totalVal = billVal + tipVal;
            tipAns.setText(String.format("%.2f", tipVal));
            totalAns.setText(String.format("%.2f", totalVal));
        } else if(v.getId() == R.id.radio_15) {                        //12%
            tipVal = billVal / 100 * 15;
            totalVal = billVal + tipVal;
            tipAns.setText(String.format("%.2f", tipVal));
            totalAns.setText(String.format("%.2f", totalVal));
        } else if(v.getId() == R.id.radio_18) {                        //18%
            tipVal = billVal / 100 * 18;
            totalVal = billVal + tipVal;
            tipAns.setText(String.format("%.2f", tipVal));
            totalAns.setText(String.format("%.2f", totalVal));
        } else if(v.getId() == R.id.radio_20) {                        //20%
            tipVal = billVal / 100 * 20;
            totalVal = billVal + tipVal;
            tipAns.setText(String.format("%.2f", tipVal));
            totalAns.setText(String.format("%.2f", totalVal));
        }

    }

    //Calculate the total per person
    public void perCalc(View v){
        String inputNum = inputPeople.getText().toString();
        if(inputNum.isEmpty()) return;
        float peopleNum = Float.parseFloat(inputNum);
        float perResult = totalVal / peopleNum;          //totalVal: 16 row
        totalPer.setText(String.format("%.2f", perResult));
    }

    //Clear All
    public  void clear(View v){
        inputBill.setText("");
        tipAns.setText("");
        totalAns.setText("");
        inputPeople.setText("");
        totalPer.setText("");
    }
}