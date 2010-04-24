package net.rocrail.androc.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;

@SuppressWarnings("deprecation")
public class LevelCanvas extends AbsoluteLayout {
  private int currentX;
  private int currentY;

  public LevelCanvas(Context context, AttributeSet attrs) {
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
           
            scrollBy(currentX - x2 , currentY - y2);
            int x = getScrollX();
            int y = getScrollY();
            if( x < 0 || y < 0 )
              scrollTo( x < 0 ? 0:x, y < 0 ? 0:y);
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
