package com.thebegining.instacomdevassigment.ui.home;


import android.app.Dialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thebegining.instacomdevassigment.R;
import com.thebegining.instacomdevassigment.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private adapter_feed adapter;
    private final ArrayList<news> newsList = new ArrayList<>();
    private HomeViewModel viewmodel;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FloatingActionButton fab;
    TextView empty;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewmodel = new ViewModelProvider(this).get(HomeViewModel.class);

        empty=binding.emptyRecyclerview;
        fab=binding.postFab;
        recyclerView=binding.homeFeed;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new adapter_feed(newsList);
        recyclerView.setAdapter(adapter);

        if (newsList.isEmpty()){
            empty.setVisibility(View.VISIBLE);
        }
        progressBar=binding.progressBar;
        viewmodel.news().observe(getViewLifecycleOwner(), items -> {

            newsList.clear();
            newsList.addAll(items);
            Collections.reverse(newsList);
            adapter.notifyDataSetChanged();

        });

        viewmodel.error().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.d("error",error);

                Toast.makeText(requireContext(), "Unable to load", Toast.LENGTH_LONG).show();
            }
        });
        viewmodel.loading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){

                    progressBar.setVisibility(View.VISIBLE);
                }else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        viewmodel.loadpost();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostDialog();
            }
        });


        return root;
    }

    private void showPostDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_home);
        dialog.show();

        EditText messageEt = dialog.findViewById(R.id.message);
        Button postBtn = dialog.findViewById(R.id.post_btn);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = requireContext().getSharedPreferences("instacom", Context.MODE_PRIVATE);
                String name = prefs.getString("name", "");
                String id = prefs.getString("userId", "");
                Log.d("testing_id_home",id);
                String msg = messageEt.getText().toString().trim();
                if (msg.isEmpty()) {
                    Toast.makeText(requireContext(), "Message required", Toast.LENGTH_SHORT).show();
                    return;
                }
                empty.setVisibility(View.GONE);

                dialog.dismiss();
                viewmodel.createPost(id,name, msg);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
