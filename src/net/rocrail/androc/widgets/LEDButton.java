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
package net.rocrail.androc.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

public class LEDButton extends Button {
  public boolean ON = false;
  public boolean LED = true;

  public LEDButton(Context context) {
    super(context);
  }
  public LEDButton(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  
  public void noLED()
  {
    LED = false;
  }
  
  protected void  onDraw  (Canvas canvas) {
    super.onDraw(canvas);
    
    if(LED)
    {
  
      Paint paint = new Paint();
      paint.setAntiAlias(true);
      paint.setColor(Color.rgb(50, 50, 50));
      canvas.drawCircle(15, 12, 6, paint);
          
      if( ON ) {
        paint.setColor(Color.rgb(240, 210, 100));
        canvas.drawCircle(15, 12, 4, paint);
        
        paint.setColor(Color.rgb(255, 245, 0));
        canvas.drawCircle(14, 11, 1, paint);
      }
      else
      {
        paint.setColor(Color.rgb(100, 100, 80));
        canvas.drawCircle(15, 12, 4, paint);
        
        paint.setColor(Color.rgb(130, 130, 110));
        canvas.drawCircle(14, 11, 1, paint);
      }
    }
  }

}
