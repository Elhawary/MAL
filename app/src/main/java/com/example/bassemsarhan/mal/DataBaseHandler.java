package com.example.bassemsarhan.mal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

public class DataBaseHandler extends SQLiteOpenHelper {
    Context context;
     public final static String Movie_ID ="id";
    public final static String Movie_Poster = "poster";
    public final static String Movie_Title = "title";
    public final static String Movie_Date = "date";
    public final static  String Movie_Rate = "rate";
    public final static String Movie_Disc = "disc";
    public static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "MovieDataBase3.db";
    // Contacts table name
    public static final String TABLE_Name= "movie";
   // (getApplicationContext() ,titlee ,poteer ,ratee ,datee,"id" ,Disceptio
   String CREATE_Movie_TABLE = "CREATE TABLE " + TABLE_Name + "("
           + Movie_ID + " TEXT PRIMARY KEY ," + Movie_Title + " TEXT ,"
           + Movie_Poster + " TEXT ," + Movie_Date + " TEXT ," + Movie_Disc + " TEXT ," + Movie_Rate + " TEXT"+")";

    public DataBaseHandler(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Movie_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // Drop older table if existed
       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_Name);
        //context.deleteDatabase("MovieDataBase2.db");
       // Creating tables again
        // onCreate(db);
    }
}


