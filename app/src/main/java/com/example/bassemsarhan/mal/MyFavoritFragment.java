package com.example.bassemsarhan.mal;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MyFavoritFragment extends Fragment{

    String []posters;
    String []titles;
    String []overviews;
    String []ids;
    String []rates;
    String []dates;
    GridView gridview;
    DetailMovieFragment globleFragment;
    String pos;
    String tit;
    String rat;
    String dat;
    String iiid;
    String overView;
    public  List<MovieAttributes> allMovies;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }
    public  void checkTablet(DetailMovieFragment fr){
        globleFragment = fr;
    }

    public  String[] readData(){

        MovieOperation op = new MovieOperation(getActivity());
        op.open();
        allMovies = op.getAllMovies();
        final String[]result = new String[allMovies.size()];
        for(int i = 0 ; i<allMovies.size() ;i++){
            result[i] = allMovies.get(i).getPoster();

        }
        return result;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.check , container, false);

        final Intent intent = new Intent(getActivity(), DetailActivity.class);
        gridview = (GridView) rootView.findViewById(R.id.grid);
        gridview.setAdapter( new CustomGridAdapter(getActivity() , readData()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( globleFragment == null ) {
                    Toast.makeText(getActivity() , allMovies.get(position).getPoster()  , Toast.LENGTH_LONG ).show();
                    intent.putExtra("poster", allMovies.get(position).getPoster());
                    intent.putExtra("title",  allMovies.get(position).getTitle());
                    intent.putExtra("date", allMovies.get(position).getDate());
                    intent.putExtra("desc", allMovies.get(position).getDiscraption());
                    intent.putExtra("rate", allMovies.get(position).getRate());
                    intent.putExtra("id", allMovies.get(position).getId());


                    startActivity(intent);
                }else{
                    globleFragment.show(allMovies.get(position).getPoster() , allMovies.get(position).getId() , allMovies.get(position).getTitle() ,
                            allMovies.get(position).getDate(),allMovies.get(position).getRate() ,allMovies.get(position).getDiscraption());
                }

            }
        });


        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forcastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


}
