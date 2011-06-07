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
  private boolean firstMove = false;
  public ZoomButtonsController zoomButtonsController = null;

  public LevelCanvas(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  
  @Override 
  protected void onDetachedFromWindow () {
    if( zoomButtonsController != null )
      zoomButtonsController.setVisible(false);
  }
  
  @Override 
  protected void onWindowVisibilityChanged (int visibility) {
    if( visibility != View.VISIBLE && zoomButtonsController != null )
      zoomButtonsController.setVisible(false);
  }
  

  @Override 
  public boolean onTouchEvent(MotionEvent event) {
    System.out.println("LevelCanvas::onTouchEvent action=" + event.getAction());
    switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
      System.out.println("LevelCanvas::DOWN");
      currentX = (int) event.getRawX();
      currentY = (int) event.getRawY();
      startMoveInited = true;
      firstMove = true;
      break;


    case MotionEvent.ACTION_MOVE:
      System.out.println("LevelCanvas::MOVE");
      if (startMoveInited) {
        int x2 = (int) event.getRawX();
        int y2 = (int) event.getRawY();

        int xDelta = currentX - x2;
        int yDelta = currentY - y2;
        
        if( !firstMove || (xDelta >= 16 || xDelta <= -16 || yDelta >= 16 || yDelta <= -16) ) {
          scrollBy(currentX - x2, currentY - y2);
          int x = getScrollX();
          int y = getScrollY();
          if (x < 0 || y < 0)
            scrollTo(x < 0 ? 0 : x, y < 0 ? 0 : y);
          currentX = x2;
          currentY = y2;
          firstMove = false;
        }
      }
      else {
        currentX = (int) event.getRawX();
        currentY = (int) event.getRawY();
        startMoveInited = true;
        firstMove = true;
      }
      return true;


    case MotionEvent.ACTION_UP:
      System.out.println("LevelCanvas::UP");
      startMoveInited = false;
      firstMove = true;
      break;

    }
    
    return false;
  }

}
