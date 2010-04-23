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
import net.rocrail.androc.objects.Loco;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LocoProps extends Base {
  Loco m_Loco = null;
  
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
          m_RocrailService.sendMessage("sys", String.format("<lc id=\"%s\" cmd=\"%s\"/>", 
              m_Loco.ID, m_Loco.AutoStart?(m_Loco.HalfAuto?"gomanual":"go"):"stop") );
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

   
    
  }
  
  
  

}
