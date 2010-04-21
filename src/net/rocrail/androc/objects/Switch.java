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
package net.rocrail.androc.objects;

import org.xml.sax.Attributes;

import android.view.View;

import net.rocrail.androc.RocrailService;

public class Switch extends Item implements View.OnClickListener {
  boolean Dir = false;

  public Switch( RocrailService rocrailService, String id, Attributes atts) {
    super(atts);
    m_RocrailService = rocrailService;
    Dir = Item.getAttrValue(atts, "dir", false );
  }

  public void onClick(View v) {
    m_RocrailService.sendMessage("sw", String.format( "<sw id=\"%s\" cmd=\"flip\"/>", ID ) );
  }

  
  public String getImageName() {
    int orinr = getOriNr();
    
    if( orinr == 1 )
      orinr = 3;
    else if( orinr == 3 )
      orinr = 1;
    
    if( Type.equals("right") ) {
      if( State.equals("straight"))
        ImageName = String.format("turnout_rs_%d", orinr);
      else 
        ImageName = String.format("turnout_rt_%d", orinr);
      
    } else if( Type.equals("left")) {
        if( State.equals("straight"))
          ImageName = String.format("turnout_ls_%d", orinr);
        else 
          ImageName = String.format("turnout_lt_%d", orinr);
    
    } else if( Type.equals("threeway")) {
      if( State.equals("straight"))
        ImageName = String.format("threeway_s_%d", orinr);
      else if( State.equals("left"))
        ImageName = String.format("threeway_l_%d", orinr);
      else 
        ImageName = String.format("threeway_r_%d", orinr);
      
    } else if( Type.equals("dcrossing") ){
      char st = 's';
      
      if( State.equals("straight"))
        st = 's';
      else if( State.equals("turnout"))
        st = 't';
      else if( State.equals("left"))
        st = 'l';
      else if( State.equals("right"))
        st = 'r';
      
      ImageName = String.format("dcrossing%s_%c_%d", (Dir?"left":"right"), st, orinr);
      
      cX = orinr % 2 == 0 ? 1:2; 
      cY = orinr % 2 == 0 ? 2:1; 
    } else if( Type.equals("crossing") ) {
      ImageName = "cross";
    } else if( Type.equals("ccrossing") ) {
      ImageName = String.format("ccrossing_%d", (orinr % 2 == 0 ? 2:1));
      cX = orinr % 2 == 0 ? 1:2; 
      cY = orinr % 2 == 0 ? 2:1; 
    } else if( Type.equals("decoupler") ) {
      ImageName = String.format("decoupler_%d", (orinr % 2 == 0 ? 2:1));
    }
    
    return ImageName;
    
  }

}
