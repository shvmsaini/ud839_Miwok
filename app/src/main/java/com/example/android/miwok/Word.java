package com.example.android.miwok;

public class Word {
    private String mDefaultTranslation;
    private String mMiwokTranslation;

    private static final int NO_IMAGE_PROVIDED = -1;
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private int mAudioResourceId;
    public Word (String DT, String MT,int resourceID,int audioID){
        mDefaultTranslation = DT;
        mMiwokTranslation = MT;
        mImageResourceId = resourceID;
        mAudioResourceId = audioID;
    }
    public Word (String DT, String MT,int audioID){
        mDefaultTranslation = DT;
        mMiwokTranslation = MT;
        mAudioResourceId = audioID;

    }



    public String getmMiwokTranslation(){
        return mMiwokTranslation;
    }

    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }
    public int getmAudioResourceId() {
        return mAudioResourceId;
    }

    public boolean hasImage(){
        return mImageResourceId !=NO_IMAGE_PROVIDED;
    }
}
