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
  public static final String PREFS_CTRLCODE = "ctrlcode";
  public static final String PREFS_LOCOID = "locoid";
  public static final String PREFS_LOCOID2 = "locoid2";
  public static final String PREFS_LOCOID3 = "locoid3";
  public static final String PREFS_LOCOID4 = "locoid4";
  public static final String PREFS_LOCOID5 = "locoid5";
  public static final String PREFS_LOCOID6 = "locoid6";
  public static final String PREFS_MONITORING = "monitoring";
  public static final String PREFS_KEEPSCREENON = "keepscreenon";
  public static final String PREFS_RRNETHOST = "rrnethost";
  public static final String PREFS_RRNETPORT = "rrnetport";
  public static final String PREFS_ACCNR = "accnr";
  public static final String PREFS_ACCTYPE = "acctype";
  public static final String PREFS_CVNR = "cvnr";
  public static final String PREFS_MODVIEW = "modview";
  public static final String PREFS_SIZE = "size";
  public static final String PREFS_SMALLTHROTTLE = "smallthrottle";
  public static final String PREFS_IMAGESONDEMAND = "imagesondemand";
  public static final String PREFS_SORTBYADDR = "sortbyaddr";
  public static final String PREFS_POWEROFF4EBREAK = "poweroff4ebreak";
  public static final String PREFS_COLOR = "color";
  public static final String PREFS_LOCOCATLIST = "lococatlist";
  public static final String PREFS_USEALLSPEEDSTEPS = "useallspeedsteps";
  public static final String PREFS_SYNCSPEED = "syncspeed";
  
  public static final String ACCTYPE_MADA = "M";
  public static final String ACCTYPE_FADA = "F";
  public static final String ACCTYPE_PADA = "P";
  
  public String  Recent       = "Demo:rocrail.dyndns.org:8051;";
  public String  Host         = "rocrail.dyndns.org";
  public String  CtrlCode     = "";
  public int     Port         = 8051;
  public String  RRHost       = "224.0.0.1";
  public int     RRPort       = 1234;
  public boolean Monitoring   = false;
  public boolean KeepScreenOn = false;
  public boolean SmallThrottle  = true;
  public boolean ImagesOnDemand = false;
  public boolean SortByAddr   = false;
  public boolean LocoCatList  = false;
  public boolean Modview      = true;
  public boolean PowerOff4EBreak = false;
  public boolean UseAllSpeedSteps = false;
  public boolean SyncSpeed = true;
  public String  LocoID       = "";
  public String  LocoID2      = "";
  public String  LocoID3      = "";
  public String  LocoID4      = "";
  public String  LocoID5      = "";
  public String  LocoID6      = "";
  public String  Title        = "";
  public int     AccNr        = 1;
  public int     CvNr         = 1;
  public String  AccType      = ACCTYPE_MADA;
  RocrailService rocrailService = null;
  boolean Initialized = false;
  public int     Size         = 32;
  public int     Color        = 0;
  
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
    CtrlCode     = settings.getString(PREFS_CTRLCODE, CtrlCode);
    RRHost       = settings.getString(PREFS_RRNETHOST, RRHost);
    RRPort       = settings.getInt(PREFS_RRNETPORT, RRPort);
    Recent       = settings.getString(PREFS_RECENT, Recent);
    Monitoring   = settings.getBoolean(PREFS_MONITORING, Monitoring);
    KeepScreenOn = settings.getBoolean(PREFS_KEEPSCREENON, KeepScreenOn);
    LocoID       = settings.getString(PREFS_LOCOID, LocoID);
    LocoID2      = settings.getString(PREFS_LOCOID2, LocoID2);
    LocoID3      = settings.getString(PREFS_LOCOID3, LocoID3);
    LocoID4      = settings.getString(PREFS_LOCOID4, LocoID4);
    LocoID5      = settings.getString(PREFS_LOCOID5, LocoID5);
    LocoID6      = settings.getString(PREFS_LOCOID6, LocoID6);
    AccType      = settings.getString(PREFS_ACCTYPE, AccType);
    AccNr        = settings.getInt(PREFS_ACCNR, AccNr);
    CvNr         = settings.getInt(PREFS_CVNR, CvNr);
    Modview      = settings.getBoolean(PREFS_MODVIEW, Modview);
    PowerOff4EBreak = settings.getBoolean(PREFS_POWEROFF4EBREAK, PowerOff4EBreak);
    UseAllSpeedSteps = settings.getBoolean(PREFS_USEALLSPEEDSTEPS, UseAllSpeedSteps);
    SyncSpeed = settings.getBoolean(PREFS_SYNCSPEED, SyncSpeed);
    Size         = settings.getInt(PREFS_SIZE, Size);
    SmallThrottle  = settings.getBoolean(PREFS_SMALLTHROTTLE, SmallThrottle);
    ImagesOnDemand = settings.getBoolean(PREFS_IMAGESONDEMAND, ImagesOnDemand);
    SortByAddr    = settings.getBoolean(PREFS_SORTBYADDR, SortByAddr);
    LocoCatList   = settings.getBoolean(PREFS_LOCOCATLIST, LocoCatList);
    Color         = settings.getInt(PREFS_COLOR, Color);
    
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
  
  public String getLocoID(int nr) {
    System.out.println("getLocoID: "+"["+nr+"]");
    switch( nr ) {
    case 2: return LocoID2;
    case 3: return LocoID3;
    case 4: return LocoID4;
    case 5: return LocoID5;
    case 6: return LocoID6;
    default: return LocoID;
    }
  }
  
  public void saveLoco(String ID, int nr) {
    SharedPreferences settings = rocrailService.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    System.out.println("saveLoco: "+ID+"["+nr+"]");
    switch( nr ) {
    case 2: LocoID2 = ID; editor.putString(PREFS_LOCOID2, ID); break;
    case 3: LocoID3 = ID; editor.putString(PREFS_LOCOID3, ID); break;
    case 4: LocoID4 = ID; editor.putString(PREFS_LOCOID4, ID); break;
    case 5: LocoID5 = ID; editor.putString(PREFS_LOCOID5, ID); break;
    case 6: LocoID6 = ID; editor.putString(PREFS_LOCOID6, ID); break;
    default: LocoID = ID; editor.putString(PREFS_LOCOID, ID); break;
    }
    editor.commit();
  }
  
  public void saveAccessory(String type, int nr) {
    SharedPreferences settings = rocrailService.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(PREFS_ACCTYPE, type);
    editor.putInt(PREFS_ACCNR, nr);
    editor.commit();
  }
  
  public void saveProgramming(int cv) {
    CvNr = cv;
    SharedPreferences settings = rocrailService.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putInt(PREFS_CVNR, cv);
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
    editor.putBoolean(PREFS_MODVIEW, Modview);
    editor.putInt(PREFS_SIZE, Size);
    editor.putBoolean(PREFS_SMALLTHROTTLE, SmallThrottle);
    editor.putBoolean(PREFS_IMAGESONDEMAND, ImagesOnDemand);
    editor.putBoolean(PREFS_SORTBYADDR, SortByAddr);
    editor.putBoolean(PREFS_LOCOCATLIST, LocoCatList);
    editor.putBoolean(PREFS_POWEROFF4EBREAK, PowerOff4EBreak);
    editor.putBoolean(PREFS_USEALLSPEEDSTEPS, UseAllSpeedSteps);
    editor.putBoolean(PREFS_SYNCSPEED, SyncSpeed);
    editor.putInt(PREFS_COLOR, Color);
    editor.commit();
  }
  
  public void saveConnection() {
    saveConnection(Host, Port, CtrlCode, true);
  }
  public void saveConnection(String host, int port, String ctrlcode, boolean recent) {
    SharedPreferences settings = rocrailService.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(PREFS_HOST, host);
    editor.putInt(PREFS_PORT, port);
    editor.putString(PREFS_CTRLCODE, ctrlcode);
    if( recent) {
      Recent = RRConnection.serialize(conList);
      editor.putString(PREFS_RECENT, Recent);
    }
    editor.commit();
    
  }
  
  
  
  
}
