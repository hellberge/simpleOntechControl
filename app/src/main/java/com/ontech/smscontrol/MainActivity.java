package com.ontech.smscontrol;

import android.app.*;
import android.content.*;
import android.os.*;
import android.telephony.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends Activity {
	Button btnStatus;
	static EditText etPhoneNo;
    EditText etPin;
    StringBuilder msg;
    static TextView logWindow;
    SharedPreferences sharedPref;

	private static final String SMS_DELIVERED = "SMS_DELIVERED";
	private static final String SMS_SENT = "SMS_SENT";

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
                    sendSms(phoneNo, msg.toString());
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }

            }
        });
		
	}

	private void sendSms(String phoneNo, String msg) {
		PendingIntent piSend = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
		PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);
		
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNo, null, msg.toString(), piSend, piDelivered);
		Toast.makeText(getApplicationContext(), "Message Sent",
					   Toast.LENGTH_LONG).show();
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
	
	public static void appendSMSLog(String msg) {
		logWindow.append("\n");
		logWindow.append(msg);
	}
}
