package com.thebegining.instacomdevassigment.ui.account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.thebegining.instacomdevassigment.R;
import com.thebegining.instacomdevassigment.sign_inActivity;
import com.thebegining.instacomdevassigment.sign_up;
import com.thebegining.instacomdevassigment.ui.home.adapter_feed;
import com.thebegining.instacomdevassigment.ui.home.news_repository;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class your_post_adapter extends RecyclerView.Adapter<your_post_adapter.Feed> {
private ArrayList<your_data> list;
private Context context;


public your_post_adapter(ArrayList<your_data> list, Context context) {
    this.context=context;
    this.list = list;

}

@NonNull
@Override
public Feed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_feed_item, parent, false);
    return new your_post_adapter.Feed(v);
}

@Override
public void onBindViewHolder(@NonNull your_post_adapter.Feed holder, @SuppressLint("RecyclerView") int position) {
    your_data item = list.get(position);
    holder.name.setText(item.getName());
    holder.message.setText(item.getMessage());
    String dialog_message=item.getMessage();
    Log.d("error_your_data","adapter item name: "+item.getName());
    Log.d("error_your_data","adapter item message: "+item.getMessage());
    Log.d("error_your_data id","adapter item message: "+item.getId());


    holder.id_hidden.setText(item.getId());

    Picasso.get().load(item.getAvatar()).placeholder(R.drawable.logo).into(holder.avatar);
    holder.btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            PopupMenu menu = new PopupMenu(context, v);
            menu.getMenuInflater().inflate(R.menu.feed_menu, menu.getMenu());

            menu.setOnMenuItemClickListener(item2 -> {
                if (item2.getItemId() == R.id.edit) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_home);
                    dialog.show();

                    EditText messageEt = dialog.findViewById(R.id.message);
                    Button postBtn = dialog.findViewById(R.id.post_btn);

                    messageEt.setText(dialog_message);
                    postBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences prefs = context.getSharedPreferences("instacom", Context.MODE_PRIVATE);
                            String name = prefs.getString("name", "");
                            String id = prefs.getString("userId", "");
                            Log.d("Error_updating",id);
                            String msg = messageEt.getText().toString().trim();
                            edit_post(msg,holder.id_hidden.getText().toString());
                            if (msg.isEmpty()) {
                                Toast.makeText(context, "Message required", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            item.setMessage(msg);

                            notifyItemChanged(position);
                            dialog.dismiss();

                        }
                    });


                    return true;
                } else if (item2.getItemId() == R.id.delete) {
                    new AlertDialog.Builder(context)
                            .setTitle("Are you sure")
                            .setMessage("You want to delete your post")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("Error_deleting=  ", String.valueOf(holder.getBindingAdapterPosition()));
                                    delete_post(holder.id_hidden.getText().toString());
                                    list.remove(holder.getBindingAdapterPosition());
                                    notifyItemRemoved(holder.getBindingAdapterPosition());
                                }
                            })
                            .show();




                    return true;
                }
                return false;
            });

            menu.show();

        }
    });
}
    public void edit_post(String message,String id) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                Log.d("Error_updating= id ",id);
                URL url = new URL("https://697b55d90e6ff62c3c5bc0f9.mockapi.io/instacom/news/"+id);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                JSONObject json = new JSONObject();
                json.put("message", message);

                byte[] body = json.toString().getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(body);
                os.flush();
                os.close();
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

                if (code < 200 || code >= 300) {

                    Log.d("Error_updating",String.valueOf(code));

                }






            } catch (Exception e) {

            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
    public void  delete_post(String id) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                Log.d("Error_updating= id ",id);
                URL url = new URL("https://697b55d90e6ff62c3c5bc0f9.mockapi.io/instacom/news/"+id);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Content-Type", "application/json");

                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);





                int code = conn.getResponseCode();
                Log.d("Error_deleting", "HTTP code: " + code);

                InputStream is = (code >= 200 && code < 300)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                String response = sb.toString();
                Log.d("Error_deleting", "Response: " + response);









            } catch (Exception e) {
                Log.d("Error_deleting", "Response: " + e);
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
@Override
public int getItemCount() {  Log.d("error_your_data","Adapter size: "+ list.size());

    return list.size();


}

static class Feed extends RecyclerView.ViewHolder {
    TextView name, message,id_hidden;
    ImageView avatar;
    ImageButton btn;
    Feed(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        message = itemView.findViewById(R.id.message);
        avatar = itemView.findViewById(R.id.avatar);
        btn=itemView.findViewById(R.id.feed_options);
        id_hidden=itemView.findViewById(R.id.id_hidden);
    }
}
}
