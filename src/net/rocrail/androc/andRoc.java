package net.rocrail.androc;

import net.rocrail.androc.R;
import android.app.TabActivity;
import android.graphics.drawable.Drawable;
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
		
		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Throttle", this.getResources().getDrawable(R.drawable.loco)).setContent(R.id.tabviewThrottle));
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("System", this.getResources().getDrawable(R.drawable.system)).setContent(R.id.tabviewSystem));
		mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("Layout", this.getResources().getDrawable(R.drawable.layout)).setContent(R.id.tabviewLayout));
		mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator("Menu",this.getResources().getDrawable(R.drawable.menu)).setContent(R.id.tabviewMenu));
		
		mTabHost.setCurrentTab(0);
		
	    Spinner s = (Spinner) findViewById(R.id.spinnerLoco);
	    s.setPrompt(new String("Loco"));
	    
	    ArrayAdapter m_adapterForSpinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item);        
	    m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    s.setAdapter(m_adapterForSpinner);
	    m_adapterForSpinner.add("E19");
	    m_adapterForSpinner.add("V160");
	}
}