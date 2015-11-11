package com.myapplication.punchlineprep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * Created by Jit Sun Tung on 10/13/2015.
 * Contributions by: Derek Charles, Darpan Mehta
 *
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] jokename, audioTxt, timestampTxt;
    private final Integer[] imgid,upbtnid,downbtnid,upvotesTxt,downvotesTxt;
    private final String[] votedArr;
    public static final String TAG = "UploadFragment";
    Handler myHandler;
    private Runnable seekRun;
    private static MediaPlayer m;
    JokeDBHandler jokeDb = JokeDBHandler.getInstance(getContext());
    boolean start = false;
    String jokePlayed;
    private Context mContext;

    int currentPosition;

    public CustomListAdapter(Activity context, String[] jokename, Integer[] imgid,
                             Integer[] upbtnid, Integer[] downbtnid, Integer[] upvotesTxt,
                             Integer[] downvotesTxt, String[] audioTxt, String[] timestampTxt,
                             String[] votedArr){
        super(context, R.layout.listview_layout, jokename);
        this.context=context;
        this.jokename=jokename;
        this.imgid=imgid;
        this.upbtnid=upbtnid;
        this.downbtnid=downbtnid;
        this.upvotesTxt=upvotesTxt;
        this.downvotesTxt=downvotesTxt;
        this.audioTxt=audioTxt;
        this.timestampTxt=timestampTxt;
        this.votedArr=votedArr;
        LayoutInflater inflater = LayoutInflater.from(context);
        mContext = context;

    }

    public View getView(final int position,View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView;

        if (view == null) {
            rowView = inflater.inflate(R.layout.listview_layout, parent, false);
        } else {
            rowView = view;
        }

        TextView txtTitle = (TextView) rowView.findViewById(R.id.joke);
        final ImageButton playBtn = (ImageButton) rowView.findViewById(R.id.play);
        ImageButton upVoteBtn = (ImageButton) rowView.findViewById(R.id.upvote);
        ImageButton downVoteBtn = (ImageButton) rowView.findViewById(R.id.downvote);
        final SeekBar seekbar = (SeekBar) rowView.findViewById(R.id.seek_bar);
        seekbar.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_bar));
        final TextView numUpvotes = (TextView) rowView.findViewById(R.id.numUpvotes);
        final TextView numDownvotes = (TextView) rowView.findViewById(R.id.numDownvotes);
        TextView audioLength = (TextView) rowView.findViewById(R.id.audioLength);
        final TextView audioPos = (TextView) rowView.findViewById(R.id.audioPos);
        TextView timeStamp = (TextView) rowView.findViewById(R.id.timestamp);


        txtTitle.setText(jokename[position]);
        playBtn.setImageResource(imgid[position]);
        upVoteBtn.setImageResource(upbtnid[position]);
        downVoteBtn.setImageResource(downbtnid[position]);
        numUpvotes.setText(upvotesTxt[position].toString());
        numDownvotes.setText(downvotesTxt[position].toString());
        audioLength.setText(audioTxt[position]);
        timeStamp.setText(timestampTxt[position]);

        playBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                playBtn.setImageResource(R.drawable.stopfeed);
                m.reset();
                seekbar.setProgress(0);
                start = false;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playBtn.setImageResource(R.drawable.play);
                    }
                }, 800);

                return true;
            }
        });





        playBtn.setOnClickListener(new View.OnClickListener() {
            int length;
            boolean paused;


            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                if (jokePlayed != jokename[position] && jokePlayed != null) {
                    Log.v("playing", "HJDJSFSDF");
                    m.reset();
                    seekbar.setProgress(0);
                    start = false;
                    jokePlayed=null;
                } else {
                    if (start == false && paused == false) {
                        m = new MediaPlayer();
                        jokePlayed = jokename[position];
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
                                    audioPos.setVisibility(View.VISIBLE);
                                    audioPos.setText(m.getCurrentPosition() / 1000 + " / ");
                                    currentPosition = m.getCurrentPosition();
                                    seekbar.setProgress(currentPosition);
                                    myHandler.postDelayed(this, 1000);
                                } else if (m != null && currentPosition >= m.getDuration()) {
                                    audioPos.setVisibility(View.GONE);
                                    currentPosition = 0;
                                    seekbar.setProgress(currentPosition);
                                    playBtn.setImageResource(R.drawable.play);
                                }
                            }


                        };

                        seekRun.run();
                        m.start();
                        start = true;
                        playBtn.setImageResource(R.drawable.pausefeed);
                    } else if (start == true && paused == true && m.getCurrentPosition() < m.getDuration()) {
                        m.seekTo(length);
                        m.start();
                        seekRun.run();
                        playBtn.setImageResource(R.drawable.pausefeed);
                        length = 0;
                        paused = false;


                    } else if (start == true && paused == false && m.getCurrentPosition() < m.getDuration()) {
                        m.pause();
                        length = m.getCurrentPosition();
                        seekbar.setProgress(length);
                        playBtn.setImageResource(R.drawable.play);
                        paused = true;

                    } else {
                        m.reset();
                        seekbar.setProgress(0);
                        start = false;
                        length = 0;
                        paused = false;
                    }
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
                if (start) {
                    return false;
                } else {
                    return true;
                }
            }
        });


        upVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String log = "";
                List<JokeClass> jokes = jokeDb.getAllJokes();
                for (JokeClass j : jokes) {
                    String jokeT = j.getTitle();
                    if (jokename[position].equalsIgnoreCase(jokeT)) {
                        if (j.getVoted().equalsIgnoreCase("false")) {
                            numUpvotes.setText(String.valueOf(Integer.valueOf(numUpvotes.getText().toString())+1));
                            Log.v(TAG, "JokeT: " + jokeT);
                            String upInStr = j.getUpvotes();
                            Integer upInInt = Integer.valueOf(upInStr);
                            upInInt += 1;
                            upInStr = upInInt.toString();
                            j.setUpvotes(upInStr);
                            j.setVoted("true");
                            jokeDb.updateJoke(j);
                        } else {
                            break;
                        }

                    }
                    log = log + "ID: " + j.getID() + ", Title: " + j.getTitle() + ", UpVotes: " + j.getUpvotes()
                            + ", Downvotes: " + j.getDownvotes() + ", Voted?: " + j.getVoted() + "\n";


                    Log.v(TAG, log);

                }
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
                        if (j.getVoted().equalsIgnoreCase("false")) {
                            numDownvotes.setText(String.valueOf(Integer.valueOf(numDownvotes.getText().toString())+1));
                            Log.v(TAG, "JokeT: " + jokeT);
                            String upInStr = j.getDownvotes();
                            Integer upInInt = Integer.valueOf(upInStr);
                            upInInt += 1;
                            upInStr = upInInt.toString();
                            j.setDownvotes(upInStr);
                            j.setVoted("true");
                            jokeDb.updateJoke(j);
                        } else {
                            break;
                        }

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
