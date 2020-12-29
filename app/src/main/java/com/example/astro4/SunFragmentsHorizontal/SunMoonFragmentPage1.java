package com.example.astro4.SunFragmentsHorizontal;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.astro4.DatabaseHelper;
import com.example.astro4.R;
import com.example.astro4.SunFragments.SunFragmentPage2;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SunMoonFragmentPage1 extends Fragment {

    private TextView sunriseView, latitudeView, longitudeView, sunsetView, sunriseAzimuthView,
            sunsetAzimuthView, sunDusk, sunDawn, deviceTimeView,  moonRiseView, moonSetView, newMoonView,
            fullMoonView, lunaPhaseView;
    private Button jsonButton, SUN, MOON, SETTINGS;
    private TextClock textClock;
    private String sunriseJson, sunsetJson, latJson, lonJson, sunAzimuthJson, sunsetAzimuthJson,
            sunDuskJson, sunDawnJson, moonRiseJson, moonSetJson, newMoonJson, fullMoonJson, lunaPhaseJson;;
    DatabaseHelper mDatabaseHelper;
    private Fragment fragment;
    Runnable refresh;
    int refreshing_time = 1000;
    boolean visited = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sun_moon_1, container, false);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Handler handler = new Handler();
        if (isVisibleToUser) {
            if (visited){
                Cursor data = mDatabaseHelper.getData();
                ArrayList<String> listData = new ArrayList<>();
                while (data.moveToNext()) {
                    //get the value from the database in column 1
                    //then add it to the ArrayList
                    listData.add(data.getString(1));
                    listData.add(data.getString(2));
                    listData.add(data.getString(3));
                }
                if (listData.size() > 0) {
                    refreshing_time = Integer.parseInt(listData.get(0));
                    refreshing_time = refreshing_time * 1000;
                    latitudeView.setText(listData.get(1));
                    longitudeView.setText(listData.get(2));

                }
                try {
                    jsonParse(latitudeView.getText().toString(), longitudeView.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setValues();

                if(refreshing_time >= 1000) {
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
                        }
                    };
                    handler.post(refresh);
                }
            }
            visited = true;


        }
    }
    public void setValues(){
        sunriseView.setText(sunriseJson);
        sunsetView.setText(sunsetJson);
        sunriseAzimuthView.setText(sunAzimuthJson);
        sunsetAzimuthView.setText(sunsetAzimuthJson);
        sunDusk.setText(sunDuskJson);
        sunDawn.setText(sunDawnJson);
        moonRiseView.setText(moonRiseJson);
        moonSetView.setText(moonSetJson);
        newMoonView.setText(newMoonJson);
        fullMoonView.setText(fullMoonJson);
        lunaPhaseView.setText(lunaPhaseJson);
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
        moonRiseView = rootView.findViewById(R.id.moonrise_time);
        moonSetView = rootView.findViewById(R.id.moonset_time);
        newMoonView = rootView.findViewById(R.id.new_moon_time);
        fullMoonView = rootView.findViewById(R.id.full_moon_time);
        lunaPhaseView = rootView.findViewById(R.id.luna_phase);


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
            JSONObject moon = new JSONObject(IOUtils.toString(new URL("https://www.icalendar37.net/lunar/api/?lang=en&month=12&year=2020"), Charset.forName("UTF-8")));

            // JSONObject resultJson = jsonAPI.getJSONObject("results");

            int day = new Date().getDate();
            JSONObject phase = moon.getJSONObject("phase");
            JSONObject phaseName = phase.getJSONObject(String.valueOf(day));
            String pn = phaseName.getString("phaseName");
            long lighting = phaseName.getLong("lighting");
            long roundedLighting = Math.round(lighting);

            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = new Date().getMonth()+1;
            Calendar c = Calendar.getInstance();
            int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = 1; i <= monthMaxDays; i++ ){
                JSONObject tmp_obj = phase.getJSONObject(String.valueOf(i));
                if (tmp_obj.getString("phaseName").equals("Full moon")){
                    fullMoonJson = String.valueOf(i) + "." + month + "." + year;
                }
                if (tmp_obj.getString("phaseName").equals("New Moon")){
                    newMoonJson = String.valueOf(i) + "." + month + "." + year;
                }
            }

            lunaPhaseJson = pn + " " + String.valueOf(roundedLighting) + "%";
            moonRiseJson = json.getString("moonrise");
            moonSetJson = json.getString("moonset");


            System.out.println("Update Json Data");
        }

    }


}
