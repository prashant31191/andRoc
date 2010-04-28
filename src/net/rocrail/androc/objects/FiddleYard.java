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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class FiddleYard extends Item  {
  int NrTracks = 3;
  int Occupied = 0;

  public FiddleYard(RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    NrTracks = Item.getAttrValue(atts, "nrtracks", 3 );
  }
  
  
  public String getImageName() {
    if( Ori.equals("west") || Ori.equals("east") ) {
      cX = NrTracks;
      cY = 1;
    }
    else {
      cX = 1;
      cY = NrTracks;
    }
    return null;
  }
  
  
  @Override
  public void Draw( Canvas canvas ) {
    Paint paint = new Paint();
    
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(Color.GRAY);

    if( Occupied == 1 ) {
      //(255,200,200)
      paint.setColor(Color.RED);
    }
    else if( Occupied == 2 ) {
      //(255,255,200)
      paint.setColor(Color.YELLOW);
    }

    paint.setStrokeWidth(2);
    
    /*
public void drawRoundRect (RectF rect, float rx, float ry, Paint paint)
rect
The rectangular bounds of the roundRect to be drawn
rx
The x-radius of the oval used to round the corners
ry
The y-radius of the oval used to round the corners
paint
The paint used to draw the roundRect

     */

    if( Ori.equals("west") || Ori.equals("east") ) {
      RectF rect = new RectF(1,3, (32 * NrTracks) - 1, 28);
      canvas.drawRoundRect(rect, 10, 10, paint);
    }
    else {
      RectF rect = new RectF(3,1, 28, (32 * NrTracks) - 1);
      canvas.drawRoundRect(rect, 10, 10, paint);
    }

/*
    if( StrOp.equals( ori, wItem.south ) )
      dc.DrawRotatedText( wxString(m_Label,wxConvUTF8), 32-5, 3, 270.0 );
    else if( StrOp.equals( ori, wItem.north ) )
      dc.DrawRotatedText( wxString(m_Label,wxConvUTF8), 5, (32 * nrtracks)-3, 90.0 );
    else
      dc.DrawRotatedText( wxString(m_Label,wxConvUTF8), 5, 5, 0.0 );
*/
    
  }



}
