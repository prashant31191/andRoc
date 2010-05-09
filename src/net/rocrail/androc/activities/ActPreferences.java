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

package net.rocrail.androc.activities;

import net.rocrail.androc.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class ActPreferences extends ActBase {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = 0;
    connectWithService();
  }
  
  public void connectedWithService() {
    super.connectedWithService();
    initView();
  }
  
  public void initView() {
    setContentView(R.layout.preferences);
    CheckBox cb = (CheckBox)findViewById(R.id.prefMonitoring);
    cb.setChecked(m_RocrailService.Prefs.Monitoring);
    cb = (CheckBox)findViewById(R.id.prefKeepScreenOn);
    cb.setChecked(m_RocrailService.Prefs.KeepScreenOn);
    Button b = (Button)findViewById(R.id.prefClearRecent);
    b.setEnabled(m_RocrailService.Prefs.Recent.length() > 0);
    b.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        m_RocrailService.Prefs.Recent = "";
        v.setEnabled(m_RocrailService.Prefs.Recent.length() > 0);
      }
    });
    
    EditText et = (EditText)findViewById(R.id.prefR2RHost);
    et.setText(m_RocrailService.Prefs.RRHost);
    et = (EditText)findViewById(R.id.prefR2RPort);
    et.setText(""+m_RocrailService.Prefs.RRPort);

  }
  
  void savePrefs() {
    CheckBox cb = (CheckBox)findViewById(R.id.prefMonitoring);
    m_RocrailService.Prefs.Monitoring = cb.isChecked();
    cb = (CheckBox)findViewById(R.id.prefKeepScreenOn);
    m_RocrailService.Prefs.KeepScreenOn = cb.isChecked();
  
    EditText et = (EditText)findViewById(R.id.prefR2RHost);
    m_RocrailService.Prefs.RRHost = et.getText().toString();
    et = (EditText)findViewById(R.id.prefR2RPort);
    m_RocrailService.Prefs.RRPort = Integer.parseInt(et.getText().toString());
    
    m_RocrailService.Prefs.save(this);
  }
  
  @Override
  protected void onPause() {
    super.onPause();
    savePrefs();
  }
  
  
}
