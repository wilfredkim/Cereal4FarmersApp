package com.example.gilbert.myproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.http.RequestQueue;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FertilizerbookingActivity extends AppCompatActivity {

    AlertDialog.Builder alert;
    ProgressDialog pDialog;
    Button insert;
    CheckBox checkBox;
    Spinner quantity, brand;
    EditText phone, describe, landsize, cultsize;
    //String Fert_brand,Quantity,Phone_number,Describe;

    public String Snumber, Sdes, Sbra, Squantity;


    JSONObject json;
    JSONParser jParser = new JSONParser();
    public static final String TAG_SUCCESS = "responseCode";
    String success;
    private static final String url_post_regDetails = "http://192.168.43.189:9000/viewFert";
    private static final String url_regDetails = "http://192.168.43.189:9000/regLogin";
    public SharedPreferences savedData;


    Context context;

    String Fert_brand, Quantity, Phone_number, Describe, Total, Csize;
    public SharedPreferences sharedPreferences;
    //public String myPreferences;
    //public  String Fbrand,Quant,Phone_numb;
    public static final String Fbrand = "Fert_brand";
    public static final String myPreferences = "mypref";
    public static final String Quant = "Quantity";
    public static final String usern = "username";
    public static final String id = "Idnumber";
    public static final String desc = "Describe";
    public static final String total = "Total";
    public static final String csize = "Csize";
    public static final String Phone_numb = "Phone_number";
    public static final String pp1 = "";
    String price;
    int order;
    int land;
    JSONParser jsonParser = new JSONParser();
    public static final String TAG_PID = "Ida";
    String latitude;
    String longitude;
    public static final String TAG_ITEMLAND = "Land_Size";
    public static final String TAG_ITEMID = "Id";
    public static final String TAG_ITEMSIZE = "Cultivation_Size";
    ArrayList<HashMap<String, String>> itemList;
    String reid, reland, recsize,reId;


    private static final String TAG_MESSAGE = "message";
    String Idnumber, Password, username;

    EditText Id, password, Username;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizerbooking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phone = (EditText) findViewById(R.id.phone);
        describe = (EditText) findViewById(R.id.des);
        // checkBox = (CheckBox) findViewById(R.id.check);
        brand = (Spinner) findViewById(R.id.fert_brand);
        quantity = (Spinner) findViewById(R.id.fert_qua);
        landsize = (EditText) findViewById(R.id.size);
        cultsize = (EditText) findViewById(R.id.csize);

        // savedData= PreferenceManager.getDefaultSharedPreferences(this);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.quantity12, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        quantity.setAdapter(adapter2);
        quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position) + " selected", Toast.LENGTH_LONG);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fert_brand, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        brand.setAdapter(adapter);
        brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position) + " selected", Toast.LENGTH_LONG);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        insert = (Button) findViewById(R.id.reg);

        // savedData = PreferenceManager.getDefaultSharedPreferences(this);


        insert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (phone.getText().toString().equals("") || describe.getText().toString().equals("") || landsize.getText().toString().equals("") || cultsize.getText().toString().equals("")) {
                    alert = new AlertDialog.Builder(FertilizerbookingActivity.this);
                    alert.setTitle("Details");
                    alert.setMessage("Please fill all the  details");
                    alert.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog a = alert.create();
                    a.show();


                } else if ((Integer.parseInt(quantity.getSelectedItem().toString())) > (Integer.parseInt(cultsize.getText().toString()))) {
                    alert = new AlertDialog.Builder(FertilizerbookingActivity.this);
                    alert.setTitle("Orders!");
                    alert.setMessage("Sorry customer you cannot order more than size of cultivation land. please reduce the quantity");
                    alert.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog a = alert.create();
                    a.show();


                } else {
                    /*RegisterActivity registerActivity = new RegisterActivity();
                    try{ registerActivity.sharedPreferences= getSharedPreferences(registerActivity.myPrefenrences,context.MODE_PRIVATE);
                        fname = registerActivity.sharedPreferences.getString(registerActivity.fname,"") ;
                        sname = registerActivity.sharedPreferences.getString(registerActivity.sname,"");
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/
                   /* registerActivity=new RegisterActivity();
                    registerActivity.savedData=getPreferences(MODE_PRIVATE);
                    Bundle bundle = getIntent().getExtras();
                    fname = bundle.getString("fname");
                    sname = bundle.getString("sname");
                    Intent i=new Intent(MainActivity.this,Next.class);
*/


                    showLogin();

                }


            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void savePreference(String key, String value) {

        savedData = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor e = savedData.edit();
        e.putString(key, value);
        e.commit();
        //Toast.makeText(RegisterActivity.this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void showPreference(String key) {
        savedData = getPreferences(MODE_PRIVATE);
        String text = savedData.getString(key, "");
    }

    public void showLogin() {
        LayoutInflater layoutInflater = LayoutInflater.from(FertilizerbookingActivity.this);
        View promptView = layoutInflater.inflate(R.layout.login_layout, null);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(FertilizerbookingActivity.this);
        alertDialogBuilder.setTitle("ENTER YOUR CREDENTIALS");
        alertDialogBuilder.setView(promptView);
        Username = (EditText) promptView.findViewById(R.id.Username);
        Id = (EditText) promptView.findViewById(R.id.username);
        password = (EditText) promptView.findViewById(R.id.password);


        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Id.getText().toString().length() == 0) {
                            Id.setError("Id Number is required");
                        } else if (password.getText().toString().length() == 0) {
                            password.setError("Your password is required");
                        } else {

                            try {
                                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                if (conMgr.getActiveNetworkInfo() != null
                                        && conMgr.getActiveNetworkInfo().isAvailable()
                                        && conMgr.getActiveNetworkInfo().isConnected()) {


                                    try {
                                        new loginDetails().execute();
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
                        }

                        // current activity


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent wilfred = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(wilfred);
                                // dialog.cancel();
                            }
                        });

        // create an alert dialog
        android.support.v7.app.AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }

