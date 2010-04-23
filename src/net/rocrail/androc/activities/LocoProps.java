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
import net.rocrail.androc.objects.Block;
import net.rocrail.androc.objects.Loco;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class LocoProps extends Base implements OnItemSelectedListener {
  Loco m_Loco = null;
  String ScheduleID = null;
  String BlockID    = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = 0;
    connectWithService();
  }
  
  public void connectedWithService() {
    initView();
    updateTitle();
  }


  public void initView() {
    setContentView(R.layout.locoprops);
    
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String id = extras.getString("id");
      m_Loco = m_RocrailService.m_Model.getLoco(id);
    }
    else {
      m_Loco = m_RocrailService.SelectedLoco;
    }

    if( m_Loco == null )
      return;
    
    ImageView image = (ImageView)findViewById(R.id.locoImage);
    
    if( m_Loco.getLocoBmp(null) != null ) {
      if( image != null ) {
        image.setImageBitmap(m_Loco.getLocoBmp(null));
      }
    }

    image.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        finish();
      }
  });
    
    final LEDButton autoStart = (LEDButton) findViewById(R.id.locoStart);
    autoStart.ON = m_Loco.AutoStart;
    autoStart.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_Loco.AutoStart = !m_Loco.AutoStart;
          ((LEDButton)v).ON = m_Loco.AutoStart;
          
          if( m_Loco.AutoStart && ScheduleID != null) {
            m_RocrailService.sendMessage("lc", 
                String.format("<lc id=\"%s\" cmd=\"useschedule\" scheduleid=\"%s\"/>", 
                    m_Loco.ID, ScheduleID ) );
          }
          else {
            m_RocrailService.sendMessage("lc", String.format("<lc id=\"%s\" cmd=\"%s\"/>", 
                m_Loco.ID, m_Loco.AutoStart?(m_Loco.HalfAuto?"gomanual":"go"):"stop") );
          }
        }
    });

   
    final LEDButton halfAuto = (LEDButton) findViewById(R.id.locoHalfAuto);
    halfAuto.ON = m_Loco.HalfAuto;
    halfAuto.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_Loco.HalfAuto = !m_Loco.HalfAuto;
          ((LEDButton)v).ON = m_Loco.HalfAuto;
        }
    });

   
    final Button setInBlock = (Button) findViewById(R.id.locoSetInBlock);
    setInBlock.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( BlockID != null ) {
            m_RocrailService.sendMessage("lc", 
                String.format("<lc id=\"%s\" cmd=\"block\" blockid=\"%s\"/>", m_Loco.ID, BlockID ) );
          }
        }
    });

   
    // Block spinner
    Spinner s = (Spinner) findViewById(R.id.locoBlocks);
    s.setPrompt(new String("Select Block"));

    ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
    m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);

    m_adapterForSpinner.add("none");

    Iterator<Block> it = m_RocrailService.m_Model.m_BlockMap.values().iterator();
    while( it.hasNext() ) {
      Block block = it.next();
      m_adapterForSpinner.add(block.ID);
    }
    
    s.setOnItemSelectedListener(this);
    // s.setSelection(m_RocrailService.m_iSelectedLoco);    
    

    // Schedule spinner
    s = (Spinner) findViewById(R.id.locoSchedules);
    s.setPrompt(new String("Select Schedule"));

    m_adapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
    m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);

    m_adapterForSpinner.add("none");

    Iterator<String> itSc = m_RocrailService.m_Model.m_ScheduleList.iterator();
    while( itSc.hasNext() ) {
      String sc = itSc.next();
      m_adapterForSpinner.add(sc);
    }
    
    s.setOnItemSelectedListener(this);
    // s.setSelection(m_RocrailService.m_iSelectedLoco);    
    

    
    
    
  }

  @Override
  public void onItemSelected(AdapterView<?> adview, View view, int position, long longID) {
    Spinner bk = (Spinner) findViewById(R.id.locoBlocks);
    Spinner sc = (Spinner) findViewById(R.id.locoSchedules);
    String id = (String)adview.getSelectedItem();
    if( bk == adview ) {
      BlockID = id.equals("none")?null:id;
      ScheduleID = null;
    }
    else if(sc == adview) {
      ScheduleID = id.equals("none")?null:id;
      BlockID = null;
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub
    
  }
  
  
  

}
