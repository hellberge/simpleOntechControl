package com.ontech.smscontrol;
import android.content.*;

public class SMSHandler extends BroadcastReceiver
{
	@Override
    public void onReceive(Context context, Intent intent) {
		MainActivity.appendSMSLog(intent.getAction());
	}
}
