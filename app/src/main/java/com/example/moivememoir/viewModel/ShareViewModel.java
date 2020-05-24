package com.example.moivememoir.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moivememoir.entities.Person;

public class ShareViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private Person user;

    public ShareViewModel(){
        mText = new MutableLiveData<>();
    }
    public void setMessage(String message) {
        mText.setValue(message);
    }
    public void setUser(Person user){
        this.user = user;
    }

    public LiveData<String> getText() {
        return mText;
    }
}
