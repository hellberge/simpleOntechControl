package com.ontech.smscontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;


/**
 * Created by Erik on 2015-04-14.
 */
public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus=(Object[])intent.getExtras().get("pdus");
        SmsMessage shortMessage=SmsMessage.createFromPdu((byte[]) pdus[0]);

        Log.d("SMSReceiver", "SMS message sender: " +
                shortMessage.getOriginatingAddress());
        Log.d("SMSReceiver","SMS message text: "+
                shortMessage.getDisplayMessageBody());
        MainActivity.appendSMSLog(shortMessage);
    }
}
