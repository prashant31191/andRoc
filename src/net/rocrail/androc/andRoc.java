package net.rocrail.androc;

import net.rocrail.androc.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;

public class andRoc extends Activity {
  public static final String PREFS_NAME = "andRoc.ini";
  String m_Host = "?";
  int m_iPort = 0;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Restore preferences
    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    m_Host = settings.getString("host", "rocrail.dyndns.org");
    m_iPort = settings.getInt("port", 8080);
    connectView();
  }

  /* Creates the menu items */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, 6, 0, "Connect").setIcon(R.drawable.connect);
    menu.add(0, 1, 0, "Throttle").setIcon(R.drawable.loco);
    menu.add(0, 3, 0, "System").setIcon(R.drawable.system);
    menu.add(0, 4, 0, "Layout").setIcon(R.drawable.layout);
    menu.add(0, 5, 0, "Menu").setIcon(R.drawable.menu);
    menu.add(0, 2, 0, "Quit").setIcon(R.drawable.quit);
    return true;
  }

  /* Handles item selections */
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case 1:
      mainView();
      return true;
    case 3:
      setContentView(R.layout.system);
      return true;
    case 2: {
      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
      SharedPreferences.Editor editor = settings.edit();
      editor.putString("host", m_Host);
      editor.putInt("port", m_iPort);
      editor.commit();
      finish();
      return true;
    }
    case 6:
      connectView();
      return true;
    }
    return false;
  }

  void connectView() {
    setContentView(R.layout.connect);
    
    final Button button = (Button) findViewById(R.id.ButtonConnect);
    button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            // Perform action on click
          EditText s = (EditText) findViewById(R.id.connectHost);
          m_Host = s.getText().toString();
          s = (EditText) findViewById(R.id.connectPort);
          m_iPort = Integer.parseInt(s.getText().toString());
          mainView();
        }
    });

    
    EditText s = (EditText) findViewById(R.id.connectHost);
    s.setText(m_Host);
    s = (EditText) findViewById(R.id.connectPort);
    s.setText(""+m_iPort);
  }

  void mainView() {
    setContentView(R.layout.main);
    Spinner s = (Spinner) findViewById(R.id.spinnerLoco);
    s.setPrompt(new String("Loco"));

    ArrayAdapter m_adapterForSpinner = new ArrayAdapter(this,
        android.R.layout.simple_spinner_item);
    m_adapterForSpinner
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);
    m_adapterForSpinner.add("NS 2418");
    m_adapterForSpinner.add("E19");
    m_adapterForSpinner.add("V160");
  }
}