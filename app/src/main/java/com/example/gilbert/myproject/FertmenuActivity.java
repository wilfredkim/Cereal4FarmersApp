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

public class FertmenuActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static final String TAG_ITEMBRAND = "Brand";
    public static final String TAG_ITEMQ = "Quantity";
    public static final String TAG_PRICE = "Price";
    //public static final String TAG_LONGITUDE="Longitude";
    private ProgressDialog pDialog;
   // private static final String url_news = "http://192.168.43.189:9000/backnews";

    JSONObject json;
    JSONArray jsonArray;
    public static final String TAG_PID = "num";
    ArrayList<HashMap<String, String>> itemList;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertmenu);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       */

        new GetLocationByCategoryClicked().execute();

    }

    class GetLocationByCategoryClicked extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FertmenuActivity.this);
            pDialog.setMessage("Loading menu.Please wait...");
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
            jsonArray = jsonParser.makeHttpRequest(Utility.url_fert, "GET", news);
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
                            String itemName = c.getString(TAG_ITEMBRAND);
                            String itemDescription = c.getString(TAG_PRICE);
                            String itemLatitude=c.getString(TAG_ITEMQ);
                                //String itemLongitude=c.getString(TAG_LONGITUDE);


                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_PID, id);
                            map.put(TAG_ITEMBRAND, itemName);
                            map.put(TAG_ITEMQ,itemLatitude);
                            map.put(TAG_PRICE,itemDescription);
                               // map.put(TAG_LONGITUDE,itemLongitude);

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
                Toast.makeText(FertmenuActivity.this, "No news in this Category", Toast.LENGTH_SHORT).show();
            }
            return null;


        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

           /* if(jsonArray == null){
                Toast.makeText(getApplicationContext(), "Server not available,try again later", Toast.LENGTH_LONG).show();

            } else {*/
                ListAdapter adapter = new SimpleAdapter(
                        FertmenuActivity.this, itemList,
                        R.layout.fertmenuevent, new String[]{
                        TAG_ITEMBRAND, TAG_ITEMQ,TAG_PRICE},
                        new int[]{R.id.brand, R.id.quantity,R.id.price});

                try {
                    if (adapter.getCount() != 0) {


                        final ListView listView = (ListView) findViewById(R.id.eventfert);

                        listView.setAdapter(adapter);


                    } else {
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

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
       // }

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


    }
}
