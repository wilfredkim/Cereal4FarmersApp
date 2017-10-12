package com.example.gilbert.myproject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class NewsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {



    ArrayList<String> location_lat;
    ArrayList<String> location_long;
    String[] date;
    String[] news;

    public static final String TAG_LONGITUDE="Longitude";
    public static final String TAG_PID="num";
   // ArrayList<HashMap<String, String>> itemList;
    String latitude;
    String longitude;
    public static final String TAG_ITEMEVENT="Event";
    public static final String TAG_ITEMDATE="Date";
    public static final String TAG_ITEMNEWS="News";
    //public static final String TAG_LATITUDE="latitude";
    private ProgressDialog pDialog;
    private static final String  url_news = "http://192.168.43.189:9000/backnews";
   // public static final String TAG_PID="num";

    JSONObject json;
    JSONArray jsonArray;
    final Context context = this;

ArrayList<HashMap<String , String>>  itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        new GetLocationByCategoryClicked().execute();



    }
    class GetLocationByCategoryClicked extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(NewsActivity.this);
                pDialog.setMessage("Loading news.Please wait...");
                pDialog.setIndeterminate(false);

                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected Void doInBackground(Void... arg0) {




                    JSONParser jsonParser = new JSONParser();

                    // Creating service handler class instance
                    List<NameValuePair> news = new ArrayList<NameValuePair>();
                    // getting JSON string from URL

                    JSONArray jsonArray;
                jsonArray = jsonParser.makeHttpRequest(Utility.url_news, "GET", news);
                //jsonArray = jsonParser.makeHttpRequest(url_news, "GET", news);

                    itemList = new ArrayList<HashMap<String, String>>();
                    // Check your log cat for JSON reponse
                    Log.d("news lists are..", jsonArray.toString());

                    if (jsonArray != null) {


                        try {
                            if (jsonArray.length() > 0) {

                                // looping through All Products
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);

                                    // Storing each json item in variable
                                    String id = c.getString(TAG_PID);

                                    String ev =c.getString(TAG_ITEMEVENT);
                                    String itemName = c.getString(TAG_ITEMDATE);
                                    String itemDescription = c.getString(TAG_ITEMNEWS);
                               /* String itemLatitude=c.getString(TAG_LATITUDE);
                                String itemLongitude=c.getString(TAG_LONGITUDE);
*/

                                    // creating new HashMap
                                    HashMap<String, String> map = new HashMap<String, String>();

                                    // adding each child node to HashMap key => value
                                    map.put(TAG_PID, id);
                                    map.put(TAG_ITEMEVENT,ev);
                                    map.put(TAG_ITEMDATE, itemName);
                                    map.put(TAG_ITEMNEWS, itemDescription);
                               /* map.put(TAG_LATITUDE,itemLatitude);
                                map.put(TAG_LONGITUDE,itemLongitude);
*/
                                    // adding HashList to ArrayList

                                    itemList.add(map);


                                }

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("out of array", "out of Arrayy");


                    } else {
                        Toast.makeText(NewsActivity.this, "No news in this Category", Toast.LENGTH_SHORT).show();
                    }
                    return null;


            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                pDialog.dismiss();
/*
                if(jsonArray == null){
                    Toast.makeText(getApplicationContext(), "Server not available,try again later", Toast.LENGTH_LONG).show();


                }else{*/
                ListAdapter adapter = new SimpleAdapter(
                        NewsActivity.this, itemList,
                        R.layout.eventnews, new String[]{
                        TAG_ITEMEVENT,TAG_ITEMDATE, TAG_ITEMNEWS, },
                        new int[]{R.id.event,R.id.date, R.id.news});

                try {
                    if (adapter.getCount()!=0) {


                        final ListView listView = (ListView) findViewById(R.id.eventlist);

                        listView.setAdapter(adapter);


                    }else {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);

                        // set title
                        alertDialogBuilder.setTitle("News");
                        alertDialogBuilder.setCancelable(true);
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("There are no news in this Choosen category")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }}



        //}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
        /*if (TextUtils.isEmpty(newText))
        {
            listView.clearTextFilter();
        }
        else
        {
            listView.setFilterText(newText.toString());
        }*/

return true;


        }}

