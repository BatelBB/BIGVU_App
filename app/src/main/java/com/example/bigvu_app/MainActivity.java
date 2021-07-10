package com.example.bigvu_app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<String> videoUrls = new ArrayList<>();
    public ArrayList<ImageView> mImageViews = new ArrayList<>();

    private RequestQueue mQueue;
    public CustomItemList customItemList;

    Handler mainHandler = new Handler();
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        new fetchData().start();

//        jsonParse();

        TextView textView = new TextView(this);
        textView.setTypeface(Typeface.DEFAULT_BOLD);


        ListView listView=(ListView)findViewById(R.id.list);

        customItemList = new CustomItemList(this, names, descriptions, mImageViews);
        listView.setAdapter(customItemList);
        Log.v("TAG","DONE");
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                //Toast.makeText(getApplicationContext(),"You Selected "+countryNames[position-1]+ " as Country",Toast.LENGTH_SHORT).show();        }
//            });
//        }
    }


    class fetchData extends Thread {

        @Override
        public void run() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBar = new ProgressBar(MainActivity.this);
                    mProgressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                }
            });

            String url = "https://bigvu-interviews-assets.s3.amazonaws.com/workshops.json";
            JsonArrayRequest request = new JsonArrayRequest (Request.Method.GET, url, null,
                    new Response.Listener<JSONArray >() {
                        @Override
                        public void onResponse(JSONArray response) {
                            processResponse(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);


            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mProgressBar.isShown())
                        mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void processResponse(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject element = response.getJSONObject(i);

                names.add(element.getString("name"));
                imageUrls.add(element.getString("image"));
                descriptions.add(element.getString("description"));
                texts.add(element.getString("text"));
                videoUrls.add(element.getString("video"));

            }
            for(int i =0; i< imageUrls.size(); i++){
                ImageView tempView = new ImageView(this);
                Picasso.get().load(imageUrls.get(i)).into(tempView);
                mImageViews.add(tempView);
            }
            customItemList.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("TAG", e.getMessage());

        }
    }}

