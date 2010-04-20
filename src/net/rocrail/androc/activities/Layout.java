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

import net.rocrail.androc.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Layout extends ListActivity implements ServiceListener {
  Base m_Base = null;
  String[] m_Levels = null;
  
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    m_Base = new Base(this, this);
    m_Base.connectWithService();
  }
  
  public void connectedWithService() {
    initView();
  }


  public void initView() {
    m_Levels = new String[m_Base.m_RocrailService.m_Model.m_ZLevelList.size()];
    Iterator<String> it = m_Base.m_RocrailService.m_Model.m_ZLevelList.iterator();
    int idx = 0;
    while( it.hasNext() ) {
      m_Levels[idx] = it.next();
      idx++;
    }
    setListAdapter(new ArrayAdapter<String>(this, R.layout.layoutitem, m_Levels));

    ListView lv = getListView();
    lv.setTextFilterEnabled(true);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view,
          int position, long id) {
        // When clicked, show a toast with the TextView text
        Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
            Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Layout.this,net.rocrail.androc.activities.Level.class);
        intent.putExtra("level", position);
        startActivity(intent);
      }
    });
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    return m_Base.onCreateOptionsMenu(menu);
  }
  
  public boolean onOptionsItemSelected(MenuItem item) {
    return m_Base.onOptionsItemSelected(item);
  }
  
}