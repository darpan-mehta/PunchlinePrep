package com.myapplication.punchlineprep;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Darpan on 10/20/2015.
 */
public class TopJokesFragment extends Fragment {

    public static final String ARG_PAGE = "Arg_Page";
    public static final String TAG = "TopJokesFragment";

    public static TopJokesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TopJokesFragment fragment = new TopJokesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View topJokesView = inflater.inflate(R.layout.fragment_topjokes, container, false);

        return topJokesView;
    }
}
