package com.myapplication.punchlineprep;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
    private Boolean rerecord = false;

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
                    + "s");
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
        seekBar.setVisibility(View.INVISIBLE);

        stop.setVisibility(View.GONE);
        //stop.setEnabled(false);
        play.setVisibility(View.GONE);
        //play.setEnabled(false);
        upload.setEnabled(false);
        upload.setAlpha(128);

        // Saves the recording as temp.3gp
        // if "upload" button is never pressed, "temp.3gp" will always be in directory.
        outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/temp.3gp";

        record.setOnClickListener(new View.OnClickListener() {
            //Record metho
            @Override
            public void onClick(View v) {

                if(rerecord){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    seekBar.setVisibility(View.VISIBLE);
                                    //Yes button clicked
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
                                    record.setAlpha(128);
                                    stop.setVisibility(View.VISIBLE);
                                    //stop.setEnabled(true);
                                    upload.setEnabled(false);
                                    play.setVisibility(View.GONE);

                                    Toast.makeText(getActivity().getApplicationContext(), "Recording starting", Toast.LENGTH_LONG).show();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure you want to record a new Joke? Your previous joke will be overwritten.").setPositiveButton("Confirm", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show();

                }
                else{
                    seekBar.setVisibility(View.VISIBLE);
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
                    record.setAlpha(128);
                    stop.setVisibility(View.VISIBLE);
                    //stop.setEnabled(true);
                    upload.setEnabled(false);
                    play.setVisibility(View.GONE);

                    Toast.makeText(getActivity().getApplicationContext(), "Recording starting. You have 90 seconds.", Toast.LENGTH_LONG).show();
                }


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
                    TextView mTextField = (TextView) getView().findViewById(R.id.mTextField);
                    int endindex = mTextField.getText().toString().indexOf("s");

                    mTextField.setText(String.valueOf(90 - Integer.valueOf(mTextField.getText().toString().substring(0,endindex)))+"s");
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
                rerecord = true;
                record.setAlpha(255);
                play.setEnabled(true);
                upload.setEnabled(true);
                upload.setAlpha(255);
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
                                try{
                                    currentPosition = m.getCurrentPosition();
                                    updateProgress(currentPosition);
                                }
                                catch (IllegalStateException e1){
                                    e1.printStackTrace();
                                }


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

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
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

                                    Long tsLong = System.currentTimeMillis()/1000;
                                    String ts = tsLong.toString();


                                    JokeDBHandler jokeDb = JokeDBHandler.getInstance(getContext());
                                    //jokeDb.delTable();
                                    jokeDb.addJoke(new JokeClass(jokeTitle, "0", "0",audioLength,ts, "false"));

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
                                    seekBar.setVisibility(View.INVISIBLE);
                                    ((TextView) getView().findViewById(R.id.mTextField)).setVisibility(View.GONE);
                                    ((TextView) getView().findViewById(R.id.jokeName)).setText("");
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Confirm Upload?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
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