/*
    public void savePreference(String key, String value) {
        savedData = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor e = savedData.edit();
        e.putString(key, value);
        e.commit();
        Toast.makeText(FertilizerbookingActivity.this, "Saved", Toast.LENGTH_SHORT).show();

    }

    public void showPreference(String key) {
        savedData = getPreferences(MODE_PRIVATE);
        String text = savedData.getString(key, "");
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Fertilizerbooking Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.gilbert.myproject/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Fertilizerbooking Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.gilbert.myproject/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void limit() {


    }


    class loginDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FertilizerbookingActivity.this);
            pDialog.setMessage("Sending Login  Details...Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {

            String Idnumber, Password, username;
            username = Username.getText().toString().trim();

            Idnumber = Id.getText().toString().trim();

            Password = password.getText().toString().trim();


            // Building Parameters
            List<NameValuePair> loginDetails = new ArrayList<NameValuePair>();
            loginDetails.add(new BasicNameValuePair("username", username));
            loginDetails.add(new BasicNameValuePair("idnumber", Idnumber));
            loginDetails.add(new BasicNameValuePair("password", Password));


            // getting JSON string from URL
            // JSONObject json = jsonParser.makeHttpRequest(
            //        inserUrl, "POST", params);

            json = jParser.makeHttpRequest1(url_regDetails, "POST", loginDetails);

            try {
//get the responseCode from the web server
                success = json.getString(TAG_SUCCESS);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String response) {
            pDialog.dismiss();
//            Log.d("onPostResponse##",serverResponse);

            if (success.equals("100")) {
                Toast.makeText(getApplicationContext(), "Login fails credentials failed", Toast.LENGTH_LONG).show();

            } else if (success.equals("24")) {
                //Toast.makeText(getApplicationContext(), "Request has been received successfully", Toast.LENGTH_LONG).show();
                try {
                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {

                        try {
                            new checkstatus().execute();   /**********/
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
                ///put here

            } else {
                Toast.makeText(getApplicationContext(), "Request has not been received, please try again", Toast.LENGTH_LONG).show();

            }
        }
    }

    class checkstatus extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FertilizerbookingActivity.this);
            pDialog.setMessage("checking whether registration is approved...Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            String price;


            // String Idnumber, Password;
            username = Username.getText().toString().trim();
            Idnumber = Id.getText().toString().trim();
            Password = password.getText().toString().trim();
            Phone_number = phone.getText().toString().trim();
            Describe = describe.getText().toString().trim();
            Fert_brand = brand.getSelectedItem().toString().trim();
            Quantity = quantity.getSelectedItem().toString().trim();
            Total = landsize.getText().toString().trim();
            Csize = cultsize.getText().toString().trim();


            // Building Parameters
            List<NameValuePair> loginDetails = new ArrayList<NameValuePair>();
            loginDetails.add(new BasicNameValuePair("idnumber", Idnumber));
            loginDetails.add(new BasicNameValuePair("password", Password));


            // getting JSON string from URL
            // JSONObject json = jsonParser.makeHttpRequest(
            //        inserUrl, "POST", params);

            json = jParser.makeHttpRequest1(Utility.url_status, "POST", loginDetails);

            try {
//get the responseCode from the web server
                success = json.getString(TAG_SUCCESS);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String response) {
            pDialog.dismiss();
//            Log.d("onPostResponse##",serverResponse);

            if (success.equals("120")) {
                Toast.makeText(getApplicationContext(), "Sorry you are registration has not been approved wait until it is approved", Toast.LENGTH_LONG).show();

            } else if (success.equals("50")) {
                //Toast.makeText(getApplicationContext(), "Request has been received successfully", Toast.LENGTH_LONG).show();
                try {
                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {

                        try {
                            new checkuserexist().execute();  /**********/

                         /*   sharedPreferences=getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString(usern,username);
                            editor.putString(id,Idnumber);
                            editor.putString(desc,Describe);
                            editor.putString(total,Total);
                            editor.putString(csize,Csize);
                            editor.putString(Fbrand,Fert_brand);
                            editor.putString(Quant,Quantity);
                            editor.putString(Phone_numb,Phone_number);
                            editor.commit();

                            // Log.d("","in fertilizers class"+ Fert_brand);
                            Intent wilfred  = new Intent(getApplicationContext(),ViewbookfActivity.class);
                            startActivity(wilfred);*/
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
                ///put here

            } else {
                Toast.makeText(getApplicationContext(), "Request has not been received, please try again", Toast.LENGTH_LONG).show();

            }
        }
    }

    class checkuserexist extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FertilizerbookingActivity.this);
            pDialog.setMessage("checking whether it is the first time...Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {



            // String Idnumber, Password;
            username = Username.getText().toString().trim();
            Idnumber = Id.getText().toString().trim();
            Password = password.getText().toString().trim();
            Phone_number = phone.getText().toString().trim();
            Describe = describe.getText().toString().trim();
            Fert_brand = brand.getSelectedItem().toString().trim();
            Quantity = quantity.getSelectedItem().toString().trim();
            Total = landsize.getText().toString().trim();
            Csize = cultsize.getText().toString().trim();



            // Building Parameters
            List<NameValuePair> loginDetails = new ArrayList<NameValuePair>();
            loginDetails.add(new BasicNameValuePair("idnumber", Idnumber));
            loginDetails.add(new BasicNameValuePair("password", Password));


            // getting JSON string from URL
            // JSONObject json = jsonParser.makeHttpRequest(
            //        inserUrl, "POST", params);

            json = jParser.makeHttpRequest1(Utility.url_existfert, "POST", loginDetails);

            try {
//get the responseCode from the web server
                success = json.getString(TAG_SUCCESS);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String response) {
            pDialog.dismiss();
//            Log.d("onPostResponse##",serverResponse);

            if (success.equals("120")) {
                Toast.makeText(getApplicationContext(), "Sorry you cannot be allowed to make order more than one wait until the next ordering season", Toast.LENGTH_LONG).show();

            } else if (success.equals("24")) {
                //Toast.makeText(getApplicationContext(), "Request has been received successfully", Toast.LENGTH_LONG).show();
                try {
                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {

                        try {
                            new CreateUser().execute();   /**********/


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
                ///put here

            } else {
                Toast.makeText(getApplicationContext(), "Request has not been received, please try again", Toast.LENGTH_LONG).show();

            }
        }
    }


    class CreateUser extends AsyncTask<String, String, String> {


        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FertilizerbookingActivity.this);
            pDialog.setMessage("Sending Order data.......");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {



            username = Username.getText().toString().trim();
            Idnumber = Id.getText().toString().trim();

            Password = password.getText().toString().trim();


            Phone_number = phone.getText().toString().trim();
            Describe = describe.getText().toString().trim();
            Fert_brand = brand.getSelectedItem().toString().trim();
            Quantity = quantity.getSelectedItem().toString().trim();
            Total = landsize.getText().toString().trim();
            Csize = cultsize.getText().toString().trim();
            int p = Integer.parseInt(quantity.getSelectedItem().toString().trim());
            int pp = 1800 * p;
            price = String.valueOf(pp);


            sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(usern, username);
            editor.putString(id, Idnumber);
            editor.putString(desc, Describe);
            editor.putString(total, Total);
            editor.putString(csize, Csize);
            editor.putString(Fbrand, Fert_brand);
            editor.putString(Quant, Quantity);
            editor.putString(Phone_numb, Phone_number);
            editor.putString(pp1, price);
            editor.commit();




            // try {
            // Building Parameter
            List<NameValuePair> regDetails = new ArrayList<NameValuePair>();
            regDetails.add(new BasicNameValuePair("idnumber", Idnumber));
            regDetails.add(new BasicNameValuePair("username", username));

            regDetails.add(new BasicNameValuePair("password", Password));
            regDetails.add(new BasicNameValuePair("fert", Fert_brand));
            regDetails.add(new BasicNameValuePair("phonenumber", Phone_number));
            regDetails.add(new BasicNameValuePair("description", Describe));
            regDetails.add(new BasicNameValuePair("quantity", Quantity));
            regDetails.add(new BasicNameValuePair("size", Total));
            regDetails.add(new BasicNameValuePair("cultivation", Csize));


            //Posting user data to script

            json = jParser.makeHttpRequest1(Utility.url_land, "POST", regDetails);

            try {
//get the responseCode from the web server
                success = json.getString(TAG_SUCCESS);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String response) {


            // dismiss the dialog once product deleted
            pDialog.dismiss();



           if (success.equals("100")) {
                //Toast.makeText(getApplicationContext(), "Sorry customer your previous size of land does not match with the current one", Toast.LENGTH_LONG).show();

               alert = new AlertDialog.Builder(FertilizerbookingActivity.this);
               alert.setTitle("Landsize!");
               alert.setMessage("Sorry customer your sizes of land does not match with previous sizes please provide correct sizes please");
               alert.setPositiveButton("OK",
                       new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               Intent wilfred = new Intent(getApplicationContext(), MainActivity.class);
                               startActivity(wilfred);

                               dialog.dismiss();
                           }
                       });
               AlertDialog a = alert.create();
               a.show();

            } else if (success.equals("24")) {


                //Toast.makeText(getApplicationContext(), "Request has been received successfully", Toast.LENGTH_LONG).show();*//**//*
               int p = Integer.parseInt(quantity.getSelectedItem().toString().trim());
               int pp = 1800 * p;
               price = String.valueOf(pp);
               sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.putString(usern, username);
               editor.putString(id, Idnumber);
               editor.putString(desc, Describe);
               editor.putString(total, Total);
               editor.putString(csize, Csize);
               editor.putString(Fbrand, Fert_brand);
               editor.putString(Quant, Quantity);
               editor.putString(Phone_numb, Phone_number);
               editor.putString(pp1, price);
               editor.commit();
               Intent wilfred = new Intent(getApplicationContext(), ViewbookfActivity.class);
               startActivity(wilfred);

           } else if (success.equals("48")){
               Toast.makeText(getApplicationContext(), "Sorry Customer the brand you ordered is not available.Please order another Brand we will notify as soon as possible when Brand is available", Toast.LENGTH_LONG).show();

           }
            else {
                Toast.makeText(getApplicationContext(), "Request has not been received, please try again", Toast.LENGTH_LONG).show();

            }


        }
    }

    /*class GetLocationByCategoryClicked extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FertilizerbookingActivity.this);
            pDialog.setMessage("...");
            pDialog.setIndeterminate(false);

            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            JSONParser jsonParser = new JSONParser();

            // Creating service handler class instance
            List<NameValuePair> land1 = new ArrayList<NameValuePair>();
            // getting JSON string from URL

            JSONArray jsonArray;
            jsonArray = jsonParser.makeHttpRequest(Utility.url_getland, "GET", land1);
            //jsonArray = jsonParser.makeHttpRequest(url_news, "GET", news);

            itemList = new ArrayList<HashMap<String, String>>();
            // Check your log cat for JSON reponse
            Log.d("land list a are ..", jsonArray.toString());

            if (jsonArray != null) {


                try {
                    if (jsonArray.length() > 0) {

                        // looping through All Products
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);

                            // Storing each json item in variable
                            reid = c.getString(TAG_PID);

                            reland = c.getString(TAG_ITEMLAND);
                            recsize = c.getString(TAG_ITEMSIZE);
                            reId  = c.getString(TAG_ITEMID);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_PID, reid);
                            map.put(TAG_ITEMLAND, reland);
                            map.put(TAG_ITEMSIZE, recsize);
                            map.put(TAG_ITEMID, reId);


                               *//* map.put(TAG_LATITUDE,itemLatitude);
                                map.put(TAG_LONGITUDE,itemLongitude);
*//*
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
                Toast.makeText(FertilizerbookingActivity.this, "No items in this Category", Toast.LENGTH_SHORT).show();
            }
            return null;


        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
           *//* Log.d("this values",reland);*//*

                *//* int total = Integer.parseInt(Total);
                 int csize =Integer.parseInt(Csize);
                 int Rel = Integer.parseInt(reland);
                 int Rec = Integer.parseInt(recsize);*//*

            Toast.makeText(getApplicationContext(), recsize, Toast.LENGTH_LONG).show();
           /// while (Id.getText().toString().equals(reId)){



            if ((cultsize.getText().toString().equals(recsize))) {


                    Intent wilfred = new Intent(getApplicationContext(), ViewbookfActivity.class);
                    startActivity(wilfred);


                } else {

                    alert = new AlertDialog.Builder(FertilizerbookingActivity.this);
                    alert.setTitle("Landsize!");
                    alert.setMessage("Sorry customer your sizes of land does not match with previous sizes please provide correct sizes please");
                    alert.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent wilfred = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(wilfred);

                                    dialog.dismiss();
                                }
                            });
                    AlertDialog a = alert.create();
                    a.show();


                }

           // }
            }


        }

*/



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
            Intent ww = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(ww);
            return true;
        }
        if (id == R.id.action_logout) {
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

