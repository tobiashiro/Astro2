package com.example.astro4.SunFragments;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;


import com.example.astro4.SettingsDataHelper;
import com.example.astro4.SunFragments.SunFragmentPage3;
import com.example.astro4.DatabaseHelper;
import com.example.astro4.R;
import com.example.astro4.SlidePagerAdapter;


import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TaggedOutputStream;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class SunFragmentPage1 extends Fragment {

    private TextView sunriseView, latitudeView, longitudeView, sunsetView, sunriseAzimuthView,
            sunsetAzimuthView, sunDusk, sunDawn, deviceTimeView;
    private Button jsonButton, SUN, MOON, SETTINGS;
    private TextClock textClock;
    private String sunriseJson, sunsetJson, latJson, lonJson, sunAzimuthJson, sunsetAzimuthJson,
            sunDuskJson, sunDawnJson;
    DatabaseHelper mDatabaseHelper;
    private Fragment fragment;
    Runnable refresh;
    int refreshing_time = 5000;
    boolean isVisited = true;
    SettingsDataHelper settingsDataHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sun_page1, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);








        mDatabaseHelper = new DatabaseHelper(getActivity());
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
            listData.add(data.getString(2));
            listData.add(data.getString(3));
        }




        setUpViews(rootView);
        if (listData.size() > 0) {
            refreshing_time = Integer.parseInt(listData.get(0));
            refreshing_time = refreshing_time * 1000;
            latitudeView.setText(listData.get(1));
            longitudeView.setText(listData.get(2));
            System.out.println(refreshing_time);

        }

        try {
            jsonParse(latitudeView.getText().toString(), longitudeView.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setValues();







        return rootView;
    }
    public void setValues(){
        sunriseView.setText(sunriseJson);
        sunsetView.setText(sunsetJson);
        sunriseAzimuthView.setText(sunAzimuthJson);
        sunsetAzimuthView.setText(sunsetAzimuthJson);
        sunDusk.setText(sunDuskJson);
        sunDawn.setText(sunDawnJson);
        System.out.println("Update values on device");
    }
    public void setUpViews(ViewGroup rootView) {

        sunriseView = rootView.findViewById(R.id.sunrise_time);
        latitudeView = rootView.findViewById(R.id.sunrise_lat);
        longitudeView = rootView.findViewById(R.id.sunrise_longitude);
        sunsetView = rootView.findViewById(R.id.sunset_time);
        sunriseAzimuthView = rootView.findViewById(R.id.azimuth_sunrise);
        sunsetAzimuthView = rootView.findViewById(R.id.sunset_azimuth);
        sunDusk = rootView.findViewById(R.id.sun_dusk);
        sunDawn = rootView.findViewById(R.id.sun_dawn);


    }

    private void jsonParse(String lat, String lng) throws IOException, JSONException {
        if(Double.parseDouble(lat) > -180 && Double.parseDouble(lat) < 180 ){
            String KEY = "3b4a1cbca288475aa9cfcfe3e944a763";
            //String KEY = "NIC";
            JSONObject json = new JSONObject(IOUtils.toString(new URL("https://api.ipgeolocation.io/astronomy?apiKey="+KEY+"&lat="+lat+"&long="+lng), Charset.forName("UTF-8")));
            JSONObject jsonAPI = new JSONObject(IOUtils.toString(new URL("https://api.sunrise-sunset.org/json?lat="+lat+"&lng="+lng), Charset.forName("UTF-8")));
            JSONObject resultJson = jsonAPI.getJSONObject("results");
            sunriseJson = json.getString("sunrise");
            sunsetJson = json.getString("sunset");
            sunAzimuthJson = String.format("%.2f", Double.parseDouble(json.getString("sun_azimuth")));
            sunsetAzimuthJson = String.format("%.2f", Double.parseDouble(json.getString("sun_azimuth")));
            sunDuskJson = resultJson.getString("civil_twilight_begin");
            sunDawnJson = resultJson.getString("civil_twilight_end");
            System.out.println("Data uploaded from api");;


        }

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        final Handler handler = new Handler();
        if (isVisibleToUser) {
            if (refreshing_time == 0 ){
                refreshing_time = 1000;
            }
            if(refreshing_time >= 1000){

                    refresh = new Runnable() {
                        public void run() {
                            try {
                                jsonParse(latitudeView.getText().toString(), longitudeView.getText().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setValues();
                            handler.postDelayed(refresh, refreshing_time);

                            System.out.println("Refreshing time set: "+refreshing_time);
                        }
                    };
                    handler.post(refresh);



            }
        }

    }


}
