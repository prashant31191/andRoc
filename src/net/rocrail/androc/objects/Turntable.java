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


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;


public class Turntable extends Item {
  public List<TTTrack> Tracks = new ArrayList<TTTrack>();
  int Bridgepos = 0;
  boolean Sensor1 = false;
  boolean Sensor2 = false;
  public boolean Closed = false;


  public Turntable(RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    Bridgepos = Item.getAttrValue(atts, "bridgepos", Bridgepos );
    Sensor1 = Item.getAttrValue(atts, "state1", Sensor1 );
    Sensor2 = Item.getAttrValue(atts, "state2", Sensor2 );
  }
  
  public String getImageName(boolean ModPlan) {
    this.ModPlan = ModPlan;
    cX = 6;
    cY = 6;
    return null;
  }
  
  

  public void updateWithAttributes(Attributes atts ) {    
    // turntable bridge position
    Bridgepos = Item.getAttrValue(atts, "bridgepos", Bridgepos );
    Sensor1 = Item.getAttrValue(atts, "state1", Sensor1 );
    Sensor2 = Item.getAttrValue(atts, "state2", Sensor2 );
    Closed   = State.equals("closed");

    if( Tracks != null ) {
      Iterator<TTTrack> it = Tracks.iterator();
      while(it.hasNext()) {
        TTTrack track = it.next();
        track.State = ( track.Nr == Bridgepos );
      }
    }

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
    double dBridgepos = 0;
    int size = m_RocrailService.Prefs.Size;
    double c79 = (79*size)/32;
    double c36 = (36*size)/32;
    double c32 = (32*size)/32;
    
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.STROKE);
   
    paint.setColor(Color.LTGRAY);
    paint.setStrokeWidth(1);
	  canvas.drawCircle((int)c79, (int)c79, (int)c79, paint);

    
    Iterator<TTTrack> it = Tracks.iterator();
    while(it.hasNext()) {
      TTTrack track = it.next();
      
      double degr = 7.5 * track.Nr;
      double a = (degr*2*StrictMath.PI)/360;
      double xa = StrictMath.cos(a) * c79;
      double ya = StrictMath.sin(a) * c79;
      int x = (int)c79 + (int)xa;
      int y = (int)c79 - (int)ya;

      if( track.State || (Bridgepos == track.Nr) ) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(Math.max(1,(6*size)/32));
        dBridgepos = degr;
      }
      else {
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(Math.max(1,(6*size)/32));
      }

      if( track.Show )
        canvas.drawLine( (int)c79, (int)c79, x, y, paint );

    }


    paint.setStrokeWidth(Math.max(1,(2*size/32)));
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(Color.WHITE);
	  canvas.drawCircle((int)c79, (int)c79, (int)c36, paint);

    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(Color.BLACK);
    canvas.drawCircle((int)c79, (int)c79, (int)c36, paint);
	  canvas.drawCircle((int)c79, (int)c79, (int)c32, paint);


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
    int size = m_RocrailService.Prefs.Size;
    double c79 = (79*size)/32;
    double c36 = (36*size)/32;
    double c32 = (32*size)/32;
    float originX = 0;
    float originY = 0;
    double[] bp = { 10.0, 170.0, 190.0, 350.0 };

    for( int i = 0; i < 4; i++ ) {
      double angle = ori+bp[i];
      if( angle > 360.0 )
        angle = angle -360.0;
      double a = (angle*2*StrictMath.PI)/360;
      double xa = StrictMath.cos(a) * c32;
      double ya = StrictMath.sin(a) * c32;
      
      if( i == 0 ) {
        originX = (int)c79 + (int)xa;
        originY = (int)c79 - (int)ya;
        p.moveTo((int)c79 + (int)xa, (int)c79 - (int)ya);
      }
      else {
        p.lineTo((int)c79 + (int)xa, (int)c79 - (int)ya);
      }
    }
    
    // end point to close the polygon
    p.lineTo(originX, originY);
    
    return p;
  }
  

  Path rotateBridgeSensors( double ori ) {
    Path p = new Path();
    int size = m_RocrailService.Prefs.Size;
    double c79 = (79*size)/32;
    double c36 = (36*size)/32;
    double c32 = (32*size)/32;
    double c20 = (20*size)/32;
    float originX = 0;
    float originY = 0;
    double[] bp = { 10.0, 170.0, 190.0, 350.0 };

    for( int i = 0; i < 4; i++ ) {
      double angle = ori+bp[i];
      if( angle > 360.0 )
        angle = angle -360.0;
      double a = (angle*2*StrictMath.PI)/360;
      double xa = StrictMath.cos(a) * c20;
      double ya = StrictMath.sin(a) * c20;
      if( i == 0 ) {
        originX = (int)c79 + (int)xa;
        originY = (int)c79 - (int)ya;
        p.moveTo((int)c79 + (int)xa, (int)c79 - (int)ya);
      }
      else {
        p.lineTo((int)c79 + (int)xa, (int)c79 - (int)ya);
      }
    }
    // end point to close the polygon
    p.lineTo(originX, originY);
    return p;
  }

  public void onClick(View v) {
    propertiesView();
  }
  
  public void OpenClose() {
    Closed = !Closed;
    m_RocrailService.sendMessage("tt", String.format( "<tt id=\"%s\" state=\"%s\"/>", 
        ID, Closed?"closed":"open" ) );
  }
  
 
  public void propertiesView() {
    try {
      Intent intent = new Intent(activity,net.rocrail.androc.activities.ActTurntable.class);
      intent.putExtra("id", Turntable.this.ID);
      activity.startActivity(intent);
    }
    catch(Exception e) {
      // invalid activity
    }
  }


  public class TTTrack {
    public int Nr = 0;
    public boolean State = false;
    public boolean Show = true;
  }

}



