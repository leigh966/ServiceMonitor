package com.itsthenikolai.servicemonitor.ui.add_device;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddDeviceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddDeviceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Add Device");
    }

    public LiveData<String> getText() {
        return mText;
    }
}