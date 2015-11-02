package com.myapplication.punchlineprep;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by Darpan on 10/9/2015.
 *
 */
public class FeedFragment extends Fragment{
    public static final String ARG_PAGE = "Arg_Page";
    public static final String TAG = "FeedFragment";
    String[] itemname,audiolength;
    Integer[] playBtn,upVoteBtn,downVoteBtn,numUpvotes,numDownvotes;
    ListView listView;
    SwipeRefreshLayout swipeView;
    JokeDBHandler jokeDb;

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

        jokeDb = jokeDb.getInstance(getActivity());


    }

    /* Jit
        Edit to enable List View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        List<JokeClass> jokes = jokeDb.getAllJokes();
        int jokeCount = jokeDb.getJokeCount();


        // declare arrays of the same length as the number of files in directory
        itemname = new String[jokeCount];
        playBtn = new Integer[jokeCount];
        upVoteBtn = new Integer[jokeCount];
        downVoteBtn = new Integer[jokeCount];
        numUpvotes = new Integer[jokeCount];
        numDownvotes = new Integer[jokeCount];
        audiolength = new String[jokeCount];

        int i =0;
        String log ="";
        for (JokeClass j : jokes) {
            itemname[i] = j.getTitle();
            playBtn[i] = R.drawable.play; // set the play button for each joke
            upVoteBtn[i] = R.drawable.uparrow;
            downVoteBtn[i] = R.drawable.downarrow;
            numUpvotes[i] = Integer.valueOf(j.getUpvotes());
            numDownvotes[i] = Integer.valueOf(j.getDownvotes());
            audiolength[i] = j.getLength();
            i++;
        }

        //If we want to display the array backwards
        int midPt = itemname.length /2;
        for (int j = 0; j<midPt; j++){
            String itemtemp = itemname[j];
            itemname[j] = itemname[itemname.length-j-1];
            itemname[itemname.length-j-1] = itemtemp;

            Integer numUptemp = numUpvotes[j];
            numUpvotes[j] = numUpvotes[numUpvotes.length-j-1];
            numUpvotes[numUpvotes.length-j-1] = numUptemp;

            Integer numDowntemp = numUpvotes[j];
            numDownvotes[j] = numDownvotes[numDownvotes.length-j-1];
            numDownvotes[numDownvotes.length-j-1] = numDowntemp;

            String lengthtemp = audiolength[j];
            audiolength[j] = audiolength[audiolength.length-j-1];
            audiolength[audiolength.length-j-1] = lengthtemp;
        }

/*
        // declare the file path, create a file directory, and create an array of files from that directory.
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/";
        final File f = new File(filepath);
        File file[] = f.listFiles();

        // declare arrays of the same length as the number of files in directory
        itemname = new String[file.length];
        playBtn = new Integer[file.length];
        upVoteBtn = new Integer[file.length];
        downVoteBtn = new Integer[file.length];

        for (int i= 0; i<file.length ; i++) {
            int endindex = file[i].getName().indexOf(".3gp");
            itemname[i] = file[i].getName().substring(0,endindex);
            playBtn[i] = R.drawable.rsz_play; // set the play button for each joke
            upVoteBtn[i] = R.drawable.rsz_uparrow;
            downVoteBtn[i] = R.drawable.rsz_downarrow;
        }

        //If we want to display the array backwards
        int midPt = itemname.length /2;
        for (int i = 0; i<midPt; i++){
            String temp = itemname[i];
            itemname[i] = itemname[itemname.length-i-1];
            itemname[itemname.length-i-1] = temp;
        }*/

        CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), itemname, playBtn,upVoteBtn,downVoteBtn,numUpvotes,numDownvotes,audiolength);

        listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
                swipeView.setRefreshing(false);
            }
        });

        return rootView;
    }
    private void refreshContent(){
        // declare the file path, create a file directory, and create an array of files from that directory.
        /*String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/";
        final File f = new File(filepath);
        File file[] = f.listFiles();

        // declare arrays of the same length as the number of files in directory
        itemname = new String[file.length];
        playBtn = new Integer[file.length];
        upVoteBtn = new Integer[file.length];
        downVoteBtn = new Integer[file.length];


        for (int i= 0; i<file.length ; i++) {
            int endindex = file[i].getName().indexOf(".3gp");
            itemname[i] = file[i].getName().substring(0,endindex);
            playBtn[i] = R.drawable.rsz_play; // set the play button for each joke
            upVoteBtn[i] = R.drawable.rsz_uparrow;
            downVoteBtn[i] = R.drawable.rsz_downarrow;
        }

        //If we want to display the array backwards
        int midPt = itemname.length /2;
        for (int i = 0; i<midPt; i++){
            String temp = itemname[i];
            itemname[i] = itemname[itemname.length-i-1];
            itemname[itemname.length-i-1] = temp;
        }*/

        List<JokeClass> jokes = jokeDb.getAllJokes();
        int jokeCount = jokeDb.getJokeCount();


        // declare arrays of the same length as the number of files in directory
        itemname = new String[jokeCount];
        playBtn = new Integer[jokeCount];
        upVoteBtn = new Integer[jokeCount];
        downVoteBtn = new Integer[jokeCount];
        numUpvotes = new Integer[jokeCount];
        numDownvotes = new Integer[jokeCount];
        audiolength = new String[jokeCount];

        int i =0;
        String log ="";
        for (JokeClass j : jokes) {
            itemname[i] = j.getTitle();
            playBtn[i] = R.drawable.play; // set the play button for each joke
            upVoteBtn[i] = R.drawable.uparrow;
            downVoteBtn[i] = R.drawable.downarrow;
            numUpvotes[i] = Integer.valueOf(j.getUpvotes());
            numDownvotes[i] = Integer.valueOf(j.getDownvotes());
            audiolength[i] = j.getLength();
            log = log+ "ID: " + j.getID() + ", Title: " + j.getTitle() + ", UpVotes: " + j.getUpvotes()
                    + ", Downvotes: " + j.getDownvotes()+"\n";
            i++;
        }
        Log.v(TAG,log);

        //If we want to display the array backwards
        int midPt = itemname.length /2;
        for (int j = 0; j<midPt; j++){
            String itemtemp = itemname[j];
            itemname[j] = itemname[itemname.length-j-1];
            itemname[itemname.length-j-1] = itemtemp;

            Integer numUptemp = numUpvotes[j];
            numUpvotes[j] = numUpvotes[numUpvotes.length-j-1];
            numUpvotes[numUpvotes.length-j-1] = numUptemp;

            Integer numDowntemp = numUpvotes[j];
            numDownvotes[j] = numDownvotes[numDownvotes.length-j-1];
            numDownvotes[numDownvotes.length-j-1] = numDowntemp;

            String lengthtemp = audiolength[j];
            audiolength[j] = audiolength[audiolength.length-j-1];
            audiolength[audiolength.length-j-1] = lengthtemp;
        }

        CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), itemname, playBtn,upVoteBtn,downVoteBtn,numUpvotes,numDownvotes,audiolength);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}



