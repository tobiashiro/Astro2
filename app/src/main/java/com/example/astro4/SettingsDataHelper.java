package com.example.astro4;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SettingsDataHelper {
    private ArrayList isChangedSettings;


    // page1 - SunFragmentPage1
    // page2 - SunFragmentPage2
    // page3 - SunMoonFragmentPage1
    // page4 - TabletFragment

    public void setSettingsData(){
        for (int i = 0 ; i < 4; i++){
            isChangedSettings.add(true);
        }
    }

    public boolean getDataRefreshed(String fragment, int page){
        if (isChangedSettings.get(page).equals(true)){
            isChangedSettings.set(page, false);
            System.out.println(fragment+" has refreshed from bool");
            return true;
        }
        else {
            return false;
        }
    }
}
