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
import net.rocrail.androc.objects.Loco;
import net.rocrail.androc.objects.Switch;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.AbsoluteLayout.LayoutParams;

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
    AbsoluteLayout levelView = (AbsoluteLayout)findViewById(R.id.levelView);
    ScrollView scrollView = (ScrollView)findViewById(R.id.levelScrollView);
    
    Iterator<Switch> it = m_RocrailService.m_Model.m_SwitchList.iterator();
    while( it.hasNext() ) {
      Switch sw = it.next();
      
      ImageView image = new ImageView(this);
      image.setImageResource(R.drawable.turnout_ls_1);
      
      image.setOnClickListener(sw);

      LayoutParams lp = new LayoutParams(32, 32, sw.X*32, sw.Y*32);
      levelView.addView(image, lp);
      
    }
    
    scrollView.requestLayout();

  }

}