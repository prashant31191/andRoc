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


package net.rocrail.androc.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.rocrail.androc.objects.Item;
import net.rocrail.androc.objects.ZLevel;
import net.rocrail.androc.widgets.LevelCanvas;
import net.rocrail.androc.widgets.LevelItem;


import net.rocrail.androc.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Toast;
import android.widget.ZoomButtonsController;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ZoomButtonsController.OnZoomListener;

@SuppressWarnings("deprecation")
public class ActLevel extends ActBase implements OnZoomListener, OnLongClickListener {
  public static final int PROGRESS_DIALOG = 0;
  boolean ModPlan = false;
  int Z = 0;
  ProgressDialog progressDialog = null;
  LevelCanvas levelView = null;
  boolean quitShowed = false;
  List<ZLevel> zlevelList = new ArrayList<ZLevel>();
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      Z = extras.getInt("level", 0);
      ModPlan = (Z == -1 ? true:false);
    }

    MenuSelection = ActBase.MENU_THROTTLE | ActBase.MENU_MENU | ActBase.MENU_SYSTEM | 
                    ActBase.MENU_PREFERENCES | ActBase.MENU_ACCESSORY;
    
    Finish = false;
    connectWithService();
  }
  
  public void connectedWithService() {
    super.connectedWithService();
    m_RocrailService.LevelView = this;
    initView();
    if( !(m_RocrailService.m_Model.ModPlan && m_RocrailService.Prefs.Modview) ) {
      MenuSelection |= ActBase.MENU_LAYOUT;
    }
  }
  
  public void showDonate() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Rocrail runs entirely on volunteer labor. " +
    		"However, Rocrail also needs contributions of money. " +
    		"Your continued support is vital for keeping Rocrail available. " +
    		"If you already did donate you can ask a key to disable this on startup dialog: donate@rocrail.net\n" +
    		"andRoc will shutdown in 5 minutes!")
           .setCancelable(false)
           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
                 //System.out.println("***EXIT***");
                 //ActLevel.this.finish();
               }
           });
    AlertDialog alert = builder.create();  
    alert.show();
  }

  
  
  
  protected Dialog onCreateDialog(int id) {
    switch(id) {
    case PROGRESS_DIALOG:
        progressDialog = new ProgressDialog(ActLevel.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(getText(R.string.CreatingModview));
        return progressDialog;
    default:
        return null;
    }
  }
  

  public void setBackgroundColor() {
    levelView = (LevelCanvas)findViewById(R.id.levelView);
    switch( m_RocrailService.Prefs.Color ) {
    case 1:
      levelView.setBackgroundColor(0xFFCCCCCC);
      break;
    case 2:
      levelView.setBackgroundColor(0xFFCCCCEE);
      break;
    default:
      levelView.setBackgroundColor(0xFFCCEECC);
      break;
    }
  }
  
  public void initView() {
    setContentView(R.layout.level);

    levelView = (LevelCanvas)findViewById(R.id.levelView);
    levelView.setPadding(0,0,0,0);
    
    setBackgroundColor();
    
    if( ModPlan ) {
      setTitle(m_RocrailService.m_Model.m_Title);
    }
    else if(m_RocrailService.m_Model.m_ZLevelList.size() > 0 ) {
      ZLevel zlevel = m_RocrailService.m_Model.m_ZLevelList.get(Z);
      setTitle(zlevel.Title);
    }
    else {
      setTitle("Empty Plan");
    }
    
    new LevelTask().execute(this);
    
/*    
    plansize = CGSizeMake(ITEMSIZE*cx, ITEMSIZE*cy); 
    scrollView.contentSize = plansize;
*/
  }

  
  void Zoom() {
    int size = m_RocrailService.Prefs.Size;
    Iterator<ZLevel> itZ = zlevelList.iterator();
    while( itZ.hasNext() ) {
      ZLevel zlevel = itZ.next();
      Iterator<Item> it = zlevel.itemList.iterator();
      int xOffset = 0;
      int yOffset = 0;
      if( ModPlan ) {
        xOffset = zlevel.X;
        yOffset = zlevel.Y;
      }
      while( it.hasNext() ) {
        Item item = it.next();
        int x = ModPlan?item.Mod_X:item.X;
        int y = ModPlan?item.Mod_Y:item.Y;
        LayoutParams lp = new LayoutParams(item.cX*size, item.cY*size, (x+xOffset)*size, (y+yOffset)*size);
        item.imageView.size = size;
        levelView.updateViewLayout(item.imageView, lp);
      }
    }
  }
  

  void doLevel(LevelCanvas levelView, ZLevel zlevel) {
    int cx = 0;
    int cy = 0;
    int xOffset = 0;
    int yOffset = 0;
    int size = m_RocrailService.Prefs.Size;
    
    if( ModPlan ) {
      xOffset = zlevel.X;
      yOffset = zlevel.Y;
    }

    Iterator<Item> itemIt = zlevel.itemList.iterator();
    while( itemIt.hasNext() ) {
      Item item = itemIt.next();
      
      LevelItem image = new LevelItem(ActLevel.this, levelView, item, size );
      String imgname = item.getImageName(ModPlan);
      if( imgname != null ) {
        Bitmap bMap = BitmapFactory.decodeFile("/sdcard/androc/symbols/"+imgname+".png");
        if( bMap != null ) {
          image.setImageBitmap(bMap);
        }
        else {
          int resId = getResources().getIdentifier(imgname, "raw", "net.rocrail.androc");
          if( resId != 0 ) {
            image.setImageResource(resId);
          }
        }
      }
      
      image.setOnClickListener(item);
      image.setOnLongClickListener(item);
      item.imageView = image;
      item.activity = this;
      
      int x = ModPlan?item.Mod_X:item.X;
      int y = ModPlan?item.Mod_Y:item.Y;

      LayoutParams lp = new LayoutParams(item.cX*size, item.cY*size, (x+xOffset)*size, (y+yOffset)*size);
      if( item.X + item.cX > cx ) cx = item.X + item.cX;
      if( item.Y + item.cY > cy ) cy = item.Y + item.cY;

      levelView.addView(item.imageView, lp);
      
    }
    
  }
  
  
  List<Item> createLevelList(LevelCanvas levelView, ZLevel zlevel) {
    List<Item> list = new ArrayList<Item>();
    int Z = zlevel.Z;

    Iterator<Item> itemIt = m_RocrailService.m_Model.m_ItemList.iterator();
    while( itemIt.hasNext() ) {
      Item item = itemIt.next();
      if( item.Z == Z && item.Show ) {
        list.add(item);
      }
      Thread.yield();

    }
    return list;
  }
  
  


  class LevelTask extends AsyncTask<ActLevel, ZLevel, Void> {
    ActLevel level = null;
    int levelIdx = 0;
    int levelCnt = 0;
    int levelWeight = 1;
    
    @Override
    protected void onPreExecute() {
      if( ActLevel.this.ModPlan ) {
        ActLevel.this.showDialog(ActLevel.PROGRESS_DIALOG);
      }
  
    }
    
    @Override
    protected Void doInBackground(ActLevel... levels) {
      level = levels[0];
      if( level.ModPlan ) {
        levelCnt = level.m_RocrailService.m_Model.m_ZLevelList.size();
        levelWeight = (100 / levelCnt);
        Iterator<ZLevel> it = level.m_RocrailService.m_Model.m_ZLevelList.iterator();
        while( it.hasNext() ) {
          ZLevel zlevel = it.next();
          zlevel.itemList = level.createLevelList(level.levelView, zlevel);
          levelIdx++;
          zlevel.progressIdx = levelIdx;
          publishProgress(zlevel);
          zlevelList.add(zlevel);
          Thread.yield();
        }
        
      }
      else if(level.m_RocrailService.m_Model.m_ZLevelList.size() > 0) {
        ZLevel zlevel = level.m_RocrailService.m_Model.m_ZLevelList.get(level.Z);
        zlevel.itemList = level.createLevelList(level.levelView, zlevel);
        publishProgress(zlevel);
        zlevelList.add(zlevel);
      }
      return null;
    }
  
    @Override
    protected void onProgressUpdate(ZLevel...zlevels) {
      if( level.progressDialog != null ) {
        level.progressDialog.setProgress(zlevels[0].progressIdx*levelWeight);
      }
  
      level.doLevel(level.levelView, zlevels[0]);
    }
  
    @Override
    protected void onPostExecute(Void v) {
      if( level.progressDialog != null ) {
        level.dismissDialog(ActLevel.PROGRESS_DIALOG);
      }
      System.out.println(""+(!m_RocrailService.m_Model.m_bDonKey?"NO ":"")+"DonKey Set.");
      if( !m_RocrailService.m_Model.m_bDonKey && !m_RocrailService.m_bDidShowDonate ) {
        m_RocrailService.startTimer();
        showDonate();
        m_RocrailService.m_bDidShowDonate = true;
      }
      levelView.setLongClickable(true);
      levelView.setOnLongClickListener(ActLevel.this);
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      //Handle the back button
      if(keyCode == KeyEvent.KEYCODE_BACK) {
        
        if(quitShowed) {
          ActLevel.this.finish();
          return true;
        }
        
        Toast.makeText(getApplicationContext(), R.string.BackAgainQuit,
            Toast.LENGTH_SHORT).show();
        quitShowed = true;
        
          //Ask the user if they want to quit
          /*
          new AlertDialog.Builder(this)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .setTitle(R.string.quit)
          .setMessage(R.string.really_quit)
          .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

              @Override
              public void onClick(DialogInterface dialog, int which) {

                  //Stop the activity
                  ActThrottle.this.finish();    
              }

          })
          .setNegativeButton(R.string.no, null)
          .show();
          */

        return true;
      }
      else {
        quitShowed = false;
        return super.onKeyDown(keyCode, event);
      }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (m_RocrailService.Prefs.Zoom && levelView.zoomButtonsController == null) {
      levelView.zoomButtonsController = new ZoomButtonsController(getWindow().getDecorView());
      levelView.zoomButtonsController.setOnZoomListener(this);
      levelView.zoomButtonsController.setZoomInEnabled(m_RocrailService.Prefs.Size < 64 );
      levelView.zoomButtonsController.setZoomOutEnabled(m_RocrailService.Prefs.Size > 8 );
    }
    switch (event.getAction()) {
    case MotionEvent.ACTION_UP:
      System.out.println("uptime="+SystemClock.uptimeMillis()+" downtime="+event.getDownTime());
      if (m_RocrailService.Prefs.Zoom && (SystemClock.uptimeMillis() - event.getDownTime()) > 1000 && levelView.zoomButtonsController != null) {
        levelView.zoomButtonsController.setVisible(true);
        levelView.zoomButtonsController.setFocusable(true);
        levelView.zoomButtonsController.getZoomControls().setFocusable(true);
        levelView.zoomButtonsController.getZoomControls().setFocusableInTouchMode(true);
        levelView.zoomButtonsController.getZoomControls().requestFocus();
        return true;
      }
    }
    return levelView.onTouchEvent(event);
  }
  
  @Override
  public void onVisibilityChanged(boolean visible) {
  }

  @Override
  public void onZoom(boolean zoomin) {
    if( zoomin && m_RocrailService.Prefs.Size < 64 )
      m_RocrailService.Prefs.Size += 2;
    else if( m_RocrailService.Prefs.Size > 8 )
      m_RocrailService.Prefs.Size -= 2;

    levelView.zoomButtonsController.setZoomInEnabled(m_RocrailService.Prefs.Size < 64 );
    levelView.zoomButtonsController.setZoomOutEnabled(m_RocrailService.Prefs.Size > 8 );
    
    m_RocrailService.Prefs.save();

    Zoom();
    /*
    Intent intent = new Intent(this,net.rocrail.androc.activities.ActLevel.class);
    if( m_RocrailService.m_Model.ModPlan && m_RocrailService.Prefs.Modview ) {
      intent.putExtra("level", -1);
    }
    else
      intent.putExtra("level", Z);

    startActivity(intent);
    levelView.zoomButtonsController.setVisible(false);
    finish();
    */
  }

  @Override
  public boolean onLongClick(View view) {
    layoutView();
    return true;
  }


  @Override
  protected void onStop() {
    super.onPause();
    /*
    if( RocrailServiceConnection != null)
      unbindService(RocrailServiceConnection);
    */
  }
}

