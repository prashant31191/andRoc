/*
 Rocrail - Model Railroad Software

 Copyright (C) 2002-2011 - Rob Versluis <r.j.versluis@rocrail.net>

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
package net.rocrail.android.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LED extends View implements Runnable {
  public static final int RED    = 0;
  public static final int GREEN  = 1;
  public static final int YELLOW = 2;
  
  public boolean ON       = false;
  public int     LEDColor = RED;
  public boolean Flash    = false;
  
  public LED(Context context) {
    super(context);
  }
  public LED(Context context, AttributeSet attrs) {
    super(context, attrs);
    Flash = attrs.getAttributeBooleanValue(null, "flash", false);
    ON    = attrs.getAttributeBooleanValue(null, "on", false);
    String color = attrs.getAttributeValue(null, "ledcolor");
    if( color != null && color.equals("green") )
      LEDColor = GREEN;
    else if( color != null && color.equals("yellow") )
      LEDColor = YELLOW;
    
    new Thread(this).start();
  }

  protected void  onDraw  (Canvas canvas) {
    super.onDraw(canvas);
    
    double cx = getWidth();
    double cy = getHeight();

    Paint paint = new Paint();
    paint.setAntiAlias(true);
    
    double radius = 0;
    if( cx < cy ) 
      radius = cx / 3;
    else
      radius = cy / 3;

    paint.setColor(Color.rgb(70, 70, 70));
    canvas.drawCircle((float)(cx/2), (float)(cy/2), (float)radius, paint);
    
    if( LEDColor == GREEN )
      paint.setColor(Color.rgb(100, ON?255:150, 100));
    else if( LEDColor == YELLOW )
      paint.setColor(Color.rgb(ON?255:150, ON?255:150, 100));
    else
      paint.setColor(Color.rgb(ON?255:150, 100, 100));
    canvas.drawCircle((float)(cx/2), (float)(cy/2), (float)radius-2, paint);
    
  }

  @Override
  public void run() {
    do {
      try {Thread.sleep(1000);} catch (InterruptedException e){}
        if( Flash ) {
        ON = !ON;
        LED.this.post(new Runnable() {
          public void run() {
            invalidate();
          }
        });
      }
    }while(true);
  }
  
}
