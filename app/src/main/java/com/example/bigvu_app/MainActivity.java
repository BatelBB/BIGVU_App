package com.example.bigvu_app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;


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

    private static final String NAME = "name";
    private static final String IMAGE = "image";
    private static final String DESC = "description";
    private static final String TEXT = "text";
    private static final String VIDEO = "video";

    private RequestQueue mQueue;
    public CustomItemList customItemList;

    public SearchView searchView;
    public ProgressBar mProgressBar;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Initialize the search view method
        initSearchView();
        //Adding new Volley request
        mQueue = Volley.newRequestQueue(this);
        //Calling the runnable data to fetch Json objects
        new fetchData().start();

        //List view component
        listView = (ListView) findViewById(R.id.list);
        //Progress bar component
        mProgressBar = (ProgressBar) findViewById(R.id.indeterminateBar);

        //initializing the custom made list and setting it as the adapter of the listview.
        customItemList = new CustomItemList(this, names, descriptions, imageUrls);
        listView.setAdapter(customItemList);

        //Supposed to be sort method
//        customItemList.sort(new Comparator() {
//            @Override
//            public int compare(Object o, Object t1) {
//                return o;
//            }
//        });

        //On click listener for list items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Opening a new activity and sending the data.
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra(EXTRA_NAME, names.get(position));
                intent.putExtra(EXTRA_DESCRIPTION, descriptions.get(position));
                intent.putExtra(EXTRA_TEXT, texts.get(position));
                intent.putExtra(EXTRA_VIDEO, videoUrls.get(position));
                startActivity(intent);
            }
        });
    }
    //Runnable anonymous class
    class fetchData extends Thread {
        @Override
        public void run() {
            //Fetching the json object.
            String url = "https://bigvu-interviews-assets.s3.amazonaws.com/workshops.json";
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //A method to deal with the JsonArray
                            processResponse(response);
                            //After changing the list, updating the adapter
                            customItemList.notifyDataSetChanged();
                            //Once the data is up the progress bar is gone.
                            mProgressBar.setVisibility(View.GONE);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            //Volley request queue
            mQueue.add(request);
        }
    }

    /**
     * A private class to handle the JsonArray object.
     * @param response The JsonArray object
     */
    private void processResponse(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                //Takes every single Json object from the file
                JSONObject element = response.getJSONObject(i);
                //Adda the relevant strings to the ArrayList
                names.add(element.getString(NAME));
                imageUrls.add(element.getString(IMAGE));
                descriptions.add(element.getString(DESC));
                texts.add(element.getString(TEXT));
                videoUrls.add(element.getString(VIDEO));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * A private method to handle the search view
     */
    private void initSearchView(){
        //Initializes the search view component
        searchView = (SearchView) findViewById(R.id.searchView);
        //A listener to query texts changes.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Temp ArrayLists for the relevant search result
                ArrayList<String> filteredDesc = new ArrayList<>();
                ArrayList<String> filteredNames = new ArrayList<>();
                ArrayList<String> filteredImageUrl = new ArrayList<>();

                //If the description list contains the search string,
                //adds the different search items to the relevant ArrayLists.
                for(int i=0; i< descriptions.size(); i++){
                    if(descriptions.get(i).toLowerCase().contains(s.toLowerCase())) {
                        filteredDesc.add(descriptions.get(i));
                        filteredNames.add(names.get(i));
                        filteredImageUrl.add(imageUrls.get(i));
                    }
                }
                //A temp custom list to update the list view.
                CustomItemList newCustomList = new CustomItemList(MainActivity.this,
                        filteredNames, filteredDesc, filteredImageUrl);
                listView.setAdapter(newCustomList);

                return false;
            }
        });
    }
}

