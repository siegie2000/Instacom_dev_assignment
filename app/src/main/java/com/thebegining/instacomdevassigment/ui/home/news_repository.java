package com.thebegining.instacomdevassigment.ui.home;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
public class news_repository {

    public interface Callback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    public void fetchNews(Callback<ArrayList<news>> cb) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("https://697b55d90e6ff62c3c5bc0f9.mockapi.io/instacom/news");
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
                ArrayList<news> temp = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    temp.add(new news(
                            o.optString("name"),
                            o.optString("message"),
                            o.optString("avatar"),
                            o.optString("createdAt")

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

    public void createPost(String id,String name, String message, Callback<Void> cb) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("https://697b55d90e6ff62c3c5bc0f9.mockapi.io/instacom/news");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                JSONObject json = new JSONObject();
                json.put("message", message);
                json.put("name", name);
                json.put("userid",id);

                byte[] body = json.toString().getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(body);
                os.flush();
                os.close();

                int code = conn.getResponseCode();
                if (code < 200 || code >= 300) {
                    cb.onError("Post failed: HTTP " + code);
                    return;
                }

                cb.onSuccess(null);

            } catch (Exception e) {
                cb.onError(e.getMessage());
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
}
