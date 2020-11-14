package com.example.android.miwok;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;


public class NumberFragment extends Fragment {

    private MediaPlayer m;
    private AudioManager mAudioManager;
    private AudioFocusRequest mFocusRequest;
    private final MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                m.pause();
                m.seekTo(0);
                Log.d("*******","Lost audio focus Transient");

            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                Log.d("****","AUDIOFOCUS_REQUEST_GAIN");
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                m.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                Log.d("****","Lost audio focus");
//                m.pause();
                releaseMediaPlayer();
//                mAudioManager.abandonAudioFocusRequest(mFocusRequest);
                // Stop playback and clean up resources


            }
        }
    };


    public NumberFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.word_list, container, false);

        final ArrayList<Word> words = new ArrayList<>();

        words.add( new Word("one","lutti",R.drawable.number_one,R.raw.number_one));
        words.add( new Word("two","otiiko",R.drawable.number_two,R.raw.number_two));
        words.add( new Word("three","tolookosu",R.drawable.number_three,R.raw.number_three));
        words.add(new Word("four","oyyisa",R.drawable.number_four,R.raw.number_four));
        words.add( new Word("five","massokka",R.drawable.number_five,R.raw.number_five));
        words.add( new Word("six","temmokka",R.drawable.number_six,R.raw.number_six));
        words.add( new Word("seven","kenekaku",R.drawable.number_seven,R.raw.number_seven));
        words.add( new Word("eight","kawinta",R.drawable.number_eight,R.raw.number_eight));
        words.add( new Word("nine","wo’e",R.drawable.number_nine,R.raw.number_nine));
        words.add( new Word("ten","na'aacha",R.drawable.number_ten,R.raw.number_ten));

        final WordAdapter itemsAdapter =
                new WordAdapter( getActivity(),words,R.color.category_numbers_list);
        ListView listView =  rootView.findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                mAudioManager = (AudioManager) Objects.requireNonNull(getActivity()).getSystemService(Context.AUDIO_SERVICE);
                //getActivity because fragments can't access system services
                AudioAttributes mAudioAttributes =
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build();
                mFocusRequest = new AudioFocusRequest
                        .Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                        .setAudioAttributes(mAudioAttributes)
                        .setAcceptsDelayedFocusGain(true)
                        .setOnAudioFocusChangeListener(mOnAudioFocusChangeListener)
                        .build();
                int focusRequest = mAudioManager.requestAudioFocus(mFocusRequest);
                switch (focusRequest) {
                    case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                        Log.d("****","AUDIOFOCUS_REQUEST_FAILED");  // don’t start playback
                    case AudioManager.AUDIOFOCUS_REQUEST_GRANTED: {
                        Log.d("****","AUDIOFOCUS_REQUEST_GRANTED");
                        Word word = words.get(position);
                        m  = MediaPlayer.create(getActivity(),word.getmAudioResourceId());
                        m.start();
                        m.setOnCompletionListener(mCompletionListener);

                    }


                }


            }
        });
        return rootView;

    }

    public void releaseMediaPlayer() {

        if (m != null) {
            m.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            m = null;
            mAudioManager.abandonAudioFocusRequest(mFocusRequest);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}