package net.swierczynski.autoresponder.calls;

import static android.telephony.TelephonyManager.CALL_STATE_IDLE;
import static android.telephony.TelephonyManager.CALL_STATE_OFFHOOK;
import static android.telephony.TelephonyManager.CALL_STATE_RINGING;
import net.swierczynski.autoresponder.TxtMsgSender;
import net.swierczynski.autoresponder.preferences.UserPreferences;

import android.content.Context;
import android.telephony.PhoneStateListener;

public class UnreceivedCallListener extends PhoneStateListener {
	private boolean callWasUnreceived = false;
	private String phoneNumber;

    Context mctx;

	private int CALL_COUNT;

    private TxtMsgSender msgSender;
	
	public UnreceivedCallListener(TxtMsgSender msgSender, Context mCtx) {
		this.msgSender = msgSender;
        this.CALL_COUNT = UserPreferences.getCallCountPrefs(mCtx);
        this.mctx=mCtx;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		switch(state) {
			case CALL_STATE_RINGING:
				checkCallAsUnreceived(incomingNumber);
				break;
			case CALL_STATE_OFFHOOK:	//call was received
				checkCallAsReceived();
				break;
			case CALL_STATE_IDLE:		//no active calls
				sendMsgIfCallWasntReceived();
				break;
		}
	}

	private void checkCallAsUnreceived(String incomingNumber) {
		phoneNumber = incomingNumber;
		callWasUnreceived = true;
	}

	private void checkCallAsReceived() {
		phoneNumber = null;
		callWasUnreceived = false;
	}

	private void sendMsgIfCallWasntReceived() {
        int CURR_COUNT = UserPreferences.readCallCount(mctx, phoneNumber);
        CURR_COUNT++;
		if(callWasUnreceived && phoneNumber != null && CURR_COUNT == CALL_COUNT) {
			msgSender.sendTextMessageIfPossible(phoneNumber);
            UserPreferences.writeCallCount(mctx,0,phoneNumber);
			callWasUnreceived = false;
			UserPreferences.writeLastNotifiedNumber(mctx,phoneNumber);
			phoneNumber = null;
		}else {
            UserPreferences.writeCallCount(mctx,CURR_COUNT,phoneNumber);
        }
	}
}
