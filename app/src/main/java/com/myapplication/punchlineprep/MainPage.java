package com.myapplication.punchlineprep;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.astuetz.PagerSlidingTabStrip;

import java.io.File;
import java.io.IOException;

/**
 * Created by Darpan on 10/9/2015.
 *
 */
public class MainPage extends AppCompatActivity {
    String name;
    String type;
    Bitmap image;
    int page;
    public static final String TAG = "taggy";

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter{
        final int PAGE_COUNT = 4;
        private String tabTitles[] = new String[] {"Upload", "Feed", "Top Jokes", "My Jokes"};

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {return PAGE_COUNT;}

        @Override
        public Fragment getItem(int position) {

            switch(position) {
                case 0:
                    return UploadFragment.newInstance(position+1);
                case 1:
                    return FeedFragment.newInstance(position+1);
                case 2:
                    return TopJokesFragment.newInstance(position+1);
                case 3:
                    return MyJokesFragment.newInstance(position+1);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }


    @Override
    @TargetApi(16)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Create filepath for jokes if it does not exist
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/";
        File f = new File(filepath);
        if(!f.exists())
        {
            f.mkdirs();
        }

        name = getIntent().getStringExtra("name");

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
        Log.v(TAG,"WHKHJK");

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        viewPager.setCurrentItem(1,false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload_page, menu);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


