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
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Info extends ListActivity implements ServiceListener {
  Base m_Base = null;
  String[] m_Items = null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    m_Base = new Base(this, this);
    m_Base.connectWithService();
  }
  
  public void connectedWithService() {
    initView();
    m_Base.updateTitle();
  }


  public void initView() {
    m_Items = new String[7];
    m_Items[0] = "Copyrights Rob Versluis\nhttp://www.rocrail.net\nGNU GENERAL PUBLIC LICENSE";
    m_Items[1] = "andRoc Version:\n0.1.52";
    m_Items[2] = "Device ID:\n"+m_Base.m_RocrailService.getDeviceName();
    m_Items[3] = "Rocrail Version:\n" + m_Base.m_RocrailService.m_Model.m_RocrailVersion;
    m_Items[4] = "Layout Title:\n" + m_Base.m_RocrailService.m_Model.m_Title;
    m_Items[5] = m_Base.m_RocrailService.m_Model.m_LocoList.size() + " Locos";
    m_Items[6] = m_Base.m_RocrailService.m_Model.m_SwitchList.size() + " Switches";
      
    setListAdapter(new ArrayAdapter<String>(this, R.layout.menuitem, m_Items));

    ListView lv = getListView();
    lv.setTextFilterEnabled(true);
  }
}
