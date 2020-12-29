package com.example.astro4;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.example.astro4.SunFragments.SunFragmentPage3;
import com.example.astro4.SunFragments.SunFragmentPage1;
import com.example.astro4.SunFragments.SunFragmentPage2;
import com.example.astro4.SunFragmentsHorizontal.SunMoonFragmentPage1;
import com.example.astro4.SunFragmentsHorizontal.SunMoonFragmentPage2;
import com.example.astro4.TabletFragment.TabletFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private ViewPager pagerSun;
    private PagerAdapter pagerAdapterSun;
    private TabLayout tabLayout;
    private android.R.attr SharedCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            //code for big screen (like tablet)
           //Tablet
            System.out.println("jestem TABLET");
            setContentView(R.layout.activity_main);
            List<Fragment> list = new ArrayList<>();
            list.add(new TabletFragment());
            pagerSun = findViewById(R.id.pagerSunMoonTablet);
            tabLayout = findViewById(R.id.tablayout);
            pagerAdapterSun = new SlidePagerAdapter(getSupportFragmentManager(), list);
        } else {
            System.out.println("W ELSIE");
           //phone
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setContentView(R.layout.activity_main2);
                List<Fragment> list = new ArrayList<>();
                list.add(new SunMoonFragmentPage1());
                list.add(new SunFragmentPage3());

                pagerSun = findViewById(R.id.pagerSunMoon);
                tabLayout = findViewById(R.id.tablayout);
                pagerAdapterSun = new SlidePagerAdapter(getSupportFragmentManager(), list);
            } else {
                setContentView(R.layout.activity_main);

                List<Fragment> list = new ArrayList<>();
                list.add(new SunFragmentPage1());
                list.add(new SunFragmentPage2());
                list.add(new SunFragmentPage3());

                pagerSun = findViewById(R.id.pagerSun);
                tabLayout = findViewById(R.id.tablayout);
                pagerAdapterSun = new SlidePagerAdapter(getSupportFragmentManager(), list);
            }
        }
        pagerSun.setAdapter(pagerAdapterSun);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getPosition() == 0){
                        pagerSun.setCurrentItem(0);
                    }
                    else if(tab.getPosition() == 1){
                        pagerSun.setCurrentItem(1);
                    }
                    else if(tab.getPosition() == 2){
                        pagerSun.setCurrentItem(2);
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            pagerSun.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            }



    @Override
    public void onBackPressed() {
        if (pagerSun.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pagerSun.setCurrentItem(pagerSun.getCurrentItem() - 1);
        }
    }







}