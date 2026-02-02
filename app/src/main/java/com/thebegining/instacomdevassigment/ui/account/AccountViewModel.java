package com.thebegining.instacomdevassigment.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;





import java.util.ArrayList;

public class AccountViewModel extends ViewModel {
    private final account_repository repo = new account_repository();

    private final MutableLiveData<ArrayList<your_data>> your_data = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loadingLive = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorLive = new MutableLiveData<>(null);

    public LiveData<ArrayList<your_data>> your_data_live() { return your_data; }
    public LiveData<Boolean> loading() { return loadingLive; }
    public LiveData<String> error() { return errorLive; }

    public void load_your_feed(String id) {
        loadingLive.postValue(true);
        repo.fetchyour_data(id,new account_repository.Callback<ArrayList<your_data>>() {
            @Override public void onSuccess(ArrayList<your_data> data) {
                loadingLive.postValue(false);
                your_data.postValue(data);
            }
            @Override public void onError(String error) {
                loadingLive.postValue(false);
                errorLive.postValue(error);
            }
        });
    }


}
