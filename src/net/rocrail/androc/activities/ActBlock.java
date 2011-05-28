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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ActBlock extends ActBase implements OnItemSelectedListener {
  Block m_Block = null;
  String LocoID = null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = 0;
    connectWithService();
  }
  
  public void connectedWithService() {
    super.connectedWithService();
    initView();
  }
  
  
  void updateLoco() {
    TextView text = (TextView)findViewById(R.id.blockID);
    text.setText(m_Block.ID + ": " + (LocoID==null?"-":LocoID) );

    LocoImage image = (LocoImage)findViewById(R.id.blockLocoImage);
    
    if( LocoID == null ) {
      image.setImageResource(R.drawable.noimg);
      image.ID = null;
      return;
    }
    
    Loco lc = m_RocrailService.m_Model.getLoco(LocoID);
    image.ID = LocoID;
    
    if (lc != null && lc.getLocoBmp(image) != null) {
      image.setImageBitmap(lc.getLocoBmp(null));
    }
    else {
      image.setImageResource(R.drawable.noimg);
    }
  }


  class LocoComparator implements Comparator<String> {
    @Override
    public int compare(String loco1, String loco2) {
      return loco1.toLowerCase().compareTo(loco2.toLowerCase());
    }
  }
  
  public void initView() {
    setContentView(R.layout.block);
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String id = extras.getString("id");
      m_Block = m_RocrailService.m_Model.m_BlockMap.get(id);
    }

    if( m_Block == null )
      return;
    
    LocoID = m_Block.LocoID;
    updateTitle("Block \'"+m_Block.ID+"\'");
    
    // Loco spinner
    Spinner s = (Spinner) findViewById(R.id.blockLocos);
    s.setPrompt(getText(R.string.SelectLoco));

    ArrayAdapter<String> m_adapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
    m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    s.setAdapter(m_adapterForSpinner);

    Iterator<Loco> it = m_RocrailService.m_Model.m_LocoMap.values().iterator();
    while( it.hasNext() ) {
      Loco loco = it.next();
      m_adapterForSpinner.add(loco.ID);
    }
    
    m_adapterForSpinner.sort(new LocoComparator());
    m_adapterForSpinner.insert(getText(R.string.FreeBlock).toString(), 0);
    if( LocoID != null ) {
      int select = m_adapterForSpinner.getPosition(LocoID);
      s.setSelection(select);
    }
    s.setOnItemSelectedListener(this);

    updateLoco();
    LocoImage image = (LocoImage)findViewById(R.id.blockLocoImage);
    
    image.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        if( m_Block.LocoID!=null ) {
          Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLoco.class);
          intent.putExtra("id", m_Block.LocoID);
          intent.putExtra("blockid", m_Block.ID);
          startActivity(intent);
          finish();
        }
      }
    });
    
    
    final Button Loco = (Button) findViewById(R.id.blockLoco);
    Loco.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          if( m_Block.LocoID!=null ) {
            Intent intent = new Intent(m_Activity,net.rocrail.androc.activities.ActLoco.class);
            intent.putExtra("id", m_Block.LocoID);
            intent.putExtra("blockid", m_Block.ID);
            startActivity(intent);
            finish();
          }
        }
    });

    
    final LEDButton openBlock = (LEDButton) findViewById(R.id.blockOpen);
    openBlock.ON = !m_Block.Closed;
    openBlock.setText(m_Block.Closed?getText(R.string.OpenBlock):getText(R.string.CloseBlock));
    openBlock.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_Block.OpenClose();
          ((LEDButton)v).ON = !m_Block.Closed;
          ((LEDButton)v).setText(m_Block.Closed?getText(R.string.OpenBlock):getText(R.string.CloseBlock));
        }
    });

    final Button acceptIdentBlock = (Button) findViewById(R.id.blockAcceptIdent);
    acceptIdentBlock.setText(getText(R.string.AcceptIdent));
    acceptIdentBlock.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_Block.AcceptIdent();
        }
    });



 
  }
  

  @Override
  public void onItemSelected(AdapterView<?> adview, View view, int position, long longID) {
    String id = (String)adview.getSelectedItem();
    LocoID = (id.equals(getText(R.string.FreeBlock).toString())?null:id);
    if( LocoID != null ) {
      m_RocrailService.sendMessage("lc", 
          String.format("<lc id=\"%s\" cmd=\"block\" blockid=\"%s\"/>", LocoID, m_Block.ID ) );
    }
    else {
      m_RocrailService.sendMessage("bk", 
          String.format("<bk id=\"%s\" cmd=\"loc\" locid=\"\"/>", m_Block.ID ) );
    }
    updateLoco();
  }

  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub
    
  }
  
}
