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

import java.util.Comparator;
import java.util.Iterator;

import net.rocrail.androc.interfaces.Mobile;
import net.rocrail.androc.objects.Block;
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.widgets.LEDButton;
import net.rocrail.androc.widgets.LocoImage;


import net.rocrail.androc.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ActLoco extends ActBase implements OnItemSelectedListener, OnSeekBarChangeListener {
  Mobile m_Loco = null;
  String ScheduleID  = null;
  String BlockID     = null;
  String ThisBlockID = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = 0;
    Finish = false;
    connectWithService();
  }
  
  public void connectedWithService() {
    initView();
    updateTitle(m_Loco!=null?m_Loco.getID():getText(R.string.LocoProps).toString());
  }


  void updateLoco() {
    LocoImage image = (LocoImage)findViewById(R.id.locoImage);
    
    image.ID = m_Loco.getID();
    
    if (m_Loco.getBmp(image) != null) {
      image.setImageBitmap(m_Loco.getBmp(null));
    }
    else {
      image.setImageResource(R.drawable.noimg);
    }
  }


  public void initView() {
    setContentView(R.layout.loco);
    
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String id = extras.getString("id");
      BlockID = extras.getString("blockid");
      ThisBlockID = extras.getString("blockid");
      m_Loco = m_RocrailService.m_Model.getLoco(id);
    }
    else {
      m_Loco = m_RocrailService.SelectedLoco;
    }

    if( m_Loco == null )
      return;

    if( BlockID == null || BlockID.length() == 0 ) {
      Block block = m_RocrailService.m_Model.findBlock4Loco(m_Loco.getID());
      if( block != null )
        BlockID = block.ID;
    }
    
    TextView addr = (TextView)findViewById(R.id.locoAddress);
    addr.setText(getText(R.string.Address) + ": " + m_Loco.getAddr() + "/" + m_Loco.getSteps());
    
    TextView runtime = (TextView)findViewById(R.id.locoRuntime);
    int hours = (int)(m_Loco.getRunTime()/3600);
    int mins  = (int)((m_Loco.getRunTime() - hours * 3600) / 60);
    int secs  = (int)(m_Loco.getRunTime()%60);
    runtime.setText(getText(R.string.Runtime) + ": " + String.format("%d:%02d.%02d", hours, mins, secs ) ); 
    
    TextView desc = (TextView)findViewById(R.id.locoDesc);
    desc.setText(getText(R.string.Description) + ": " + m_Loco.getDescription());
    TextView road = (TextView)findViewById(R.id.locoRoadname);
    road.setText(getText(R.string.Roadname) + ": " + m_Loco.getRoadname());
    
    updateLoco();
    LocoImage image = (LocoImage)findViewById(R.id.locoImage);
    
    image.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        //finish();
        if( m_Loco != null ) {
          Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLocoSetup.class);
          intent.putExtra("id", m_Loco.getID());
          startActivity(intent);
        }
      }
  });
    
    final LEDButton autoStart = (LEDButton) findViewById(R.id.locoStart);
    autoStart.setEnabled(m_RocrailService.AutoMode);
    autoStart.setLongClickable(true);
    autoStart.ON = m_Loco.isAutoStart();
    autoStart.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_Loco.setAutoStart( !m_Loco.isAutoStart() );
          ((LEDButton)v).ON = m_Loco.isAutoStart();
          
          if( m_Loco.isAutoStart() && ScheduleID != null) {
            m_RocrailService.sendMessage("lc", 
                String.format("<lc id=\"%s\" cmd=\"useschedule\" scheduleid=\"%s\"/>", 
                    m_Loco.getID(), ScheduleID ) );
          }

          m_RocrailService.sendMessage("lc", String.format("<lc id=\"%s\" cmd=\"%s\"/>", 
              m_Loco.getID(), m_Loco.isAutoStart()?(m_Loco.isHalfAuto()?"gomanual":"go"):"stop") );

        }
    });
    
    autoStart.setOnLongClickListener(new View.OnLongClickListener() {
      public boolean onLongClick(View v) {
        if( BlockID != null && !BlockID.equals(ThisBlockID)) {
          m_RocrailService.sendMessage("lc", 
              String.format("<lc id=\"%s\" cmd=\"gotoblock\" blockid=\"%s\"/>", 
                  m_Loco.getID(), BlockID ) );
          finish();
        }
        return true;
      }
    });


   
    final LEDButton halfAuto = (LEDButton) findViewById(R.id.locoHalfAuto);
    halfAuto.ON = m_Loco.isHalfAuto();
    halfAuto.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_Loco.setHalfAuto( !m_Loco.isHalfAuto() );
          ((LEDButton)v).ON = m_Loco.isHalfAuto();
        }
    });

   
    final Button setInBlock = (Button) findViewById(R.id.locoSetInBlock);
    setInBlock.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( BlockID != null ) {
            m_RocrailService.sendMessage("lc", 
                String.format("<lc id=\"%s\" cmd=\"block\" blockid=\"%s\"/>", m_Loco.getID(), BlockID ) );
          }
        }
    });

   
    final LEDButton swapLoco = (LEDButton) findViewById(R.id.locoSwap);
    swapLoco.ON = m_Loco.isPlacing();
    swapLoco.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_Loco.setPlacing( !m_Loco.isPlacing());
          m_Loco.swap();
          ((LEDButton)v).ON = m_Loco.isPlacing();
        }
    });

   
    // Block spinner
    Spinner s = (Spinner) findViewById(R.id.locoBlocks);
    s.setPrompt(getText(R.string.SelectBlock));

    ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
    m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);

    Iterator<Block> it = m_RocrailService.m_Model.m_BlockMap.values().iterator();
    while( it.hasNext() ) {
      Block block = it.next();
      m_adapterForSpinner.add(block.ID);
    }
    
    m_adapterForSpinner.sort(new IDComparator());
    m_adapterForSpinner.insert(getText(R.string.BlockList).toString(), 0);
    s.setSelection(0);

    if( BlockID != null ) {
      int cnt = m_adapterForSpinner.getCount();
      for( int i = 0; i < cnt; i++ ) {
        if( m_adapterForSpinner.getItem(i).equals(BlockID) ) {
          s.setSelection(i);
          break;
        }
      }
    }
    
    s.setOnItemSelectedListener(this);
    // s.setSelection(m_RocrailService.m_iSelectedLoco);    
    

    // Schedule spinner
    s = (Spinner) findViewById(R.id.locoSchedules);
    s.setPrompt(getText(R.string.SelectSchedule));

    m_adapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
    m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);


    Iterator<String> itSc = m_RocrailService.m_Model.m_ScheduleList.iterator();
    while( itSc.hasNext() ) {
      String sc = itSc.next();
      m_adapterForSpinner.add(sc);
    }

    m_adapterForSpinner.sort(new IDComparator());
    m_adapterForSpinner.insert(getText(R.string.ScheduleList).toString(), 0);
    
    s.setOnItemSelectedListener(this);
    // s.setSelection(m_RocrailService.m_iSelectedLoco);    
    

    
    
    
  }
  
  @Override
  protected void  onResume() {
    super.onResume();
    if( m_Loco != null ) {
      final LEDButton autoStart = (LEDButton) findViewById(R.id.locoStart);
      autoStart.setEnabled(m_RocrailService.AutoMode);
      autoStart.ON = m_Loco.isAutoStart();
      
      final LEDButton halfAuto = (LEDButton) findViewById(R.id.locoHalfAuto);
      halfAuto.ON = m_Loco.isHalfAuto();
    }
  }


  @Override
  public void onItemSelected(AdapterView<?> adview, View view, int position, long longID) {
    Spinner bk = (Spinner) findViewById(R.id.locoBlocks);
    Spinner sc = (Spinner) findViewById(R.id.locoSchedules);
    String id = (String)adview.getSelectedItem();
    if( bk == adview ) {
      BlockID = id.equals(getText(R.string.BlockList).toString())?null:id;
      ScheduleID = null;
      Button setInBlock = (Button) findViewById(R.id.locoSetInBlock);
      setInBlock.setEnabled(BlockID != null);
    }
    else if(sc == adview) {
      ScheduleID = id.equals(getText(R.string.ScheduleList).toString())?null:id;
      BlockID = null;
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onStartTrackingTouch(SeekBar arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onStopTrackingTouch(SeekBar arg0) {
    // TODO Auto-generated method stub
    
  }
  
  
  class IDComparator implements Comparator<String> {
    @Override
    public int compare(String id1, String id2) {
      return id1.toLowerCase().compareTo(id2.toLowerCase());
    }
  }
  
  

}
