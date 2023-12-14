package com.example.soulmate.ui.medicalHistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MedicalHistoryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MedicalHistoryViewModel () {
        mText = new MutableLiveData<> ();
        mText.setValue ( "This is medical history fragment" );
    }

    public LiveData<String> getText () {
        return mText;
    }
}