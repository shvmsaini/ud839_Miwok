package com.example.android.miwok;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;


public class PhraseFragment extends Fragment {
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
//                m.pause();
//                m.seekTo(0);
                m.stop();
                releaseMediaPlayer();
                Log.d("*******","Lost audio focus Transient");

            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                Log.d("****","AUDIOFOCUS_REQUEST_GAIN");
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
//                m.start();
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

    public PhraseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.word_list, container, false);
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("Where are you going?", "minto wuksus",R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinn ә oyaase'n ә",R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...",R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchiachit",R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?",R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’әәnәm",R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm",R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis",R.raw.phrase_lets_go));
        words.add(new Word("Come here.","әnni'nem",R.raw.phrase_come_here));
        WordAdapter itemsAdapter =
                new WordAdapter( getActivity(),words,R.color.category_phrases_list);
        ListView listView =  rootView.findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word word = words.get(position);
                mAudioManager = (AudioManager) Objects.requireNonNull(getActivity()).getSystemService(Context.AUDIO_SERVICE);
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
        // If the media player is not null, then it may be currently playing a sound.
        if (m != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            if(m.isPlaying()) {
                m.stop();
            }
            m.reset();
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
        super.onStop(); releaseMediaPlayer();
    }
}