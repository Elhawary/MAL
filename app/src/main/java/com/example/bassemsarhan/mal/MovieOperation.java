package com.example.bassemsarhan.mal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bassem Sarhan on 4/15/2016.
 */
public class MovieOperation {
    DataBaseHandler dataBaseHandler ;
    SQLiteDatabase db ;
    Context context;
   public MovieOperation(Context c){
       dataBaseHandler = new DataBaseHandler(c);

   }
    public void open() throws SQLException {

        db = dataBaseHandler.getWritableDatabase();
    }
    public boolean addMovie(MovieAttributes movieAttributes) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.Movie_Date, movieAttributes.date);
        values.put(DataBaseHandler.Movie_Poster, movieAttributes.poster);
        values.put(DataBaseHandler.Movie_Disc, movieAttributes.discraption);
        values.put(DataBaseHandler.Movie_Rate, movieAttributes.rate);
        values.put(DataBaseHandler.Movie_Title, movieAttributes.title);
        values.put(DataBaseHandler.Movie_ID, movieAttributes.id);
        List<MovieAttributes> checkExsit = getAllMovies();
        boolean check = true;
        for(int i = 0 ; i< checkExsit.size() ; i++){
            if(checkExsit.get(i).getId().equals(movieAttributes.getId())){
                   check = false;
                   break;

            }
        }
        if(check == true){
            db.insert(DataBaseHandler.TABLE_Name, null, values);
            db.close(); // Closing database connection
            return  true;
        }else {

           return false;
        }

    }

    public List<MovieAttributes> getAllMovies() {
     List<MovieAttributes> myFavorite = new ArrayList<MovieAttributes>();
// Select All Query
        String selectQuery = "SELECT * FROM " + DataBaseHandler.TABLE_Name;
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor.getString(0) != null) {
            do {
                MovieAttributes  movies = new MovieAttributes();
                movies.setId(cursor.getString(0));
                movies.setTitle(cursor.getString(1));
                movies.setPoster(cursor.getString(2));
                movies.setDate(cursor.getString(3));
                movies.setDiscraption(cursor.getString(4));
                movies.setRate(cursor.getString(5));

// Adding movie to list
                myFavorite.add(movies);
            } while (cursor.moveToNext());
        }
// return movie list
        return myFavorite;
    }
  // Deleting movie
    public void deleteMovie(MovieAttributes movieAttributes) {
        /*
        db.execSQL("DELETE FROM " + DataBaseHandler.TABLE_Name+ " WHERE "+DataBaseHandler.Movie_ID + "= '" +movieAttributes.getId() + "'" +DataBaseHandler.Movie_Title + "= '" + movieAttributes.getTitle() + "'"
                + DataBaseHandler.Movie_Poster + "= '" + movieAttributes.getPoster() + "'"+DataBaseHandler.Movie_Date+ "= '" + movieAttributes.getDate() + "'"
                + DataBaseHandler.Movie_Disc + "= '" + movieAttributes.getDiscraption() + "'"+DataBaseHandler.Movie_Rate+ "= '" + movieAttributes.getRate()+"'");
        db.close();*/
     db.delete(DataBaseHandler.TABLE_Name,DataBaseHandler.Movie_ID ,null);
        //new String[]{ movieAttributes.getId() , movieAttributes.getTitle() ,movieAttributes.getRate() ,
       // movieAttributes.getDate() ,movieAttributes.getPoster() , movieAttributes.getDiscraption()
         db.close();
    }

}
