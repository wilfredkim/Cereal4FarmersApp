package com.example.gilbert.myproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SwipeRefreshLayout swipeContainer;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)

                // once the network request has completed successfully.

                fetchTimelineAsync();

            }

        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);


        try {
            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {

                try {
                    new GetLocationByCategoryClicked().execute();/**********/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                Context context = getApplicationContext();
                CharSequence text = "No internet connection.Check your internet settings...";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }


        } catch (Exception e) {
            e.printStackTrace();

        }


/*reg =(Button)findViewById(R.id.btn_reg);
        news =(Button)findViewById(R.id.btn_news);
        fert =(Button)findViewById(R.id.btn_fert);
        menu=(Button)findViewById(R.id.btn_menu);
        seeds =(Button)findViewById(R.id.btn_seed);
        pay=(Button)findViewById(R.id.btn_pay);
        reg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent wilfred = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(wilfred);


            }});
        fert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent wilfred = new Intent(getApplicationContext(), FertilizerbookingActivity.class);
                startActivity(wilfred);

            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent wilfred = new Intent(getApplicationContext(), NewsActivity.class);
                startActivity(wilfred);


            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showmenu();

            }
        });
        seeds.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent wilfred = new Intent(getApplicationContext(), SeedsbookingActivity.class);
                startActivity(wilfred);

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });*/



                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);


            }
    public void fetchTimelineAsync() {


     new GetLocationByCategoryClicked().execute();
        swipeContainer.setRefreshing(false);

      }






class GetLocationByCategoryClicked extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
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
                Toast.makeText(MainActivity.this, "No news in this Category", Toast.LENGTH_SHORT).show();
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
                    MainActivity.this, itemList,
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


    public void showmenu() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.row_layout, null);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("CHOOSE MENU WANT TO SEE");
        alertDialogBuilder.setView(promptView);
        Button seed = (Button) promptView.findViewById(R.id.btnseed);
        Button ferts = (Button) promptView.findViewById(R.id.btnfert);
        ferts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent wilfred = new Intent(getApplicationContext(), FertmenuActivity.class);
                startActivity(wilfred);


            }
        });
        seed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent wilfred = new Intent(getApplicationContext(), SeedsmenuActivity.class);
                startActivity(wilfred);


            }
        });
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

            @Override
            public void onBackPressed() {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    super.onBackPressed();
                }
            }

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
                if (id == R.id.action_about) {
                    Intent ww = new Intent(getApplicationContext(),AboutActivity.class);
                    startActivity(ww);
                    return true;
                }
                if(id == R.id.action_terms)
                {
                    Intent wilfred  =  new Intent(getApplicationContext(),TermsActivity.class);
                    startActivity(wilfred);
                }
                if (id == R.id.action_logout) {
                    System.exit(0);
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }

            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent wilfred = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(wilfred);
                    // Handle the camera action
                } else if (id == R.id.nav_help) {
                    Intent wilfred = new Intent(getApplicationContext(), AboutActivity.class);
                    startActivity(wilfred);


                } else if (id == R.id.nav_reg) {
                    Intent wilfred = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(wilfred);

                } else if (id == R.id.nav_fertilizer) {
                    Intent wilfred = new Intent(getApplicationContext(), FertilizerbookingActivity.class);
                    startActivity(wilfred);


                } else if (id == R.id.nav_seeds) {

                    Intent wilfred = new Intent(getApplicationContext(), SeedsbookingActivity.class);
                    startActivity(wilfred);

                }

                    else if (id == R.id.nav_inquire) {
                    Intent wilfred = new Intent(getApplicationContext(), InquireActivity.class);
                    startActivity(wilfred);

                } else if (id == R.id.nav_news) {
                    showmenu();

                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }


        }
