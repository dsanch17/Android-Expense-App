package com.itcs4180.hw2_expenseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public static final int CODE_ADD = 101;
    public static final int CODE_EDIT = 102;
    public static final int CODE_DELETE = 103;

    public static final int PERM_EXT_STORAGE = 4001;

    public static final int REQUEST_IMAGE_GET = 1;


    public static final String KEY_EXPENSE = "This is an expense";
    public static final String KEY_EXPENSE_ARRAY = "This is an expense array";
    public static final String KEY_EXPENSE_CHANGED_INDEX = "expense at index was edited or deleted";


    public static final String DATE_FORMAT = "yyyy-MM-dd";


    ArrayList<Expense> expenseList = new ArrayList<>();
    Button buttonEdit;
    Button buttonDelete;
    Button buttonShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonEdit = (Button) findViewById(R.id.buttonEditExpense);
        buttonDelete = (Button) findViewById(R.id.buttonDeleteExpense);
        buttonShow = (Button) findViewById(R.id.buttonShowExpenses);

        if (expenseList.size() == 0) {
            enableButtons(false);
        }


    }


    public void addButton (View view) {
        Log.d("test", "add button method");

        Intent launchAddActivity = new Intent(MainActivity.this, AddExpenseActivity.class);

        startActivityForResult(launchAddActivity, CODE_ADD);
    }

    public void editButton (View view) {
        Log.d("test", "edit button method");

        Intent launchEditActivity = new Intent(MainActivity.this, EditExpenseActivity.class);
        launchEditActivity.putParcelableArrayListExtra(KEY_EXPENSE_ARRAY, expenseList);

        startActivityForResult(launchEditActivity, CODE_EDIT);
    }

    public void deleteButton (View view) {
        Log.d("test", "delete button method");

        Intent launchDeleteActivity = new Intent(MainActivity.this, DeleteExpenseActivity.class);
        launchDeleteActivity.putParcelableArrayListExtra(KEY_EXPENSE_ARRAY, expenseList);

        startActivityForResult(launchDeleteActivity, CODE_DELETE);
    }

    public void showButton (View view) {
        Log.d("test", "show button method");

        Intent launchShowActivity = new Intent(MainActivity.this, ShowExpenseActivity.class);
        launchShowActivity.putParcelableArrayListExtra(KEY_EXPENSE_ARRAY, expenseList);

        startActivity(launchShowActivity);
    }



    public void finishButton (View view) {
        //finish
        finish();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //on receiving a new expense add it to the expenseList
        if (requestCode == CODE_ADD && resultCode == RESULT_OK) {
            Expense newExpense = (Expense) data.getExtras().getParcelable(KEY_EXPENSE);
            expenseList.add(newExpense);
            Log.d("test", "first item name: " + expenseList.get(0).name);


            Collections.sort(expenseList);
            enableButtons(true);
        }

        if (requestCode == CODE_EDIT && resultCode == RESULT_OK) {
            //Get Expense and the index inside of which was changed
            Expense editedExpense = (Expense) data.getExtras().getParcelable(KEY_EXPENSE);
            int editedIndex = data.getExtras().getInt(KEY_EXPENSE_CHANGED_INDEX);

            //remove the expense at that index, add the new (altered) expense, then sort the list again
            expenseList.remove(editedIndex);
            expenseList.add(editedExpense);

            Collections.sort(expenseList);
        }
        
        if (requestCode == CODE_DELETE && resultCode == RESULT_OK) {
            int editedIndex = data.getExtras().getInt(KEY_EXPENSE_CHANGED_INDEX);
            expenseList.remove(editedIndex);
            if (expenseList.size() == 0)
                enableButtons(false);
        }


    }

    void enableButtons(boolean bool) {
        buttonEdit.setEnabled(bool);
        buttonShow.setEnabled(bool);
        buttonDelete.setEnabled(bool);
    }


}
