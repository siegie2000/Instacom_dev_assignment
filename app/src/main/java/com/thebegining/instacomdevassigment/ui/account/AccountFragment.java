package com.thebegining.instacomdevassigment.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.thebegining.instacomdevassigment.R;
import com.thebegining.instacomdevassigment.databinding.FragmentAccountBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    RecyclerView recyclerView;
    private your_post_adapter adapter;
    private final ArrayList<your_data> account_data = new ArrayList<>();
    ImageButton settings;
    ProgressBar progressBar;
    TextView empty,name;
    ImageView profile_pic;
    String prof, username;
    private boolean done=false;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        settings=binding.settingBtn;

        empty=binding.emptyRecyclerviewAccount;

        profile_pic=binding.accountProfilePic;
        name=binding.accountName;
        progressBar=binding.progressbarAccount;
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), settings.class);
                startActivity(intent);
            }
        });
        SharedPreferences prefs = requireContext().getSharedPreferences("instacom", Context.MODE_PRIVATE);
        String id = prefs.getString("userId", "");

        recyclerView=binding.accountRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new your_post_adapter(account_data,requireContext());
        recyclerView.setAdapter(adapter);

        fetchyour_data();

        accountViewModel.your_data_live().observe(getViewLifecycleOwner(), items -> {
            account_data.clear();
            account_data.addAll(items);
            adapter.notifyDataSetChanged();
            if (done && account_data.isEmpty()){
                empty.setVisibility(View.VISIBLE);
            }else {
                Picasso.get().load(prof).placeholder(R.drawable.logo).into(profile_pic);
                name.setText(username);
            }
        });

        accountViewModel.error().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.d("error",error);
                Toast.makeText(requireContext(), "Unable to load", Toast.LENGTH_LONG).show();
            }
        });
        accountViewModel.loading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    progressBar.setVisibility(View.VISIBLE);
                }else {
                    done=true;

                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        accountViewModel.load_your_feed(id);





        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void fetchyour_data( ) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                SharedPreferences prefs = requireContext().getSharedPreferences("instacom", Context.MODE_PRIVATE);
                String name5 = prefs.getString("username", "");
                String urlStr =
                        "https://697b55d90e6ff62c3c5bc0f9.mockapi.io/instacom/users?username="
                                + URLEncoder.encode(name5, "UTF-8");

                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                int code = conn.getResponseCode();
                Log.d("error_account", "HTTP code: " + code);

                InputStream is = (code >= 200 && code < 300)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                String response = sb.toString();
                Log.d("error_account", "Response: " + response);

                if (code < 200 || code >= 300) {

                    return;
                }

                JSONArray array = new JSONArray(response);

                if (array.length() == 0) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(requireActivity(), "User not found", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                JSONObject user = array.getJSONObject(0);







                String avatar = user.optString("avatar");
                String name_ = user.optString("name");

                prof=avatar;
                username=name_;



            } catch (Exception e) {
                Log.e("error", "account retrieval failed", e);

            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();

    }


}