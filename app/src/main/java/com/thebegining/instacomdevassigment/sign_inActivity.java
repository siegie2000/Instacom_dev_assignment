package com.thebegining.instacomdevassigment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.thebegining.instacomdevassigment.databinding.SignInBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class sign_inActivity extends Activity {
    private SignInBinding binding;
    Button sign_in;
    Button sign_up;
    EditText email;
    EditText password;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = SignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        logo=binding.logoSignIn;
        sign_in=binding.signIn;
        sign_up=binding.signUp;
        email=binding.email;
        password=binding.password;
        SharedPreferences prefs2 =getSharedPreferences("instacom", Context.MODE_PRIVATE);


        String signin = prefs2.getString("name", null);

        if (signin!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        logo.setImageResource(R.drawable.mainlogo);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_text=email.getText().toString();
                String password_text=password.getText().toString();
                if (email_text.isEmpty() || password_text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    HttpURLConnection conn = null;
                    try {
                        String urlStr =
                                "https://697b55d90e6ff62c3c5bc0f9.mockapi.io/instacom/users?username="
                                        + URLEncoder.encode(email_text, "UTF-8");

                        URL url = new URL(urlStr);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(10000);
                        conn.setReadTimeout(10000);

                        int code = conn.getResponseCode();
                        Log.d("error_sign_in", "HTTP code: " + code);

                        InputStream is = (code >= 200 && code < 300)
                                ? conn.getInputStream()
                                : conn.getErrorStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) sb.append(line);
                        reader.close();

                        String response = sb.toString();
                        Log.d("error_sign_in", "Response: " + response);

                        if (code < 200 || code >= 300) {
                            runOnUiThread(() ->
                                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show()
                            );
                            return;
                        }

                        JSONArray array = new JSONArray(response);

                        if (array.length() == 0) {
                            runOnUiThread(() ->
                                    Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show()
                            );
                            return;
                        }

                        JSONObject user = array.getJSONObject(0);

                        String storedPassword = user.optString("password");

                        String name = user.optString("name");
                        String id = user.optString("id");
                        if (!password_text.equals(storedPassword)) {
                            runOnUiThread(() ->
                                    Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show()
                            );
                            return;
                        }


                        String userId = user.optString("userid");
                        String username = user.optString("username");

                        runOnUiThread(() -> {
                            SharedPreferences prefs = getSharedPreferences("instacom", MODE_PRIVATE);
                            prefs.edit()
                                    .putString("userId", userId)
                                    .putString("username", username)
                                    .putString("name", name)
                                    .putString("id", id)

                                    .apply();

                            Log.d("testing_id",userId);
                            Toast.makeText(getApplicationContext(), "Signed in!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        });

                    } catch (Exception e) {
                        Log.e("error_sign_in", "Login failed", e);
                        runOnUiThread(() ->
                                Toast.makeText(getApplicationContext(), "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                        );
                    } finally {
                        if (conn != null) conn.disconnect();
                    }
                }).start();

            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), sign_up.class);
                startActivity(intent);
            }
        });





    }

}
