package com.example.android.miwok;

import android.app.Activity;
import android.media.Image;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    private final int colorResourceId;

    public WordAdapter(Activity context, ArrayList<Word>  words,int colorID) {
        super(context,0, words);
        colorResourceId= colorID;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(convertView==null){
            Log.d("Word","In the convertview");
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Word currentWord = getItem(position);
        assert listItemView != null;
        TextView nameTextView = listItemView.findViewById(R.id.default_text_view);
        assert currentWord != null;
        nameTextView.setText(currentWord.getmDefaultTranslation());

        TextView  miwokTextView = listItemView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(currentWord.getmMiwokTranslation());

        ImageView miwokIcon = listItemView.findViewById(R.id.icon);
        if (currentWord.hasImage()){

            miwokIcon.setImageResource(currentWord.getmImageResourceId());
        }
        else{
            miwokIcon.setVisibility(View.GONE);
        }
        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(),colorResourceId);
        textContainer.setBackgroundColor(color);


        return listItemView;
    }


}
