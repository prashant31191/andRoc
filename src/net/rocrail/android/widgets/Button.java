package net.rocrail.android.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Button extends View {

  public String  Text = "Button";
  public boolean Down = false;

  public Button(Context context) {
    super(context);
  }
  public Button(Context context, AttributeSet attrs) {
    super(context, attrs);
    String text = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
    if( text != null ) {
      System.out.println("text="+text);
      if( text.startsWith("@")) {
        try {
          int resId = Integer.parseInt(text.substring(1)); 
          text = getContext().getString(resId);
        }
        catch(NumberFormatException e) {
          e.printStackTrace();
        }
      }
      Text = text;
    }
  }
  
  public void setText(String text) {
    Text = text;
  }

  public void setText(CharSequence text) {
    Text = text.toString();
  }

  protected void  onDraw  (Canvas canvas) {
    super.onDraw(canvas);
    
    double cx = getWidth();
    double cy = getHeight();
    double xu = cx/10;
    double yu = cy/10;

    Paint paint = new Paint();
    paint.setAntiAlias(true);
    
    paint.setColor(Color.rgb(70, 70, 70));
    RectF rect = new RectF();
    rect.left = (float)(xu/2);
    rect.right = (float)(cx-xu/2);
    rect.top = (float)(yu/2);
    rect.bottom = (float)(cy-yu/2);
    canvas.drawRoundRect (rect, (float)xu/2, (float)xu/2, paint);

    if( Down )
      paint.setColor(Color.rgb(180, 180, 180));
    else
      paint.setColor(Color.rgb(100, 100, 100));
    rect.left = (float)(xu/2)+2;
    rect.right = (float)(cx-xu/2)-2;
    rect.top = (float)(yu/2)+2;
    rect.bottom = (float)(cy-yu/2)-2;
    canvas.drawRoundRect (rect, (float)xu/2, (float)xu/2, paint);
    
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
      invalidate();
    }
    return super.onTouchEvent(event);
  }

}
