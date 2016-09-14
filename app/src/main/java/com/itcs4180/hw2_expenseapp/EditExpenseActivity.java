package com.itcs4180.hw2_expenseapp;

/**
 * ITCS 4180 Homework2
 * HW2.zip
 * Dallas Sanchez
 * Patrick King
 */

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

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

    int editedIndex;


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
                alertSelectorBuilder.setCancelable(false).setTitle("Pick an Expense")
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
        amountBox.setText((String.format(Locale.ENGLISH, "%.2f", exp.amount)));
        categoryBox.setSelection(exp.categoryPosition);

        expenseDate = exp.date;

        SimpleDateFormat dateFormatter = new SimpleDateFormat(MainActivity.DATE_FORMAT);
        dateOutput.setText(dateFormatter.format(expenseDate));

        imageUri = exp.img;

        receiptImage.setImageURI(imageUri);

        editedIndex = i;
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

    public void editImageButton(View v) {
        checkPermission();
    }


    //called by the receipt ImageButton
    void selectImage() {
        Intent intent;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        else
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, MainActivity.REQUEST_IMAGE_GET);
        }
    }

    void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MainActivity.PERM_EXT_STORAGE);
        } else
            selectImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MainActivity.PERM_EXT_STORAGE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // external storage related task you need to do.
                    selectImage();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Code to handle the result from the Receipt Image picker request
        if (requestCode == MainActivity.REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            receiptImage.setImageURI(fullPhotoUri);
            imageUri = fullPhotoUri;
        }
    }


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
            } else if (validateNumberString(amtString)) {
                Double expenseAmount = Double.parseDouble(amtString);

                //compile the inputs into a Expense object and use putExtra to send it back to MainActivity
                Expense newExpense = new Expense(expenseName, category, expenseAmount, expenseDate, imageUri, categoryIndex);

                Intent returnData = new Intent();
                returnData.putExtra(MainActivity.KEY_EXPENSE, newExpense);
                returnData.putExtra(MainActivity.KEY_EXPENSE_CHANGED_INDEX, editedIndex);
                this.setResult(RESULT_OK, returnData);
                finish();
            }

        }
    }

    public void buttonCancel(View v) {
        this.setResult(RESULT_CANCELED);
        finish();
    }

    private boolean validateNumberString(String num) {

        if(Pattern.matches("^[0-9]+(\\.[0-9]{1,2})?$", num)) {
            Log.d("test", num);
            return true;
        }
        else {
            alertBuilder.setMessage("Invalid input. Please enter a number with up to 2 decimal places");
            alert = alertBuilder.create();
            alert.show();

            Log.d("test", "Invalid input: " + num);
            return false;
        }
    }
}
