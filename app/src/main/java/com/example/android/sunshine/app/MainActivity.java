package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements ForecastFragment.Callback {


    private final String LOG_TAG = MainActivity.class.getSimpleName();
    //private final String FORECASTFRAGMENT_TAG = "FFTAG";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mLocation;
    private boolean mTwoPane;
    @Override





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "INTO OnCreate()");

        mLocation = Utility.getPreferredLocation(this);

        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
//                    .commit();
//        }
        if (findViewById(R.id.weather_detail_container) != null){
            mTwoPane = true;
            if (savedInstanceState == null){

                getSupportFragmentManager().beginTransaction().replace(
                        R.id.weather_detail_container,
                        new DetailActivityFragment(),DETAILFRAGMENT_TAG).commit();
            }
        }else{
            mTwoPane = false;
        }
    }

    protected void onStart(){
        super.onStart();
        Log.v(LOG_TAG, "INTO OnStart()");
    }


    protected void onResume(){
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        if (location != null && !location.equals(mLocation)){
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if (null != ff){
                ff.onLocationChanged();
            }
            DetailActivityFragment df = (DetailActivityFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if ( null != df ){
                df.onLocationChanged(location);
            }
            mLocation = location;
        }

    }
    public void onItemSelected(Uri contentUri){
        if (mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_URI, contentUri);
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        }else{
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
        }
    }

    protected  void onPause(){
        super.onPause();
        Log.v(LOG_TAG, "INTO OnPause()");
    }

    protected void onStop(){
        super.onStop();
        Log.v(LOG_TAG, "INTO OnStop()");


    }

    protected void onDestroy(){
        super.onDestroy();
        Log.v(LOG_TAG, "INTO OnDestroy()");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.show_map) {

            showMap();
            //startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMap(){

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        String location = Utility.getPreferredLocation(this);
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q", location).build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Log.d(LOG_TAG, "Couldn't call" + location + ", no receiving apps installed!");
        }

    }
}