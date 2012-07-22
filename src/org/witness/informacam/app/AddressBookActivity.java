package org.witness.informacam.app;

import org.witness.informacam.R;
import org.witness.informacam.app.MainRouter.OnRoutedListener;
import org.witness.informacam.utils.Constants.App;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class AddressBookActivity extends Activity implements OnClickListener, OnRoutedListener {
	
	Handler h;
	
	ImageButton navigation;
	Button new_contact;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		
		MainRouter.show(this);
	}
	
	private void initLayout() {
		setContentView(R.layout.addressbookactivity);
		
		navigation = (ImageButton) findViewById(R.id.navigation_button);
		navigation.setOnClickListener(this);
		
		new_contact = (Button) findViewById(R.id.new_contact_button);
		new_contact.setOnClickListener(this);
	}
	
	private void getAddresses() {
		
	}
	
	@Override
	public void onClick(View v) {
		if(v == navigation)
			finish();
		else if(v == new_contact) {
			Intent intent = new Intent(this, AddressBookChooserActivity.class);
			startActivityForResult(intent, App.AddressBook.FROM_CONTACT_CHOOSER);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK && requestCode == App.AddressBook.FROM_CONTACT_CHOOSER) {
			// TODO: handle the addition
		}
		
	}

	@Override
	public void onRouted() {
		h.post(new Runnable() {
			@Override
			public void run() {
				getAddresses();
			}
		});
		
	}

}
