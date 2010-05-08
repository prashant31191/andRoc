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

import android.app.Activity;
import android.content.SharedPreferences;

public class Preferences {
  public static final String PREFS_NAME = "andRoc.ini";
  public static final String PREFS_RECENT = "recent";
  public static final String PREFS_HOST = "host";
  public static final String PREFS_PORT = "port";
  public static final String PREFS_LOCOID = "locoid";
  public static final String PREFS_MONITORING = "monitoring";
  public static final String PREFS_KEEPSCREENON = "keepscreenon";
  
  public String  Recent       = "rocrail.dyndns.org:8080;";
  public String  Host         = "rocrail.dyndns.org";
  public int     Port         = 8080;
  public boolean Monitoring   = false;
  public boolean KeepScreenOn = false;
  public String  LocoID       = "";

  public void restore(Activity activity) {
  // Restore preferences
    SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
    Host         = settings.getString(PREFS_HOST, "rocrail.dyndns.org");
    Port         = settings.getInt(PREFS_PORT, 8080);
    Recent       = settings.getString(PREFS_RECENT, "rocrail.dyndns.org:8080;");
    Monitoring   = settings.getBoolean(PREFS_MONITORING, false);
    KeepScreenOn = settings.getBoolean(PREFS_KEEPSCREENON, false);
    LocoID       = settings.getString(PREFS_LOCOID, "");
  }
  
  public void saveLoco(Activity activity, String ID) {
    SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(PREFS_LOCOID, ID);
    editor.commit();
  }
  
  public void save(Activity activity) {
    SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(PREFS_RECENT, Recent);
    editor.putBoolean(PREFS_MONITORING, Monitoring);
    editor.putBoolean(PREFS_KEEPSCREENON, KeepScreenOn);
    editor.commit();
  }
  
  public void saveConnection(Activity activity) {
    saveConnection(activity, Host, Port, true);
  }
  public void saveConnection(Activity activity, String host, int port, boolean recent) {
    SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(PREFS_HOST, host);
    editor.putInt(PREFS_PORT, port);
    if( recent)
      editor.putString(PREFS_RECENT, Recent);
    editor.commit();
    
  }
  
}
