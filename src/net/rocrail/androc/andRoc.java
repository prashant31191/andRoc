/*
 Rocrail - Model Railroad Software

 Copyright (C) 2002-2010 - Rob Versluis <r.j.versluis@rocrail.net>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package net.rocrail.androc;

import net.rocrail.androc.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class andRoc extends Activity {
  System m_System = null;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    m_System = new System(this);
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
      m_System.exit();
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
          m_System.m_Host = s.getText().toString();
          s = (EditText) findViewById(R.id.connectPort);
          m_System.m_iPort = Integer.parseInt(s.getText().toString());
          // TODO: progress dialog
          try {
            m_System.connect();
            mainView();
          }
          catch( Exception e ) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(andRoc.this); 
            builder.setMessage(e.getClass().getName()+"\nCould not connect to " + m_System.m_Host+":"+m_System.m_iPort)
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              //andRoc.this.finish();
              dialog.cancel();
            }});
            
            AlertDialog alert = builder.create();
            alert.show();
          
          }
        }
    });

    
    EditText s = (EditText) findViewById(R.id.connectHost);
    s.setText(m_System.m_Host);
    s = (EditText) findViewById(R.id.connectPort);
    s.setText(""+m_System.m_iPort);
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