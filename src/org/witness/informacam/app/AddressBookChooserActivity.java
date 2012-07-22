package org.witness.informacam.app;

import org.witness.informacam.R;
import org.witness.informacam.app.mods.InformaEditText;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class AddressBookChooserActivity extends Activity implements OnClickListener {
	Button input_contact, pick_contact, import_contact, done;
	InformaEditText input_contact_text;
	LinearLayout contact_info;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
	}
	
	private void initLayout() {
		setContentView(R.layout.addressbookchooseractivity);
		
		input_contact = (Button) findViewById(R.id.import_contact);
		pick_contact = (Button) findViewById(R.id.pick_contact);
		import_contact = (Button) findViewById(R.id.import_contact);
		done = (Button) findViewById(R.id.address_book_done);
		
		input_contact_text = (InformaEditText) findViewById(R.id.input_contact_text);
		contact_info = (LinearLayout) findViewById(R.id.contact_info);
	}
	
	@Override
	public void onClick(View v) {
		if(v == input_contact) {
			
		} else if(v == pick_contact) {
			
		} else if(v == import_contact) {
			
		} else if(v == done) {
			
		}
		
	}

}
