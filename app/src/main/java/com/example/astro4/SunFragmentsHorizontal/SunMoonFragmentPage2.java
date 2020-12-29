package com.example.astro4.SunFragmentsHorizontal;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.astro4.DatabaseHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.astro4.R;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SunMoonFragmentPage2 extends Fragment {
    private EditText latitudeView, longitudeView;
    private Button confirmSettings;
    private TextView latSunTextView, lngSunTextView, latMoonTextView, lngMoonTextView, testngView, seekbarText;
    SeekBar seekBar;
    int refresh_time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup  rootView = (ViewGroup)inflater.inflate(R.layout.fragment_sun_moon_page2, container, false);
        ViewGroup  page2 = (ViewGroup)inflater.inflate(R.layout.sun_fragment_page2, container, false);
        ViewGroup  page1 = (ViewGroup)inflater.inflate(R.layout.fragment_sun_page1, container, false);


        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getActivity());
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
            listData.add(data.getString(2));
            listData.add(data.getString(3));
        }


        setUpViews(rootView, page1, page2);
        if (listData.size() > 0) {
            latitudeView.setText(listData.get(1));
            longitudeView.setText(listData.get(2));

        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekbarText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                refresh_time = Integer.parseInt(seekbarText.getText().toString());
            }
        });


        confirmSettings = rootView.findViewById(R.id.confirm_button);;
        confirmSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabaseHelper.addData(refresh_time, Double.parseDouble(latitudeView.getText().toString()), Double.parseDouble(longitudeView.getText().toString()));

                Toast.makeText(rootView.getContext(), "Data set up",  Toast.LENGTH_SHORT).show();
            }
        });
        if (listData.size() > 0) {

            latSunTextView.setText(listData.get(1));
            latSunTextView.setText(listData.get(2));
            latMoonTextView.setText(listData.get(1));
            lngMoonTextView.setText(listData.get(2));

        }
        return rootView;
    }
    public void setUpViews(ViewGroup rootView, ViewGroup page1, ViewGroup page2) {
        latitudeView = rootView.findViewById(R.id.sunrise_lat);
        longitudeView = rootView.findViewById(R.id.sunrise_longitude);
        latSunTextView = page1.findViewById(R.id.sunrise_lat);
        lngSunTextView = page1.findViewById(R.id.sunrise_longitude);
        latMoonTextView = page2.findViewById(R.id.sunrise_lat);
        lngMoonTextView = page2.findViewById(R.id.sunrise_longitude);
        seekBar= rootView.findViewById(R.id.SeekBar);
        seekbarText = rootView.findViewById(R.id.seekbarText);

    }

}
