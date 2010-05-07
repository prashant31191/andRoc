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
import net.rocrail.androc.interfaces.ServiceListener;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActMenu extends  ListActivity implements ServiceListener {
  ActBase m_Base = null;
  String[] m_Items = null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    m_Base = new ActBase(this, this);
    m_Base.MenuSelection = ActBase.MENU_THROTTLE | ActBase.MENU_SYSTEM | ActBase.MENU_LAYOUT | ActBase.MENU_PREFERENCES;
    m_Base.connectWithService();
  }
  
  public void connectedWithService() {
    initView();
    m_Base.updateTitle("Menu");
  }


  public void initView() {
    m_Items = new String[2];
    m_Items[0] = "Info";
    m_Items[1] = "Routes";
      
    setListAdapter(new ArrayAdapter<String>(this, R.layout.menuitem, m_Items));

    ListView lv = getListView();
    lv.setTextFilterEnabled(true);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // When clicked, show a toast with the TextView text
        Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
            Toast.LENGTH_SHORT).show();
        switch( position ) {
          case 0: {
            Intent intent = new Intent(ActMenu.this,net.rocrail.androc.activities.ActInfo.class);
            startActivity(intent);
          }
          break;
          case 1: {
            Intent intent = new Intent(ActMenu.this,net.rocrail.androc.activities.ActRoutes.class);
            startActivity(intent);
          }
          break;
        }
      }
    });
  }

  public boolean onCreateOptionsMenu(android.view.Menu menu) {
    return m_Base.onCreateOptionsMenu(menu);
  }
  
  public boolean onOptionsItemSelected(android.view.MenuItem item) {
    return m_Base.onOptionsItemSelected(item);
  }
  
}
