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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.rocrail.androc.RocrailService;

import org.xml.sax.Attributes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;


public class Turntable extends Item {
  List<TTTrack> Tracks = new ArrayList<TTTrack>();
  int Bridgepos = 0;
  double dBridgepos = 0;
  boolean Sensor1 = false;
  boolean Sensor2 = false;


  public Turntable(RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    //NrTracks = Item.getAttrValue(atts, "nrtracks", 3 );
    //LocoID   = Item.getAttrValue(atts, "locid", ID); 
  }
  
  public String getImageName() {
    cX = 6;
    cY = 6;
    return null;
  }
  
  

  public void updateWithAttributes(Attributes atts ) {
    super.updateWithAttributes(atts);
  }

  public void addTrack(Attributes atts ) {
    TTTrack track = new TTTrack();
    track.Nr = Item.getAttrValue(atts, "nr", 0 );
    track.State = Item.getAttrValue(atts, "state", false );
    track.Show  = Item.getAttrValue(atts, "show", true );
    Tracks.add(track);
  }


  
  @Override
  public void Draw( Canvas canvas ) {

    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.STROKE);
   
    paint.setColor(Color.LTGRAY);
    paint.setStrokeWidth(1);
	  canvas.drawCircle(79, 79, 79, paint);

    
    Iterator<TTTrack> it = Tracks.iterator();
    while(it.hasNext()) {
      TTTrack track = it.next();
      
      double degr = 7.5 * track.Nr;
      double a = (degr*2*StrictMath.PI)/360;
      double xa = StrictMath.cos(a) * 79.0;
      double ya = StrictMath.sin(a) * 79.0;
      int x = 79 + (int)xa;
      int y = 79 - (int)ya;

      if( track.State || (Bridgepos == track.Nr) ) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(6);
        dBridgepos = degr;
      }
      else {
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(6);
      }

      if( track.Show )
        canvas.drawLine( 79, 79, x, y, paint );

    }


    paint.setStrokeWidth(2);
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(Color.WHITE);
	  canvas.drawCircle(79, 79, 36, paint);

    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(Color.BLACK);
    canvas.drawCircle(79, 79, 36, paint);
	  canvas.drawCircle(79, 79, 32, paint);


    canvas.drawPath( rotateBridge( dBridgepos ), paint);

    if( Sensor1 && Sensor2 )
      paint.setColor(Color.RED);
    else if( Sensor1 || Sensor2 ) {
      paint.setColor(Color.YELLOW);
    }
    else
      paint.setColor(Color.GREEN);

    paint.setStyle(Paint.Style.FILL);
    canvas.drawPath( rotateBridgeSensors( dBridgepos ), paint);

  }

  
  Path rotateBridge( double ori ) {
    Path p = new Path();
    float originX = 0;
    float originY = 0;
    double[] bp = { 10.0, 170.0, 190.0, 350.0 };

    for( int i = 0; i < 4; i++ ) {
      double angle = ori+bp[i];
      if( angle > 360.0 )
        angle = angle -360.0;
      double a = (angle*2*StrictMath.PI)/360;
      double xa = StrictMath.cos(a) * 32.0;
      double ya = StrictMath.sin(a) * 32.0;
      
      if( i == 0 ) {
        originX = 79 + (int)xa;
        originY = 79 - (int)ya;
        p.moveTo(79 + (int)xa, 79 - (int)ya);
      }
      else {
        p.lineTo(79 + (int)xa, 79 - (int)ya);
      }
    }
    
    // end point to close the polygon
    p.lineTo(originX, originY);
    
    return p;
  }
  

  Path rotateBridgeSensors( double ori ) {
    Path p = new Path();
    float originX = 0;
    float originY = 0;
    double[] bp = { 10.0, 170.0, 190.0, 350.0 };

    for( int i = 0; i < 4; i++ ) {
      double angle = ori+bp[i];
      if( angle > 360.0 )
        angle = angle -360.0;
      double a = (angle*2*StrictMath.PI)/360;
      double xa = StrictMath.cos(a) * 20.0;
      double ya = StrictMath.sin(a) * 20.0;
      if( i == 0 ) {
        originX = 79 + (int)xa;
        originY = 79 - (int)ya;
        p.moveTo(79 + (int)xa, 79 - (int)ya);
      }
      else {
        p.lineTo(79 + (int)xa, 79 - (int)ya);
      }
    }
    // end point to close the polygon
    p.lineTo(originX, originY);
    return p;
  }


}


class TTTrack {
  int Nr = 0;
  boolean State = false;
  boolean Show = true;
}

