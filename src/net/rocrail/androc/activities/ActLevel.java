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

import net.rocrail.androc.R;
import net.rocrail.androc.objects.Item;
import net.rocrail.androc.objects.ZLevel;
import net.rocrail.androc.widgets.LevelCanvas;
import net.rocrail.androc.widgets.LevelItem;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AbsoluteLayout.LayoutParams;

@SuppressWarnings("deprecation")
public class ActLevel extends ActBase {
  public static final int PROGRESS_DIALOG = 0;
  boolean ModPlan = false;
  int Z = 0;
  ProgressDialog progressDialog = null;
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
      showDialog(PROGRESS_DIALOG);

//      Progress = ProgressDialog.show(this, "", 
  //        "Loading. Please wait...", true, true);
    }
    
    MenuSelection = ActBase.MENU_THROTTLE | ActBase.MENU_SYSTEM;
    Finish = true;
    connectWithService();
  }
  
  public void connectedWithService() {
    super.connectedWithService();
    initView();
  }
  
  
  
  protected Dialog onCreateDialog(int id) {
    switch(id) {
    case PROGRESS_DIALOG:
        progressDialog = new ProgressDialog(ActLevel.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Creating module view...");
        return progressDialog;
    default:
        return null;
    }
  }
  


  public void initView() {
    setContentView(R.layout.level);

    levelView = (LevelCanvas)findViewById(R.id.levelView);
    levelView.setPadding(0,0,0,0);

    if( ModPlan ) {
      setTitle(m_RocrailService.m_Model.m_Title);
    }
    else {
      ZLevel zlevel = m_RocrailService.m_Model.m_ZLevelList.get(Z);
      setTitle(zlevel.Title);
    }
    
    new LevelTask().execute(this);

    
    /*
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
    */
   
    
/*    
    plansize = CGSizeMake(ITEMSIZE*cx, ITEMSIZE*cy); 
    scrollView.contentSize = plansize;
*/

  }

  
  void doLevel(LevelCanvas levelView, ZLevel zlevel) {
    int cx = 0;
    int cy = 0;
    int xOffset = 0;
    int yOffset = 0;
    
    if( ModPlan ) {
      xOffset = zlevel.X;
      yOffset = zlevel.Y;
    }

    Iterator<Item> itemIt = zlevel.itemList.iterator();
    while( itemIt.hasNext() ) {
      Item item = itemIt.next();
      
      LevelItem image = new LevelItem(ActLevel.this, levelView, item );
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
  
  
}


class LevelTask extends AsyncTask<ActLevel, ZLevel, Void> {
  ActLevel level = null;
  int levelIdx = 0;
  int levelCnt = 0;
  int levelWeight = 1;
  
  @Override
  protected void onPreExecute() {
    
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
        Thread.yield();
      }
      
    }
    else {
      ZLevel zlevel = level.m_RocrailService.m_Model.m_ZLevelList.get(level.Z);
      zlevel.itemList = level.createLevelList(level.levelView, zlevel);
      publishProgress(zlevel);
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
  }
}



