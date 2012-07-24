/*
 Rocrail - Model Railroad Software

 Copyright (C) 2002-2011 - Rob Versluis <r.j.versluis@rocrail.net>

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.rocrail.androc.R;
import net.rocrail.androc.activities.LocoSort;
import net.rocrail.androc.interfaces.ServiceListener;
import net.rocrail.androc.objects.Loco;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ActLocoList extends ListActivity implements ServiceListener {
  ActBase m_Base = null;
  List<Loco> m_LocoList = new ArrayList<Loco>();
  LocoAdapter m_Adapter = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    m_Base = new ActBase(this, this);
    m_Base.MenuSelection = 0;

    m_Base.connectWithService();
  }
  
  public Loco getLoco(int selected) {
    return m_LocoList.get(selected);
  }
  
  public void connectedWithService() {
    m_Base.connectedWithService();
    initView();
  }

  public void initView() {
    String Consist = null;
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      Consist = extras.getString("consist");
    }

    
    Iterator<Loco> it = m_Base.m_RocrailService.m_Model.m_LocoMap.values().iterator();
    while( it.hasNext() ) {
      Loco loco = it.next();
      if( loco.Show ) {
        if( Consist == null )
          m_LocoList.add(loco);
        else if( Consist.contains(loco.ID) )
          m_LocoList.add(loco);
      }
    }
    
    Collections.sort(m_LocoList, new LocoSort(m_Base.m_RocrailService.Prefs.SortByAddr));
    m_Adapter = new LocoAdapter(this, R.layout.locorow, m_LocoList, m_Base.m_RocrailService.Prefs.SortByAddr);
    setListAdapter(m_Adapter);

    Iterator<Loco> itList = m_LocoList.iterator();
    while( itList.hasNext() ) {
      Loco loco = itList.next();
      if( loco.Show )
        m_Adapter.add(loco.toString());
    }

    ListView lv = getListView();
    lv.setTextFilterEnabled(false);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Set selected loco.
        //ActLocoList.this.setResult(position);
        getIntent().putExtra("selectedID", m_LocoList.get(position).ID);
        ActLocoList.this.setResult(position, getIntent());
        finish();
      }
    });
    
    if (extras != null) {
      int sel = extras.getInt("selected");
      setSelection(sel);
    }

    
    setResult(-1);
  }

}
