package com.example.gilbert.myproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class ViewbookfActivity extends AppCompatActivity {




    public SharedPreferences sharedPreferences;
    //public String myPreferences;
    String s1,s2,s3,s4,s5,s6,price;
    int q;

    FertilizerbookingActivity fertilizerbookingActivity;
    //SeedsbookingActivity  seed;
    EditText Brand,Quantity,Phone,Tprice,Mquatity,Mprice;
    Button Decline;
    ImageView Accept;
    public static final String pay ="paymentAmount";
    public static final String myPreferences="mypref";
    public static final int PAYPAL_REQUEST_CODE = 123;
    public String paymentAmount;


    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewbookf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Brand = (EditText)findViewById(R.id.bname);
        Quantity =(EditText)findViewById(R.id.qua);
        Phone = (EditText)findViewById(R.id.phone);
        Tprice =(EditText)findViewById(R.id.total);
        Mquatity =(EditText)findViewById(R.id.mul);
        Mprice =(EditText)findViewById(R.id.multp);
        Accept =(ImageView)findViewById(R.id.accept);
        Decline =(Button)findViewById(R.id.decline);
        Brand.setKeyListener(null);
        Quantity.setKeyListener(null);
        Phone.setKeyListener(null);
        Mquatity.setKeyListener(null);
        Mprice.setKeyListener(null);

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
        fertilizerbookingActivity = new FertilizerbookingActivity();
        //seed  = new SeedsbookingActivity();
        try {
            fertilizerbookingActivity.sharedPreferences = getSharedPreferences(fertilizerbookingActivity.myPreferences, Context.MODE_PRIVATE);
            s1 = fertilizerbookingActivity.sharedPreferences.getString(fertilizerbookingActivity.Fbrand, "");
            s2 = fertilizerbookingActivity.sharedPreferences.getString(fertilizerbookingActivity.Quant, "");
            s3 = fertilizerbookingActivity.sharedPreferences.getString(fertilizerbookingActivity.Phone_numb, "");
            price = fertilizerbookingActivity.sharedPreferences.getString(fertilizerbookingActivity.pp1, "");

           /* seed.sharedPreferences = getSharedPreferences(seed.myPreferences, Context.MODE_PRIVATE);
            s1 = seed.sharedPreferences.getString(seed.Fbrand, "");
            s2 = seed.sharedPreferences.getString(seed.Quant, "");
            s3 = seed.sharedPreferences.getString(seed.Phone_numb, "");*/

            /*seed.savedData=getPreferences(MODE_PRIVATE);
            Bundle bundle = getIntent().getExtras();
             s1 = bundle.getString("brand");
             s2 = bundle.getString("quantity");
             s2 =bundle.getString("phone");*/
            Brand.setText(s1);
            Quantity.setText(s2);
            Phone.setText(s3);
             s5 = "1 * " + Quantity.getText().toString();
             s6 = "1800 *" + Quantity.getText().toString();
            // q = Integer.parseInt(s2);
           // price  = 1800 * q;

            Mquatity.setText(s5);
            Mprice.setText(s6);
            Tprice.setText(price);


        }catch (Exception e){
            e.printStackTrace();
        }

       /* fertilizerbookingActivity.savedData   = getPreferences(MODE_PRIVATE);
        try{
        Bundle bundle = getIntent().getExtras();
        String s1 = bundle.getString("brand");
        String s3 = bundle.getString("quantity");
        String s2 =bundle.getString("phone");


        }
        catch (Exception e){
            e.printStackTrace();
        }*/
        Accept.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                getPayment();


            } });
        Decline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent wilfred = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(wilfred);


            }
        });

    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    private void getPayment() {
        //Getting the amount from editText
        paymentAmount= Tprice.getText().toString();
        sharedPreferences=getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(pay,paymentAmount);
        editor.commit();


        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Amount",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
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

        return super.onOptionsItemSelected(item);
    }
}


