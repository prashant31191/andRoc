package net.rocrail.androc;

import net.rocrail.androc.R;
import android.app.TabActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;

public class andRoc extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost mTabHost = getTabHost();
		
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Throttle").setContent(R.id.tabview1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("System").setContent(R.id.tabview2));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("Layout").setContent(R.id.textview3));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test4").setIndicator("Menu").setContent(R.id.textview4));
		
		mTabHost.setCurrentTab(0);
		
	    Spinner s = (Spinner) findViewById(R.id.spinner);
	    s.setPrompt(new String("Loco"));
	    
	    ArrayAdapter m_adapterForSpinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item);        
	    m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    s.setAdapter(m_adapterForSpinner);
	    m_adapterForSpinner.add("E19");
	    m_adapterForSpinner.add("V160");
	}
}