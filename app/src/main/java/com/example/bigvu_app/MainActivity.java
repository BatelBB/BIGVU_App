package com.example.bigvu_app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<String> videoUrls = new ArrayList<>();

    private static final String EXTRA_NAME = "com.example.bigvu_app.name";
    private static final String EXTRA_DESCRIPTION = "com.example.bigvu_app.description";
    private static final String EXTRA_TEXT = "com.example.bigvu_app.text";
    private static final String EXTRA_VIDEO = "com.example.bigvu_app.video";


    private RequestQueue mQueue;
    public CustomItemList customItemList;

    SearchView searchView;
    Handler mainHandler;
    ProgressBar mProgressBar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        initSearchView();
        mQueue = Volley.newRequestQueue(this);
        mainHandler = new Handler();
        new fetchData().start();




        TextView textView = new TextView(this);
        textView.setTypeface(Typeface.DEFAULT_BOLD);


        listView = (ListView) findViewById(R.id.list);
        mProgressBar = (ProgressBar) findViewById(R.id.indeterminateBar);

        customItemList = new CustomItemList(this, names, descriptions, imageUrls);
        listView.setAdapter(customItemList);
//        customItemList.sort(new Comparator() {
//            @Override
//            public int compare(Object o, Object t1) {
//                return o;
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra(EXTRA_NAME, names.get(position));
                intent.putExtra(EXTRA_DESCRIPTION, descriptions.get(position));
                intent.putExtra(EXTRA_TEXT, texts.get(position));
                intent.putExtra(EXTRA_VIDEO, videoUrls.get(position));
                startActivity(intent);

            }
        });

    }


    class fetchData extends Thread {

        @Override
        public void run() {
            String url = "https://bigvu-interviews-assets.s3.amazonaws.com/workshops.json";
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            processResponse(response);
                            customItemList.notifyDataSetChanged();
                            mProgressBar.setVisibility(View.GONE);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            mQueue.add(request);
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
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void initSearchView(){
        searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<String> filteredDesc = new ArrayList<>();
                ArrayList<String> filteredNames = new ArrayList<>();
                ArrayList<String> filteredImageUrl = new ArrayList<>();
                for(int i=0; i< descriptions.size(); i++){
                    if(descriptions.get(i).toLowerCase().contains(s.toLowerCase())) {
                        filteredDesc.add(descriptions.get(i));
                        filteredNames.add(names.get(i));
                        filteredImageUrl.add(imageUrls.get(i));
                    }
                }
                CustomItemList newCustomList = new CustomItemList(MainActivity.this,
                        filteredNames, filteredDesc, filteredImageUrl);
                listView.setAdapter(newCustomList);

                return false;
            }
        });
    }


}

