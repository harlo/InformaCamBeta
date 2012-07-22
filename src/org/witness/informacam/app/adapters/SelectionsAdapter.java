package org.witness.informacam.app.adapters;

import java.util.ArrayList;

import org.witness.informacam.R;
import org.witness.informacam.app.mods.InformaTextView;
import org.witness.informacam.app.mods.Selections;
import org.witness.informacam.utils.Constants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

public class SelectionsAdapter extends BaseAdapter {
	ArrayList<Selections> _selections;
	boolean _isMulti;
	LayoutInflater li;
	
	public SelectionsAdapter(Context c, ArrayList<Selections> selections, String multiType) {
		_selections = selections;
		
		if(multiType.compareTo(Constants.Mods.Selections.SELECT_ONE) == 0)
			_isMulti = false;
		else
			_isMulti = true;
		
		li = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return _selections.size();
	}

	@Override
	public Object getItem(int position) {
		return _selections.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		convertView = li.inflate(R.layout.select_listview, null);
		InformaTextView selectText = (InformaTextView) convertView.findViewById(R.id.selectText);
		selectText.setText(_selections.get(position)._optionValue);
		
		CheckBox selectBox = (CheckBox) convertView.findViewById(R.id.selectBox);
		selectBox.setSelected(_selections.get(position).getSelected());
		if(!_isMulti) {
			
			selectBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					
					if(isChecked) {
						for(Selections s: _selections) {
							if(position != _selections.indexOf(s)) {
								LinearLayout ll = (LinearLayout) parent.getChildAt(_selections.indexOf(s));
								CheckBox cb = (CheckBox) ll.findViewById(R.id.selectBox);
								
								s.setSelected(false);
								cb.setChecked(false);
							} else
								s.setSelected(true);
						}
						
						
					}
				
				}
			
			});
		} else {
			selectBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if(isChecked) {
						_selections.get(position).setSelected(true);
					}
				}
			});
			
		}
		return convertView;
	}
}