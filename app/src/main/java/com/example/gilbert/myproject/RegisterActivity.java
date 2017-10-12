package com.example.gilbert.myproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    EditText Fname, Sname, Id, add, pass, con, email, block, lands, csize;
    Button bb;
    AlertDialog.Builder alert;
    ProgressDialog pDialog;
    String fname,sname,id,Pass,Email,Block,Lands,Csize,addr;
     public SharedPreferences savedData;
    public SharedPreferences sharedPreferences;
    public String myPrefenrences;
    //String fname,sname;


    JSONObject json;
    JSONParser jParser = new JSONParser();
    public static final String TAG_SUCCESS = "responseCode";
   String success;
    private static final String  url_post_regDetails = "http://192.168.43.189:9000/regMobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Fname = (EditText) findViewById(R.id.Fname);
        Sname = (EditText) findViewById(R.id.Sname);
        Id = (EditText) findViewById(R.id.Id);
        add = (EditText) findViewById(R.id.address);
       pass = (EditText) findViewById(R.id.pass);
       con = (EditText) findViewById(R.id.conpass);
       email = (EditText) findViewById(R.id.email);
       // block= (EditText) findViewById(R.id.block);
        lands = (EditText) findViewById(R.id.lands);
       csize = (EditText) findViewById(R.id.csize);
       bb   = (Button) findViewById(R.id.reg);
         savedData= PreferenceManager.getDefaultSharedPreferences(this);
        bb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Fname.getText().toString().equals("") || Sname.getText().toString().equals("") || Id.getText().toString().equals("")
                        || add.getText().toString().equals("") || pass.getText().toString().equals("") || con.getText().toString().equals("")
                        || email.getText().toString().equals("")/* || block.getText().toString().equals("") */|| lands.getText().toString().equals("")
                        || csize.getText().toString().equals("")) {
                    alert = new AlertDialog.Builder(RegisterActivity.this);
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


                } else if (!(pass.getText().toString().equals(con.getText().toString()))) {
                    alert = new AlertDialog.Builder(RegisterActivity.this);
                    alert.setTitle("Details");
                    alert.setMessage("Password are not matching");
                    alert.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog a = alert.create();
                    a.show();


                } else {
                    // TODO Auto-generated method stub
                        fname=Fname.getText().toString();
                        sname = Sname.getText().toString();
                        savePreference(fname,fname);
                        savePreference(sname,sname);
                        Intent i=new Intent(getApplicationContext(), RegisterActivity.class);
                        // add below line
                        i.putExtra("fname", fname);
                        i.putExtra("sname", sname);
                        //startActivity(i);

                    try {
                        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (conMgr.getActiveNetworkInfo() != null
                                && conMgr.getActiveNetworkInfo().isAvailable()
                                && conMgr.getActiveNetworkInfo().isConnected()) {
                           /* fname = Fname.getText().toString().trim();
                       *//* if(TextUtils.isEmpty(Company_Name)) {
                            Company_Name.setError("Required!");
                            return;
                        }*//*
                            sname = Sname.getText().toString().trim();
                        *//*if (TextUtils.isEmpty(Product_Name))
                            Toast.makeText(MainActivity.this, "Your Password is required!!", Toast.LENGTH_SHORT).show();*//*
                            id = Id.getText().toString().trim();
                            Pass = pass.getText().toString().trim();
                            Email = email.getText().toString().trim();
                            Block= block.getText().toString().trim();
                            Lands = lands.getText().toString().trim();
                            Csize= csize.getText().toString().trim();
                            addr= add.getText().toString().trim();
*/
                        /*if (TextUtils.isEmpty(Permit_Number))
                            Toast.makeText(MainActivity.this, "Your Password is required!!", Toast.LENGTH_SHORT).show();*/
                            try {
                                new PostregDetails().execute();   /**********/
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

            }

        });
        //SharedPreferences = get
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





    class PostregDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Sending Registration  Details...Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {

            fname = Fname.getText().toString().trim();
                       /* if(TextUtils.isEmpty(Company_Name)) {
                            Company_Name.setError("Required!");
                            return;
                        }*/
            sname = Sname.getText().toString().trim();
                        /*if (TextUtils.isEmpty(Product_Name))
                            Toast.makeText(MainActivity.this, "Your Password is required!!", Toast.LENGTH_SHORT).show();*/
            id = Id.getText().toString().trim();
            Pass = pass.getText().toString().trim();
            Email = email.getText().toString().trim();
           // Block= block.getText().toString().trim();
            Lands = lands.getText().toString().trim();
            Csize= csize.getText().toString().trim();
            addr= add.getText().toString().trim();

            // Building Parameters
            List<NameValuePair> regDetails = new ArrayList<NameValuePair>();
            regDetails.add(new BasicNameValuePair("firstname", fname));
            regDetails.add(new BasicNameValuePair("secondname", sname));
            regDetails.add(new BasicNameValuePair("idnumber",id));
            regDetails.add(new BasicNameValuePair("phonenumber",addr));
            regDetails.add(new BasicNameValuePair("password",Pass));
            regDetails.add(new BasicNameValuePair("email",Email));
           // regDetails.add(new BasicNameValuePair("blocknumber",Block));
            regDetails.add(new BasicNameValuePair("landsize",Lands));
            regDetails.add(new BasicNameValuePair("cultivation",Csize));


            // getting JSON string from URL
           // JSONObject json = jsonParser.makeHttpRequest(
            //        inserUrl, "POST", params);

            json = jParser.makeHttpRequest1(url_post_regDetails, "POST", regDetails);

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
                Toast.makeText(getApplicationContext(), "User alreay exist", Toast.LENGTH_LONG).show();

            } else if (success.equals("24")) {
                sharedPreferences = getSharedPreferences(myPrefenrences,Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putString(fname,fname);
                    e.putString(sname,sname);
                    e.commit();
                Toast.makeText(getApplicationContext(), "Request has been received successfully you will get response as soon as possible", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Request has not been received, please try again", Toast.LENGTH_LONG).show();

            }
            Intent wilfred = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(wilfred);
        }
    }
}
