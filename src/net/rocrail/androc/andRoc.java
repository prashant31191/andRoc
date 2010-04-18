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
import net.rocrail.androc.activities.Connect;
import net.rocrail.androc.activities.Throttle;
import net.rocrail.androc.activities.System;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Application entry activity representing a splash screen.
 * @author rob
 *
 */
public class andRoc extends Activity {
  RocrailService     m_RocrailService     = null;
  Throttle   m_Throttle   = null;
  //Connect m_Connection = null;
  System     m_System     = null;
  
  final static int MENU_CONNECT  = 1;
  final static int MENU_THROTTLE = 2;
  final static int MENU_SYSTEM   = 3;
  final static int MENU_LAYOUT   = 4;
  final static int MENU_MENU     = 5;
  final static int MENU_QUIT     = 6;
  

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    m_RocrailService = new RocrailService(this);
    
    m_Throttle   = new Throttle(this);
    //m_Connection = new Connect(this);
    m_System     = new System(this);
    
    //m_Connection.initView();
    setContentView(R.layout.androc);

  }
  
  public RocrailService getSystem() {
    return m_RocrailService;
  }
  
  public void Connected() {
    m_RocrailService.sendMessage("model","<model cmd=\"plan\" disablemonitor=\"true\"/>");
    m_Throttle.initView();
  }

  /* Creates the menu items */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_CONNECT , 0, "Connect").setIcon(R.drawable.connect);
    menu.add(0, MENU_THROTTLE, 0, "Throttle").setIcon(R.drawable.loco);
    menu.add(0, MENU_SYSTEM  , 0, "System").setIcon(R.drawable.system);
    menu.add(0, MENU_LAYOUT  , 0, "Layout").setIcon(R.drawable.layout);
    menu.add(0, MENU_MENU    , 0, "Menu").setIcon(R.drawable.menu);
    menu.add(0, MENU_QUIT    , 0, "Quit").setIcon(R.drawable.quit);
    return true;
  }

  /* Handles item selections */
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_CONNECT:
    {
      //m_Connection.initView();
      Intent intent = new Intent(this,Connect.class);
      startActivity(intent);
      return true;
    }
    case MENU_THROTTLE:
      m_Throttle.initView();
      return true;
    case MENU_SYSTEM:
      m_System.initView();
      return true;
    case MENU_QUIT:
      m_RocrailService.exit();
      finish();
      return true;
    }
    return false;
  }
  
  public void onPause() {
    super.onPause();
    // 1 also received at orientation change
    return;
  }

  public void onStop() {
    super.onStop();
    // 2 also received at orientation change
    return;
  }

}