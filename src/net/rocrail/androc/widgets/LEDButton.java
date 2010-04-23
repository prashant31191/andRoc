package net.rocrail.androc.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

public class LEDButton extends Button {
  public boolean ON = true;

  public LEDButton(Context context) {
    super(context);
  }
  public LEDButton(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  
  protected void  onDraw  (Canvas canvas) {
    super.onDraw(canvas);
    if( ON ) {
      Paint paint = new Paint();
      paint.setAntiAlias(true);
      paint.setColor(Color.DKGRAY);
      canvas.drawCircle(15, 12, 7, paint);
      paint.setColor(Color.YELLOW);
      canvas.drawCircle(15, 12, 5, paint);
    }
  }

}
