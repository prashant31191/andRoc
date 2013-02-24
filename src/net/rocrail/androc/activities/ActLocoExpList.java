package net.rocrail.androc.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.rocrail.androc.R;
import net.rocrail.androc.interfaces.Mobile;
import net.rocrail.androc.objects.Loco;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;


public class ActLocoExpList extends ActBase {
  List<Mobile> m_LocoList = new ArrayList<Mobile>();
  List<Mobile> m_CarList = new ArrayList<Mobile>();
  LocoExpListAdapter m_Adapter = null;
  ExpandableListView m_ListView = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.connectWithService();
    MenuSelection = MENU_PREFERENCES;

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
    }
  }
  
  public void connectedWithService() {
    super.connectedWithService();
    initView();
  }


  public void initView() {
    setContentView(R.layout.locoexplist);
    m_ListView = (ExpandableListView) findViewById(R.id.locoExpList);

    Iterator<Mobile> it = m_RocrailService.m_Model.m_LocoMap.values().iterator();
    while( it.hasNext() ) {
      Mobile loco = it.next();
      if(loco.isShow())
        m_LocoList.add(loco);
    }
    
    Collections.sort(m_LocoList, new LocoSort(m_RocrailService.Prefs.SortByAddr));

    it = m_RocrailService.m_Model.m_CarMap.values().iterator();
    while( it.hasNext() ) {
      Mobile car = it.next();
      if(car.isShow())
        m_CarList.add(car);
    }

    m_ListView.setOnChildClickListener(new OnChildClickListener() 
    {
      @Override
      public boolean onChildClick(ExpandableListView list, View view, int group, int position, long id) {
        // Set selected loco.
        System.out.println("group/position="+group+"/"+position);
        position = m_Adapter.getRealPosition(group, position);
        System.out.println("real position="+position);
        ActLocoExpList.this.setResult(position);
        finish();
        return false;
      }
      
    });

    List<Mobile> list = new ArrayList<Mobile>();
    list.addAll(m_LocoList);
    list.addAll(m_CarList);
    m_Adapter = new LocoExpListAdapter(this, list);
    m_ListView.setAdapter(m_Adapter);

    setResult(-1);

  }
  
}
