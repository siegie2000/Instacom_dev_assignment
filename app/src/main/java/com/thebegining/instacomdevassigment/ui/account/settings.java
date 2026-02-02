package com.thebegining.instacomdevassigment.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;
import com.thebegining.instacomdevassigment.databinding.SettingsBinding;
import com.thebegining.instacomdevassigment.sign_in;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class settings extends Activity {
    ArrayList<your_data> feed = new ArrayList<>();

    SettingsBinding binding;
    Button sign_out;
    Button delete;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

            setActionBar(binding.materialToolbar);


        sign_out=binding.signOut;
        delete=binding.deleteUserAccount;
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("instacom", MODE_PRIVATE);
                prefs.edit()
                        .putString("userId", null)
                        .putString("username", null)
                        .putString("name", null)
                        .apply();
                Intent intent = new Intent(getApplicationContext(), sign_in.class);
                startActivity(intent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new AlertDialog.Builder(settings.this)
                        .setTitle("Are you sure")
                        .setMessage("You want to delete your account")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("instacom", Context.MODE_PRIVATE);

                                        String name = prefs.getString("name", "");
                                        String id = prefs.getString("userId", "");
                                        fetchyour_data(id);

                                        delete_user_feed(feed);

                                        delete_user_account();

                                        SharedPreferences prefs2 = getSharedPreferences("instacom", MODE_PRIVATE);
                                        prefs2.edit()
                                                .putString("userId", null)
                                                .putString("username", null)
                                                .putString("name", null)
                                                .apply();

                                        Intent intent = new Intent(getApplicationContext(), sign_in.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                      .show();





            }
        });


    }

    public void delete_user_account(){
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {

                SharedPreferences prefs = getApplicationContext().getSharedPreferences("instacom", Context.MODE_PRIVATE);

                String id = prefs.getString("id", "");
                URL url = new URL("https://697b55d90e6ff62c3c5bc0f9.mockapi.io/instacom/users/"+id);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Content-Type", "application/json");

                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);





                int code = conn.getResponseCode();
                Log.d("Error_updating", "HTTP code: " + code);

                InputStream is = (code >= 200 && code < 300)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                String response = sb.toString();
                Log.d("Error_updating", "Response: " + response);









            } catch (Exception e) {

            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
    public void delete_user_feed(ArrayList<your_data> data ){
        Log.d("Error_updating2", "feed_size: " + String.valueOf(data.size()));
        new Thread(() -> {
            Log.d("Error_updating1", "feed_size: " + String.valueOf(data.size()));
            HttpURLConnection conn = null;
            try {

                for(int i = 0; i < data.size(); i++){

                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("instacom", Context.MODE_PRIVATE);
                        Log.d("Error_updating", "feed_size: " + String.valueOf(data.size()));
                        String id = prefs.getString("userId", "");
                        URL url = new URL("https://697b55d90e6ff62c3c5bc0f9.mockapi.io/instacom/news/"+data.get(i).getId());
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("DELETE");
                        conn.setRequestProperty("Content-Type", "application/json");

                        conn.setConnectTimeout(10000);
                        conn.setReadTimeout(10000);





                        int code = conn.getResponseCode();
                        Log.d("Error_updating", "HTTP code: " + code);

                        InputStream is = (code >= 200 && code < 300)
                                ? conn.getInputStream()
                                : conn.getErrorStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) sb.append(line);
                        reader.close();

                        String response = sb.toString();
                        Log.d("Error_updating", "Response: " + response);
                    }
              ;











            } catch (Exception e) {

            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
    public void fetchyour_data(String id) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("https://697b55d90e6ff62c3c5bc0f9.mockapi.io/instacom/news?userid="+id);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                int code = conn.getResponseCode();
                InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                if (code < 200 || code >= 300) {

                    return;
                }

                JSONArray arr = new JSONArray(sb.toString());


                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    feed.add(new your_data(
                            o.optString("name"),
                            o.optString("message"),
                            o.optString("avatar"),
                            o.optString("id")
                    ));
                }

                delete_user_feed(feed);


            } catch (Exception e) {

            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
    private void setActionBar(MaterialToolbar materialToolbar) {
        materialToolbar.setTitle("Settings");
    }
}
