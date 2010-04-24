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

import net.rocrail.androc.objects.Item;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class LevelItem extends ImageView {
  LevelCanvas levelCanvas = null;
  private Item item = null;
  private int currentX;
  private int currentY;

  
  public LevelItem(Context context, LevelCanvas levelCanvas, Item item) {
    super(context);
    this.levelCanvas = levelCanvas;
    this.item = item;
  }
  public LevelItem(Context context) {
    super(context);
  }
  public LevelItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  
  protected void  onDraw  (Canvas canvas) {
    super.onDraw(canvas);
    if( item.Text != null && item.Text.trim().length() > 0 ) {
      // draw the text on top of the image
      if( item.textVertical ) {
        drawRotatedText(canvas);
      }
      else {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize((float)20.0);
        canvas.drawText(item.Text, (float)6.0, (float)22.0, paint);
      }
    }
  }

  @Override 
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            currentX = (int) event.getRawX();
            currentY = (int) event.getRawY();
            break;
        }

        case MotionEvent.ACTION_MOVE: {
            int x2 = (int) event.getRawX();
            int y2 = (int) event.getRawY();
           
            levelCanvas.scrollBy(currentX - x2 , currentY - y2);
            int x = levelCanvas.getScrollX();
            int y = levelCanvas.getScrollY();
            if( x < 0 || y < 0 )
              levelCanvas.scrollTo( x < 0 ? 0:x, y < 0 ? 0:y);
            currentX = x2;
            currentY = y2;
            break;
        }   
        case MotionEvent.ACTION_UP: {
            break;
        }
    }
      return true; 
  }
  
  
  void drawRotatedText(Canvas canvas) {
    // draw some rotated text
    // get text width and height
    // set desired drawing location
    int x = 0;
    int y = 56;
    Paint paint = new Paint();
    paint.setColor(Color.BLACK);
    paint.setAntiAlias(true);
    paint.setTextSize(20);

    // draw bounding rect before rotating text
    Rect rect = new Rect();
    paint.getTextBounds(item.Text, 0, item.Text.length(), rect);
    canvas.translate(x, y);
    // rotate the canvas on center of the text to draw
    canvas.rotate(-90, x + rect.exactCenterX(), y + rect.exactCenterY());
    // draw the rotated text
    paint.setStyle(Paint.Style.FILL);
    canvas.drawText(item.Text, x, y, paint);

    //undo the rotate
    canvas.restore();
    
  }
  

  
}
