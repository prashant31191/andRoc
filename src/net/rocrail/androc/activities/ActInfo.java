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

import net.rocrail.androc.interfaces.ServiceListener;

import net.rocrail.androc.R;
import android.app.ListActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActInfo extends ListActivity implements ServiceListener {
  ActBase m_Base = null;
  String[] m_Items = null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    m_Base = new ActBase(this, this);
    m_Base.MenuSelection = 0;
    m_Base.connectWithService();
  }
  
  public void connectedWithService() {
    m_Base.connectedWithService();
    initView();
    m_Base.updateTitle();
  }


  public void initView() {
    int versionCode = 0; 
    String versionName = "?"; 
    PackageInfo pinfo;
    try {
      pinfo = getPackageManager().getPackageInfo 
      (this.getPackageName(), 0);
      versionCode = pinfo.versionCode; 
      versionName = pinfo.versionName; 
    } catch (NameNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 

    m_Items = new String[5];
    m_Items[0] = "TM and Copyright 2010-2011 Rob Versluis\nhttp://www.rocrail.net";
    m_Items[1] = getText(R.string.License) + ": GNU GENERAL PUBLIC LICENSE";
    m_Items[2] = "andRoc " + getText(R.string.Version) + ": "+versionName;
    m_Items[3] = getText(R.string.ThrottleID) + ": "+m_Base.m_RocrailService.getDeviceName();
    m_Items[4] = "Rocrail " + getText(R.string.Version) + ": " + m_Base.m_RocrailService.m_Model.m_RocrailVersion;
      
    setListAdapter(new ArrayAdapter<String>(this, R.layout.menuitem, m_Items));

    ListView lv = getListView();
    lv.setTextFilterEnabled(true);
  }
}
