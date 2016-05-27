package com.example.narayaniyer.phone_dialer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button callBtn;
    private Button dialBtn;
    private EditText number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number = (EditText) findViewById(R.id.phoneNumber);
        callBtn = (Button) findViewById(R.id.call);
        dialBtn = (Button) findViewById(R.id.dial);

        //add phone listener for monitoring
        MyPhoneListener phoneListener = new MyPhoneListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        //receive notifications of telephony state changes
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //set data
                    String uri = "tel:" + number.getText().toString();
                    Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callintent);


                } catch (Exception e) {
                    //set the data
                    Toast.makeText(getApplicationContext(), "Your call has failed....", Toast.LENGTH_LONG).show();
                    e.printStackTrace();


                }
            }
        });

      dialBtn.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v) {
              try {
                      String uri="tel"+number.getText().toString();
                      Intent dialIntent=new Intent(Intent.ACTION_DIAL,Uri.parse(uri));
                      startActivity(dialIntent);
                  }
              catch(Exception e) {
                  Toast.makeText(getApplicationContext(),"your call has failed",Toast.LENGTH_LONG).show();
                  e.printStackTrace();

              }
          }
       });




    }
    private class MyPhoneListener extends PhoneStateListener{
        private boolean onCall=false;

        @Override
        public void onCallStateChanged(int state,String incomingno)
        {
             switch(state)
             {
                 case TelephonyManager.CALL_STATE_RINGING:
                     //Phone Ringing
                     Toast.makeText(MainActivity.this, incomingno+"Calls you",Toast.LENGTH_LONG).show();
                     break;

                 case TelephonyManager.CALL_STATE_OFFHOOK:
                     //one call exists thats dialing,active or on hold
                     Toast.makeText(MainActivity.this,"on call...", Toast.LENGTH_LONG).show();
                     //because user answers the incoming call
                     onCall=true;
                     break;
                 case TelephonyManager.CALL_STATE_IDLE:
                     // in initialization of the class and at the end of phone call

                     // detect flag from CALL_STATE_OFFHOOK
                     if(onCall==true)
                     {
                         Toast.makeText(MainActivity.this,"restart app after call...",Toast.LENGTH_LONG).show();

                         //restart our call
                         Intent restart=getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                         restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         startActivity(restart);
                         onCall=false;

                     }
                     break;
                 default:
                     break;

             }
        }
    }
}

