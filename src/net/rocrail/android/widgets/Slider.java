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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Slider extends View {

  boolean Vertical = false;
  double V = 0;
  List<SliderListener> m_Listeners = new ArrayList<SliderListener>();

  public void addListener(SliderListener listener) {
    m_Listeners.add(listener);
  }
  
  void informListeners() {
    for( SliderListener listener : m_Listeners ) {
      listener.onSliderChange(this, (int)V);
    }
  }
  
  public Slider(Context context) {
    super(context);
  }

  public Slider(Context context, AttributeSet attrs) {
    super(context, attrs);
    Vertical = attrs.getAttributeBooleanValue(null, "vertical", false);
  }

  public void setV(int v) {
    V = v;
    invalidate();
  }

  protected void  onDraw  (Canvas canvas) {
    super.onDraw(canvas);
    
    double cx = getWidth();
    double cy = getHeight();
    double xu = cx / 10.0;
    double yu = cy / 10.0;

    System.out.println("slider size ("+cx+","+cy+")");

    
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setColor(Color.rgb(170,170,170));
    
    if( Vertical ) {
      RectF rect = new RectF();
      double x1 = 4.5 * xu;
      double y1 = .75 * yu;
      double x2 = 5.5 * xu;
      double y2 = 9.25 * yu;
      rect.left   = (float)x1;
      rect.right  = (float)x2;
      rect.top    = (float)y1;
      rect.bottom = (float)y2;
      canvas.drawRoundRect (rect, (float)xu/3, (float)xu/3, paint);
      
      float y = (float)(y2 - ((V * yu*8.5)/100.0) ); 

      rect.left   = (float) xu * 1;
      rect.right  = (float) xu * 9;
      rect.top    = (float) (y - .75 * yu);
      rect.bottom = (float) (y + .75 * yu);

      paint.setColor(Color.rgb(70,70,70));
      canvas.drawRoundRect (rect, (float)xu/2, (float)xu/2, paint);

      rect.left   = (float) xu * 1 + 2;
      rect.right  = (float) xu * 9 - 2;
      rect.top    = (float) (y - .75 * yu) + 2;
      rect.bottom = (float) (y + .75 * yu) - 2;

      int v = (int)V;
      paint.setColor(Color.rgb(v+120,120,120));
      canvas.drawRoundRect (rect, (float)xu/2, (float)xu/2, paint);
      paint.setColor(Color.rgb(100,100,100));
      
      float lx1 = (float)(xu * 1.2);
      float lx2 = (float)(xu * 8.6);
      float ly1 = (float)((1.5 * yu) / 5);
      canvas.drawLine(lx1, (float)((y - .75 * yu) + ly1*1), lx2, (float)((y - .75 * yu) + ly1*1), paint);
      canvas.drawLine(lx1, (float)((y - .75 * yu) + ly1*2), lx2, (float)((y - .75 * yu) + ly1*2), paint);
      canvas.drawLine(lx1, (float)((y - .75 * yu) + ly1*3), lx2, (float)((y - .75 * yu) + ly1*3), paint);
      canvas.drawLine(lx1, (float)((y - .75 * yu) + ly1*4), lx2, (float)((y - .75 * yu) + ly1*4), paint);
      
    }
    else {
      RectF rect = new RectF();
      double y1 = 4.5 * yu;
      double x1 = .75 * xu;
      double y2 = 5.5 * yu;
      double x2 = 9.25 * xu;
      rect.left   = (float)x1;
      rect.right  = (float)x2;
      rect.top    = (float)y1;
      rect.bottom = (float)y2;
      canvas.drawRoundRect (rect, (float)yu/3, (float)yu/3, paint);
      
      float x = (float)(.75 * xu + (V * xu*8.5)/100.0); 
  
      rect.left   = (float) (x - .75 * xu);
      rect.right  = (float) (x + .75 * xu);
      rect.top    = (float) yu * 1;
      rect.bottom = (float) yu * 9;
  
      paint.setColor(Color.rgb(70,70,70));
      canvas.drawRoundRect (rect, (float)yu/2, (float)yu/2, paint);
  
      rect.left   = (float) (x - .75 * xu) + 2;
      rect.right  = (float) (x + .75 * xu) - 2;
      rect.top    = (float) yu * 1 + 2;
      rect.bottom = (float) yu * 9 - 2;
  
      int v = (int)V;
      paint.setColor(Color.rgb(v+120,120,120));
      canvas.drawRoundRect (rect, (float)yu/2, (float)yu/2, paint);
      paint.setColor(Color.rgb(100,100,100));
      
      
      float ly1 = (float)(yu * 1.2);
      float ly2 = (float)(yu * 8.6);
      float lx1 = (float)((1.5 * xu) / 5);
      canvas.drawLine((float)((x - .75 * xu) + lx1*1), ly1, (float)((x - .75 * xu) + lx1*1), ly2, paint);
      canvas.drawLine((float)((x - .75 * xu) + lx1*2), ly1, (float)((x - .75 * xu) + lx1*2), ly2, paint);
      canvas.drawLine((float)((x - .75 * xu) + lx1*3), ly1, (float)((x - .75 * xu) + lx1*3), ly2, paint);
      canvas.drawLine((float)((x - .75 * xu) + lx1*4), ly1, (float)((x - .75 * xu) + lx1*4), ly2, paint);
    }
    
  }
  
  public boolean onTouchEvent (MotionEvent event) {
    if( Vertical ) {
      double cy = getHeight();
      double yu = cy / 10.0;
      
      double y = event.getY();
      if( y < .75 * yu ) y = .75 * yu;
      if( y > 9.25 * yu ) y = 9.25 * yu;
      V = 100 - (100 * ((y - (.75 * yu)) / (8.5 * yu) ) );
    }
    else {
      double cx = getWidth();
      double xu = cx / 10.0;
      
      double x = event.getX();
      if( x < .75 * xu ) x = .75 * xu;
      if( x > 9.25 * xu ) x = 9.25 * xu;
      V = 100 * ((x - (.75 * xu)) / (8.5 * xu) );
    }
    
    invalidate();
    informListeners();
    return true;
  }

}
