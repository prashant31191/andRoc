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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class LevelItem extends ImageView {
  LevelCanvas levelCanvas = null;
  private int currentX;
  private int currentY;

  
  public LevelItem(Context context, LevelCanvas levelCanvas) {
    super(context);
    this.levelCanvas = levelCanvas;
  }
  public LevelItem(Context context) {
    super(context);
  }
  public LevelItem(Context context, AttributeSet attrs) {
    super(context, attrs);
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
  
  

  
}
