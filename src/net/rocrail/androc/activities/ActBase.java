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

import net.rocrail.androc.RocrailService;
import net.rocrail.androc.interfaces.ServiceListener;

import net.rocrail.androc.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

public class ActBase extends Activity implements ServiceListener {

  //final static int MENU_CONNECT  = 0x0001;
  final static int MENU_THROTTLE = 0x0002;
  final static int MENU_SYSTEM   = 0x0004;
  final static int MENU_LAYOUT   = 0x0008;
  final static int MENU_MENU     = 0x0010;
  final static int MENU_LOCO     = 0x0040;
  final static int MENU_BLOCK    = 0x0080;
  final static int MENU_PREFERENCES = 0x0100;
  final static int MENU_ACCESSORY   = 0x0200;
  final static int MENU_LOCOSETUP   = 0x0400;
  final static int MENU_ZOOM        = 0x0800;
  final static int MENU_POM         = 0x1000;
  
  Activity        m_Activity = null;
  ServiceListener m_Listener = null;
  
  public int MenuSelection = 0;
  public boolean Finish = false;
  
  public RocrailService             m_RocrailService       = null;
  RocrailService.RocrailLocalBinder m_RocrailServiceBinder = null;

  
  protected ServiceConnection RocrailServiceConnection = new ServiceConnection() {
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
  
  public ActBase( Activity activity, ServiceListener listener ) {
    m_Activity = activity;
    m_Listener = listener;
  }
  
  public ActBase() {
    m_Activity = this;
    m_Listener = this;
  }
  
  public void connectWithService() {
    Intent intent = new Intent(m_Activity.getApplicationContext(), RocrailService.class);
    m_Activity.bindService(intent, RocrailServiceConnection, Context.BIND_AUTO_CREATE);
  }

  public void connectedWithService() {
    if(m_RocrailService.Prefs.KeepScreenOn && getWindow() != null) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
  }
  
  public void updateTitle(String title) {
    m_Activity.setTitle("andRoc " + title);
  }
  public void updateTitle() {
    m_Activity.setTitle("andRoc " + m_RocrailService.m_Model.m_Title);
  }
  
 
  /* Creates the menu items */
  public boolean onCreateOptionsMenu(Menu menu) {
    //if( (MenuSelection & MENU_CONNECT)  == MENU_CONNECT )
    //  menu.add(0, MENU_CONNECT , 0, R.string.Connect).setIcon(R.drawable.connect);
    if( (MenuSelection & MENU_THROTTLE)  == MENU_THROTTLE )
      menu.add(0, MENU_THROTTLE, 0, R.string.Throttle ).setIcon(R.drawable.throttle);
    if( (MenuSelection & MENU_SYSTEM)  == MENU_SYSTEM )
      menu.add(0, MENU_SYSTEM  , 0, R.string.System).setIcon(R.drawable.system);
    if( (MenuSelection & MENU_LAYOUT)  == MENU_LAYOUT )
      menu.add(0, MENU_LAYOUT  , 0, R.string.Layout).setIcon(R.drawable.layout);
    if( (MenuSelection & MENU_MENU)  == MENU_MENU )
      menu.add(0, MENU_MENU    , 0, R.string.Menu).setIcon(R.drawable.menu);
    if( (MenuSelection & MENU_LOCO)  == MENU_LOCO )
      menu.add(0, MENU_LOCO    , 0, R.string.Loco).setIcon(R.drawable.loco);
    if( (MenuSelection & MENU_ZOOM)  == MENU_ZOOM )
      menu.add(0, MENU_ZOOM    , 0, R.string.Zoom).setIcon(R.drawable.zoom);
    if( (MenuSelection & MENU_PREFERENCES)  == MENU_PREFERENCES )
      menu.add(0, MENU_PREFERENCES    , 0, R.string.Preferences).setIcon(R.drawable.preferences);
    if( (MenuSelection & MENU_ACCESSORY)  == MENU_ACCESSORY )
      menu.add(0, MENU_ACCESSORY    , 0, R.string.Accessory).setIcon(R.drawable.accessory);
    if( (MenuSelection & MENU_LOCOSETUP)  == MENU_LOCOSETUP )
      menu.add(0, MENU_LOCOSETUP    , 0, R.string.LocoSetup).setIcon(R.drawable.locosetup);
    if( (MenuSelection & MENU_ZOOM)  == MENU_ZOOM )
      menu.add(0, MENU_ZOOM    , 0, R.string.Zoom).setIcon(R.drawable.zoom);
    if( (MenuSelection & MENU_POM)  == MENU_POM )
      menu.add(0, MENU_POM    , 0, R.string.CV).setIcon(R.drawable.locosetup);
    return true;
  }

  /* Handles item selections */
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
//    case MENU_CONNECT:
//      connectView();
//      return true;
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
    case MENU_LOCO:
      locoView();
      return true;
    case MENU_PREFERENCES:
      preferencesView();
      return true;
    case MENU_ACCESSORY:
      accessoryView();
      return true;
    case MENU_LOCOSETUP:
      locosetupView();
      return true;
    case MENU_ZOOM:
      zoomLevel();
      return true;
    case MENU_POM:
      locosetupView();
      return true;
    }
    return false;
  }
  
  public void connectView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActConnect.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    m_Activity.startActivityIfNeeded(intent,0);
    if(Finish) 
      m_Activity.finish();
  }
  
  public void throttleView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActThrottle.class);
    m_Activity.startActivityIfNeeded(intent,0);
    if(Finish) 
      m_Activity.finish();
  }
  
  public void systemView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActSystem.class);
    m_Activity.startActivityIfNeeded(intent,0);
    if(Finish) 
      m_Activity.finish();
  }
  
  public void preferencesView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActPreferences.class);
    m_Activity.startActivityIfNeeded(intent,0);
    if(Finish) 
      m_Activity.finish();
  }
  
  public void accessoryView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActAccessory.class);
    m_Activity.startActivityIfNeeded(intent,0);
    if(Finish) 
      m_Activity.finish();
  }
  
  public void locosetupView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLocoSetup.class);
    m_Activity.startActivityIfNeeded(intent,0);
    if(Finish) 
      m_Activity.finish();
  }
  
  public void menuView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActMenu.class);
    m_Activity.startActivityIfNeeded(intent,0);
    if(Finish) 
      m_Activity.finish();
  }
  
  public void layoutView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLayout.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    m_Activity.startActivityIfNeeded(intent,0);
    if(Finish) 
      m_Activity.finish();
  }
  
  public void layoutView(boolean init) {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLayout.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("init", "true");
    m_Activity.startActivityIfNeeded(intent,0);
    if(Finish) 
      m_Activity.finish();
  }
  
  public void locoView() {
    Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLoco.class);
    m_Activity.startActivityIfNeeded(intent,0);
    if(Finish) 
      m_Activity.finish();
  }

  public void zoomLevel() {
    MotionEvent event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 
        (float)0.0, (float)0.0, (float)0.0, (float)0.0, 0, (float)0.0, (float)0.0, 0, 0);
    onTouchEvent(event);
  }

  public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub
    return super.onKeyDown(keyCode, event);
  }
  
}
