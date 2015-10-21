package com.myapplication.punchlineprep;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by jtung on 10/13/2015.
 *
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] jokename;
    private final Integer[] imgid;
    public static final String TAG = "UploadFragment";
     SeekBar seekbar;
    Handler myHandler;
    private Runnable seekRun;
    MediaPlayer m;

    int currentPosition;

    public CustomListAdapter(Activity context, String[] jokename, Integer[] imgid){
        super(context, R.layout.listview_layout, jokename);
        this.context=context;
        this.jokename=jokename;
        this.imgid=imgid;
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
        seekbar = (SeekBar) rowView.findViewById(R.id.seek_bar);
        //

        txtTitle.setText(jokename[position]);
        playBtn.setImageResource(imgid[position]);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                m = new MediaPlayer();
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
Derek - Addition of Seekbar , not yet fully functional
 */
                seekbar.setMax(m.getDuration());
                myHandler = new Handler();
                seekRun = new Runnable() {
                    public void run() {
                        if (m != null) {
                            currentPosition = m.getCurrentPosition();
                            seekbar.setProgress(currentPosition);
                        }
                        myHandler.postDelayed(this, 1000);
                    }
                };

                seekRun.run();
                m.start();
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




        return rowView;
    }
}
