package com.myapplication.punchlineprep;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by Darpan on 10/9/2015.
 *
 */
public class UploadFragment extends Fragment{
    public static final String ARG_PAGE = "Arg_Page";
    public static final String TAG = "UploadFragment";
    ImageButton play, record, upload, stop;
    TextView mTextField;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    MediaPlayer m;
    String audioLength;
    int currentPosition;
    SeekBar seekBar;
    boolean timerStarted;
    private Handler myHandler;
    private Runnable seekRun;

    private Thread progressBarThread;
    private int maxProgress;

    public static UploadFragment newInstance (int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        UploadFragment fragment = new UploadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /* Derek
timer creation
 */
    CountDownTimer timer = new CountDownTimer(90000, 1) {

        public void onTick(long millisUntilFinished) {
            TextView mTextField = (TextView) getView().findViewById(R.id.mTextField);
            mTextField.setVisibility(View.VISIBLE);
            mTextField.setText("" + millisUntilFinished / 1000
                    + ":" + millisUntilFinished % 1000);
            seekBar.setMax(90000);
            seekBar.setProgress(90000-(int)millisUntilFinished);

        }


        public void onFinish() {
            TextView mTextField = (TextView) getView().findViewById(R.id.mTextField);
            mTextField.setText("0:000");
            timerStarted = false;
            //added so timer finish acts the same as pressing Stop button
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;

            play.setVisibility(View.VISIBLE);
            stop.setVisibility(View.GONE);
            play.setEnabled(true);
            upload.setEnabled(true);


            Toast.makeText(getActivity().getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();

        }

    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        /*
        * Derek Charles - Implementation of the Recording and Upload
         */
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        play = (ImageButton) view.findViewById(R.id.play);
        stop = (ImageButton) view.findViewById(R.id.stop);
        record = (ImageButton) view.findViewById(R.id.record);
        upload = (ImageButton) view.findViewById(R.id.upload);
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);

        stop.setVisibility(View.GONE);
        //stop.setEnabled(false);
        play.setVisibility(View.GONE);
        //play.setEnabled(false);

        // Saves the recording as temp.3gp
        // if "upload" button is never pressed, "temp.3gp" will always be in directory.
        outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/temp.3gp";

        record.setOnClickListener(new View.OnClickListener() {
            //Record metho
            @Override
            public void onClick(View v) {

                myAudioRecorder = new MediaRecorder();
                myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                myAudioRecorder.setOutputFile(outputFile);

                if (!timerStarted) {
                    timer.start();
                    timerStarted = true;
                }

                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                record.setEnabled(false);
                stop.setVisibility(View.VISIBLE);
                //stop.setEnabled(true);
                upload.setEnabled(false);
                play.setVisibility(View.GONE);

                Toast.makeText(getActivity().getApplicationContext(), "Recording starting", Toast.LENGTH_LONG).show();
          /*      new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        mTextField.setText("Time Remaining" + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        myAudioRecorder.stop();
                        myAudioRecorder.release();
                        myAudioRecorder = null;
                        play.setVisibility(View.VISIBLE);
                        stop.setVisibility(View.GONE);
                        play.setEnabled(true);
                        upload.setEnabled(true);

                        Toast.makeText(getActivity().getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();

                    }
                }.start();*/
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAudioRecorder != null) {
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                    Toast.makeText(getActivity().getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
                }
                if (timerStarted) {
                    timer.cancel();
                    timerStarted = false;
                    //  mTextField.setVisibility(View.GONE);
                }
                if (progressBarThread != null) {
                    progressBarThread.interrupt();
                }
                if (m != null) {
                    m.pause();
                    m.release();
                    m = null;

                }

                play.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);
                record.setEnabled(true);
                play.setEnabled(true);
                upload.setEnabled(true);
                seekBar.setProgress(0);

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {

                m = new MediaPlayer();

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
                maxProgress = m.getDuration();
                seekBar.setMax(maxProgress);

                Log.v("durationtag","Duration:" + String.valueOf(maxProgress/1000));
                audioLength = String.valueOf(maxProgress/1000) +"s";

                /*
                Derek - Handler and Runnable allows for Seekbar update in UI with
                current position of the MediaPlayer
                 */
               /* myHandler = new Handler();
                seekRun = new Runnable() {
                    public void run() {
                        if (m != null) {
                            currentPosition = m.getCurrentPosition();
                            seekBar.setProgress(currentPosition);
                        }
                        myHandler.postDelayed(this, 1000);
                    }
                };*/

                progressBarThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.interrupted()) {
                            if (m != null) {
                                currentPosition = m.getCurrentPosition();
                                updateProgress(currentPosition);

                                if (currentPosition >= maxProgress) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            seekBar.setProgress(0);
                                            stop.setVisibility(View.GONE);
                                            play.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    return;
                                }
                            }
                            try {
                                Thread.sleep(10);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                progressBarThread.start();

                m.start();

                Toast.makeText(getActivity().getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
                play.setVisibility(View.GONE);

                stop.setVisibility(View.VISIBLE);


            }

        });
        upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText titleHolder = (EditText) getActivity().findViewById(R.id.jokeName);
                String jokeTitle = titleHolder.getText().toString();
                Log.v(TAG, jokeTitle);

                if(jokeTitle.equals("") || jokeTitle.equals("Enter a Joke Name")){

                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a joke title! Numbnut....", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Uploading....", Toast.LENGTH_LONG).show();

                    // deletes the file "temp.3gp" and creates a new file with the joke title.
                    File tempJoke = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/temp.3gp");
                    File myJoke = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/" + jokeTitle+ ".3gp");
                    tempJoke.renameTo(myJoke);


                    JokeDBHandler jokeDb = JokeDBHandler.getInstance(getContext());
                    //jokeDb.delTable();
                    jokeDb.addJoke(new JokeClass(jokeTitle, "0", "0",audioLength));

                    List<JokeClass> jokes = jokeDb.getAllJokes();
                    String log ="";
                    for (JokeClass j : jokes) {
                        log = log + "ID: " + j.getID() + ", Title: " + j.getTitle() + ", UpVotes: " + j.getUpvotes()
                                + ", Downvotes: " + j.getDownvotes()+"\n";

                    }
                    Log.v(TAG, log);


                    //add insert SQL queries

                    //Reset Upload Screen
                    play.setVisibility(View.GONE);
                    seekBar.setProgress(0);
                    ((TextView) getView().findViewById(R.id.mTextField)).setVisibility(View.GONE);
                    ((TextView) getView().findViewById(R.id.jokeName)).setText("");


                }

            }
        });


        /*
        Derek - SeekBar Listener shows changes made to the seekbar by the user
         */

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Seekbar changes made from the user
                if (m != null && fromUser) {
                    m.seekTo(progress);
                    m.start();
                }

            }
        });


        return view;
        }

    private void updateProgress(int progress) {
        seekBar.setProgress(progress);
    }

        }