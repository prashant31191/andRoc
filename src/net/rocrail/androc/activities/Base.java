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
import net.rocrail.androc.RocrailService;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

public class Base extends Activity implements ServiceListener {
  public static final String PREFS_NAME = "andRoc.ini";

  final static int MENU_CONNECT  = 1;
  final static int MENU_THROTTLE = 2;
  final static int MENU_SYSTEM   = 3;
  final static int MENU_LAYOUT   = 4;
  final static int MENU_MENU     = 5;
  final static int MENU_QUIT     = 6;
  
  Activity        m_Activity = null;
  ServiceListener m_Listener = null;
  
  public RocrailService             m_RocrailService       = null;
  RocrailService.RocrailLocalBinder m_RocrailServiceBinder = null;

  
  private ServiceConnection RocrailServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName className, IBinder binder) {
      m_RocrailServiceBinder = (RocrailService.RocrailLocalBinder)binder;
      m_RocrailService = m_RocrailServiceBinder.getService();
      m_Listener.connectedWithService();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
      // TODO Auto-generated method stub
      
    }
  };
  
  public Base( Activity activity, ServiceListener listener ) {
    m_Activity = activity;
    m_Listener = listener;
  }
  
  public Base() {
    m_Activity = this;
    m_Listener = this;
  }
  
  public void connectWithService() {
    Intent intent = new Intent(m_Activity.getApplicationContext(), RocrailService.class);
    m_Activity.bindService(intent, RocrailServiceConnection, Context.BIND_AUTO_CREATE);
  }

  public void connectedWithService() {
    
  }
  
  public void updateTitle() {
    m_Activity.setTitle("andRoc " + m_RocrailService.m_Model.m_Title);
  }
  

  
  public void restorePreferences() {
  // Restore preferences
    SharedPreferences settings = m_Activity.getSharedPreferences(PREFS_NAME, 0);
    if( m_RocrailService != null ) {
      m_RocrailService.m_Host  = settings.getString("host", "rocrail.dyndns.org");
      m_RocrailService.m_iPort = settings.getInt("port", 8080);
    }
  }

  
  /* Creates the menu items */
  public boolean onCreateOptionsMenu(Menu menu) {
    //menu.add(0, MENU_CONNECT , 0, "Connect").setIcon(R.drawable.connect);
    menu.add(0, MENU_THROTTLE, 0, R.string.Throttle ).setIcon(R.drawable.loco);
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
      connectView();
      return true;
    case MENU_THROTTLE:
      throttleView();
      return true;
    case MENU_SYSTEM:
      systemView();
      return true;
    case MENU_LAYOUT:
      layoutView();
      return true;
    case MENU_MENU:
      menuView();
      return true;
    case MENU_QUIT:
      m_Activity.finish();
      return true;
    }
    return false;
  }
  
  public void connectView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.Connect.class);
    m_Activity.startActivity(intent);
    m_Activity.finish();
  }
  
  public void throttleView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.Throttle.class);
    m_Activity.startActivity(intent);
    m_Activity.finish();
  }
  
  public void systemView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.System.class);
    m_Activity.startActivity(intent);
    m_Activity.finish();
  }
  
  public void menuView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.Menu.class);
    m_Activity.startActivity(intent);
    m_Activity.finish();
  }
  
  public void layoutView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.Layout.class);
    m_Activity.startActivity(intent);
    m_Activity.finish();
  }
  
}
