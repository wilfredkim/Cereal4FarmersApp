package com.example.gilbert.myproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InquireActivity extends AppCompatActivity {

    AlertDialog.Builder alert;
    ProgressDialog pDialog;
    static final int DATE_DIALOG_ID = 999;
    private int year;
    private int month;
    private int day;

    final Calendar c = Calendar.getInstance();
    Spinner product, quantity;
    EditText name, id, phone, date, message;
    Button Send;
    CheckBox  checkBox;
    public static final String TAG_SUCCESS = "responseCode";
    String success;
    JSONObject json;
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton setdate = (ImageButton) findViewById(R.id.imageButton1);
        name = (EditText) findViewById(R.id.name);
       checkBox = (CheckBox) findViewById(R.id.check);
        id = (EditText) findViewById(R.id.id);
        phone = (EditText) findViewById(R.id.phone);
        date = (EditText) findViewById(R.id.txtDate);
        message = (EditText) findViewById(R.id.message);
        Send = (Button) findViewById(R.id.send);
        Send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (phone.getText().toString().equals("") || name.getText().toString().equals("") || id.getText().toString().equals("") ||
                        date.getText().toString().equals("") || message.getText().toString().equals("")) {
                    alert = new AlertDialog.Builder(InquireActivity.this);
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
                } else if(!(checkBox.isChecked())) {

                        alert.setTitle("Details");
                        alert.setMessage("Please confirm if you agree with terms and condition");
                        alert.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog a = alert.create();
                        a.show();

                }else {

                    new Createinquire().execute();
                }
            }
        });

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        setdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });}

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);
        }
        return null;
    }

    public DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {

                // when dialog box is closed, below method will be called.
                public void onDateSet(DatePicker view, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    year = selectedYear;
                    month = selectedMonth;
                    day = selectedDay;

                    // set selected date into edittext
                    EditText txtDate = (EditText) findViewById(R.id.txtDate);
                    txtDate.setText(new StringBuilder().append(month + 1).append("-")
                            .append(day).append("-").append(year).append(" "));

                }
            };


    class Createinquire extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InquireActivity.this);
            pDialog.setMessage("Sending inquiry... please wait.....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String Name, Id, Phone, Date, Message;


            Name = name.getText().toString().trim();
            Id = id.getText().toString().trim();
            Phone = phone.getText().toString().trim();
            Date = date.getText().toString().trim();
            Message = message.getText().toString().trim();

            // try {
            // Building Parameter
            List<NameValuePair> inqDetails = new ArrayList<NameValuePair>();
            inqDetails.add(new BasicNameValuePair("name", Name));
            inqDetails.add(new BasicNameValuePair("id", Id));
            inqDetails.add(new BasicNameValuePair("phone", Phone));
            inqDetails.add(new BasicNameValuePair("date", Date));
            inqDetails.add(new BasicNameValuePair("message", Message));


            //Posting user data to script

            json = jParser.makeHttpRequest1(Utility.url_inq , "POST", inqDetails);

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
                Toast.makeText(getApplicationContext(), "Request has been received successfullyyou will received a reply soon ", Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(getApplicationContext(), "Request has not been received, please try again", Toast.LENGTH_LONG).show();

            }


            // Toast.makeText(getApplicationContext(), "user created successfully!!!", Toast.LENGTH_LONG).show();
            // if (file_url != null){
            //	Toast.makeText(getApplicationContext(), "Done!!!", Toast.LENGTH_LONG).show();
            //
            //}
            Intent wilfred = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(wilfred);

        }


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




