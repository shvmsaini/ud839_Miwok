/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setOffscreenPageLimit(4);
        //night mode
        int currentNightMode = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//        Log.d("NightOrNot","" + currentNightMode);

        switch (currentNightMode){
            case Configuration.UI_MODE_NIGHT_YES: {
                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.navigation_night));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                break;
            }
            case Configuration.UI_MODE_NIGHT_NO: {
                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_primary));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

                break;

            }
        }

        // another interesting way to detect night mode
        //       if ( getResources().getString(R.string.Mode).equals("Night")){
        //           getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.navigation_night));
        //           getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        //        }
        //       else{
        //           getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.category_colors));
        //           getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        //       }


     PagerAdapter2 pagerAdapter2 = new PagerAdapter2(getSupportFragmentManager(),getLifecycle());
      viewPager2.setAdapter(pagerAdapter2);
     TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                  tab.setText(R.string.category_numbers);
                } else if (position == 1) {
                    tab.setText(R.string.category_colors);
                } else if (position == 2) {
                    tab.setText(R.string.category_phrases);
                } else {
                    tab.setText(R.string.category_family);
                }
            }
        });
        tabLayoutMediator.attach();




    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recreate();
    }
}
