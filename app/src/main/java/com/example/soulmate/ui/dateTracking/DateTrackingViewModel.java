package com.example.soulmate.ui.dateTracking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DateTrackingViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DateTrackingViewModel () {
        mText = new MutableLiveData<> ();
        mText.setValue ( "This is date tracking fragment" );
    }

    public LiveData<String> getText () {
        return mText;
    }
}