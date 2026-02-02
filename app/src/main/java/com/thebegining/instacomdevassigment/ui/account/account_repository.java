package com.thebegining.instacomdevassigment.ui.account;

import com.thebegining.instacomdevassigment.ui.account.your_data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
public class account_repository {

    public interface Callback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    public void fetchyour_data(String id,Callback<ArrayList<your_data>> cb) {
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
                    cb.onError("Fetch failed: HTTP " + code);
                    return;
                }

                JSONArray arr = new JSONArray(sb.toString());
                ArrayList<your_data> temp = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    temp.add(new your_data(
                            o.optString("name"),
                            o.optString("message"),
                            o.optString("avatar"),
                            o.optString("id")
                    ));
                }

                cb.onSuccess(temp);

            } catch (Exception e) {
                cb.onError(e.getMessage());
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }


}
