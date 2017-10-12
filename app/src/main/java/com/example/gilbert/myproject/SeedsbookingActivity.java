package com.example.gilbert.myproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SeedsbookingActivity extends AppCompatActivity {
    EditText phone,describe,landsize,cultsize;
    CheckBox check1;
    Spinner quantity,brand;
    Button submit;
    AlertDialog.Builder alert;
    ProgressDialog pDialog;
    public SharedPreferences savedData;
    String Seed_brand,Quantity,Phone_number,Describe,Csize,Total;
    String Idnumber,Password,username, price;

  EditText Id,password,Username;
    JSONObject json;
    JSONParser jParser = new JSONParser();
    public static final String TAG_SUCCESS = "responseCode";
    String success;
    ArrayAdapter<CharSequence> adapter;
    private static final String  url_post_regDetails = "http://192.168.43.189:9000/viewSeed";
    private static final String  url_regDetails = "http://192.168.43.189:9000/regLogin";
    public SharedPreferences sharedPreferences;
    public static final String Fbrand ="Seed_brand";
    public static final String myPreferences="mypref";
    public static final String Quant="Quantity";
    public static final String Phone_numb="Phone_number";
    public static final String usern="username";
    public static final String id="Idnumber";
    public static final String desc="Describe";
    public static final String total="Total";
    public static final String csize="Csize";
    public static final String pp1="";
    public static int order;
    public static int land;
   // public  String Fbrand,Quant,Phone_numb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seedsbooking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phone = (EditText)findViewById(R.id.phone);
        describe =(EditText) findViewById(R.id.des);
       // check1 =(CheckBox)findViewById(R.id.check);
        quantity=(Spinner)findViewById(R.id.seed_qua);
        brand =(Spinner) findViewById(R.id.seed_brand);
        landsize =(EditText)findViewById(R.id.size);
        cultsize= (EditText)findViewById(R.id.csize);
        savedData= PreferenceManager.getDefaultSharedPreferences(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.maizeseeds, android.R.layout.simple_spinner_item);
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
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.quantity12, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        quantity.setAdapter(adapter2);
        quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position) + " selected", Toast.LENGTH_LONG);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit= (Button)findViewById(R.id.sub);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (phone.getText().toString().equals("") || describe.getText().toString().equals("") || quantity.getSelectedItem().toString().equals("") || landsize.getText().toString().equals("")|| brand.getSelectedItem().toString().equals("")
                        || cultsize.getText().toString().equals("")){
                    alert = new AlertDialog.Builder(SeedsbookingActivity.this);
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


                }  else if( (Integer.parseInt(quantity.getSelectedItem().toString())) > (Integer.parseInt(cultsize.getText().toString())) ){
                    alert = new AlertDialog.Builder(SeedsbookingActivity.this);
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


                }






                else  {
                    showLogin();;




                    }


                }


    });}
    public void showLogin(){
        LayoutInflater layoutInflater = LayoutInflater.from(SeedsbookingActivity.this);
        View promptView = layoutInflater.inflate(R.layout.login_layout, null);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(SeedsbookingActivity.this);
        alertDialogBuilder.setTitle("ENTER YOUR CREDENTIALS");
        alertDialogBuilder.setView(promptView);
        Username =(EditText)promptView.findViewById(R.id.Username);
         Id = (EditText) promptView.findViewById(R.id.username);
         password  = (EditText) promptView.findViewById(R.id.password);


        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Id.getText().toString().length()==0) {
                            Id.setError("Id Number is required");
                        }
                        else if (password.getText().toString().length()==0) {
                            password.setError("Your password is required");}
                        else{

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
                                Intent wilfred= new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(wilfred);
                               // dialog.cancel();
                            }
                        });

        // create an alert dialog
        android.support.v7.app.AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }

    class loginDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SeedsbookingActivity.this);
            pDialog.setMessage("Sending Login  Details...Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {

            String Idnumber,Password,username;

            username = Username.getText().toString().trim();
            Idnumber = Id.getText().toString().trim();
                       /* if(TextUtils.isEmpty(Company_Name)) {
                            Company_Name.setError("Required!");
                            return;
                        }*/
            Password = password.getText().toString().trim();
                        /*if (TextUtils.isEmpty(Product_Name))
                            Toast.makeText(MainActivity.this, "Your Password is required!!", Toast.LENGTH_SHORT).show();*/


            // Building Parameters
            List<NameValuePair> loginDetails = new ArrayList<NameValuePair>();
            loginDetails.add(new BasicNameValuePair("username",username));

            loginDetails.add(new BasicNameValuePair("idnumber",Idnumber));
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
                            new checkstatus().execute();  /**********/
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    } else {

                        Context context = getApplicationContext();
                        CharSequence text = "No internet connection.Check your internet settings...";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }


                }catch (Exception e){
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
            pDialog = new ProgressDialog(SeedsbookingActivity.this);
            pDialog.setMessage("checking whether registration is approved...Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {



            Idnumber = Id.getText().toString().trim();

            Password = password.getText().toString().trim();
            username = Username.getText().toString().trim();
            Seed_brand = brand.getSelectedItem().toString().trim();


            Phone_number = phone.getText().toString().trim();

            Describe = describe.getText().toString().trim();
            Quantity = quantity.getSelectedItem().toString().trim();
            Total =landsize.getText().toString().trim();
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
                          new  checkuserexist().execute();
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
            pDialog = new ProgressDialog(SeedsbookingActivity.this);
            pDialog.setMessage("checking whether it is the first time ...Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {



            Idnumber = Id.getText().toString().trim();

            Password = password.getText().toString().trim();
            username = Username.getText().toString().trim();
            Seed_brand = brand.getSelectedItem().toString().trim();


            Phone_number = phone.getText().toString().trim();

            Describe = describe.getText().toString().trim();
            Quantity = quantity.getSelectedItem().toString().trim();
            Total =landsize.getText().toString().trim();
            Csize = cultsize.getText().toString().trim();
           /* int p = Integer.parseInt(quantity.getSelectedItem().toString().trim());
            int pp = 320* p;
            price = String.valueOf(pp);

            sharedPreferences=getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString(Phone_numb, Phone_number);
            editor.putString(Fbrand,Seed_brand);
            editor.putString(Quant,Quantity);
            editor.putString(usern,username);
            editor.putString(id,Idnumber);
            editor.putString(desc,Describe);
            editor.putString(total,Total);
            editor.putString(csize,Csize);
            editor.putString(pp1,price);
            editor.commit();*/


            // Building Parameters
            List<NameValuePair> loginDetails = new ArrayList<NameValuePair>();
            loginDetails.add(new BasicNameValuePair("idnumber", Idnumber));
            loginDetails.add(new BasicNameValuePair("password", Password));


            // getting JSON string from URL
            // JSONObject json = jsonParser.makeHttpRequest(
            //        inserUrl, "POST", params);

            json = jParser.makeHttpRequest1(Utility.url_existseed, "POST", loginDetails);

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
                             new PostregDetails().execute();   /**********/

                           /* sharedPreferences=getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString(Phone_numb, Phone_number);
                            editor.putString(Fbrand,Seed_brand);
                            editor.putString(Quant,Quantity);
                            editor.putString(usern,username);
                            editor.putString(id,Idnumber);
                            editor.putString(desc,Describe);
                            editor.putString(total,Total);
                            editor.putString(csize,Csize);
                            editor.putString(pp1,price);
                            editor.commit();
                            Intent wilfred  = new Intent(getApplicationContext(),PayActivity.class);
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



    class PostregDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SeedsbookingActivity.this);
            pDialog.setMessage("Sending Order  Details...Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }



        protected String doInBackground(String... args) {
            String price;

            //String Seed_brand,Quantity,Phone_number,Describe;

            username = Username.getText().toString().trim();
            Seed_brand = brand.getSelectedItem().toString().trim();
            Idnumber = Id.getText().toString().trim();

            Password = phone.getText().toString().trim();

            Phone_number = phone.getText().toString().trim();

            Describe = describe.getText().toString().trim();
            Quantity = quantity.getSelectedItem().toString().trim();
            Total =landsize.getText().toString().trim();
            Csize = cultsize.getText().toString().trim();
            int p = Integer.parseInt(quantity.getSelectedItem().toString().trim());
            int pp = 320* p;
            price = String.valueOf(pp);
            sharedPreferences=getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString(Phone_numb, Phone_number);
            editor.putString(Fbrand,Seed_brand);
            editor.putString(Quant,Quantity);
            editor.putString(usern,username);
            editor.putString(id,Idnumber);
            editor.putString(desc,Describe);
            editor.putString(total,Total);
            editor.putString(csize,Csize);
            editor.putString(pp1,price);
            editor.commit();

            // Building Parameters
            List<NameValuePair> regDetails = new ArrayList<NameValuePair>();
            regDetails.add(new BasicNameValuePair("username",username));
            regDetails.add(new BasicNameValuePair("idnumber",Idnumber));
            regDetails.add(new BasicNameValuePair("password", Password));
            regDetails.add(new BasicNameValuePair("phonenumber", Phone_number));
            regDetails.add(new BasicNameValuePair("description", Describe));
            regDetails.add(new BasicNameValuePair("quantity",Quantity));
            regDetails.add(new BasicNameValuePair("brandname",Seed_brand));
            regDetails.add(new BasicNameValuePair("size",Total));
            regDetails.add(new BasicNameValuePair("cultivation",Csize));





            // getting JSON string from URL
            // JSONObject json = jsonParser.makeHttpRequest(
            //        inserUrl, "POST", params);

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
            pDialog.dismiss();
//            Log.d("onPostResponse##",serverResponse);

            if (json == null) {
                Toast.makeText(getApplicationContext(), "Server not available,try again later", Toast.LENGTH_LONG).show();

            } else if (success.equals("24")) {
                int p = Integer.parseInt(quantity.getSelectedItem().toString().trim());
                int pp = 320* p;
                price = String.valueOf(pp);
                sharedPreferences=getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(Phone_numb, Phone_number);
                editor.putString(Fbrand,Seed_brand);
                editor.putString(Quant,Quantity);
                editor.putString(usern,username);
                editor.putString(id,Idnumber);
                editor.putString(desc,Describe);
                editor.putString(total,Total);
                editor.putString(csize,Csize);
                editor.putString(pp1,price);
                editor.commit();
                Intent wilfred  = new Intent(getApplicationContext(),PayActivity.class);
                startActivity(wilfred);

            } else {
                Toast.makeText(getApplicationContext(), "Request has not been received, please try again", Toast.LENGTH_LONG).show();

            }
            //Intent wilfred = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(wilfred);
        }
    }
    public void savePreference(String key,String value) {

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
        if (id == R.id.action_logout) {
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}