package singlepageapp.mohanty.dinesh.com.musicplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private  MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    ImageView imageView ;
    private int currentPosition = -1;
     public  ArrayList<Song> songs;
    SeekBar seekBar;
    android.os.Handler handler;




    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

          if(focusChange  == AudioManager.AUDIOFOCUS_LOSS)
          {
              mediaPlayer.release();
              audioManager.abandonAudioFocus(audioFocusChangeListener);
              imageView.setImageResource(R.drawable.ic_play_circle_filled_black_36dp);
          }else  if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
          {
              mediaPlayer.pause();
              imageView.setImageResource(R.drawable.ic_play_circle_filled_black_36dp);
          } else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
          {
              mediaPlayer.pause();
              imageView.setImageResource(R.drawable.ic_play_circle_filled_black_36dp);
          }


        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






      imageView  =(ImageView) findViewById(R.id.play_pause_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null)
                {
                    if(mediaPlayer.isPlaying())
                    {
                        mediaPlayer.pause();
                        imageView.setImageResource(R.drawable.ic_play_circle_filled_black_36dp);
                    }
                    else
                    {
                        pauseToPlay();
                    }
                }

                else if (mediaPlayer== null)
                {
                    startPlaying(0);
                    currentPosition= 0;
                }


            }
        });


        seekBar = (SeekBar) findViewById(R.id.seekbar_main);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser) {

                    if(mediaPlayer != null) {
                        mediaPlayer.seekTo(progress);
                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });







        songs = new ArrayList<Song>();
        songs.add(new Song("Wave the nature gift" , "Ustad salim ali khan" , R.drawable.songq , R.raw.wave));
        songs.add(new Song("Back music beautiful" , "jennifer de costa" , R.drawable.song_two , R.raw.back_music));
        songs.add(new Song("Indian National Anthem" , "Indian" , R.drawable.indian_national_flag , R.raw.indianationalanthem));
        songs.add(new Song("Wave the nature gift" , "Ustad salim ali khan" , R.drawable.song_one , R.raw.crowdcheering));
        songs.add(new Song("Rebel master is here" , "Ankit Mishra" , R.drawable.song_three , R.raw.wave));
        songs.add(new Song("Back music beautiful" , "jennifer de costa" , R.drawable.song_five , R.raw.back_music));
        songs.add(new Song("Wave the nature gift" , "Ustad salim ali khan" , R.drawable.song_six , R.raw.wave));
        songs.add(new Song("Wave the nature gift" , "memete minati" , R.drawable.song_four , R.raw.crowdcheering));



       final SongAdapter songAdapter = new SongAdapter(this , songs);
        final ListView listView = (ListView) findViewById(R.id.view_recycle);
        listView.setAdapter(songAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                mediaRelease();
                currentPosition = position;
               startPlaying(position);





            }
        });



        ImageView imageView1 = (ImageView) findViewById(R.id.skip_next_button);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPosition != -1 && currentPosition < songs.size()-1) {
                    mediaRelease();
                    startPlaying(currentPosition + 1);
                    currentPosition = currentPosition +1;
                }
            }
        });


        ImageView imageView2 = (ImageView) findViewById(R.id.skip_previous_button);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( currentPosition > 0) {
                    mediaRelease();
                    startPlaying(currentPosition-1);
                    currentPosition = currentPosition - 1;
                }
            }
        });





    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaRelease();


    }

    private void mediaRelease()
    {
        if(mediaPlayer != null )
        {
            mediaPlayer.release();
            mediaPlayer = null;

        }
        imageView.setImageResource(R.drawable.ic_play_circle_filled_black_36dp);

        if(audioManager != null)
        {
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }



     private  void startPlaying(int songPosition)
     {
         audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
         int gain = audioManager.requestAudioFocus(audioFocusChangeListener , AudioManager.STREAM_MUSIC , AudioManager.AUDIOFOCUS_GAIN);

         //if it get the grant then we can play music inside the app

         if (gain == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


             final Song song= songs.get(songPosition);
             int  i = song.getSongId();
             mediaPlayer = MediaPlayer.create(MainActivity.this, i);
             mediaPlayer.start();
             imageView.setImageResource(R.drawable.ic_pause_circle_filled_black_36dp);
             TextView textView = (TextView)findViewById(R.id.text_on_player);
             textView.setText(song.getSongName() + " , " + song.getSinger());

             handler = new Handler(Looper.getMainLooper());
             seekBar.setMax(mediaPlayer.getDuration()); // Set the Maximum range of the
             seekBar.setProgress(mediaPlayer.getCurrentPosition());
             handler.removeCallbacks(moveSeekBarThread);
             handler.postDelayed(moveSeekBarThread, 100);







             mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                 @Override
                 public void onCompletion(MediaPlayer mp) {

                     if(mediaPlayer != null && currentPosition < songs.size()-1) {
                         startPlaying(currentPosition + 1);
                         currentPosition++;
                     }else if(currentPosition == songs.size() - 1)
                     {
                         startPlaying(0);
                         currentPosition = 0;
                     }


                 }
             });


         }


     }

    private Runnable moveSeekBarThread = new Runnable() {
        public void run() {
            if(mediaPlayer.isPlaying()){

                int mediaPos_new = mediaPlayer.getCurrentPosition();
                int mediaMax_new = mediaPlayer.getDuration();
                seekBar.setMax(mediaMax_new);
                seekBar.setProgress(mediaPos_new);
                handler.postDelayed(this, 100);
              //Looping the thread after 0.1 second


            }

        }
    };

    private void pauseToPlay()
    {
        mediaPlayer.start();
        imageView.setImageResource(R.drawable.ic_pause_circle_filled_black_36dp);
        handler = new Handler(Looper.getMainLooper());
        seekBar.setMax(mediaPlayer.getDuration()); // Set the Maximum range of the
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        handler.removeCallbacks(moveSeekBarThread);
        handler.postDelayed(moveSeekBarThread, 100);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if(mediaPlayer != null && currentPosition < songs.size()-1) {
                    startPlaying(currentPosition + 1);
                    currentPosition++;
                }else if(currentPosition == songs.size() - 1)
                {
                    startPlaying(0);
                    currentPosition = 0;
                }


            }
        });
    }



}
