package com.thebegining.instacomdevassigment.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private final news_repository repo = new news_repository();

    private final MutableLiveData<ArrayList<news>> newsLive = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loadingLive = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorLive = new MutableLiveData<>(null);

    public LiveData<ArrayList<news>> news() { return newsLive; }
    public LiveData<Boolean> loading() { return loadingLive; }
    public LiveData<String> error() { return errorLive; }

    public void loadpost() {
        loadingLive.postValue(true);
        repo.fetchNews(new news_repository.Callback<ArrayList<news>>() {
            @Override public void onSuccess(ArrayList<news> data) {
                loadingLive.postValue(false);
                newsLive.postValue(data);
            }
            @Override public void onError(String error) {
                loadingLive.postValue(false);
                errorLive.postValue(error);
            }
        });
    }

    public void createPost(String id,String name, String message) {
        loadingLive.postValue(true);
        repo.createPost(id ,name, message, new news_repository.Callback<Void>() {
            @Override public void onSuccess(Void unused) {
                loadingLive.postValue(false);
                loadpost();
            }
            @Override public void onError(String error) {
                loadingLive.postValue(false);
                errorLive.postValue(error);
            }
        });
    }
}
