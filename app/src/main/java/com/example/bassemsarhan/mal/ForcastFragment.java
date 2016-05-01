package com.example.bassemsarhan.mal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassemsarhan.mal.CustomGridAdapter;
import com.example.bassemsarhan.mal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForcastFragment extends Fragment {
    String []posters;
    String []titles;
    String []overviews;
    String []ids;
    String []rates;
    String []dates;
    GridView gridview;
   DetailMovieFragment globleFragment;
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        updateMovies();
    }

    public  void checkTablet(DetailMovieFragment fr){
        globleFragment = fr;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forcastfragment, menu);
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            final Intent intent = new Intent(getActivity(), DetailActivity.class);
            gridview = (GridView) rootView.findViewById(R.id.grid);
             gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if ( globleFragment == null ) {
                        intent.putExtra("poster", posters[position]);
                        intent.putExtra("title", titles[position]);
                        intent.putExtra("date", dates[position]);
                        intent.putExtra("desc", overviews[position]);
                        intent.putExtra("rate", rates[position]);
                        intent.putExtra("id", ids[position]);
                        startActivity(intent);
                    }else{
                        globleFragment.show(posters[position] , ids[position] , titles[position] ,dates[position],rates[position] ,overviews[position]);
                    }

                }
            });


        return rootView;
    }



    public  void updateMovies(){
        FetchMovieTask movieTask = new FetchMovieTask();
        String location = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.pref_location_key)
                        , getString(R.string.pref_location_default));
          movieTask.execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
       updateMovies();

    }

   public class FetchMovieTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        private ProgressDialog pDialog;
/*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Getting Movies ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }
*/
        private String[] getMovieDataFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMD_LIST = "results";
            final String TMD_POSTER = "poster_path";
            final String TMD_TITLE = "original_title";
            final String TMD_OVERVIEW = "overview";
            final String TMD_ID = "id";
            final String TMD_RATE = "vote_average";
            final String TMD_DATE = "release_date";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray moviesArray = forecastJson.getJSONArray(TMD_LIST);

            posters =new String[moviesArray.length()];
            titles =new String[moviesArray.length()];
            overviews =new String[moviesArray.length()];
            ids =new String[moviesArray.length()];
            rates =new String[moviesArray.length()];
            dates =new String[moviesArray.length()];

            for(int i = 0; i < moviesArray.length(); i++) {

                JSONObject movieInfo = moviesArray.getJSONObject(i);
                posters[i] = movieInfo.getString(TMD_POSTER);
                titles[i] = movieInfo.getString(TMD_TITLE);
                overviews[i] = movieInfo.getString(TMD_OVERVIEW);
                ids[i] = movieInfo.getString(TMD_ID);
                rates[i] = movieInfo.getString(TMD_RATE);
                dates[i] = movieInfo.getString(TMD_DATE);
            }

            return posters;

        }
        @Override
        protected String[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String APIKey = "b0f2fbfb9c651b016969bb0f5ca2f06d";

            try {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String unitType = sharedPrefs.getString(getString(R.string.pref_units_key),getString(R.string.pref_units_pop));
                String URL;
                if(unitType.equals(R.string.pref_units_pop)){
                    URL =  "http://api.themoviedb.org/3/movie/popular?";
                }else{

                    URL =  "http://api.themoviedb.org/3/movie/top_rated?";
                }
                final String APIKey_PARAM = "api_key";

                Uri builtUri = Uri.parse(URL).buildUpon()
                        .appendQueryParameter(APIKey_PARAM,APIKey)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
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

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }
        @Override
        protected void onPostExecute(String[] result) {
            gridview.setAdapter(new CustomGridAdapter(getActivity() , result));
        }

    }

}
