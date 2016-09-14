package com.itcs4180.hw2_expenseapp;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ShowExpenseActivity extends AppCompatActivity {


    TextView nameView;
    TextView categoryView;
    TextView amountView;
    TextView dateView;

    ImageView imageView;

    Button buttonFinish;
    ImageButton buttonFirst;
    ImageButton buttonPrevious;
    ImageButton buttonNext;
    ImageButton buttonLast;
    int currentIndex = 0;

    ArrayList<Expense> expenseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        expenseList = getIntent().getExtras().getParcelableArrayList(MainActivity.KEY_EXPENSE_ARRAY);

        nameView = (TextView) findViewById(R.id.outputName);
        categoryView = (TextView) findViewById(R.id.outputCategory);
        amountView = (TextView) findViewById(R.id.outputAmount);
        dateView = (TextView) findViewById(R.id.outputDate);

        imageView = (ImageView) findViewById(R.id.imageReceipt);

        buttonFinish = (Button) findViewById(R.id.buttonFinish);

        buttonFirst = (ImageButton) findViewById(R.id.buttonFirst);
        buttonNext = (ImageButton) findViewById(R.id.buttonNext);
        buttonPrevious = (ImageButton) findViewById(R.id.buttonPrevious);
        buttonLast = (ImageButton) findViewById(R.id.buttonLast);

        displayExpense(currentIndex);


        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void displayExpense(int index) {
        Expense expense = expenseList.get(index);
        nameView.setText(expense.name);
        categoryView.setText(expense.category);

        String amtText = "$" + (String.format(Locale.ENGLISH, "%.2f", expense.amount));
        amountView.setText(amtText);

        SimpleDateFormat dateFormatter = new SimpleDateFormat(MainActivity.DATE_FORMAT);
        dateView.setText(dateFormatter.format(expense.date));

        //should work but it doesn't because of permissions
        imageView.setImageURI(expense.img);

        /** actually loads the image but crashes only sometimes???
         Drawable receiptImage;
        try {
            InputStream inputStream = this.getContentResolver().openInputStream(expense.img);
            receiptImage = Drawable.createFromStream(inputStream, expense.img.toString());
        } catch (FileNotFoundException e) {
            receiptImage = getResources().getDrawable(R.drawable.gallery_icon);
        }
        imageView.setImageDrawable(receiptImage);
         */


    }

    public void nextButton (View view) {
        if (currentIndex != expenseList.size() - 1) {
            currentIndex++;
            displayExpense(currentIndex);
        }
    }

    public void previousButton (View view) {
        if (currentIndex != 0) {
            currentIndex--;
            displayExpense(currentIndex);
        }
    }

    public void firstButton (View view) {
        currentIndex = 0;
        displayExpense(currentIndex);
    }

    public void lastButton (View view) {
        currentIndex = expenseList.size() - 1;
        displayExpense(currentIndex);
    }




}
