package net.swierczynski.autoresponder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.swierczynski.autoresponder.calls.UnreceivedCallsService;
import net.swierczynski.autoresponder.preferences.UserPreferences;
import net.swierczynski.autoresponder.texts.IncomingMsgsService;

public class AutoResponder extends Activity {

	private final static int MENU_PREFERENCES = Menu.FIRST;
	private final static int MENU_RESET = 2;
	private final static int MENU_ABOUT = 3;
	
	private AutoResponderDbAdapter dbAdapter;

	private EditText callCountTxt;

    private CheckBox textsCheckbox,callsCheckbox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main);


        textsCheckbox = (CheckBox) findViewById(R.id.enable_texts);
        callsCheckbox = (CheckBox) findViewById(R.id.enable_calls);

		
		dbAdapter = AutoResponderDbAdapter.initializeDatabase(this);
		registerCallsCheckboxListener();
		registerTextsCheckboxListener();
		displayProfilesSpinner();
		registerConfirmButtonListener();

        //Call Count Registering
		callCountTxt=(EditText)findViewById(R.id.callcount);

		try {
            callCountTxt.setText(UserPreferences.getCallCountPrefs(AutoResponder.this)+"");
        }catch (RuntimeException e){
            callCountTxt.setText("1");

        }

        callCountTxt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void afterTextChanged(Editable editable) {
                try {
                    UserPreferences.setCallCountPrefs(AutoResponder.this,Integer.parseInt(callCountTxt.getText().toString()));
                }catch (NumberFormatException e){
                    UserPreferences.setCallCountPrefs(AutoResponder.this,1);
                }
            }
        });

		if (UserPreferences.isItFirstTime(AutoResponder.this)){
            textsCheckbox.setChecked(false);
            callsCheckbox.setChecked(false);
            UserPreferences.notAnyMore(AutoResponder.this);
        }else {
            textsCheckbox.setChecked(UserPreferences.readAutoRespondToTextsState(AutoResponder.this));
            callsCheckbox.setChecked(UserPreferences.readAutoRespondToMissedCalls(AutoResponder.this));
        }

	}

	private void registerCallsCheckboxListener() {
		callsCheckbox.setChecked(UnreceivedCallsService.isActive);
		callsCheckbox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				boolean enabled = cb.isChecked();
				setServiceState(enabled, "calls");
                UserPreferences.saveAutoRespondToMissedCalls(AutoResponder.this.getApplicationContext(),enabled);
			}

		});
	}
	
	private void registerTextsCheckboxListener() {
		textsCheckbox.setChecked(IncomingMsgsService.isActive);
		textsCheckbox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				boolean enabled = cb.isChecked();
				setServiceState(enabled, "texts");
				UserPreferences.saveAutoRespondToTextsState(AutoResponder.this.getApplicationContext(),enabled);
			}
		});
	}

	private void displayProfilesSpinner() {
		Spinner profilesSpinner = (Spinner) findViewById(R.id.profile);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.profiles_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    profilesSpinner.setAdapter(adapter);
	    profilesSpinner.setSelection(adapter.getPosition(TxtMsgSender.getProfile()));
	    
	    registerProfilesSpinnerListener(profilesSpinner);
	}

	private void registerProfilesSpinnerListener(Spinner profilesSpinner) {
		profilesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				String choosenProfile = parent.getItemAtPosition(pos).toString();
				TxtMsgSender.setProfile(choosenProfile);
				fillMessageBodyField();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// Do nothing
			}
		});
	}
	
	private void setServiceState(boolean enabled, String mode) {
		Intent service = new Intent(this, AutoResponderService.class);
		service.putExtra("isEnabled", enabled);
		service.putExtra("mode", mode);
		startService(service);
	}
	
	private void fillMessageBodyField() {
		String text = dbAdapter.fetchMessageBody(TxtMsgSender.getProfile());
		setMessageContent(text);
	}

	private void registerConfirmButtonListener() {
		Button confirmButton = (Button) findViewById(R.id.confirm);
		confirmButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String msgBody = getMessageContent();
				persistMessageContent(msgBody);
				showConfirmation();
			}

			private void showConfirmation() {
				Context context = getApplicationContext();
				String text = getText(R.string.message_saved) + " " + TxtMsgSender.getProfile();
				int duration = Toast.LENGTH_LONG;
				Toast confirmationMessage = Toast.makeText(context, text, duration);
				confirmationMessage.show();
			}
		});
	}
	
	private EditText getMessageBodyField() {
		return (EditText) findViewById(R.id.body);
	}
	
	private void setMessageContent(String content) {
		getMessageBodyField().setText(content);
	}
	
	private String getMessageContent() {
		return getMessageBodyField().getText().toString();
	}
	
	private void persistMessageContent(String content) {
		dbAdapter.saveMessage(TxtMsgSender.getProfile(), content);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		String content = getMessageContent();
		persistMessageContent(content);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_PREFERENCES, 0, R.string.menu_preferences);
		menu.add(Menu.NONE, MENU_RESET, 1, R.string.menu_reset);
		menu.add(Menu.NONE, MENU_ABOUT, 2, R.string.menu_about);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()) {
			case MENU_PREFERENCES: {
				Intent i = new Intent(this, UserPreferences.class);
				startActivity(i);
				return true;
			}
			case MENU_RESET: {
				Intent intent = new Intent(NotificationArea.RESET);
				sendBroadcast(intent);
				return true;
			}
			case MENU_ABOUT: {
				Intent i = new Intent(this, About.class);
				startActivity(i);
				return true;
			}
		}
		
		return false;
	}

}