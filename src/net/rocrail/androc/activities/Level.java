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
import net.rocrail.androc.objects.Switch;
import net.rocrail.androc.objects.ZLevel;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.AbsoluteLayout.LayoutParams;

@SuppressWarnings("deprecation")
public class Level extends Base {

  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    connectWithService();
  }
  
  public void connectedWithService() {
    initView();
  }

  public void initView() {
    setContentView(R.layout.level);
    
    int Z = 0;

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      int level = extras.getInt("level", 0);
      ZLevel zlevel = m_RocrailService.m_Model.m_ZLevelList.get(level);
      setTitle(zlevel.Title);
      Z = zlevel.Z;
    }

    AbsoluteLayout levelView = (AbsoluteLayout)findViewById(R.id.levelView);
    ScrollView scrollView = (ScrollView)findViewById(R.id.levelScrollView);
    
    Iterator<Switch> it = m_RocrailService.m_Model.m_SwitchList.iterator();
    while( it.hasNext() ) {
      Switch sw = it.next();
      if( sw.Z == Z ) {
        ImageView image = new ImageView(this);
        
        int resId = getResources().getIdentifier(sw.getImageName(), "raw", "net.rocrail.androc");

        //image.setImageResource(R.raw.turnout_ls_1);
        
        image.setImageResource(resId);
        
        image.setOnClickListener(sw);
  
        LayoutParams lp = new LayoutParams(sw.cX*32, sw.cY*32, sw.X*32, sw.Y*32);
        levelView.addView(image, lp);
      }
      
    }
    
    scrollView.requestLayout();

  }

}
