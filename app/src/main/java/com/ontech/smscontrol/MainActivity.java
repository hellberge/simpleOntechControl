package com.ontech.smscontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsMessage;
import android.telephony.PhoneNumberUtils;

public class MainActivity extends Activity {
	Button btnStatus;
	static EditText etPhoneNo;
    EditText etPin;
    StringBuilder msg;
    static TextView logWindow;
    SharedPreferences sharedPref;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		setContentView(R.layout.activity_main);
		etPhoneNo = (EditText) findViewById(R.id.phno);
        etPhoneNo.setText(sharedPref.getString("phno", "070"));
        etPin = (EditText) findViewById(R.id.pinno);
        etPin.setText(sharedPref.getString("pin", "1"));
		btnStatus = (Button) findViewById(R.id.bStatus);
         logWindow = (TextView) findViewById(R.id.logWindow);
        msg = new StringBuilder();
        msg.append(etPin.getText().toString()).append('#').append('8').append('#');

		btnStatus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = etPhoneNo.getText().toString();

                try {

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, msg.toString(), null, null);
                    Toast.makeText(getApplicationContext(), "Message Sent",
                            Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }

            }
        });
		
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("phno", etPhoneNo.getText().toString());
        editor.putString("pin", etPin.getText().toString());
        editor.commit();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

    public static void appendSMSLog(SmsMessage sms) {
		if(PhoneNumberUtils.compare(sms.getOriginatingAddress(), etPhoneNo.getText().toString())) {
            logWindow.append("\n");
			logWindow.append(sms.getDisplayMessageBody());
		}
  
    }
}
