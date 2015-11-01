package com.myapplication.punchlineprep;


import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;

/**
 * Created by Darpan on 10/20/2015.
 *
 */
public class MyJokesFragment extends Fragment {

    public static final String ARG_PAGE = "Arg_Page";
    public static final String TAG = "MyJokesFragment";
    String[] itemname;
    Integer[] playBtn,upVoteBtn,downVoteBtn,numUpvotes,numDownvotes;
    ListView listView;
    SwipeRefreshLayout swipeView;

    public static MyJokesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MyJokesFragment fragment = new MyJokesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        // declare the file path, create a file directory, and create an array of files from that directory.
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/";
        final File f = new File(filepath);
        File file[] = f.listFiles();

        // declare arrays of the same length as the number of files in directory
        itemname = new String[file.length];
        playBtn = new Integer[file.length];
        upVoteBtn = new Integer[file.length];
        downVoteBtn = new Integer[file.length];
        numUpvotes = new Integer[file.length];
        numDownvotes = new Integer[file.length];

        for (int i= 0; i<file.length ; i++) {
            int endindex = file[i].getName().indexOf(".3gp");
            itemname[i] = file[i].getName().substring(0,endindex);
            playBtn[i] = R.drawable.play; // set the play button for each joke
            upVoteBtn[i] = R.drawable.uparrow;
            downVoteBtn[i] = R.drawable.downarrow;
            numUpvotes[i] = 0;
            numDownvotes[i] = 0;

        }

        //If we want to display the array backwards
        int midPt = itemname.length /2;
        for (int i = 0; i<midPt; i++){
            String temp = itemname[i];
            itemname[i] = itemname[itemname.length-i-1];
            itemname[itemname.length-i-1] = temp;
        }

        CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), itemname, playBtn,upVoteBtn,downVoteBtn,numUpvotes,numDownvotes);

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
        String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/";
        final File f = new File(filepath);
        File file[] = f.listFiles();

        // declare arrays of the same length as the number of files in directory
        itemname = new String[file.length];
        playBtn = new Integer[file.length];
        upVoteBtn = new Integer[file.length];
        downVoteBtn = new Integer[file.length];
        numUpvotes = new Integer[file.length];
        numDownvotes = new Integer[file.length];


        for (int i= 0; i<file.length ; i++) {
            int endindex = file[i].getName().indexOf(".3gp");
            itemname[i] = file[i].getName().substring(0,endindex);
            playBtn[i] = R.drawable.play; // set the play button for each joke
            upVoteBtn[i] = R.drawable.uparrow;
            downVoteBtn[i] = R.drawable.downarrow;
            numUpvotes[i] = 0;
            numDownvotes[i] = 0;
        }

        //If we want to display the array backwards
        int midPt = itemname.length /2;
        for (int i = 0; i<midPt; i++){
            String temp = itemname[i];
            itemname[i] = itemname[itemname.length-i-1];
            itemname[itemname.length-i-1] = temp;
        }

        CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), itemname, playBtn,upVoteBtn,downVoteBtn,numUpvotes,numDownvotes);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
