package com.itcs4180.hw2_expenseapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditExpenseActivity extends AppCompatActivity {

    //static EditText to be set by the datePicker dialog

    public static EditText dateOutput;
    ImageButton receiptImage;
    ImageButton calenderIcon;
    Uri imageUri;
    EditText nameBox;
    EditText amountBox;
    Spinner categoryBox;
    public static Date expenseDate;
    Button buttonSave;
    ArrayList<Expense> expenseList;

    AlertDialog.Builder alertBuilder;
    AlertDialog alert;

    int edittedIndex;


    /** TODO
     * test the switcher more.
     * make it so it doesn't crash if you have no expenses. or maybe disable the button from main
     * delete functionallity
     */
    //datePicker Code *** Declare an innerclass to handle the calender dialog
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            expenseDate = new GregorianCalendar(year, month, day).getTime();
            SimpleDateFormat dateFormatter = new SimpleDateFormat(MainActivity.DATE_FORMAT);
            dateOutput.setText(dateFormatter.format(expenseDate));

        }
    }
    // End DatePicker *********

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        nameBox = (EditText) findViewById(R.id.inputName);
        amountBox = (EditText) findViewById(R.id.inputAmount);
        categoryBox = (Spinner) findViewById(R.id.categoryPicker);
        receiptImage = (ImageButton) findViewById(R.id.receiptImage);
        dateOutput = (EditText) findViewById(R.id.dateOutput);
        calenderIcon = (ImageButton) findViewById(R.id.calenderIcon);
        buttonSave = (Button) findViewById(R.id.buttonDelete);

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

        /** actually loads the image but crashes only sometimes???

         Drawable newImage;
        try {
            InputStream inputStream = this.getContentResolver().openInputStream(exp.img);
            newImage = Drawable.createFromStream(inputStream, exp.img.toString());
        } catch (FileNotFoundException e) {
            newImage = getResources().getDrawable(R.drawable.gallery_icon);
        }

        receiptImage.setImageDrawable(newImage);
         */


        edittedIndex = i;
        activateButtons(true);
    }

    void activateButtons (boolean bool) {
        nameBox.setEnabled(bool);
        amountBox.setEnabled(bool);
        categoryBox.setEnabled(bool);
        receiptImage.setEnabled(bool);
        dateOutput.setEnabled(bool);
        calenderIcon.setEnabled(bool);
        buttonSave.setEnabled(bool);
    }


    //Called by the date ImageButton or by the user tapping on the date text
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    //called by the receipt ImageButton
    public void selectImage(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, MainActivity.REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Code to handle the result from the Receipt Image picker request
        if (requestCode == MainActivity.REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            // Do work with photo saved at fullPhotoUri
            receiptImage.setImageURI(fullPhotoUri);
            imageUri = fullPhotoUri;
        }
    }
    /**update to edit
     *
     *
     *
     *
     * */
    //called from the onclick() of the Add Expense button and used to finish return to the main activity
    public void finishEditExpense(View v) {
        String expenseName = nameBox.getText().toString();
        String category = categoryBox.getSelectedItem().toString();
        int categoryIndex = categoryBox.getSelectedItemPosition();
        String amtString = amountBox.getText().toString();

        //alertBuilder with empty onClick since it is just there to alert the user.
        alertBuilder = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //could code here for OK
                    }
                });

        //check if the input boxes are filled and display an alert box if not.
        if (amtString.length() <= 0) {
            alertBuilder.setMessage("Please enter an amount for the expense.");
            alert = alertBuilder.create();
            alert.show();
        } else {
            if (expenseName.length() <= 0) {
                alertBuilder.setMessage("Please enter an expense name.");
                alert = alertBuilder.create();
                alert.show();
            } else {
                Double expenseAmount = Double.parseDouble(amtString);

                //compile the inputs into a Expense object and use putExtra to send it back to MainActivity
                Expense newExpense = new Expense(expenseName, category, expenseAmount, expenseDate, imageUri, categoryIndex);

                Intent returnData = new Intent();
                returnData.putExtra(MainActivity.KEY_EXPENSE, newExpense);
                returnData.putExtra(MainActivity.KEY_EXPENSE_CHANGED_INDEX, edittedIndex);
                this.setResult(RESULT_OK, returnData);
                finish();
            }

        }
    }

    public void buttonCancel(View v) {
        this.setResult(RESULT_CANCELED);
        finish();
    }
}
