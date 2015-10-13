package com.myapplication.punchlineprep;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Darpan on 10/9/2015.
 *
 */
public class FeedFragment extends Fragment {
    public static final String ARG_PAGE = "Arg_Page";
    public static final String TAG = "FeedFragment";

    public static FeedFragment newInstance (int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,page);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /* Jit
        Edit to enable List View
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        // declare the file path, create a file directory, and create an array of files from that directory.
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/";
        File f = new File(filepath);
        File file[] = f.listFiles();

        // declare arrays of the same length as the number of files in directory
        String[] itemname = new String[file.length];
        Integer[] playBtn = new Integer[file.length];

        for (int i= 0; i<file.length ; i++) {
                int endindex = file[i].getName().indexOf(".3gp");
                itemname[i] = file[i].getName().substring(0,endindex);
                playBtn[i] = R.drawable.testplaybtn; // set the play button for each joke
        }

        //If we want to display the array backwards
        int midPt = itemname.length /2;
        for (int i = 0; i<midPt; i++){
            String temp = itemname[i];
            itemname[i] = itemname[itemname.length-i-1];
            itemname[itemname.length-i-1] = temp;
        }

        CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), itemname, playBtn);

        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        return rootView;
    }

}


