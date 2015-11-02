package com.myapplication.punchlineprep;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

/**
 * Created by jtung on 10/13/2015.
 *
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] jokename, audioTxt;
    private final Integer[] imgid,upbtnid,downbtnid,upvotesTxt,downvotesTxt;
    public static final String TAG = "UploadFragment";
    Handler myHandler;
    private Runnable seekRun;
     MediaPlayer m;
    JokeDBHandler jokeDb = JokeDBHandler.getInstance(getContext());
    boolean start = false;

    int currentPosition;

    public CustomListAdapter(Activity context, String[] jokename, Integer[] imgid,
                             Integer[] upbtnid, Integer[] downbtnid, Integer[] upvotesTxt,
                             Integer[] downvotesTxt, String[] audioTxt){
        super(context, R.layout.listview_layout, jokename);
        this.context=context;
        this.jokename=jokename;
        this.imgid=imgid;
        this.upbtnid=upbtnid;
        this.downbtnid=downbtnid;
        this.upvotesTxt=upvotesTxt;
        this.downvotesTxt=downvotesTxt;
        this.audioTxt=audioTxt;
    }

    public View getView(final int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView;

        if (view == null) {
            rowView = inflater.inflate(R.layout.listview_layout, parent, false);
        } else {
            rowView = view;
        }

        TextView txtTitle = (TextView) rowView.findViewById(R.id.joke);
        ImageButton playBtn = (ImageButton) rowView.findViewById(R.id.play);
        ImageButton upVoteBtn = (ImageButton) rowView.findViewById(R.id.upvote);
        ImageButton downVoteBtn = (ImageButton) rowView.findViewById(R.id.downvote);
        final SeekBar seekbar = (SeekBar) rowView.findViewById(R.id.seek_bar);
        TextView numUpvotes = (TextView) rowView.findViewById(R.id.numUpvotes);
        TextView numDownvotes = (TextView) rowView.findViewById(R.id.numDownvotes);
        TextView audioLength = (TextView) rowView.findViewById(R.id.audioLength);
        

        txtTitle.setText(jokename[position]);
        playBtn.setImageResource(imgid[position]);
        upVoteBtn.setImageResource(upbtnid[position]);
        downVoteBtn.setImageResource(downbtnid[position]);
        numUpvotes.setText(upvotesTxt[position].toString());
        numDownvotes.setText(downvotesTxt[position].toString());
        audioLength.setText(audioTxt[position]);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {

                m = new MediaPlayer();
                if (start == false) {
                    String outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            + "/Punchline/" + jokename[position] + ".3gp";

                    try {
                        m.setDataSource(outputFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        m.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
/*
Derek - Addition of Seekbar
 */
                    seekbar.setMax(m.getDuration());
                    myHandler = new Handler();
                    seekRun = new Runnable() {
                        public void run() {
                            if (m != null && currentPosition < m.getDuration()) {

                                currentPosition = m.getCurrentPosition();
                                seekbar.setProgress(currentPosition);
                                myHandler.postDelayed(this, 1000);
                            } else if (m != null && currentPosition >= m.getDuration()) {
                                currentPosition = 0;
                                seekbar.setProgress(currentPosition);

                            }
                        }


                    };

                    seekRun.run();
                    m.start();
                    start = true;
                } else {
                    m.stop();
                    seekbar.setProgress(0);
                    start = false;
                }
            }

        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                if (m != null && fromUser) {
                    m.seekTo(progress);
                    m.start();
                }
            }
        });

        // Sets the seekbar to not be touchable to reduce accidental click when trying to vote
        seekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        upVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String log = "";
                List<JokeClass> jokes = jokeDb.getAllJokes();
                for (JokeClass j : jokes) {
                    String jokeT = j.getTitle();
                    if(jokename[position].equalsIgnoreCase(jokeT)) {
                        Log.v(TAG, "JokeT: " + jokeT);
                        String upInStr = j.getUpvotes();
                        Integer upInInt = Integer.valueOf(upInStr);
                        upInInt += 1;
                        upInStr = upInInt.toString();
                        j.setUpvotes(upInStr);
                        jokeDb.updateJoke(j);

                    }

                    log = log + "ID: " + j.getID() + ", Title: " + j.getTitle() + ", UpVotes: " + j.getUpvotes()
                            + ", Downvotes: " + j.getDownvotes() + "\n";
                }

                Log.v(TAG, log);
            }
        });

        downVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String log = "";
                List<JokeClass> jokes = jokeDb.getAllJokes();
                for (JokeClass j : jokes) {
                    String jokeT = j.getTitle();
                    if (jokename[position].equalsIgnoreCase(jokeT)) {
                        Log.v(TAG, "JokeT: " + jokeT);
                        String upInStr = j.getDownvotes();
                        Integer upInInt = Integer.valueOf(upInStr);
                        upInInt += 1;
                        upInStr = upInInt.toString();
                        j.setDownvotes(upInStr);
                        jokeDb.updateJoke(j);
                    }
                    log = log + "ID: " + j.getID() + ", Title: " + j.getTitle() + ", UpVotes: " + j.getUpvotes()
                            + ", Downvotes: " + j.getDownvotes() + "\n";
                }

                Log.v(TAG, log);
            }
        });


        return rowView;
    }
}
