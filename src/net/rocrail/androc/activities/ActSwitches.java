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

import java.util.Iterator;

import net.rocrail.androc.interfaces.ServiceListener;
import net.rocrail.androc.objects.Switch;


import net.rocrail.androc.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActSwitches extends ListActivity implements ServiceListener   {

  ActBase m_Base = null;
  String[] m_Switches = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    m_Base = new ActBase(this, this);
    m_Base.MenuSelection = 0; //Base.MENU_THROTTLE | Base.MENU_SYSTEM | Base.MENU_MENU;

    m_Base.connectWithService();
  }
  
  public void connectedWithService() {
    m_Base.connectedWithService();
    initView();
    m_Base.updateTitle(getText(R.string.Switches).toString());
  }


  public void initView() {
    m_Switches = new String[m_Base.m_RocrailService.m_Model.m_SwitchList.size()];
    Iterator<String> it = m_Base.m_RocrailService.m_Model.m_SwitchList.iterator();
    int idx = 0;
    while( it.hasNext() ) {
      m_Switches[idx] = it.next();
      idx++;
    }
    setListAdapter(new ArrayAdapter<String>(this, R.layout.menuitem, m_Switches));

    ListView lv = getListView();
    lv.setTextFilterEnabled(true);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // When clicked, show a toast with the TextView text
        Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
        // send route command
        Switch sw = m_Base.m_RocrailService.m_Model.m_SwitchMap.get(((TextView) view).getText());
        if( sw != null ) {
          sw.flip();
        }
      }
    });
  }
}
