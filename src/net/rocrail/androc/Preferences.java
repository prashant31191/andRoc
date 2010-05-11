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

import java.util.List;

import net.rocrail.androc.objects.RRConnection;
import android.content.SharedPreferences;

public class Preferences {
  public static final String PREFS_NAME = "andRoc.ini";
  public static final String PREFS_RECENT = "connections";
  public static final String PREFS_HOST = "host";
  public static final String PREFS_PORT = "port";
  public static final String PREFS_LOCOID = "locoid";
  public static final String PREFS_MONITORING = "monitoring";
  public static final String PREFS_KEEPSCREENON = "keepscreenon";
  public static final String PREFS_RRNETHOST = "rrnethost";
  public static final String PREFS_RRNETPORT = "rrnetport";
  
  public String  Recent       = "Demo:rocrail.dyndns.org:8080;";
  public String  Host         = "rocrail.dyndns.org";
  public int     Port         = 8080;
  public String  RRHost       = "224.0.0.1";
  public int     RRPort       = 1234;
  public boolean Monitoring   = false;
  public boolean KeepScreenOn = false;
  public String  LocoID       = "";
  public String  Title        = "";
  RocrailService rocrailService = null;
  boolean Initialized = false;
  
  public List<RRConnection> conList = null; 

  public Preferences( RocrailService rocrailService) {
    this.rocrailService = rocrailService;
  }

  public void restore() {
    if( Initialized ) {
      return;
    }
    Initialized = true;
    
  // Restore preferences
    SharedPreferences settings = rocrailService.getSharedPreferences(PREFS_NAME, 0);
    Host         = settings.getString(PREFS_HOST, Host);
    Port         = settings.getInt(PREFS_PORT, Port);
    RRHost       = settings.getString(PREFS_RRNETHOST, RRHost);
    RRPort       = settings.getInt(PREFS_RRNETPORT, RRPort);
    Recent       = settings.getString(PREFS_RECENT, Recent);
    Monitoring   = settings.getBoolean(PREFS_MONITORING, Monitoring);
    KeepScreenOn = settings.getBoolean(PREFS_KEEPSCREENON, KeepScreenOn);
    LocoID       = settings.getString(PREFS_LOCOID, LocoID);
    
    conList = RRConnection.parse(Recent);

    try {
      if( Integer.parseInt(android.os.Build.VERSION.SDK) >= 4 ) {
        R2RNet RrNet = new R2RNet(rocrailService, this);
        RrNet.set(RRHost, RRPort);
        // wait some time for the RRNet to get connections?
        Thread.sleep(500);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  public void saveLoco(String ID) {
    SharedPreferences settings = rocrailService.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(PREFS_LOCOID, ID);
    editor.commit();
  }
  
  public void save() {
    Recent = RRConnection.serialize(conList);
    SharedPreferences settings = rocrailService.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(PREFS_RECENT, Recent);
    editor.putBoolean(PREFS_MONITORING, Monitoring);
    editor.putBoolean(PREFS_KEEPSCREENON, KeepScreenOn);
    editor.putString(PREFS_RRNETHOST, RRHost);
    editor.putInt(PREFS_RRNETPORT, RRPort);
    editor.commit();
  }
  
  public void saveConnection() {
    saveConnection(Host, Port, true);
  }
  public void saveConnection(String host, int port, boolean recent) {
    SharedPreferences settings = rocrailService.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(PREFS_HOST, host);
    editor.putInt(PREFS_PORT, port);
    if( recent) {
      Recent = RRConnection.serialize(conList);
      editor.putString(PREFS_RECENT, Recent);
    }
    editor.commit();
    
  }
  
  
  
  
}
