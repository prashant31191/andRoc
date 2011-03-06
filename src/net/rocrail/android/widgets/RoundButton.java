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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class RoundButton extends View {
  public static final int RED    = 0;
  public static final int GREEN  = 1;
  public static final int YELLOW = 2;
  
  public boolean ON            = false;
  public int     LuminateColor = RED;
  public boolean Down          = false;
  
  public String Text = "f0";

  public RoundButton(Context context) {
    super(context);
  }
  public RoundButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    ON = attrs.getAttributeBooleanValue(null, "on", false);
    String text = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
    if( text != null )
      Text = text;
  }

  protected void  onDraw  (Canvas canvas) {
    super.onDraw(canvas);
    
    double cx = getWidth();
    double cy = getHeight();

    Paint paint = new Paint();
    paint.setAntiAlias(true);
    
    double radius = 0;
    if( cx < cy ) 
      radius = cx / 2.5;
    else
      radius = cy / 2.5;
    
    paint.setColor(Color.rgb(70, 70, 70));
    canvas.drawCircle((float)(cx/2), (float)(cy/2), (float)radius, paint);

    paint.setColor(Color.rgb(ON?255:150, 100, 100));
    canvas.drawCircle((float)(cx/2), (float)(cy/2), (float)radius-2, paint);
    
    if( Down )
      paint.setColor(Color.rgb(180, 180, 180));
    else
      paint.setColor(Color.rgb(100, 100, 100));
    canvas.drawCircle((float)(cx/2), (float)(cy/2), (float)radius-4, paint);
    
    paint.setColor(Color.rgb(255, 255, 255));
   
    Rect    bounds = new Rect();
    paint.getTextBounds(Text, 0, Text.length(), bounds);

    
    canvas.drawText(Text, (float)(cx/2-bounds.width()/2), (float)(cy/2+bounds.height()/2), paint);
    
  }
  
  public boolean onTouchEvent (MotionEvent event) {
    if( Down && event.getAction() == MotionEvent.ACTION_UP ) {
      Down = false;
      invalidate();
    }
    else if( !Down && event.getAction() == MotionEvent.ACTION_DOWN ) {
      Down = true;
      ON = !ON;
      invalidate();
    }
    return true;
  }

  
}
