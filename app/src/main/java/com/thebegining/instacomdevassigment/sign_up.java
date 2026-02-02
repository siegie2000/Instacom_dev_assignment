package com.thebegining.instacomdevassigment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.thebegining.instacomdevassigment.databinding.SignUpBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class sign_up extends Activity {



    private SignUpBinding binding;
    Button sign_up;
    EditText email;
    EditText password;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sign_up = binding.registerSignUp;
        email = binding.registerEmail;
        password = binding.registerPassword;
        imageView = binding.registerImageview;

        imageView.setImageResource(R.drawable.mainlogo);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                if (usernameText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(sign_up.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                createUserHttp(usernameText, passwordText);
            }
        });
    }

    private void createUserHttp(String username, String passwordText) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("https://697b55d90e6ff62c3c5bc0f9.mockapi.io/instacom/users");
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                Random rand = new Random();
                int randomInt = rand.nextInt();
                JSONObject json = new JSONObject();
                json.put("username", username);
                json.put("password", passwordText);
                json.put("userid", String.valueOf(randomInt));

                byte[] body = json.toString().getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(body);
                os.flush();
                os.close();

                int code = conn.getResponseCode();
                Log.d("error_signup", "HTTP code: " + code);

                InputStream is = (code >= 200 && code < 300)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                String response = sb.toString();
                Log.d("error_signup", "Response: " + response);

                if (code < 200 || code >= 300) {
                    runOnUiThread(() ->
                            Toast.makeText(sign_up.this, "Signup failed: " + code, Toast.LENGTH_LONG).show()
                    );
                    return;
                }

                JSONObject createdUser = new JSONObject(response);

                String Username = createdUser.optString("username");
                String name = createdUser.optString("name");
                String id = createdUser.optString("userid");
                String id_ = createdUser.optString("id");

                runOnUiThread(() -> {
                    SharedPreferences prefs = getSharedPreferences("instacom", MODE_PRIVATE);
                    prefs.edit()
                            .putString("username", Username)
                            .putString("name", name)
                            .putString("userId",id)
                            .putString("id",id_)


                            .apply();

                    Toast.makeText(sign_up.this, "User created!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(sign_up.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });

            } catch (Exception e) {
                Log.e("error_signup", "Create user failed", e);
                runOnUiThread(() ->
                        Toast.makeText(sign_up.this, "Signup failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
}
