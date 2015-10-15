package com.myapplication.punchlineprep;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
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
    Button play,stop,record,upload;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    private boolean timerStarted = false;
  //  public TextView mTextField;
    private final long startTime = 100 * 1000;
    private final long interval = 1 * 1000;




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
            mTextField.setText("" + millisUntilFinished / 1000
                    + ":" + millisUntilFinished % 1000);

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
        setRetainInstance(true);
        super.onCreate(savedInstanceState);



        /*
        * Derek- Implementation of the Recording and Upload
         */
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        play = (Button) view.findViewById(R.id.play);
        stop = (Button) view.findViewById(R.id.stop);
        record = (Button) view.findViewById(R.id.record);
        upload = (Button) view.findViewById(R.id.upload);


        stop.setVisibility(View.GONE);
        //stop.setEnabled(false);
        play.setVisibility(View.GONE);
        //play.setEnabled(false);

        // Saves the recording as temp.3gp
        // if "upload" button is never pressed, "temp.3gp" will always be in directory.
        outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/temp.3gp";

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        record.setOnClickListener(new View.OnClickListener() {
            //Record metho
            @Override
            public void onClick(View v) {

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


            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;
                if (timerStarted) {
                    timer.cancel();
                    timerStarted = false;
                }



                play.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);
                play.setEnabled(true);
                upload.setEnabled(true);

                Toast.makeText(getActivity().getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                MediaPlayer m = new MediaPlayer();

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

                m.start();
                Toast.makeText(getActivity().getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
                play.setVisibility(View.VISIBLE);

                stop.setVisibility(View.GONE);

            }

        });

    upload.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity().getApplicationContext(), "Uploading....", Toast.LENGTH_LONG).show();

            EditText titleHolder = (EditText) getActivity().findViewById(R.id.jokeName);
            String jokeTitle = titleHolder.getText().toString();
            Log.v(TAG, jokeTitle);

            // deletes the file "temp.3gp" and creates a new file with the joke title.
            File tempJoke = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/temp.3gp");
            File myJoke = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Punchline/" + jokeTitle + ".3gp");
            tempJoke.renameTo(myJoke);

        }
    });

            return view;
        }


    }