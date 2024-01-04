package com.example.soulmate.ui.healthTracking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HealthTrackingViewModel extends ViewModel {

    private static MutableLiveData<String> mText;

    public HealthTrackingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is health tracking fragment");
    }

    public static LiveData<String> getText() {
        return mText;
    }
}
