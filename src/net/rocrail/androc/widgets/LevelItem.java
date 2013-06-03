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
import net.rocrail.androc.objects.StageBlock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;

public class LevelItem extends ImageView implements OnGestureListener {
  LevelCanvas levelCanvas = null;
  private Item item = null;
  public int size = 32;
  
  private GestureDetector gestureDetector = null;

  
  public LevelItem(Context context, LevelCanvas levelCanvas, Item item, int size) {
    super(context);
    this.levelCanvas = levelCanvas;
    this.item = item;
    this.size = size;
    setScaleType(ImageView.ScaleType.FIT_CENTER);
    setPadding(0,0,0,0);
    
    gestureDetector = new GestureDetector(this);
  }
  public LevelItem(Context context) {
    super(context);
  }
  public LevelItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  
  protected void  onDraw  (Canvas canvas) {
    super.onDraw(canvas);
    item.Draw(canvas);
    
    if( item.Background ) {
      Paint paint = new Paint();
      paint.setAntiAlias(true);
      paint.setColor(Color.WHITE);
      paint.setStyle(Style.FILL);
      switch( item.colorName ) {
      case Item.COLOR_OCCUPIED:
        paint.setColor(Color.rgb(255, 200, 200));
        break;
      case Item.COLOR_ENTER:
        paint.setColor(Color.rgb(200, 200, 255));
        break;
      case Item.COLOR_RESERVED:
        paint.setColor(Color.rgb(255, 255, 200));
        break;
      case Item.COLOR_CLOSED:
        paint.setColor(Color.rgb(200, 200, 200));
        break;
      case Item.COLOR_ACCEPTIDENT:
        paint.setColor(Color.rgb(200, 255, 200));
        break;
      case Item.COLOR_RED:
        paint.setColor(Color.rgb(255, 0, 0));
        break;
      }
      int padding = (6 * size) / 32;
      
      if( item instanceof StageBlock ) {
        RectF rect = new RectF(padding, padding, item.cX*size-padding, item.cY*size-padding );
        canvas.drawRoundRect(rect, size/3, size/3, paint);
      }
      else {
        canvas.drawRect(padding, padding, item.cX*size-padding, item.cY*size-padding, paint);
      }
    }
    
    if( item.Text != null && item.Text.trim().length() > 0 && !item.HideText) {
      // draw the text on top of the image
      if( item.textVertical ) {
        drawRotatedText(canvas);
      }
      else {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        double tsize = (14.0 * size) / 32;
        paint.setTextSize((float)tsize);
        System.out.println("Text size = "+tsize);
        
        double tx = (6.0 * size) / 32;
        double ty = (20.0 * size) / 32;
        canvas.drawText(item.Text, (float)tx, (float)ty, paint);
      }
    }
  }
  
  
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(item.cX*size, item.cY*size);
  }
  
  
  
  @Override 
  public boolean onTouchEvent(MotionEvent event) {
    System.out.println("LevelItem::onTouchEvent action=" + event.getAction());
    if( event.getAction() == MotionEvent.ACTION_MOVE ) {
      return true;
    }
    
    if( levelCanvas.zoomButtonsController != null && levelCanvas.zoomButtonsController.isVisible() )
      return false;

    return gestureDetector.onTouchEvent(event);
  }
  
  
  void drawRotatedText(Canvas canvas) {
    Paint paint = new Paint();
    paint.setColor(Color.BLACK);
    paint.setAntiAlias(true);
    
    double tsize = (14.0 * size) / 32;
    paint.setTextSize((float)tsize);
    
    canvas.rotate(90, (15*size)/32, (15*size)/32);
    // draw the rotated text
    paint.setStyle(Paint.Style.FILL);
    
    //canvas.drawText(item.Text, 6, 22, paint);
    double tx = (6.0 * size) / 32;
    double ty = (20.0 * size) / 32;
    canvas.drawText(item.Text, (float)tx, (float)ty, paint);

    //undo the rotate
    canvas.restore();
    
  }
  @Override
  public boolean onDown(MotionEvent event) {
    System.out.println("LevelItem::onDown");
    return super.onTouchEvent(event);
  }
  @Override
  public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
    System.out.println("LevelItem::onFling");
    return false;
  }
  @Override
  public void onLongPress(MotionEvent arg0) {
    System.out.println("LevelItem::onLongPress");
    item.propertiesView();
  }
  @Override
  public boolean onScroll(MotionEvent event1, MotionEvent event2, float distX, float distY) {
    //levelCanvas.scrollBy((int)distX , (int)distY);
    System.out.println("LevelItem::onScroll");
    return false;
  }
  @Override
  public void onShowPress(MotionEvent arg0) {
    System.out.println("LevelItem::onShowPress");
  }
  @Override
  public boolean onSingleTapUp(MotionEvent event) {
    System.out.println("LevelItem::onSingleTapUp");
    item.onClickUp(this);
    return super.onTouchEvent(event);
  }

  

  
}
