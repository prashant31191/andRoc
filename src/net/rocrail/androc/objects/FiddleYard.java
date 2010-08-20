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


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class FiddleYard extends Item  {
  public int NrTracks = 3;
  int Occupied = 0;
  String LocoID = "-";
  public boolean Closed = false;

  public FiddleYard(RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    NrTracks = Item.getAttrValue(atts, "nrtracks", 3 );
    LocoID   = Item.getAttrValue(atts, "locid", ID); 
  }
  
  
  public void updateTextColor() {
    if( State.equals("closed") ) {
      Text = "Closed";
      colorName = Item.COLOR_CLOSED;
    }
    else if( LocoID != null && LocoID.trim().length() > 0 ) {
      Text = LocoID;
      if( Reserved ) 
        colorName = Item.COLOR_RESERVED;
      else if( Entering ) 
        colorName = Item.COLOR_ENTER;
      else 
        colorName = Item.COLOR_OCCUPIED;
    }
    else {
      Text = ID;
      colorName = Item.COLOR_FREE;
    }
  }

  
  
  public String getImageName(boolean ModPlan) {
    this.ModPlan = ModPlan;
    String ori = (ModPlan ? Mod_Ori:Ori);
    if( ori.equals("west") || ori.equals("east") ) {
      cX = NrTracks;
      cY = 1;
      textVertical = false;
    }
    else {
      cX = 1;
      cY = NrTracks;
      textVertical = true;
    }
    updateTextColor();
    return null;
  }
  
  
  public void updateWithAttributes(Attributes atts ) {
    LocoID   = Item.getAttrValue(atts, "locid", ID); 
    State    = Item.getAttrValue(atts, "state", State); 
    Closed   = State.equals("closed");

    updateTextColor();
    super.updateWithAttributes(atts);
  }


  
  @Override
  public void Draw( Canvas canvas ) {
    Paint paint = new Paint();
    
    paint.setAntiAlias(true);
    paint.setColor(Color.BLACK);

    if( Occupied == 1 ) {
      //(255,200,200)
      paint.setColor(Color.RED);
      paint.setStyle(Paint.Style.FILL);
    }
    else if( Occupied == 2 ) {
      //(255,255,200)
      paint.setColor(Color.YELLOW);
      paint.setStyle(Paint.Style.FILL);
    }
    else {
      paint.setStyle(Paint.Style.STROKE);
    }

    paint.setStrokeWidth(2);
    
    //if( Ori.equals("west") || Ori.equals("east") ) {
    if( !textVertical ) {
      RectF rect = new RectF(1,3, (32 * NrTracks) - 1, 28);
      canvas.drawRoundRect(rect, 10, 10, paint);
    }
    else {
      RectF rect = new RectF(3,1, 28, (32 * NrTracks) - 1);
      canvas.drawRoundRect(rect, 10, 10, paint);
    }

    
  }

  public void onClick(View v) {
    propertiesView();
  }
  
  public void OpenClose() {
    Closed = !Closed;
    m_RocrailService.sendMessage("seltab", String.format( "<seltab id=\"%s\" state=\"%s\"/>", 
        ID, Closed?"closed":"open" ) );
  }
  
  public void propertiesView() {
    try {
      Intent intent = new Intent(activity,net.rocrail.androc.activities.ActFiddleYard.class);
      intent.putExtra("id", FiddleYard.this.ID);
      activity.startActivity(intent);
    }
    catch(Exception e) {
      // invalid activity
    }
  }



}
