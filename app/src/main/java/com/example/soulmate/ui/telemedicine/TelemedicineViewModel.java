package com.example.soulmate.ui.telemedicine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TelemedicineViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public TelemedicineViewModel () {
        mText = new MutableLiveData<> ();
        mText.setValue ( "This is telemedicine fragment" );
    }

    public LiveData<String> getText () {
        return mText;
    }
}