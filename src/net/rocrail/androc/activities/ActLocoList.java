package net.rocrail.androc.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.rocrail.androc.R;
import net.rocrail.androc.activities.LocoSort;
import net.rocrail.androc.interfaces.ServiceListener;
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.objects.Switch;
import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    Iterator<Loco> it = m_Base.m_RocrailService.m_Model.m_LocoMap.values().iterator();
    while( it.hasNext() ) {
      Loco loco = it.next();
      m_LocoList.add(loco);
    }
    
    Collections.sort(m_LocoList, new LocoSort());
    m_Adapter = new LocoAdapter(this, R.layout.locorow, m_LocoList);
    setListAdapter(m_Adapter);

    Iterator<Loco> itList = m_LocoList.iterator();
    while( itList.hasNext() ) {
      Loco loco = itList.next();
      m_Adapter.add(loco.toString());
    }

    ListView lv = getListView();
    lv.setTextFilterEnabled(false);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Set selected loco.
        ActLocoList.this.setResult(position);
        finish();
      }
    });
  }

  public void onLocoRowClick(View v) { 
    LinearLayout vwParentRow = (LinearLayout)v.getParent();
    vwParentRow.setBackgroundColor(Color.CYAN); 
    vwParentRow.refreshDrawableState();
    
    TextView textView = (TextView)vwParentRow.getChildAt(1);

    vwParentRow.performClick();
  }
  
}
