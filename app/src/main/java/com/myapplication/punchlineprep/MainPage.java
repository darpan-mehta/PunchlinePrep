package com.myapplication.punchlineprep;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Darpan on 10/9/2015.
 * Contributions by Derek Charles, Jit Sun Tung
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
        private String tabTitles[] = new String[] {"Record", "Recent", "Top Jokes", "My Jokes"};

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

        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defaultTimeEntry = "true";
        String firstTime = sharedPref.getString(getString(R.string.first_time_key),defaultTimeEntry);

        if (firstTime.equalsIgnoreCase("true")){
            // Create filepath for jokes if it does not exist
            String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/";
            File f = new File(filepath);
            if(!f.exists())
            {
                f.mkdirs();
            }

            JokeDBHandler jokeDb = JokeDBHandler.getInstance(this);
            //jokeDb.delTable();
            jokeDb.addJoke(new JokeClass("Blondes & Puzzles", "12", "2","9s","1446692268", "false","system"));
            jokeDb.addJoke(new JokeClass("Changing My Facebook Name", "33", "4","9s","1446951468", "false","system"));
            jokeDb.addJoke(new JokeClass("Snowmen vs  Snowladies", "25", "20","6s","1447212571", "false","system"));

            final int[] mSongs = new int[] { R.raw.puzzlejoke, R.raw.fbjoke, R.raw.snowjoke };
            for (int i = 0; i < mSongs.length; i++) {
                try {
                    File dir = new File(filepath);
                    if (dir.mkdirs() || dir.isDirectory()) {
                        String str_song_name = i + ".3gp";
                        CopyRAWtoSDCard(mSongs[i], filepath + File.separator + str_song_name);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File tempJoke1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/0.3gp");
            File myJoke1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/" + "Blondes & Puzzles" + ".3gp");
            tempJoke1.renameTo(myJoke1);

            File tempJoke2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/1.3gp");
            File myJoke2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/" + "Changing My Facebook Name" + ".3gp");
            tempJoke2.renameTo(myJoke2);

            File tempJoke3 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/2.3gp");
            File myJoke3 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/" + "Snowmen vs  Snowladies" + ".3gp");
            tempJoke3.renameTo(myJoke3);


            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.first_time_key), "false");
            editor.commit();
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

    //Function
    // Copies 3 audio files in /res/raw and copies them to sdCard/Internal memory
    private void CopyRAWtoSDCard(int id, String path) throws IOException {
        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }

}


