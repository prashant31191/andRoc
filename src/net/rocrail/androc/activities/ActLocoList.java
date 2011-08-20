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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActLocoList extends ListActivity implements ServiceListener {
  ActBase m_Base = null;
  List<Loco> m_LocoList = new ArrayList<Loco>();

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
  }

  public void initView() {
    Iterator<Loco> it = m_Base.m_RocrailService.m_Model.m_LocoMap.values().iterator();
    while( it.hasNext() ) {
      Loco loco = it.next();
      m_LocoList.add(loco);
    }
    
    Collections.sort(m_LocoList, new LocoSort());
    LocoAdapter adapter = new LocoAdapter(this, R.layout.locorow, m_LocoList);
    setListAdapter(adapter);

    Iterator<Loco> itList = m_LocoList.iterator();
    while( itList.hasNext() ) {
      Loco loco = itList.next();
      adapter.add(loco.toString());
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

}
