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

import java.util.Iterator;

import net.rocrail.androc.R;
import net.rocrail.androc.objects.Item;
import net.rocrail.androc.objects.ZLevel;
import net.rocrail.androc.widgets.LevelCanvas;
import net.rocrail.androc.widgets.LevelItem;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.AbsoluteLayout.LayoutParams;

@SuppressWarnings("deprecation")
public class ActLevel extends ActBase {
  boolean ModPlan = false;
  int Z = 0;
  ProgressDialog Progress = null;
  LevelCanvas levelView = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      Z = extras.getInt("level", 0);
      ModPlan = (Z == -1 ? true:false);
    }
    
    if( ModPlan ) {
      Progress = ProgressDialog.show(this, "", 
          "Loading. Please wait...", true, true);
      
    }
    MenuSelection = ActBase.MENU_THROTTLE | ActBase.MENU_SYSTEM;
    Finish = true;
    connectWithService();
  }
  
  public void connectedWithService() {
    super.connectedWithService();
    initView();
  }

  public void initView() {
    setContentView(R.layout.level);

    levelView = (LevelCanvas)findViewById(R.id.levelView);
    levelView.setPadding(0,0,0,0);

    if( ModPlan )
      setTitle(m_RocrailService.m_Model.m_Title);
    else {
      ZLevel zlevel = m_RocrailService.m_Model.m_ZLevelList.get(Z);
      setTitle(zlevel.Title);
    }
    
    /*new Thread() {
      public void run() {
      */  
      if( ModPlan ) {
        Iterator<ZLevel> it = m_RocrailService.m_Model.m_ZLevelList.iterator();
        while( it.hasNext() ) {
          ZLevel zlevel = it.next();
          ActLevel.this.levelView.post(new levelThread(ActLevel.this.levelView, ActLevel.this, zlevel, !it.hasNext()));
        }
        
      }
      else {
        ZLevel zlevel = m_RocrailService.m_Model.m_ZLevelList.get(Z);
        ActLevel.this.levelView.post(new levelThread(ActLevel.this.levelView, ActLevel.this, zlevel, false));
      }
    /*
      }
    }.start();
    */
   
    
/*    
    plansize = CGSizeMake(ITEMSIZE*cx, ITEMSIZE*cy); 
    scrollView.contentSize = plansize;
*/

  }

  
  void doLevel(LevelCanvas levelView, ZLevel zlevel) {
    int Z = zlevel.Z;
    int cx = 0;
    int cy = 0;
    int xOffset = 0;
    int yOffset = 0;
    
    if( ModPlan ) {
      xOffset = zlevel.X;
      yOffset = zlevel.Y;
    }

    Iterator<Item> itemIt = m_RocrailService.m_Model.m_ItemList.iterator();
    while( itemIt.hasNext() ) {
      Item item = itemIt.next();
      if( item.Z == Z && item.Show ) {
        
        LevelItem image = new LevelItem(this, levelView, item );
        String imgname = item.getImageName(ModPlan);
        if( imgname != null ) {
          int resId = getResources().getIdentifier(imgname, "raw", "net.rocrail.androc");
          if( resId != 0 ) {
            image.setImageResource(resId);
          }
        }
        
        image.setOnClickListener(item);
        item.imageView = image;
        item.activity = this;
        int x = ModPlan?item.Mod_X:item.X;
        int y = ModPlan?item.Mod_Y:item.Y;
        LayoutParams lp = new LayoutParams(item.cX*32, item.cY*32, (x+xOffset)*32, (y+yOffset)*32);
        if( item.X + item.cX > cx ) cx = item.X + item.cX;
        if( item.Y + item.cY > cy ) cy = item.Y + item.cY;

        levelView.addView(image, lp);

      }
      
    }
    
  }
  
}


class levelThread implements Runnable {
  ActLevel level = null;
  ZLevel zlevel = null;
  boolean stopProgress = false;
  LevelCanvas levelView = null;
  
  public levelThread(LevelCanvas levelView, ActLevel level, ZLevel zlevel, boolean stopProgress) {
    this.level = level;
    this.zlevel = zlevel;
    this.stopProgress = stopProgress;
    this.levelView = levelView;
    
  }
  
  @Override
  public void run() {
    level.doLevel(levelView, zlevel);
    if( stopProgress ) {
      level.Progress.cancel();
      //levelView.invalidate();
    }
  }
  
}


