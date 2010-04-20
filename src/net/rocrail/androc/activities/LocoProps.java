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
    connectWithService();
  }
  
  public void connectedWithService() {
    initView();
  }


  public void initView() {
    setContentView(R.layout.locoprops);
    
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String id = extras.getString("id");
      m_Loco = m_RocrailService.m_Model.getLoco(id);
    }
    
    ImageView image = (ImageView)findViewById(R.id.locoImage);
    
    if( m_Loco.LocoBmp != null ) {
      if( image != null ) {
        image.setImageBitmap(m_Loco.LocoBmp);
      }
    }

    image.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        finish();
      }
  });
  }
  

}