package com.example.gilbert.myproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationseedActivity extends AppCompatActivity {
    FertilizerbookingActivity fertilizerbookingActivity;
    SeedsbookingActivity seedsbookingActivity;
    PayActivity payActivity;
    public SharedPreferences sharedPreferences;
    public String myPreferences;
    public   String Amount,rec;
    AlertDialog.Builder alert;
    ProgressDialog pDialog;
    public static final String TAG_SUCCESS = "responseCode";
    //private static final String  url_post_regDetails = "http://192.168.43.189:9000/viewSeed";
    String Seed_brand, Quantity, Phone_number, Describe, Total, Csize,Idnumber, Password,username;;
    String success;
    JSONObject json;
    JSONParser jParser = new JSONParser();
    TextView Id,Status,Amount1;
    Button Done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmationseed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        seedsbookingActivity = new SeedsbookingActivity();
        payActivity = new PayActivity();
        try {

            seedsbookingActivity.sharedPreferences = getSharedPreferences(seedsbookingActivity.myPreferences, Context.MODE_PRIVATE);
            Seed_brand = seedsbookingActivity.sharedPreferences.getString(seedsbookingActivity.Fbrand, "");
            Quantity = seedsbookingActivity.sharedPreferences.getString(seedsbookingActivity.Quant, "");
            Phone_number = seedsbookingActivity.sharedPreferences.getString(seedsbookingActivity.Phone_numb, "");
            Csize = seedsbookingActivity.sharedPreferences.getString(seedsbookingActivity.csize, "");
            Total= seedsbookingActivity.sharedPreferences.getString(seedsbookingActivity.total, "");
            Describe = seedsbookingActivity.sharedPreferences.getString(seedsbookingActivity.desc, "");
            Idnumber = seedsbookingActivity.sharedPreferences.getString(seedsbookingActivity.id, "");
            username = seedsbookingActivity.sharedPreferences.getString(seedsbookingActivity.usern, "");


        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            payActivity.sharedPreferences = getSharedPreferences(payActivity.myPreferences, Context.MODE_PRIVATE);
            rec = payActivity.sharedPreferences.getString(payActivity.pay, "");
            Amount = "KSH"+rec;

        }catch (Exception e){
            e.printStackTrace();
        }



        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Done =(Button)findViewById(R.id.done);
        Done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new PostregDetails().execute();
            }
        });


    }
    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        Id = (TextView) findViewById(R.id.paymentId);
        Status= (TextView) findViewById(R.id.paymentStatus);
        Amount1 = (TextView) findViewById(R.id.paymentAmount);


        //Showing the details from json object
        Id.setText(jsonDetails.getString("id"));
        Status.setText(jsonDetails.getString("state"));
        Amount1.setText(paymentAmount + " USD");
    }



      class PostregDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConfirmationseedActivity.this);
            pDialog.setMessage("Sending Order  Details...Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


          protected String doInBackground(String... args) {
            String price;


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
              regDetails.add(new BasicNameValuePair("brandname",Seed_brand));
              regDetails.add(new BasicNameValuePair("cultivation",Csize));
              regDetails.add(new BasicNameValuePair("amount",Amount));



            // getting JSON string from URL
            // JSONObject json = jsonParser.makeHttpRequest(
            //        inserUrl, "POST", params);

            json = jParser.makeHttpRequest1(Utility.url_seedpost, "POST", regDetails);

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

                Toast.makeText(getApplicationContext(), "You are order is succesfully completed you can now come and pick you are seeds.Thankyou in advance", Toast.LENGTH_LONG).show();
                Intent wilfred = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(wilfred);


            } else {
                Toast.makeText(getApplicationContext(), "Request has not been received, please try again", Toast.LENGTH_LONG).show();

            }
            Intent wilfred = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(wilfred);
        }
    }


    class sendpay extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConfirmationseedActivity.this);
            pDialog.setMessage("Sending pay details.....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {


            //Amount = Amount1.getText().toString();







            List<NameValuePair> regDetails = new ArrayList<NameValuePair>();
            regDetails.add(new BasicNameValuePair("amount", Amount));


            //Posting user data to script

            json = jParser.makeHttpRequest1(Utility.url_payseed, "POST", regDetails);

            try {
//get the responseCode from the web server
                success = json.getString(TAG_SUCCESS);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String response) {


            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (json == null) {
                Toast.makeText(getApplicationContext(), "Server not available,try again later", Toast.LENGTH_LONG).show();

            } else if (success.equals("24")) {

                Toast.makeText(getApplicationContext(), "You are order is succesfully completed.Thankyou", Toast .LENGTH_LONG).show();

                Intent wilfred  = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(wilfred);


            } else {
                Toast.makeText(getApplicationContext(), "Request has not been received, please try again", Toast.LENGTH_LONG).show();

            }
            Intent wilfred  = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(wilfred);



        }
    }




}
