package com.itcs4180.hw2_expenseapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DeleteExpenseActivity extends AppCompatActivity {

    public static EditText dateOutput;
    ImageButton receiptImage;
    ImageButton calenderIcon;
    Uri imageUri;
    EditText nameBox;
    EditText amountBox;
    Spinner categoryBox;
    Button buttonDelete;
    public static Date expenseDate;
    ArrayList<Expense> expenseList;

    AlertDialog alert;

    int edittedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_expense);

        nameBox = (EditText) findViewById(R.id.inputName);
        amountBox = (EditText) findViewById(R.id.inputAmount);
        categoryBox = (Spinner) findViewById(R.id.categoryPicker);
        receiptImage = (ImageButton) findViewById(R.id.receiptImage);
        dateOutput = (EditText) findViewById(R.id.dateOutput);
        calenderIcon = (ImageButton) findViewById(R.id.calenderIcon);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        activateButtons(false);

        expenseList = getIntent().getExtras().getParcelableArrayList(MainActivity.KEY_EXPENSE_ARRAY);

        Log.d("test", "array first element: " + expenseList.get(0).name);

        String[] expenseListNames = new String[expenseList.size()];

        Log.d("test", ""+expenseList.size());
        for (int i = 0; i < expenseList.size(); i++) {
            expenseListNames[i] = expenseList.get(i).name;
            Log.d("test", "adding to array " + expenseList.get(i).name);
        }

        AlertDialog.Builder alertSelectorBuilder = new AlertDialog.Builder(this);
        alertSelectorBuilder.setCancelable(false)
                .setItems(expenseListNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setViewToEditExpense(i);
                    }
                });

        alert = alertSelectorBuilder.create();

        Button expenseSwitcher = (Button) findViewById(R.id.buttonSelectExpense);
        expenseSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {alert.show();}
        });
    }

    void activateButtons (boolean bool) {
        nameBox.setEnabled(bool);
        amountBox.setEnabled(bool);
        categoryBox.setEnabled(bool);
        receiptImage.setEnabled(bool);
        dateOutput.setEnabled(bool);
        calenderIcon.setEnabled(bool);
        buttonDelete.setEnabled(bool);
    }

    void setViewToEditExpense (int i) {
        Expense exp = expenseList.get(i);
        nameBox.setText(exp.name);
        amountBox.setText(Double.toString(exp.amount));
        categoryBox.setSelection(exp.categoryPosition);
        expenseDate = exp.date;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(MainActivity.DATE_FORMAT);
        dateOutput.setText(dateFormatter.format(expenseDate));
        imageUri = exp.img;

        receiptImage.setImageURI(imageUri);

        edittedIndex = i;

        buttonDelete.setEnabled(true);
    }

    public void buttonDelete(View v) {
        Intent returnData = new Intent();
        returnData.putExtra(MainActivity.KEY_EXPENSE_CHANGED_INDEX, edittedIndex);
        this.setResult(RESULT_OK, returnData);
        finish();
    }

    public void buttonCancel(View v) {
        this.setResult(RESULT_CANCELED);
        finish();
    }


}
