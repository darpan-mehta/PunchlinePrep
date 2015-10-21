package com.myapplication.punchlineprep;

import java.util.Timer;
import java.util.TimerTask;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by Darpan on 10/9/2015.
 */
public class UploadFragment extends Fragment{
    public static final String ARG_PAGE = "Arg_Page";
    public static final String TAG = "UploadFragment";
    Button play,stop,record,upload;
    TextView mTextField;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    public static UploadFragment newInstance (int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        UploadFragment fragment = new UploadFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*
        * Derek Charles - Implementation of the Recording and Upload
         */
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        final ImageButton play = (ImageButton) view.findViewById(R.id.play);
        stop = (Button) view.findViewById(R.id.stop);
        record = (Button) view.findViewById(R.id.record);
        upload = (Button) view.findViewById(R.id.upload);

        stop.setVisibility(View.GONE);
        //stop.setEnabled(false);
        play.setVisibility(View.GONE);
        //play.setEnabled(false);
        int i = 0;

        outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Integer.toString(i)+ ".3gp";

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        record.setOnClickListener(new View.OnClickListener() {
            //Record metho
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
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

        stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;


                play.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);
                play.setEnabled(true);
                upload.setEnabled(true);

                Toast.makeText(getActivity().getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();

            }
    });
        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                }

                catch (IOException e) {
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

                //add insert SQL queries
            }
        });

            return view;
        }

/*
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload,container,false);

        return rootView;
    }

    */

        }