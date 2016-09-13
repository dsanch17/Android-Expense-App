package com.itcs4180.hw2_expenseapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddExpenseActivity extends AppCompatActivity {

    //static EditText to be set by the datePicker dialog

    public static EditText dateOutput;
    ImageButton receiptImage;
    Uri imageUri;
    EditText nameBox;
    EditText amountBox;
    Spinner categoryBox;
    public static Date expenseDate;

    AlertDialog.Builder alertBuilder;
    AlertDialog alert;

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
        setContentView(R.layout.activity_add_expense);

        nameBox = (EditText) findViewById(R.id.inputName);
        amountBox = (EditText) findViewById(R.id.inputAmount);
        categoryBox = (Spinner) findViewById(R.id.categoryPicker);
        receiptImage = (ImageButton) findViewById(R.id.receiptImage);
        dateOutput = (EditText) findViewById(R.id.dateOutput);

        //create a calender object so that we can initialize the text field to the current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        expenseDate = new GregorianCalendar(year, month, day).getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(MainActivity.DATE_FORMAT);
        dateOutput.setText(dateFormatter.format(expenseDate));

        imageUri = Uri.parse("android.resource://@android:drawable/ic_menu_gallery");


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
        } /**

        Intent intent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else{
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }

        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");

      //  Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, MainActivity.REQUEST_IMAGE_GET);
        /**
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
            dialog.dismiss();
            return;
        }
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Code to handle the result from the Receipt Image picker request
        if (requestCode == MainActivity.REQUEST_IMAGE_GET && resultCode == RESULT_OK) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                Uri fullPhotoUri = data.getData();
                receiptImage.setImageURI(fullPhotoUri);
                imageUri = fullPhotoUri;
            } else {
                Uri fullPhotoUri = data.getData();
                //  Do work with photo saved at fullPhotoUri
                receiptImage.setImageURI(fullPhotoUri);
                imageUri = fullPhotoUri;
            }

            /**
            File tempFile = new File(this.getFilesDir().getAbsolutePath(), "temp_image");

            //Copy Uri contents into temp File.
            try {
                tempFile.createNewFile();
                copyAndClose(this.getContentResolver().openInputStream(data.getData()),new FileOutputStream(tempFile));

                this.getContentResolver().openInputStream(data.getData())
            } catch (IOException e) {
                //Log Error
            }

            //Now fetch the new URI
            Uri newUri = Uri.fromFile(tempFile);
             */
        }
    }


    //called from the onclick() of the Add Expense button and used to finish return to the main activity
    public void finishAddExpense (View v) {
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
                Log.d("test", "before exiting Add. name is:  " + newExpense.name);
                returnData.putExtra(MainActivity.KEY_EXPENSE, newExpense);
                this.setResult(RESULT_OK, returnData);
                finish();
            }

        }
    }


}
