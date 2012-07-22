package org.witness.informacam.app.mods;

import org.json.JSONObject;

public class Selections {
	public String _optionValue;
	boolean _selected;
	private JSONObject _extras;
	
	public Selections(String optionValue, boolean selectDefault) {
		this(optionValue, selectDefault, null);
	}
	
	public Selections(String optionValue, boolean selectDefault, JSONObject extras) {
		_optionValue = optionValue;
		_selected = selectDefault;
		_extras = extras;
	}
	
	public boolean getSelected() {
		return _selected;
	}
	
	public void setSelected(boolean selected) {
		_selected = selected;
	}
	
	public JSONObject getExtras() {
		return _extras;
	}
}
