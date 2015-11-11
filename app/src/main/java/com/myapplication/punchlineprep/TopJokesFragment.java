package com.myapplication.punchlineprep;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Darpan on 10/20/2015.
 * Contributions by Derek Charles, Jit Sun Tung
 *
 */
public class TopJokesFragment extends Fragment {

    public static final String ARG_PAGE = "Arg_Page";
    public static final String TAG = "TopFragment";
    String[] itemname,audiolength,timestamp;
    Integer[] playBtn,upVoteBtn,downVoteBtn,numUpvotes,numDownvotes;
    String[] voted;
    ListView listView;
    SwipeRefreshLayout swipeView;
    JokeDBHandler jokeDb;

    public static TopJokesFragment newInstance (int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,page);
        TopJokesFragment fragment = new TopJokesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_topjokes, container, false);

        List<JokeClass> jokes = jokeDb.getTopJokes();
        int jokeCount = jokeDb.getJokeCount();


        // declare arrays of the same length as the number of files in directory
        itemname = new String[jokeCount];
        playBtn = new Integer[jokeCount];
        upVoteBtn = new Integer[jokeCount];
        downVoteBtn = new Integer[jokeCount];
        numUpvotes = new Integer[jokeCount];
        numDownvotes = new Integer[jokeCount];
        audiolength = new String[jokeCount];
        timestamp = new String[jokeCount];
        voted = new String[jokeCount];

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
            timestamp[i] = convertTime(Integer.valueOf(j.getTimestamp()));
            voted[i] = j.getVoted();
            i++;
            log = log + "ID: " + j.getID() + ", Title: " + j.getTitle() + ", UpVotes: " + j.getUpvotes()
                    + ", Downvotes: " + j.getDownvotes() + ", Voted?: " + j.getVoted() + "\n";
        }
        Log.v("mysqlquery",log);


        //If we want to display the array backwards
        int midPt = itemname.length /2;
        for (int j = 0; j<midPt; j++){
            String itemtemp = itemname[j];
            itemname[j] = itemname[itemname.length-j-1];
            itemname[itemname.length-j-1] = itemtemp;

            Integer numUptemp = numUpvotes[j];
            numUpvotes[j] = numUpvotes[numUpvotes.length-j-1];
            numUpvotes[numUpvotes.length-j-1] = numUptemp;

            Integer numDowntemp = numDownvotes[j];
            numDownvotes[j] = numDownvotes[numDownvotes.length-j-1];
            numDownvotes[numDownvotes.length-j-1] = numDowntemp;

            String lengthtemp = audiolength[j];
            audiolength[j] = audiolength[audiolength.length-j-1];
            audiolength[audiolength.length-j-1] = lengthtemp;

            String timetemp = timestamp[j];
            timestamp[j] = timestamp[timestamp.length-j-1];
            timestamp[timestamp.length-j-1] = timetemp;

            String votedtemp = voted[j];
            voted[j] = voted[voted.length-j-1];
            voted[voted.length-j-1] = votedtemp;
        }

        CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), itemname, playBtn,
                upVoteBtn,downVoteBtn,numUpvotes,numDownvotes,audiolength,timestamp,voted);

        listView = (ListView) rootView.findViewById(R.id.topjokes);
        listView.setAdapter(adapter);
        swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refreshTOP);
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
        List<JokeClass> jokes = jokeDb.getTopJokes();
        int jokeCount = jokeDb.getJokeCount();


        // declare arrays of the same length as the number of files in directory
        itemname = new String[jokeCount];
        playBtn = new Integer[jokeCount];
        upVoteBtn = new Integer[jokeCount];
        downVoteBtn = new Integer[jokeCount];
        numUpvotes = new Integer[jokeCount];
        numDownvotes = new Integer[jokeCount];
        audiolength = new String[jokeCount];
        timestamp = new String[jokeCount];
        voted = new String[jokeCount];

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
            timestamp[i] = convertTime(Integer.valueOf(j.getTimestamp()));
            voted[i] = j.getVoted();
            log = log+ "ID: " + j.getID() + ", Title: " + j.getTitle() + ", UpVotes: " + j.getUpvotes()
                    + ", Downvotes: " + j.getDownvotes()+"\n";
            i++;
        }
        Log.v(TAG, log);

        //If we want to display the array backwards
        int midPt = itemname.length /2;
        for (int j = 0; j<midPt; j++){
            String itemtemp = itemname[j];
            itemname[j] = itemname[itemname.length-j-1];
            itemname[itemname.length-j-1] = itemtemp;

            Integer numUptemp = numUpvotes[j];
            numUpvotes[j] = numUpvotes[numUpvotes.length-j-1];
            numUpvotes[numUpvotes.length-j-1] = numUptemp;

            Integer numDowntemp = numDownvotes[j];
            numDownvotes[j] = numDownvotes[numDownvotes.length-j-1];
            numDownvotes[numDownvotes.length-j-1] = numDowntemp;

            String lengthtemp = audiolength[j];
            audiolength[j] = audiolength[audiolength.length-j-1];
            audiolength[audiolength.length-j-1] = lengthtemp;

            String timetemp = timestamp[j];
            timestamp[j] = timestamp[timestamp.length-j-1];
            timestamp[timestamp.length-j-1] = timetemp;

            String votedtemp = voted[j];
            voted[j] = voted[voted.length-j-1];
            voted[voted.length-j-1] = votedtemp;

        }

        CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), itemname, playBtn,
                upVoteBtn,downVoteBtn,numUpvotes,numDownvotes,audiolength,timestamp, voted);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public String convertTime(long time){
        Long tsLong = System.currentTimeMillis()/1000;
        Long diff = tsLong-time;
        String ts = "";
        if (diff<60) {
            ts= "1m";
        }
        else if (diff>60 && diff <3600){
            diff=diff/60;
            ts = diff.toString()+"m";
        }
        else if (diff>3600 && diff<86400){
            diff = diff/3600;
            ts = diff.toString()+"h";
        }
        else if (diff>86400){
            diff = diff/86400;
            ts= diff.toString()+"d";
        }
        return ts;
    }

}
