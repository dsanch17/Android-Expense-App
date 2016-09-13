package com.itcs4180.hw2_expenseapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/**
 * Created by Dallas on 9/9/16.
 */
public class Expense implements Parcelable, Comparable<Expense>  {
    String name;
    String category;
    Double amount;
    Date date;
    Uri img;
    int categoryPosition;

    public Expense(String name, String category, Double amount, Date date, Uri img, int categoryPosition) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.img = img;
        this.categoryPosition = categoryPosition;
    }

    protected Expense(Parcel in) {
        name = in.readString();
        category = in.readString();
        amount = in.readDouble();
        date = new Date(in.readLong());
        img = in.readParcelable(Uri.class.getClassLoader());
        categoryPosition = in.readInt();
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(category);
        parcel.writeDouble(amount);
        parcel.writeLong(date.getTime());
        parcel.writeParcelable(img, i);
        parcel.writeInt(categoryPosition);
    }

    @Override
    public int compareTo(Expense other) {
        return this.name.compareToIgnoreCase(other.name);
    }
}
