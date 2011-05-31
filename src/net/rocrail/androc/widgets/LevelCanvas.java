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
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;
import android.widget.ZoomButtonsController;

/*
The AbsoluteLayout is needed for drawing the Layout levels.
The API is flagged as deprecated but, as it seems, with another meaning than 'obsolete':

“I'll say again: we are not going to remove AbsoluteLayout from a future
release, but we strongly discourage people from using it.”
Dianne Hackborn
Android framework engineer 
*/

@SuppressWarnings("deprecation")
public class LevelCanvas extends AbsoluteLayout {
  private int currentX;
  private int currentY;
  private boolean startMoveInited = false;
  public ZoomButtonsController zoomButtonsController = null;

  public LevelCanvas(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override 
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
      currentX = (int) event.getRawX();
      currentY = (int) event.getRawY();
      startMoveInited = true;
      break;


    case MotionEvent.ACTION_MOVE:
      if (startMoveInited) {
        int x2 = (int) event.getRawX();
        int y2 = (int) event.getRawY();

        scrollBy(currentX - x2, currentY - y2);
        int x = getScrollX();
        int y = getScrollY();
        if (x < 0 || y < 0)
          scrollTo(x < 0 ? 0 : x, y < 0 ? 0 : y);
        currentX = x2;
        currentY = y2;
      }
      else {
        currentX = (int) event.getRawX();
        currentY = (int) event.getRawY();
        startMoveInited = true;
      }
      return true;


    case MotionEvent.ACTION_UP:
      startMoveInited = false;
      break;

    }
    
    return false;
  }

}
