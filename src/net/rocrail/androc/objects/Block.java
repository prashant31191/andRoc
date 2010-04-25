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

import net.rocrail.androc.RocrailService;

import org.xml.sax.Attributes;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class Block extends Item implements View.OnClickListener {
  boolean Small    = false;
  String LocoID = "-";
  
  public Block(RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    Small    = Item.getAttrValue(atts, "smallsymbol", false );
    LocoID   = Item.getAttrValue(atts, "locid", ID); 
    Reserved = Item.getAttrValue(atts, "reserved", false); 
    Entering = Item.getAttrValue(atts, "entering", false); 
    Text = LocoID;
  }
  
  public void updateTextColor() {
    if( State.equals("closed") ) {
      Text = "Closed";
      colorName = "block_closed";
    }
    else if( LocoID.length() > 0 ) {
      Text = LocoID;
      if( Reserved ) 
        colorName = "block_reserved";
      else if( Entering ) 
        colorName = "block_enter";
      else 
        colorName = "block_free";
    }
    else {
      Text = ID;
      colorName = "block_free";
    }
  }



  
  public String getImageName() {
    int orinr = getOriNr();

    if (orinr % 2 == 0) {
      // vertical
      textVertical = true;
      cX = 1;
      cY = 4;
      if (Small) {
        cY = 2;
        ImageName = "block_2_s";
      } else {
        ImageName = "block_2";
      }
    } else {
      // horizontal
      cX = 4;
      cY = 1;
      textVertical = false;
      if (Small) {
        cX = 2;
        ImageName = "block_1_s";
      } else {
        ImageName = "block_1";
      }
    }
    
    updateTextColor();

    return ImageName;
  }

  
  public void updateWithAttributes(Attributes atts ) {
    LocoID   = Item.getAttrValue(atts, "locid", ID); 
    Reserved = Item.getAttrValue(atts, "reserved", false); 
    Entering = Item.getAttrValue(atts, "entering", false); 
    State    = Item.getAttrValue(atts, "state", State); 

    updateTextColor();
    super.updateWithAttributes(atts);
  }


  public void onClick(View v) {
    m_RocrailService.sendMessage("bk", String.format( "<bk id=\"%s\" state=\"%s\"/>", 
        ID, State.equals("open")?"closed":"open" ) );
/*
    try {
      Intent intent = new Intent(activity,net.rocrail.androc.activities.BlockProps.class);
      intent.putExtra("id", Block.this.ID);
      activity.startActivity(intent);
    }
    catch(Exception e) {
      // invalid activity
    }
    */
  }
}
