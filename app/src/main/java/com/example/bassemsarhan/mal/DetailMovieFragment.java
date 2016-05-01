package com.example.bassemsarhan.mal;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassemsarhan.mal.CustomGridAdapter;
import com.example.bassemsarhan.mal.R;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class DetailMovieFragment  extends Fragment{

    public  String keys[];
    public String names[];
    public String types[];
    public String moveID;
    public TextView title;
    public  TextView dates;
    public TextView Descriptions;
    public ImageView img ;
    public Button fav;
    public TextView rates ;
    ListView listViewTrailar;
    ListView listViewReviews;
    String reviewsID[];
    String author[];
    String conten[];
    String URLValue;
    Button Buttonreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void updateMovies(){

        String trailar = "http://api.themoviedb.org/3/movie/" + moveID + "/videos?";

        final String reviews ="http://api.themoviedb.org/3/movie/"+moveID+"/reviews?";

       FetchMovieTask movieTas= new FetchMovieTask( trailar  , 1);
        try {
            movieTas.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void show(String pos ,String id , String tit ,String dat , String rat , String overView) {
        Toast.makeText(getActivity() , pos , Toast.LENGTH_LONG).show();
        title.setText(tit);
        String URL =  "https://image.tmdb.org/t/p/original"+pos;
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        width = width/2;
        height = height/2;
        Picasso.with(getActivity()).load(URL).noFade().resize(width, height).into(img);
        dates.setText(dat );
        rates.setText(rat );
        Descriptions.setText(overView );

        moveID = id;
        final MovieAttributes ob = new MovieAttributes();
        ob.date = dat;
        ob.poster = pos;
        ob.title = tit;
        ob.discraption = overView;
        ob.id = id;
        ob.rate = rat;
        final  MovieOperation movieOperation = new MovieOperation(getActivity());
        movieOperation.open();
        boolean check = true;

        List<MovieAttributes> myFavorite = movieOperation.getAllMovies();
        for(int i = 0 ; i<myFavorite.size() ;i++){
            if(id.equals(myFavorite.get(i).getId())){
                fav.setBackgroundColor(Color.YELLOW);
                fav.setText("Remove Movie");
                check = false;
                break;
            }

        }
        final boolean finalCheck = check;
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalCheck == true){
                    movieOperation.addMovie(ob);
                }else {
                    movieOperation.deleteMovie(ob);
                }

            }
        });
        updateMovies();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.check, container, false);
         title = (TextView)rootView.findViewById(R.id.title);
          listViewTrailar = (ListView)rootView.findViewById(R.id.trailarList);
          listViewReviews = (ListView)rootView.findViewById(R.id.reviews);
          dates =(TextView)rootView.findViewById(R.id.date);
          Descriptions =(TextView)rootView.findViewById(R.id.description);
          img = (ImageView)rootView.findViewById(R.id.imageView);
          fav = (Button)rootView.findViewById(R.id.Favorite);
          rates = (TextView)rootView.findViewById(R.id.rate);
          Buttonreview = (Button)rootView.findViewById(R.id.review);

        Buttonreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FetchMovieTask movieTask = new FetchMovieTask( "http://api.themoviedb.org/3/movie/"+moveID+"/reviews?"  , 2);
                movieTask.execute("");
            }
        });
        Toast.makeText(getActivity() , title.getText().toString() + "bass" , Toast.LENGTH_LONG).show();
        return  rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[]> {
        int ch;

        FetchMovieTask(String urlValue, int checkaysnc) {
            URLValue = urlValue;
            ch = checkaysnc;
        }

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        private ProgressDialog pDialog;

        private String[] getMovieDataFromJson(String forecastJsonStr)
                throws JSONException {
            final String TMD_LIST = "results";
            final String TMD_Key = "key";
            final String TMD_Type = "type";
            final String TMD_name = "name";

            final String TMD_author = "author";
            final String TMD_content = "content";
            final String TMD_id = "id";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray moviesArray = forecastJson.getJSONArray(TMD_LIST);


            keys = new String[moviesArray.length()];
            types = new String[moviesArray.length()];
            names = new String[moviesArray.length()];

            author = new String[moviesArray.length()];
            conten = new String[moviesArray.length()];
            reviewsID = new String[moviesArray.length()];
            if (ch == 1) {
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieInfo = moviesArray.getJSONObject(i);
                    keys[i] = movieInfo.getString(TMD_Key);
                    types[i] = movieInfo.getString(TMD_Type);
                    names[i] = movieInfo.getString(TMD_name);

                }
                return names;

            } else {
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieInfo = moviesArray.getJSONObject(i);
                    author[i] = movieInfo.getString(TMD_author);
                    conten[i] = movieInfo.getString(TMD_content);
                    reviewsID[i] = movieInfo.getString(TMD_id);
                }
                return conten;
            }

        }


        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            String APIKey = "b0f2fbfb9c651b016969bb0f5ca2f06d";

            try {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String unitType = sharedPrefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_pop));
                String URL;
                //  "http://api.themoviedb.org/3/movie/" + moveID + "/videos?";
                URL = URLValue;
                final String APIKey_PARAM = "api_key";

                Uri builtUri = Uri.parse(URL).buildUpon()
                        .appendQueryParameter(APIKey_PARAM, APIKey)
                        .build();

                java.net.URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(forecastJsonStr);

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (ch == 1) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, result);
                listViewTrailar.setAdapter(adapter);
                listViewTrailar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent intent = new Intent();
                        int itemPosition = position;
                        String itemValue = (String) listViewTrailar.getItemAtPosition(position);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + keys[position])));
                    }

                });
            }
        //*************************************************
        if(ch == 2){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, result);
                 listViewReviews.setAdapter(adapter);



        }

    }
}}